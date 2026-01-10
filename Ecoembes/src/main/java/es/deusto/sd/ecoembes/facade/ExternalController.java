package es.deusto.sd.ecoembes.facade;

import es.deusto.sd.ecoembes.service.CapacityExternalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/external")
public class ExternalController {

    private final CapacityExternalService capacityService;

    public ExternalController(CapacityExternalService capacityService) {
        this.capacityService = capacityService;
    }

    // GET: http://localhost:8082/api/external/capacity?plantId=1&date=2025-01-20
    @GetMapping("/capacity")
    public ResponseEntity<?> checkCapacity(
    @RequestParam("plantId") long plantId, 
    @RequestParam("date") String date
        )    {
        
        Optional<Float> capacity = capacityService.requestCapacity(plantId, date);

        if (capacity.isPresent()) {
            return ResponseEntity.ok(Map.of("plantId", plantId, "capacity", capacity.get(), "status", "OK"));
        } else {
            return ResponseEntity.status(404).body("No se pudo conectar con la planta " + plantId);
        }
    }

    // POST: http://localhost:8082/api/external/notify-test
    // Body JSON: { "plantId": 2, "date": "2025-01-20", "totalDumpsters": 5, "totalWaste": 100.5 }
    @PostMapping("/notify-test")
    public ResponseEntity<?> testNotify(@RequestBody Map<String, Object> body) {
        try {
            // Extraemos los datos del JSON manualmente para pasarlos al servicio
            // OJO con los tipos de datos al sacarlos del Map (Integer vs Long vs Double)
            
            long plantId = ((Number) body.get("plantId")).longValue();
            String date = (String) body.get("date");
            
            // Si no vienen en el JSON, ponemos valores por defecto
            int totalDumpsters = body.containsKey("totalDumpsters") ? ((Number) body.get("totalDumpsters")).intValue() : 1;
            float totalWaste = body.containsKey("totalWaste") ? ((Number) body.get("totalWaste")).floatValue() : 0.0f;

            // ✅ Llamamos al servicio con los parámetros primitivos
            boolean success = capacityService.notifyAssignment(plantId, date, totalDumpsters, totalWaste);

            if (success) {
                return ResponseEntity.ok("✅ Notificación enviada con éxito.");
            } else {
                return ResponseEntity.status(500).body("❌ Fallo al notificar a la planta externa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error en los datos enviados: " + e.getMessage());
        }
    }
}