package com.proptechos.utils;

public class ClientUtils {

    public static String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

}
