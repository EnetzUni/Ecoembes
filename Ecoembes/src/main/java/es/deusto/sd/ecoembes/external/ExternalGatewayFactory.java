package es.deusto.sd.ecoembes.external;

import org.springframework.stereotype.Component;

@Component
public class ExternalGatewayFactory {

    private final RestRecyclingGateway restGateway;
    private final SocketRecyclingGateway socketGateway;

    // Spring inyecta ambas implementaciones
    public ExternalGatewayFactory(RestRecyclingGateway restGateway, 
                                  SocketRecyclingGateway socketGateway) {
        this.restGateway = restGateway;
        this.socketGateway = socketGateway;
    }

    // --- CORRECCIÓN: Usamos 'long' en vez de 'ExternalGatewayType' ---
    public IExternalRecyclingGateway getGateway(long plantId) {
        // ID 1 -> Sockets
        // ID 2 (o resto) -> REST
        if (plantId == 1) {
            return socketGateway;
        } else {
            return restGateway;
        }
    }
}