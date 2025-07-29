package com.example.products.web;

import com.example.products.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final WebClient authClient;
    private final String userServiceLoginUrl;

    private static final String SESSION_JWT = "JWT_TOKEN";
    private static final String SESSION_USERNAME = "USERNAME";
    private static final String FLASH_ERROR = "errorMessage";

    public LoginController(@Qualifier("authWebClient") WebClient authClient,
                           @Value("${user.service.auth-path}") String userServiceUrl) {
        this.authClient = authClient;
        this.userServiceLoginUrl = userServiceUrl + "/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                HttpSession session,
                                Model model) {
        if (session.getAttribute(SESSION_JWT) != null) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute(FLASH_ERROR, "Invalid username or password");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isInvalid(username) || isInvalid(password)) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "Username and password must not be empty");
            return "redirect:/login";
        }

        try {
            logger.info("Login attempt for user: {}", username);

            LoginResponse loginResponse = authClient.post()
                .uri(userServiceLoginUrl)
                .bodyValue(Map.of("email", username, "password", password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

            if (loginResponse != null && loginResponse.getToken() != null) {
                session.setAttribute(SESSION_JWT, loginResponse.getToken());
                session.setAttribute(SESSION_USERNAME, loginResponse.getEmail());
                logger.info("User {} logged in successfully", username);
                return "redirect:/";
            } else {
                logger.warn("Login failed: empty token received for user {}", username);
                redirectAttributes.addFlashAttribute(FLASH_ERROR, "Login failed. Please try again.");
                return "redirect:/login";
            }
        } catch (WebClientResponseException e) {
            logger.warn("Login failed for user {}: {}", username, e.getResponseBodyAsString());
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "Invalid credentials.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Unexpected login error for user {}: {}", username, e.getMessage(), e);
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "An internal error occurred.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }


    @GetMapping("/home")
    public String homePage() {
        return "productList";
    }

    private boolean isInvalid(String input) {
        return input == null || input.isBlank();
    }
}