package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.service.DailyPlantCapacityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/capacity")
public class SingleDailyPlantCapacityController {

    private final DailyPlantCapacityService service;

    public SingleDailyPlantCapacityController(DailyPlantCapacityService service) {
        this.service = service;
    }

    /**
     * GET /api/capacity?plantId=1&date=2025-11-30
     */

    @GetMapping
    public Optional<Float> getCapacity(
            @RequestParam("plantId") long plantId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date
    ) {
        return service.getCapacityByPlantAndDate(plantId, date);
    }
}
