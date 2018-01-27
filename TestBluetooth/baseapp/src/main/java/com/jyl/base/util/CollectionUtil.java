package com.jyl.base.util;

import java.util.Collection;

/**
 * Created by Simon on 16/9/12.
 */
public class CollectionUtil {

    public static boolean isNotEmpty (Collection collection) {
        return collection != null && !collection.isEmpty();
    }
}
