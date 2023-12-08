package com.example.CSVRelatedClass;

import java.util.Comparator;
import java.util.function.Function;

public class CustomComparator {

    public static <T> Comparator<T> createComparator(Function<T, String> idExtractor, int prefixLength) {
        return (d1, d2) -> {
            String id1String = idExtractor.apply(d1);
            String id2String = idExtractor.apply(d2);
            String prefix1 = id1String.substring(0, prefixLength);
            String prefix2 = id2String.substring(0, prefixLength);
            int id1 = Integer.parseInt(id1String.substring(prefixLength));
            int id2 = Integer.parseInt(id2String.substring(prefixLength));

            if (prefix1.compareTo(prefix2) != 0) {
                return prefix1.compareTo(prefix2);
            } else if (id1 != id2) {
                return Integer.compare(id1, id2);
            } else {
                return d1.toString().compareTo(d2.toString());
            }
        };
    }
}
