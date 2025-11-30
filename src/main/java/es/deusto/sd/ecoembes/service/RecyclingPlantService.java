package es.deusto.sd.ecoembes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class RecyclingPlantService {

    private final RecyclingPlantRepository recyclingPlantRepository;

    public RecyclingPlantService(RecyclingPlantRepository recyclingPlantRepository) {
        this.recyclingPlantRepository = recyclingPlantRepository;
    }

    // Obtener todas las plantas
    public List<RecyclingPlant> getAllPlants() {
        return recyclingPlantRepository.findAll();
    }

    // Buscar una planta por ID
    public RecyclingPlant getPlantById(long id) {
        return recyclingPlantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recycling plant not found"));
    }

    // Crear o actualizar una planta
    public RecyclingPlant saveOrUpdatePlant(RecyclingPlant plant) {
        return recyclingPlantRepository.save(plant);
    }

    //FUNCION: CHECK RECYCLING PLANT CAPACITY
    public float getCapacity(long plantId) {
        RecyclingPlant plant = getPlantById(plantId);
        return plant.getCapacityForToday(); // método en la entidad
    }
}

