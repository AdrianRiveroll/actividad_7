package mx.ubam.inventario.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirect")
    public String redirectByRole(Authentication auth) {
        if (auth == null) return "redirect:/";

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return "redirect:/admin/products";

        // si no es admin, lo mandamos a cliente
        return "redirect:/client/products";
    }
}
