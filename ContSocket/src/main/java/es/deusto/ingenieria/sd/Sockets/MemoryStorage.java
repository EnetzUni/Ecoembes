package es.deusto.ingenieria.sd.Sockets;

import java.util.HashMap;
import java.util.Map;

public class MemoryStorage {

    private static final Map<Long, ExternalPlantInfo> plants = new HashMap<>();
    private static final Map<Long, ExternalAssignmentDTO> assignments = new HashMap<>();

    static {
        plants.put(1L, new ExternalPlantInfo(1, "Plant Alpha", "Madrid", 5000));
        plants.put(2L, new ExternalPlantInfo(2, "Plant Beta", "Bilbao", 3000));
    }

    public static ExternalPlantInfo getPlant(long id) {
        return plants.get(id);
    }

    public static void saveAssignment(ExternalAssignmentDTO dto) {
        assignments.put(dto.getAssignmentId(), dto);
    }
}
