package es.deusto.sd.plassb.service;

import es.deusto.sd.plassb.dao.DailyPlantCapacityRepository;
import es.deusto.sd.plassb.entity.DailyPlantCapacity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DailyPlantCapacityService {

    private final DailyPlantCapacityRepository repository;

    public DailyPlantCapacityService(DailyPlantCapacityRepository repository) {
        this.repository = repository;
    }

    public List<DailyPlantCapacity> getAll() {
        return repository.findAll();
    }

    public DailyPlantCapacity save(DailyPlantCapacity capacity) {
        return repository.save(capacity);
    }

    // El método debe recibir String y pasarlo tal cual
    public Optional<Float> getCapacityByPlantAndDate(long plantId, String date) {
        return repository.findByRecyclingPlantIdAndDate(plantId, date) // <--- Aquí pasas el String
                         .map(DailyPlantCapacity::getCapacity);
    }
}
