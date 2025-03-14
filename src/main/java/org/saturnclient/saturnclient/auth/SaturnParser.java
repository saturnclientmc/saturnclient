package org.saturnclient.saturnclient.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaturnParser {
    private Map<String, String> params = new HashMap<>();

    public SaturnParser(String input) throws IOException {
        String[] parts = input.split("@");

        if (parts.length == 0) {
            throw new IOException("Invalid input string");
        }

        for (int i = 1; i < parts.length; i++) {
            String[] keyValue = parts[i].split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0].trim(), keyValue[1].trim());
            } else {
                throw new IOException("Invalid parameter format: " + parts[i]);
            }
        }
    }

    public String getString(String i) throws IOException {
        String val = params.get(i);
        if (val != null) {
            return val;
        } else {
            throw new IOException("Parameter not found " + i);
        }
    }
}
