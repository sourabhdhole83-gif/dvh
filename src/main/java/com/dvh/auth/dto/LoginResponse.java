package com.dvh.auth.dto;

import org.springframework.web.servlet.view.script.ScriptTemplateConfig;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
