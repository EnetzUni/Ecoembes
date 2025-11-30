package es.deusto.sd.ecoembes.external;

public interface IExternalRecyclingGateway {
    boolean sendAssignment(ExternalAssignmentDTO dto);
}

