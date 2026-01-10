package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.entity.DailyPlan;
import es.deusto.sd.plassb.dao.DailyPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plans") // Coincide con la URL que usa Ecoembes
public class DailyPlanController {

    private final DailyPlanRepository repository;

    // Inyectamos el repositorio (Conexión a BD)
    public DailyPlanController(DailyPlanRepository repository) {
        this.repository = repository;
    }

    // 1. RECIBIR Y GUARDAR EN ARCHIVO .DB (POST)
    @PostMapping
    public ResponseEntity<String> receiveDailyPlan(@RequestBody Map<String, Object> planData) {
        
        System.out.println("📩 PLASSB: Recibiendo plan para guardar en BD...");

        try {
            // Extraemos los datos del JSON con cuidado
            // (Los números en JSON a veces vienen como Integer o Double, por eso usamos Number)
            Long plantId = ((Number) planData.get("plantId")).longValue();
            String date = (String) planData.get("date");
            int dumpsters = ((Number) planData.get("totalDumpsters")).intValue();
            float waste = ((Number) planData.get("totalWaste")).floatValue();

            // Creamos la entidad
            DailyPlan newPlan = new DailyPlan(plantId, date, dumpsters, waste);

            // ¡GUARDAMOS EN H2 DE VERDAD!
            repository.save(newPlan);

            System.out.println("   💾 ¡Guardado en tabla DAILY_PLAN! ID generado: " + newPlan.getId());
            return ResponseEntity.ok("Plan guardado correctamente en la base de datos.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error guardando en BD: " + e.getMessage());
        }
    }

    // 2. VER LO QUE HAY EN LA BD (GET)
    @GetMapping
    public List<DailyPlan> getStoredPlans() {
        return repository.findAll();
    }
}