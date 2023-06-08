package com.github.oasis.craftprotect.utils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UrlUtils {

    public static Map<String, String> getParamMap(String query) {
        // query is null if not provided (e.g. localhost/path )
        // query is empty if '?' is supplied (e.g. localhost/path? )
        if (query == null || query.isEmpty()) return Collections.emptyMap();

        return Stream.of(query.split("&"))
                .filter(s -> !s.isEmpty())
                .map(kv -> kv.split("=", 2))
                .collect(Collectors.toMap(x -> x[0], x -> x.length > 1 ? x[1] : ""));

    }

}
