package es.deusto.ingenieria.sd.Sockets;

import java.util.HashMap;
import java.util.Map;

public class MemoryStorage {

    // Map<plantId, Map<dateString, CapacityResponseDTO>>
    private static final Map<Long, Map<String, CapacityResponseDTO>> dailyCapacities = new HashMap<>();

    static {
        // DATOS DE PRUEBA (Fechas como String para evitar problemas de hora)
        // Asegúrate de usar estas fechas al probar desde Ecoembes
        String today = "2025-01-10"; 
        
        CapacityResponseDTO plant1 = new CapacityResponseDTO(1L, 5000f, today);
        CapacityResponseDTO plant2 = new CapacityResponseDTO(2L, 3000f, today);
        CapacityResponseDTO plant3 = new CapacityResponseDTO(3L, 9999f, today); // PlasSB

        addCapacity(plant1);
        addCapacity(plant2);
        addCapacity(plant3);
    }

    private static void addCapacity(CapacityResponseDTO dto) {
        dailyCapacities.computeIfAbsent(dto.getPlantId(), k -> new HashMap<>())
                       .put(dto.getDate(), dto);
    }

    public static CapacityResponseDTO getCapacity(long plantId, String date) {
        Map<String, CapacityResponseDTO> plantCapacities = dailyCapacities.get(plantId);
        if (plantCapacities == null) return null;
        return plantCapacities.get(date);
    }
}