package com.proptechos.http.query;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {

    private final List<QueryParam> params;

    private QueryBuilder() {
        this.params = new LinkedList<>();
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    public QueryBuilder append(QueryParam queryParam) {
        this.params.add(queryParam);
        return this;
    }

    public String build() {
        return params.isEmpty() ?
            "" :
            "?" + params.stream().map(QueryParam::toString).collect(Collectors.joining("&"));
    }

}
