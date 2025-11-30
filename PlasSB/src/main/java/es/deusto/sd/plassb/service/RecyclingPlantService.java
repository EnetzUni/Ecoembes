package es.deusto.sd.plassb.service;

import es.deusto.sd.plassb.entity.RecyclingPlant;
import es.deusto.sd.plassb.repository.RecyclingPlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecyclingPlantService {

    private final RecyclingPlantRepository repository;

    public RecyclingPlantService(RecyclingPlantRepository repository) {
        this.repository = repository;
    }

    public List<RecyclingPlant> getAll() {
        return repository.findAll();
    }

    public RecyclingPlant save(RecyclingPlant plant) {
        return repository.save(plant);
    }
}
