package com.proptechos.utils;

import java.util.UUID;

public class ClientUtils {

    public static String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public static String getHistoryEndpoint(String baseUrl, UUID twinId) {
        return baseUrl + "/" + twinId + "/history";
    }

}
