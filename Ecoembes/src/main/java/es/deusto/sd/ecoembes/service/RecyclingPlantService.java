package es.deusto.sd.ecoembes.service;

import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

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
