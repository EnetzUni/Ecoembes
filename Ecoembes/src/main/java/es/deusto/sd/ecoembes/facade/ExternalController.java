package es.deusto.sd.ecoembes.facade;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.service.CapacityExternalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/external") // <--- Esto define la primera parte de la URL
public class ExternalController {

    private final CapacityExternalService service;

    public ExternalController(CapacityExternalService service) {
        this.service = service;
    }

    @PostMapping("/capacity") // <--- Esto define la segunda parte (lo que te da error 404)
    public ResponseEntity<Float> checkCapacity(@RequestBody CapacityRequestDTO request) {
        
        // Aquí conectamos la URL con tu lógica interna (Factory -> Gateway -> PlasSB)
        Optional<Float> result = service.requestCapacity(request.getPlantId(), request.getDate());

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/notify-plan")
    public ResponseEntity<String> testNotifyDailyPlan(@RequestBody Map<String, Object> body) {
        
        // Sacamos los datos del JSON que nos mandes por Postman
        // (Hacemos cast porque el Map devuelve Object)
        Integer plantId = (Integer) body.get("plantId");
        String date = (String) body.get("date");
        Integer dumpsters = (Integer) body.get("totalDumpsters");
        
        // Cuidado: los decimales en JSON a veces vienen como Double
        Number wasteNum = (Number) body.get("totalWaste"); 
        float totalWaste = wasteNum.floatValue();

        // LLAMAMOS AL SERVICIO (La lógica real)
        boolean result = service.notifyDailyWork(plantId, date, dumpsters, totalWaste);

        if (result) {
            return ResponseEntity.ok("✅ ¡Éxito! La planta confirmó la recepción.");
        } else {
            return ResponseEntity.status(500).body("❌ Error: La planta no responde o dió error.");
        }
   }    
}