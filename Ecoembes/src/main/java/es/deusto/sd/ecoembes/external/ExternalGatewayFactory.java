package es.deusto.sd.ecoembes.external;

import org.springframework.stereotype.Component;

@Component
public class ExternalGatewayFactory {

    private final RestRecyclingGateway restGateway;
    private final SocketRecyclingGateway socketGateway;

    public ExternalGatewayFactory(RestRecyclingGateway restGateway,
                                  SocketRecyclingGateway socketGateway) {
        this.restGateway = restGateway;
        this.socketGateway = socketGateway;
    }

    public IExternalRecyclingGateway getGateway(ExternalGatewayType type) {
        return switch (type) {
            case REST -> restGateway;
            case SOCKET -> socketGateway;
        };
    }
}
