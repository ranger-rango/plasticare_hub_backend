package com.plasticare_hub.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator
{
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecureToken()
    {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    // public static void main(String[] args) {
    //     System.out.println(generateSecureToken());
    // }
}