package es.deusto.sd.ecoembes.client.proxies;

import java.util.List;
import java.util.Map;

import es.deusto.sd.ecoembes.client.model.*; 

public interface IEcoembesServiceProxy {
    
    // 1. LOGIN: Este es el ÚNICO que trata con Mapas (porque devuelve Token + ID)
    Map<String, Object> login(Credentials credentials);
    
    void logout(String token);

    // 2. DUMPSTERS: ¡OJO! Aquí estaba tu error. Debe recibir String, NO Map.
    List<Dumpster> getDumpsters(String token);

    // 3. PLANTS
    List<RecyclingPlant> getPlants(String token);

    // 4. CREATE DUMPSTER
    Dumpster createDumpster(Dumpster dumpster, String token);

    // 5. CHECK CAPACITY
    float getPlantCapacity(long plantId, String date, String token);

    // 6. ASSIGN DUMPSTERS
    // Recuerda que aquí añadimos employeeId para que funcione la asignación
    void createAssignment(long plantId, List<Long> dumpsterIds, Long employeeId, String token);
    
    List<Assignment> getAssignments(String token);
}