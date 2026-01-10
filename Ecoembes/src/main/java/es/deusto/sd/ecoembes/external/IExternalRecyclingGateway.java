package es.deusto.sd.ecoembes.external;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import java.util.Optional;

public interface IExternalRecyclingGateway {
    // Definimos el método igual que en tu ejemplo
    public Optional<Float> getCapacity(CapacityRequestDTO request);

    boolean sendDailyPlan(long plantId, String date, int totalDumpsters, float totalWaste);
}