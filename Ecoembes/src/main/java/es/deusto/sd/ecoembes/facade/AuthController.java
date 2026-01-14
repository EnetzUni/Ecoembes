package es.deusto.sd.ecoembes.facade;

import java.util.Map; // Importante
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.deusto.sd.ecoembes.dto.CredentialsDTO;
import es.deusto.sd.ecoembes.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody CredentialsDTO credentials) {
        
        Optional<Map<String, Object>> result = authService.login(credentials.getEmail(), credentials.getPassword());

        return result.map(map -> new ResponseEntity<>(map, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
    
    // ... El logout se queda igual ...
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String token) {
        Optional<Boolean> result = authService.logout(token);
        return (result.isPresent() && result.get())
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}