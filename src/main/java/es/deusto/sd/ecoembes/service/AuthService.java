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

    // Almacena los tokens de sesión de los empleados conectados
    private static Map<String, Employee> tokenStore = new HashMap<>();

    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Login: devuelve token si credenciales correctas
    public Optional<String> login(String email, String password) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isPresent() && employee.get().checkPassword(password)) {
            String token = generateToken();  // generar token único
            tokenStore.put(token, employee.get());
            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }

    // Logout: elimina token de sesión
    public Optional<Boolean> logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return Optional.of(true);
        } else {
            return Optional.empty();
        }
    }

    // Obtener empleado a partir del token
    public Employee getEmployeeByToken(String token) {
        return tokenStore.get(token);
    }

    // Método sincronizado para generar token único
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
}

