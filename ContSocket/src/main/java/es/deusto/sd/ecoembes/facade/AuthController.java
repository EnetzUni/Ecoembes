package es.deusto.sd.ecoembes.facade;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dto.CredentialsDTO;
import es.deusto.sd.ecoembes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization Controller", description = "Login and logout operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login endpoint
    @Operation(
        summary = "Login to the system",
        description = "Allows an employee to login using email and password. Returns a token if successful.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Login successful, returns a token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials, login failed"),
        }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee credentials", required = true)
            @RequestBody CredentialsDTO credentials) {
        
        Optional<String> token = authService.login(credentials.getEmail(), credentials.getPassword());

        return token.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    // Logout endpoint
    @Operation(
        summary = "Logout from the system",
        description = "Allows an employee to logout by providing the authorization token.",
        responses = {
            @ApiResponse(responseCode = "204", description = "No Content: Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token, logout failed"),
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Authorization token in plain text", required = true)
            @RequestBody String token) {

        Optional<Boolean> result = authService.logout(token);

        return (result.isPresent() && result.get())
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
