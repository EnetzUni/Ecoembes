package es.deusto.sd.ecoembes.client.swing;

import java.util.List;
import java.util.stream.Collectors;

import es.deusto.sd.ecoembes.client.data.*;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;

public class SwingClientController {

    private IEcoembesServiceProxy serviceProxy = new HttpServiceProxy();
    private String token; // Guarda la "identidad del empleado"

    // 1. Login
    public boolean login(String email, String password) {
        try {
            this.token = serviceProxy.login(new Credentials(email, password));
            return true;
        } catch (Exception e) {
            return false;
        }
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

    // 4. Consultar capacidad de una planta específica
    public float checkPlantCapacity(RecyclingPlant plant) {
        if (token == null) throw new RuntimeException("No autenticado");
        return serviceProxy.getPlantCapacity(plant.id(), token);
    }

    // 5. Asignar contenedores a planta
    public void assignDumpsters(RecyclingPlant selectedPlant, List<Dumpster> selectedDumpsters) {
        if (token == null) throw new RuntimeException("No autenticado");
        
        // Extraemos solo los IDs para enviar al proxy
        List<Long> ids = selectedDumpsters.stream()
                                          .map(Dumpster::id)
                                          .collect(Collectors.toList());
        
        serviceProxy.createAssignment(selectedPlant.id(), ids, token);
    }
    
    public void logout() {
        if (token != null) serviceProxy.logout(token);
    }
}