package com.search.common.utils;

import java.util.UUID;

/**
 * @author tanglingyu
 */
public class UuidUtil {
    public UuidUtil() {
    }

    public static final String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; ++i) {
            System.out.println(generateUuid());
        }

    }
}
