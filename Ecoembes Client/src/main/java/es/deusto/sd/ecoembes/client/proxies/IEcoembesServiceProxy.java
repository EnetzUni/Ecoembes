package es.deusto.sd.ecoembes.client.proxies;

import java.util.List;
import es.deusto.sd.ecoembes.client.data.*; // Tus records: Dumpster, Assignment, etc.

public interface IEcoembesServiceProxy {
    // 1. Login (Req: Identify employee for auditing)
    String login(Credentials credentials);
    void logout(String token);

    // 2. Download Dumpster Info (Req: locations, fill levels)
    List<Dumpster> getDumpsters(String token);

    // 3. Get Plants (Req: select any recycling plant)
    List<RecyclingPlant> getPlants(String token);

    // 4. Check Capacity (Req: check its available capacity for current day)
    // Devuelve un float con la capacidad disponible en la planta para la fecha dada
    float getPlantCapacity(long plantId, String date, String token);

    // 5. Assign Dumpsters (Req: assign dumpsters... automatically receives notification)
    // Enviamos la lista de IDs de contenedores y la planta seleccionada
    void createAssignment(long plantId, List<Long> dumpsterIds, String token);
}