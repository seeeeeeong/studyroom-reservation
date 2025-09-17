package com.synclife.studyroom.config;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);

        if (token == null) {
            throw new StudyroomException(ErrorCode.AUTH_TOKEN_REQUIRED);
        }

        if ("admin-token".equals(token)) {
            request.setAttribute("userType", "ADMIN");
            request.setAttribute("userId", "admin");
            return true;
        }

        if (token.startsWith("user-token-")) {
            String userId = token.substring(11);
            request.setAttribute("userType", "USER");
            request.setAttribute("userId", userId);
            return true;
        }

        throw new StudyroomException(ErrorCode.AUTH_TOKEN_INVALID);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }
}
