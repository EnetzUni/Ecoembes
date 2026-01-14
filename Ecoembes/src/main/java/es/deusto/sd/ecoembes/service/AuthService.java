package es.deusto.sd.ecoembes.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.entity.Employee;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private static Map<String, Employee> tokenStore = new HashMap<>();

    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CAMBIO AQUÍ: Devolvemos un Mapa en lugar de un String
    public Optional<Map<String, Object>> login(String email, String password) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isPresent() && employee.get().checkPassword(password)) {
            String token = generateToken();
            tokenStore.put(token, employee.get());
            
            // Creamos el mapa con los datos que queremos enviar
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("employeeId", employee.get().getId());
            
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    // ... El resto de métodos (logout, generateToken, etc) se quedan igual ...
    
    public Optional<Boolean> logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return Optional.of(true);
        } else {
            return Optional.empty();
        }
    }

    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
}