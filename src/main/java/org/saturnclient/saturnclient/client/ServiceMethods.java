package org.saturnclient.saturnclient.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.TextNode;

import dev.selimaj.session.types.Method;

public class ServiceMethods {
    public static class AuthResponse extends JsonNode {
        @JsonProperty(required = true)
        private String token;

        @JsonProperty(required = true)
        private String userId;

        // Constructor enforcing required fields
        public AuthResponse(String token, String userId) {
            this.token = token;
            this.userId = userId;
        }

        // Default constructor needed for Jackson deserialization
        public AuthResponse() {
        }

        @Override
        public JsonNodeType getNodeType() {
            return JsonNodeType.OBJECT;
        }
    }

    public static final Method<TextNode, AuthResponse, TextNode> Authenticate = new Method<>("auth",
            TextNode.class, TextNode.class, TextNode.class);
}