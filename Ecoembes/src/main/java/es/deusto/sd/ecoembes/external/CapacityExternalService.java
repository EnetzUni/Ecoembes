package es.deusto.sd.ecoembes.external;

import org.springframework.stereotype.Service;
import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;

import java.util.Date;
import java.util.Optional;

@Service
public class CapacityExternalService {

    private final ExternalGatewayFactory gatewayFactory;

    public CapacityExternalService(ExternalGatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
    }

    /**
     * Solicita la capacidad de una planta para una fecha concreta usando sockets.
     */
    public Optional<Float> requestCapacity(long plantId, Date date) {

        IExternalRecyclingGateway gateway =
                gatewayFactory.getGateway(ExternalGatewayType.SOCKET);

        CapacityRequestDTO req = new CapacityRequestDTO(plantId, date);

        // Retorna directamente el Optional<Float> de la interfaz
        return gateway.getCapacity(req);
    }
}

