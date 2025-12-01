package es.deusto.ingenieria.sd.Sockets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MemoryStorage {

    // Map<plantId, Map<date, CapacityResponseDTO>>
    private static final Map<Long, Map<Date, CapacityResponseDTO>> dailyCapacities = new HashMap<>();

    static {
        // Inicializamos con algunas plantas y capacidades de ejemplo
        Date today = new Date();
        CapacityResponseDTO plant1 = new CapacityResponseDTO(1L, 5000f, today);
        CapacityResponseDTO plant2 = new CapacityResponseDTO(2L, 3000f, today);

        Map<Date, CapacityResponseDTO> map1 = new HashMap<>();
        map1.put(today, plant1);
        dailyCapacities.put(1L, map1);

        Map<Date, CapacityResponseDTO> map2 = new HashMap<>();
        map2.put(today, plant2);
        dailyCapacities.put(2L, map2);
    }

    public static CapacityResponseDTO getCapacity(long plantId, Date date) {
        Map<Date, CapacityResponseDTO> plantCapacities = dailyCapacities.get(plantId);
        if (plantCapacities == null) return null;
        return plantCapacities.get(date);
    }

    public static void saveCapacity(CapacityResponseDTO capacity) {
        dailyCapacities.computeIfAbsent(capacity.getPlantId(), k -> new HashMap<>())
                .put(capacity.getDate(), capacity);
    }
}
