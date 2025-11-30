package es.deusto.sd.ecoembes.external;

import org.springframework.stereotype.Service;


@Service
public class AssignmentExternalService {

    private final ExternalGatewayFactory gatewayFactory;

    public AssignmentExternalService(ExternalGatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
    }

    public boolean notifyAssignment(long plantId, long assignmentId, int numDumpsters) {

        IExternalRecyclingGateway gateway =
                gatewayFactory.getGateway(ExternalGatewayType.SOCKET); // o REST

        ExternalAssignmentDTO dto =
                new ExternalAssignmentDTO(assignmentId, plantId, numDumpsters);

        return gateway.sendAssignment(dto);
    }
}
