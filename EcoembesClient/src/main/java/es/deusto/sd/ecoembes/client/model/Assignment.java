package es.deusto.sd.ecoembes.client.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Assignment(
    long id, 
    Date date,
    
    // CAMBIO 1: Leemos el objeto completo (JSON: "employee": {...})
    @JsonProperty("employee") Map<String, Object> employeeData,
    
    // CAMBIO 2: Leemos el objeto completo (JSON: "recyclingPlant" o "plant")
    // Usamos JsonAlias por si el servidor lo llama de una forma u otra
    @JsonProperty("recyclingPlant") Map<String, Object> plantData,
    
    List<Dumpster> dumpsters
) {

    // --- TRUCO DE COMPATIBILIDAD ---
    // Creamos estos métodos manuales para que MenuController no se rompa
    // y para extraer el ID real del mapa.

    public long employeeId() {
        if (employeeData != null && employeeData.get("id") instanceof Number n) {
            return n.longValue();
        }
        return 0L; // Si viene null
    }

    public long recyclingPlantId() {
        if (plantData != null && plantData.get("id") instanceof Number n) {
            return n.longValue();
        }
        return 0L;
    }
}