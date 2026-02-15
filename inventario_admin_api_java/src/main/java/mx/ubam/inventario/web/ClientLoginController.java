package mx.ubam.inventario.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientLoginController {

    @GetMapping("/client/login")
    public String login() {
        return "client/login";
    }
}
