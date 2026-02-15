package mx.ubam.inventario.api;

import mx.ubam.inventario.api.dto.AuthDtos;
import mx.ubam.inventario.repo.UserRepository;
import mx.ubam.inventario.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public ApiAuthController(UserRepository userRepository, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthDtos.LoginResponse login(@RequestBody AuthDtos.LoginRequest req) {
        var user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generate(user.getUsername(), Map.of("role", user.getRole().name()));
        return new AuthDtos.LoginResponse(token);
        
    }
}
