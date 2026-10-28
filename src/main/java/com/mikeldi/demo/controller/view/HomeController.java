package com.mikeldi.demo.controller.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	// Redirige al login si no está autenticado,
    // o al dashboard correspondiente según el rol
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return "redirect:/login";
        return redirectByRole(user);
    }

    @GetMapping("/redirect-after-login")
    public String redirectAfterLogin(@AuthenticationPrincipal UserDetails user) {
        return redirectByRole(user);
    }

    private String redirectByRole(UserDetails user) {
        if (user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            return "redirect:/doctor/dashboard";
        }
        return "redirect:/patient/dashboard";
    }
}
