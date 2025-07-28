package com.example.products.web;

import com.example.products.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
public class LoginController {

    private final WebClient authClient;
    @Value("${user.service.auth-path}")
    private String userServiceUrl;

    public LoginController(@Qualifier("authWebClient") WebClient userClient) {
        this.authClient = userClient;
    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        try {

            LoginResponse loginResponse = authClient.post()
                .uri(userServiceUrl + "/login")
                .bodyValue(Map.of("email", username, "password", password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

            if (loginResponse != null && loginResponse.getToken() != null) {
                session.setAttribute("JWT_TOKEN", loginResponse.getToken());
                session.setAttribute("USERNAME", loginResponse.getEmail());

                return "redirect:/";
            } else {

                return "redirect:/";
            }

        } catch (Exception e) {

            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "productList";
    }
}