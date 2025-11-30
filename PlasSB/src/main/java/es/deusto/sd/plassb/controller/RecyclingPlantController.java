package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.entity.RecyclingPlant;
import es.deusto.sd.plassb.service.RecyclingPlantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class RecyclingPlantController {

    private final RecyclingPlantService service;

    public RecyclingPlantController(RecyclingPlantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecyclingPlant> getAll() {
        return service.getAll();
    }

    @PostMapping
    public RecyclingPlant create(@RequestBody RecyclingPlant plant) {
        return service.save(plant);
    }
}
