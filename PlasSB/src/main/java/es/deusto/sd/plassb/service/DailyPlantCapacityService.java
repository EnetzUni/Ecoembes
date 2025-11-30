package es.deusto.sd.plassb.service;

import es.deusto.sd.plassb.entity.DailyPlantCapacity;
import es.deusto.sd.plassb.repository.DailyPlantCapacityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
