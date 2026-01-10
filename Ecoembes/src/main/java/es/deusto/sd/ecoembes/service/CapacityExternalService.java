package es.deusto.sd.ecoembes.service;

import org.springframework.stereotype.Service;
import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.external.ExternalGatewayFactory;
import es.deusto.sd.ecoembes.external.ExternalGatewayType;
import es.deusto.sd.ecoembes.external.IExternalRecyclingGateway;

import java.util.Optional;

@Service
public class CapacityExternalService {

    private final ExternalGatewayFactory gatewayFactory;

    public CapacityExternalService(ExternalGatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
    }

    public Optional<Float> requestCapacity(long plantId, String date) {
        // 1. Pedimos a la factoría (que nos dará el RestRecyclingGateway de arriba)
        IExternalRecyclingGateway gateway = 
                gatewayFactory.getGateway(ExternalGatewayType.REST);

        // 2. Preparamos los datos
        CapacityRequestDTO request = new CapacityRequestDTO(plantId, date);

        // 3. Ejecutamos la llamada (que usará el código http del punto 2)
        return gateway.getCapacity(request);
    }

    public boolean notifyDailyWork(long plantId, String date, int dumpstersCount, float totalWaste) {
    
    IExternalRecyclingGateway gateway = 
            gatewayFactory.getGateway(ExternalGatewayType.REST);

    // Llamada directa con las variables
    boolean sent = gateway.sendDailyPlan(plantId, date, dumpstersCount, totalWaste);

    if (sent) {
        System.out.println("✅ Plan enviado.");
    }
    return sent;
}
}