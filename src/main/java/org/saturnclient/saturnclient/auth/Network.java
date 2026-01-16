package org.saturnclient.saturnclient.auth;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.interfaces.*;
import java.security.spec.X509EncodedKeySpec;

public class Network {
    static Socket socket;
    static PrintWriter out;
    static BufferedReader in;
    static byte[] key = new byte[32];

    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;

    public static class AES {
        public static byte[] encryptAES256(String message, byte[] key) throws Exception {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            return encryptedMessage;
        }

        public static String decryptAES256(byte[] encryptedMessage, byte[] key) throws Exception {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
            return new String(decryptedMessage);
        }
    }

    public static void init() throws Exception {
        // Generate AES key
        new SecureRandom().nextBytes(key);

        socket = new Socket();
        socket.connect(new InetSocketAddress("77.247.92.168", 8080), CONNECTION_TIMEOUT);
        
        socket.setSoTimeout(READ_TIMEOUT);

        // out = new DataOutputStream(socket.getOutputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        StringBuilder publicKeyBuilder = new StringBuilder();
        String line;
        while (!(line = in.readLine()).contains("-----END PUBLIC KEY-----")) {
            publicKeyBuilder.append(line).append("\n");
        }
        publicKeyBuilder.append(line);

        String publicKeyPem = publicKeyBuilder.toString();

        String publicKeyBase64 = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").replace("\n", "").replace("\r", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(key);

        out.println(Base64.getEncoder().encodeToString(encryptedKey));
    }

    public static void write(String message) {
        try {
            out.println(Base64.getEncoder().encodeToString(AES.encryptAES256(message, key)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to server: " + e.getMessage());
        }
    }

    public static String read() {
        try {
            String msg = in.readLine();
            if (msg != null) {
                return AES.decryptAES256(Base64.getDecoder().decode(msg), key);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static void close() {
        try {
            socket.close();
            out.close();
            in.close();
        } catch (Exception e) {
        }
    }
}