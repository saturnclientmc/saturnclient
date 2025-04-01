package org.saturnclient.saturnclient.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaturnParser {
    public String method = "";
    public String error;
    private Map<String, String> params = new HashMap<>();

    public SaturnParser(String input) throws IOException {
        String[] parts = input.split("@");

        if (parts.length == 0) {
            throw new IOException("Invalid input string");
        }

        if (parts[0].startsWith("!")) {
            error = input;
        }

        method = parts[0];

        for (int i = 1; i < parts.length; i++) {
            String[] keyValue = parts[i].split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0].trim(), keyValue[1].trim());
            } else if (keyValue.length == 1) {
                params.put(keyValue[0].trim(), "");
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

    public boolean getBool(String i) throws IOException {
        String val = params.get(i);
        if (val != null) {
            return val.equals("true");
        } else {
            throw new IOException("Parameter not found " + i);
        }
    }

    public String[] getArray(String i) throws IOException {
        String val = params.get(i);
        if (val != null) {
            if (val.isEmpty()) {
                return new String[] {};
            }
            return val.split("\\$");
        } else {
            throw new IOException("Parameter not found " + i);
        }
    }
}
