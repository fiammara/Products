package com.example.products;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private final HttpSession session;

    public UserSession(HttpSession session) {
        this.session = session;
    }

    public String getJwtToken() {
        return (String) session.getAttribute("JWT_TOKEN");
    }

    public String getUsername() {
        return (String) session.getAttribute("USERNAME");
    }

    public boolean isAuthenticated() {
        return getJwtToken() != null;
    }
}