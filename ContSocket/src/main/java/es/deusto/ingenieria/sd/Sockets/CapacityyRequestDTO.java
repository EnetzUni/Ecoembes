package es.deusto.ingenieria.sd.Sockets;

public class CapacityyRequestDTO { // Nota: Tiene dos 'y' como en tu archivo original
    private long plantId;
    private String date; // <--- CAMBIO: String

    public CapacityyRequestDTO() {}
    public CapacityyRequestDTO(long plantId, String date) {
        this.plantId = plantId;
        this.date = date;
    }

    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}