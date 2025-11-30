package es.deusto.sd.ecoembes.external;

import org.springframework.stereotype.Component;

@Component
public class ExternalGatewayFactory {

    private final RestRecyclingGateway restGateway;
    private final SocketGateway socketGateway;

    public ExternalGatewayFactory(RestRecyclingGateway restGateway,
                                  SocketGateway socketGateway) {
        this.restGateway = restGateway;
        this.socketGateway = socketGateway;
    }

    public IExternalRecyclingGateway getGateway(ExternalGatewayType type) {
        switch (type) {
            case REST:
                return restGateway;
            case SOCKET:
                return socketGateway;
            default:
                throw new IllegalArgumentException("Unsupported external gateway type");
        }
    }
}
