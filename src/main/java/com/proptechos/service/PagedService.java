package com.proptechos.service;

import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.http.JsonHttpClient;
import com.proptechos.http.query.IQueryFilter;
import com.proptechos.model.common.PageMetadata;
import com.proptechos.model.common.Paged;
import com.proptechos.service.filters.PageNumberFilter;
import com.proptechos.service.filters.PageSizeFilter;

class PagedService<T> {

  protected final JsonHttpClient httpClient;
  private final String apiPath;

  PagedService(String baseAppUrl, String apiPath) {
    this.httpClient = JsonHttpClient.getInstance(baseAppUrl);
    this.apiPath = apiPath;
  }

  public Paged<T> getFirstPage(long pageSize) {
    validatePageMetadata(0, pageSize);
    return httpClient.getPaged(apiPath, new PageSizeFilter(pageSize), new PageNumberFilter(0));
  }


  public Paged<T> getPage(long pageNumber, long pageSize) {
    validatePageMetadata(pageNumber, pageSize);
    return httpClient.getPaged(
        apiPath, new PageSizeFilter(pageSize), new PageNumberFilter(pageNumber));
  }

  public Paged<T> getPageFiltered(long pageNumber, long pageSize, IQueryFilter...filters) {
    validatePageMetadata(pageNumber, pageSize);
    IQueryFilter[] queryFilters = new IQueryFilter[filters.length + 2];
    queryFilters[0] = new PageSizeFilter(pageSize);
    queryFilters[1] = new PageNumberFilter(pageNumber);
    System.arraycopy(
        filters, 0, queryFilters, 2, queryFilters.length - 2);
    return httpClient.getPaged(apiPath, queryFilters);
  }

  public Paged<T> getLastPage(long pageSize) {
    validatePageMetadata(0, pageSize);
    Paged<T> firstPage = getFirstPage(pageSize);
    return httpClient.getPaged(apiPath,
        new PageSizeFilter(firstPage.getPageMetadata().getSize()),
        new PageNumberFilter(firstPage.getPageMetadata().getTotalPages() - 1));
  }

  public Paged<T> getNextPage(PageMetadata currentPageMetadata) {
    validatePageMetadata(currentPageMetadata);
    return httpClient.getPaged(apiPath,
        new PageSizeFilter(currentPageMetadata.getSize()),
        new PageNumberFilter(currentPageMetadata.getPage() + 1));
  }

  public void validatePageMetadata(long pageNumber, long pageSize) {
    if (pageNumber < 0) {
      throw new ServiceInvalidUsageException("Page number can't have negative value");
    }
    if (pageSize < 0) {
      throw new ServiceInvalidUsageException("Page size can't have negative value");
    }
  }

  public void validatePageMetadata(PageMetadata pageMetadata) {
    if (pageMetadata.getPage() < 0 ||
    pageMetadata.getSize() < 0 ||
    pageMetadata.getTotalPages() < 0 ||
    pageMetadata.getTotalElements() < 0) {
      throw new ServiceInvalidUsageException("The provided page metadata contains negative values");
    }
  }

}
