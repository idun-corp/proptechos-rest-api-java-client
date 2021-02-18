package com.proptechos.service;

import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.http.JsonHttpClient;
import com.proptechos.http.query.IQueryFilter;
import com.proptechos.model.common.PageMetadata;
import com.proptechos.model.common.Paged;
import com.proptechos.service.filters.common.PageNumberFilter;
import com.proptechos.service.filters.common.PageSizeFilter;

class PagedService<T> {

  protected final JsonHttpClient httpClient;
  protected final Class<T> typeClazz;
  private final String apiPath;

  PagedService(String baseAppUrl, String apiPath, Class<T> typeClazz) {
    this.httpClient = JsonHttpClient.getInstance(baseAppUrl);
    this.typeClazz = typeClazz;
    this.apiPath = apiPath;
  }

  /**
   * @param pageSize number of elements requested per page
   * @return first page of paged data
   *
   * @see com.proptechos.model.common.Paged
   */
  public Paged<T> getFirstPage(long pageSize) {
    validatePageMetadata(0, pageSize);
    return httpClient.getPaged(
        typeClazz, apiPath, new PageSizeFilter(pageSize), new PageNumberFilter(0));
  }

  /**
   * @param pageNumber number of requested page
   * @param pageSize number of elements requested per page
   * @return requested page of paged data
   *
   * @see com.proptechos.model.common.Paged
   */
  public Paged<T> getPage(long pageNumber, long pageSize) {
    validatePageMetadata(pageNumber, pageSize);
    return httpClient.getPaged(
        typeClazz, apiPath, new PageSizeFilter(pageSize), new PageNumberFilter(pageNumber));
  }

  /**
   * @param pageNumber number of requested page
   * @param pageSize number of elements requested per page
   * @param filters additional optional query filters
   * @return requested page of paged data
   *
   * @see com.proptechos.model.common.Paged
   * @see com.proptechos.http.query.IQueryFilter
   */
  public Paged<T> getPageFiltered(long pageNumber, long pageSize, IQueryFilter...filters) {
    validatePageMetadata(pageNumber, pageSize);
    IQueryFilter[] queryFilters = new IQueryFilter[filters.length + 2];
    queryFilters[0] = new PageSizeFilter(pageSize);
    queryFilters[1] = new PageNumberFilter(pageNumber);
    System.arraycopy(
        filters, 0, queryFilters, 2, queryFilters.length - 2);
    return httpClient.getPaged(typeClazz, apiPath, queryFilters);
  }

  /**
   * @param pageSize number of elements requested per page
   * @return last page of paged data
   */
  public Paged<T> getLastPage(long pageSize) {
    validatePageMetadata(0, pageSize);
    Paged<T> firstPage = getFirstPage(pageSize);
    return httpClient.getPaged(typeClazz, apiPath,
        new PageSizeFilter(firstPage.getPageMetadata().getSize()),
        new PageNumberFilter(firstPage.getPageMetadata().getTotalPages() - 1));
  }

  /**
   * @param currentPageMetadata metadata of current page in order to get next one
   * @return requested page of paged data
   *
   * @see com.proptechos.model.common.PageMetadata
   */
  public Paged<T> getNextPage(PageMetadata currentPageMetadata) {
    validatePageMetadata(currentPageMetadata);
    return httpClient.getPaged(typeClazz, apiPath,
        new PageSizeFilter(currentPageMetadata.getSize()),
        new PageNumberFilter(currentPageMetadata.getPage() + 1));
  }

  protected void validatePageMetadata(long pageNumber, long pageSize) {
    if (pageNumber < 0) {
      throw new ServiceInvalidUsageException("Page number can't have negative value");
    }
    if (pageSize < 0) {
      throw new ServiceInvalidUsageException("Page size can't have negative value");
    }
  }

  private void validatePageMetadata(PageMetadata pageMetadata) {
    if (pageMetadata.getPage() < 0 ||
    pageMetadata.getSize() < 0 ||
    pageMetadata.getTotalPages() < 0 ||
    pageMetadata.getTotalElements() < 0) {
      throw new ServiceInvalidUsageException("The provided page metadata contains negative values");
    }
  }

}
