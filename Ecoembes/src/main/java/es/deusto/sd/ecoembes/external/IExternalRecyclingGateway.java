package es.deusto.sd.ecoembes.external;

import java.util.Optional;

public interface IExternalRecyclingGateway {
    boolean sendAssignment(ExternalAssignmentDTO dto);

    Optional<ExternalPlantInfo> getPlantInfo(long plantId);

    ExternalAssignmentDTO getAssignmentInfo(String plantId);
}