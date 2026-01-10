package es.deusto.sd.ecoembes.external;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import java.util.Optional;

public interface IExternalRecyclingGateway {
    
    Optional<Float> getCapacity(CapacityRequestDTO request);

    // Mantenemos tus parámetros originales (primitivos), solo cambiamos el nombre
    boolean notifyAssignment(long plantId, String date, int totalDumpsters, float totalWaste);
}