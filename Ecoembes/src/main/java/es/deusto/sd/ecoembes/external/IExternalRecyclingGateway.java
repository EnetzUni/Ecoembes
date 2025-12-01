package es.deusto.sd.ecoembes.external;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;

import java.util.Optional;

public interface IExternalRecyclingGateway {
    Optional<Float> getCapacity(CapacityRequestDTO request);
}
