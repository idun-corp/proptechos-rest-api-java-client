package com.proptechos.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Paged<T> {

  private final Collection<T> content;

  private final PageMetadata pageMetadata;

  @JsonCreator
  public Paged(
      @JsonProperty("content") Collection<T> content,
      @JsonProperty("size") long size,
      @JsonProperty("number") long pageNumber,
      @JsonProperty("totalPages") long totalPages,
      @JsonProperty("totalElements") long totalElements) {
    this.content = content;
    this.pageMetadata = new PageMetadata(pageNumber, size, totalPages, totalElements);
  }

  public Collection<T> getContent() {
    return content;
  }

  public PageMetadata getPageMetadata() {
    return pageMetadata;
  }
}
