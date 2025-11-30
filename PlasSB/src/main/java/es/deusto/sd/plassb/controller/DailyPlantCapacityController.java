package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.entity.DailyPlantCapacity;
import es.deusto.sd.plassb.service.DailyPlantCapacityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capacities")
public class DailyPlantCapacityController {

    private final DailyPlantCapacityService service;

    public DailyPlantCapacityController(DailyPlantCapacityService service) {
        this.service = service;
    }

    @GetMapping
    public List<DailyPlantCapacity> getAll() {
        return service.getAll();
    }

    @PostMapping
    public DailyPlantCapacity create(@RequestBody DailyPlantCapacity capacity) {
        return service.save(capacity);
    }
}
