package com.synclife.studyroom.config;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import com.synclife.studyroom.domain.Reservation;
import com.synclife.studyroom.domain.ReservationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ReservationRepository reservationRepository;

    public AuthInterceptor(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        validateTokenExists(token);

        UserInfo userInfo = parseToken(token);
        setRequestAttributes(request, userInfo);

        validatePermissions(request, userInfo);

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }

        return authHeader.startsWith("Bearer ")
            ? authHeader.substring(7)
            : authHeader;
    }

    private void validateTokenExists(String token) {
        if (token == null) {
            throw new StudyroomException(ErrorCode.AUTH_TOKEN_REQUIRED);
        }
    }

    private UserInfo parseToken(String token) {
        if ("admin-token".equals(token)) {
            return new UserInfo("ADMIN", "admin");
        }

        if (token.startsWith("user-token-")) {
            String userId = token.substring(11);
            return new UserInfo("USER", userId);
        }

        throw new StudyroomException(ErrorCode.AUTH_TOKEN_INVALID);
    }

    private void setRequestAttributes(HttpServletRequest request, UserInfo userInfo) {
        request.setAttribute("userType", userInfo.userType());
        request.setAttribute("userId", userInfo.userId());
    }

    private void validatePermissions(HttpServletRequest request, UserInfo userInfo) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        if (isRoomCreationRequest(method, path)) {
            validateAdminPermission(userInfo);
        }

        if (isReservationCancelRequest(method, path)) {
            validateCancelPermission(path, userInfo);
        }
    }

    private boolean isRoomCreationRequest(String method, String path) {
        return "POST".equals(method) && "/api/rooms".equals(path);
    }

    private boolean isReservationCancelRequest(String method, String path) {
        return "DELETE".equals(method) && path.matches("/api/reservations/\\d+");
    }

    private void validateAdminPermission(UserInfo userInfo) {
        if ("ADMIN".equals(userInfo.userType())) {
            return;
        }
        throw new StudyroomException(ErrorCode.AUTH_FORBIDDEN);
    }

    private void validateCancelPermission(String path, UserInfo userInfo) {
        if ("ADMIN".equals(userInfo.userType())) {
            return;
        }

        Long reservationId = extractReservationId(path);
        validateReservationOwner(reservationId, userInfo.userId());
    }

    private Long extractReservationId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
    }

    private void validateReservationOwner(Long reservationId, String userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (reservation.isOwnedBy(userId)) {
            return;
        }

        throw new StudyroomException(ErrorCode.RESERVATION_CANCEL_FORBIDDEN);
    }

    private record UserInfo(String userType, String userId) {}
}
