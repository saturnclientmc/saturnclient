package org.saturnclient.saturnclient.client;

import com.fasterxml.jackson.databind.node.TextNode;

import dev.selimaj.session.types.Method;

public class ServiceMethods {
        public record AuthResponse(
                        String username,
                        String password) {
        }

        public static final Method<TextNode, AuthResponse, TextNode> Authenticate = new Method<>("auth",
                        TextNode.class, AuthResponse.class, TextNode.class);
}