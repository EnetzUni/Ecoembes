package es.deusto.sd.ecoembes.client.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.deusto.sd.ecoembes.client.model.*;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;

public class SwingClientController {

    private IEcoembesServiceProxy serviceProxy = new HttpServiceProxy();
    private Long currentEmployeeId;
    private String token; // Guarda la "identidad del empleado"

    // 1. Login
    public boolean login(String email, String password) {
        try {
            // Ahora recibimos un Mapa
            Map<String, Object> response = serviceProxy.login(new Credentials(email, password));
            
            if (response != null && response.containsKey("token")) {
                // 1. Guardamos el Token
                this.token = (String) response.get("token");
                
                // 2. Guardamos el ID (con seguridad de tipos)
                if (response.containsKey("employeeId")) {
                    Number idNum = (Number) response.get("employeeId");
                    this.currentEmployeeId = idNum.longValue();
                }
                
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Obtener Info Contenedores (Location, Fill Level...)
    public List<Dumpster> getAvailableDumpsters() {
        if (token == null) throw new RuntimeException("No autenticado");
        return serviceProxy.getDumpsters(token);
    }

    // 3. Obtener Plantas
    public List<RecyclingPlant> getRecyclingPlants() {
        if (token == null) throw new RuntimeException("No autenticado");
        return serviceProxy.getPlants(token);
    }

    // 4. Consultar capacidad de una planta específica para una fecha dada
    // pasando la fecha seleccionada en el UI (ej: "2025-01-20")
    public float checkPlantCapacity(RecyclingPlant plant, String date) {
        if (token == null) throw new RuntimeException("No autenticado");
        
        // Delegamos en el proxy pasando la fecha
        return serviceProxy.getPlantCapacity(plant.id(), date, token);
    }

    // 5. Asignar contenedores a planta
    public void assignDumpsters(RecyclingPlant selectedPlant, List<Dumpster> selectedDumpsters) {
        if (token == null) throw new RuntimeException("No autenticado");
        
        // Extraemos solo los IDs para enviar al proxy
        List<Long> ids = selectedDumpsters.stream()
                                          .map(Dumpster::id)
                                          .collect(Collectors.toList());
        
        serviceProxy.createAssignment(selectedPlant.id(), ids, currentEmployeeId, token);
    }

    // 6. Crear nuevo contenedor
    public void createNewDumpster(String location, float maxCapacity, int containerCount) {

        // Creamos un objeto temporal con ID 0 y fillLevel 0 (valores por defecto para creación)
        Dumpster newDumpster = new Dumpster(0, location, maxCapacity, containerCount, 0.0f);

        try {
            Dumpster created = serviceProxy.createDumpster(newDumpster, token);
            System.out.println("Contenedor creado con ID: " + created.id());
        } catch (Exception e) {
            System.err.println("Error al crear el contenedor: " + e.getMessage());
            // Aquí podrías lanzar la excepción hacia arriba si quieres mostrar un popup en la UI
        }
    }   
    
    public void logout() {
        if (token != null) serviceProxy.logout(token);
    }
}