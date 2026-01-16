package com.igor.tenantcrm.dev;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGen {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        String raw = "Admin@123";
        System.out.println("RAW: " + raw);
        System.out.println("HASH: " + encoder.encode(raw));
    }
}

