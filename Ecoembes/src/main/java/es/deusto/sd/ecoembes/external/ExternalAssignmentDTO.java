package es.deusto.sd.ecoembes.external;

public class ExternalAssignmentDTO {
    public long assignmentId;
    public long plantId;
    public int dumpsterCount;

    public ExternalAssignmentDTO() {}

    public ExternalAssignmentDTO(long assignmentId, long plantId, int dumpsterCount) {
        this.assignmentId = assignmentId;
        this.plantId = plantId;
        this.dumpsterCount = dumpsterCount;
    }
}
