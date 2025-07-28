package com.example.products;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserModelAttribute {

    @ModelAttribute
    public void addUserAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                model.addAttribute("username", userDetails.getUsername());
            } else if (principal instanceof String username) {
                model.addAttribute("username", username);
            }
        }
    }
}