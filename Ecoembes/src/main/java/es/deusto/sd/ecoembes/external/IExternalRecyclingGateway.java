package es.deusto.sd.ecoembes.external;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO;

import java.util.Optional;

public interface IExternalRecyclingGateway {
    Optional<Float> getCapacity(CapacityRequestDTO request);
}
