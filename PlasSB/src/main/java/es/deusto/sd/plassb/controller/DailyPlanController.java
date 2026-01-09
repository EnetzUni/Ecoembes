package es.deusto.sd.plassb.controller; // O el paquete que usen en PlasSB

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/plans") // <--- Coincide con tu PLAN_URL
public class DailyPlanController {

    @PostMapping
    public ResponseEntity<String> receiveDailyPlan(@RequestBody Map<String, Object> planData) {
        // Como no usamos DTO, sacamos los datos del Mapa por su nombre
        // OJO: Los nombres (keys) deben ser idénticos a los que pusiste en el Map de Ecoembes
        System.out.println("📩 PLASSB: Plan Diario Recibido");
        System.out.println(" - Planta ID: " + planData.get("plantId"));
        System.out.println(" - Fecha: " + planData.get("date"));
        System.out.println(" - Contenedores: " + planData.get("totalDumpsters"));
        System.out.println(" - Basura Total: " + planData.get("totalWaste"));

        // Aquí PlasSB guardaría los datos en su base de datos...
        
        return ResponseEntity.ok("Plan recibido correctamente");
    }
}