package es.deusto.ingenieria.sd.Sockets;

public class ExternalAssignmentDTO {

    private long assignmentId;
    private long plantId;
    private int numDumpsters;

    public ExternalAssignmentDTO() {}

    public ExternalAssignmentDTO(long assignmentId, long plantId, int numDumpsters) {
        this.assignmentId = assignmentId;
        this.plantId = plantId;
        this.numDumpsters = numDumpsters;
    }

    public long getAssignmentId() { return assignmentId; }
    public long getPlantId() { return plantId; }
    public int getNumDumpsters() { return numDumpsters; }

    public void setAssignmentId(long assignmentId) { this.assignmentId = assignmentId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }
    public void setNumDumpsters(int numDumpsters) { this.numDumpsters = numDumpsters; }
}