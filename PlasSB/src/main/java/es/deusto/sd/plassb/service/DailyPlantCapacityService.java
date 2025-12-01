package es.deusto.sd.plassb.service;

import es.deusto.sd.plassb.dao.DailyPlantCapacityRepository;
import es.deusto.sd.plassb.entity.DailyPlantCapacity;

import org.springframework.stereotype.Service;

import java.util.Date;
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

    public Optional<Float> getCapacityByPlantAndDate(long plantId, Date date) {
        return repository.findByRecyclingPlantIdAndDate(plantId, date)
                         .map(DailyPlantCapacity::getCapacity);
    }
}
