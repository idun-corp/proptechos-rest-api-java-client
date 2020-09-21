package com.proptechos.service;

import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.http.JsonHttpClient;
import com.proptechos.http.query.IQueryFilter;
import com.proptechos.model.common.PageMetadata;
import com.proptechos.model.common.Paged;
import com.proptechos.service.filters.PageNumberFilter;
import com.proptechos.service.filters.PageSizeFilter;

public class PagedService<T> {

  protected final JsonHttpClient<T> httpClient;
  private final String apiPath;

  public PagedService(String baseAppUrl, String apiPath) {
    this.httpClient = new JsonHttpClient<>(baseAppUrl);
    this.apiPath = apiPath;
  }

  public Paged<T> getFirstPage(long size) {
    if (size < 0) {
      throw new ServiceInvalidUsageException("Page size can't have negative value");
    }
    return httpClient.getPaged(apiPath, new PageSizeFilter(size), new PageNumberFilter(0));
  }

  public Paged<T> getFirstPage(PageMetadata pageMetadata) {
    validatePageMetadata(pageMetadata);
    return httpClient.getPaged(
        apiPath, new PageSizeFilter(pageMetadata.getSize()), new PageNumberFilter(0));
  }

  public Paged<T> getNextPage(PageMetadata currentPageMetadata) {
    validatePageMetadata(currentPageMetadata);
    return httpClient.getPaged(apiPath,
        new PageSizeFilter(currentPageMetadata.getSize()),
        new PageNumberFilter(currentPageMetadata.getPage() + 1));
  }

  public Paged<T> getLastPage(PageMetadata currentPageMetadata) {
    validatePageMetadata(currentPageMetadata);
    return httpClient.getPaged(apiPath,
        new PageSizeFilter(currentPageMetadata.getSize()),
        new PageNumberFilter(currentPageMetadata.getTotalPages() - 1));
  }

  public Paged<T> getPage(PageMetadata pageMetadata) {
    validatePageMetadata(pageMetadata);
    return httpClient.getPaged(apiPath,
        new PageSizeFilter(pageMetadata.getSize()),
        new PageNumberFilter(pageMetadata.getPage()));
  }

  public Paged<T> getPageFiltered(PageMetadata pageMetadata, IQueryFilter...filters) {
    validatePageMetadata(pageMetadata);
    IQueryFilter[] queryFilters = new IQueryFilter[filters.length + 2];
    queryFilters[0] = new PageSizeFilter(pageMetadata.getSize());
    queryFilters[1] = new PageNumberFilter(pageMetadata.getPage());
    System.arraycopy(
        filters, 0, queryFilters, 2, queryFilters.length - 2);
    return httpClient.getPaged(apiPath, queryFilters);
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
