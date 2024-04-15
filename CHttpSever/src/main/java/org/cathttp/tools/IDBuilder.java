package org.cathttp.tools;

import java.util.UUID;

public class IDBuilder {
    public static String builderId(){
        return UUID.randomUUID().toString();
    }
}
