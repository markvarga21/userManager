package com.markvarga21.studentmanager.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The logout success handler for the application.
 */
@Component
@RequiredArgsConstructor
public class AppLogoutSuccessHandler implements LogoutSuccessHandler {
    /**
     * Handles a successful logout.
     *
     * @param request the request.
     * @param response the response.
     * @param authentication the authentication.
     * @throws IOException if an I/O error occurs.
     * @throws ServletException if a servlet error occurs.
     */
    @Override
    public void onLogoutSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
