package com.example.UberApp.config;

import org.springframework.context.annotation.Configuration;

// ❌ IMPORTANT:
// This class intentionally does NOT define any UserDetailsService bean
// because we already use CustomUserDetailsService

@Configuration
public class ApplicationConfig {
}