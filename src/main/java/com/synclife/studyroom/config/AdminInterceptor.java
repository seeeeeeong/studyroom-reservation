package com.synclife.studyroom.config;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userType = (String) request.getAttribute("userType");

        if ("ADMIN".equals(userType)) {
            return true;
        }

        throw new StudyroomException(ErrorCode.AUTH_FORBIDDEN);
    }
}
