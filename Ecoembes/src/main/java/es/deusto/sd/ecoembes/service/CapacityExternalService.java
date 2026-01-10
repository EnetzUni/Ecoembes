package es.deusto.sd.ecoembes.service;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.external.ExternalGatewayFactory;
import es.deusto.sd.ecoembes.external.IExternalRecyclingGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CapacityExternalService {

    private final ExternalGatewayFactory gatewayFactory;

    public CapacityExternalService(ExternalGatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
    }

    public Optional<Float> requestCapacity(long plantId, String date) {
        IExternalRecyclingGateway gateway = gatewayFactory.getGateway(plantId);
        return gateway.getCapacity(new CapacityRequestDTO(plantId, date));
    }

    // ✅ CORREGIDO: Recibe directamente los parámetros (sin objeto Assignment)
    public boolean notifyAssignment(long plantId, String date, int totalDumpsters, float totalWaste) {
        
        // 1. Pedimos el Gateway adecuado para este ID (Socket o REST)
        IExternalRecyclingGateway gateway = gatewayFactory.getGateway(plantId);

        // 2. Pasamos los datos directamente
        return gateway.notifyAssignment(plantId, date, totalDumpsters, totalWaste);
    }
}