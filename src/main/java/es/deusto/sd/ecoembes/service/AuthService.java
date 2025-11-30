package es.deusto.sd.ecoembes.service;

import java.util.*;
import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.entity.Employee;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final Map<String, Employee> loggedInTokens = new HashMap<>();

    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //FUNCION: LOGIN 
    public String login(String email, String password) {
        Employee emp = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (!emp.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        String token = String.valueOf(System.currentTimeMillis());
        loggedInTokens.put(token, emp);
        return token;
    }

    //FUNCION: LOGOUT
    public void logout(String token) {
        loggedInTokens.remove(token);
    }

    public Employee getEmployeeByToken(String token) {
        return loggedInTokens.get(token);
    }

    public boolean isLogged(String token) {
        return loggedInTokens.containsKey(token);
    }
}
