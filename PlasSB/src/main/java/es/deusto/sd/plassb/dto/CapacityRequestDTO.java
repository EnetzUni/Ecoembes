package es.deusto.sd.plassb.dto;

public class CapacityRequestDTO {
    private long plantId;
    private String date; // <--- CAMBIO: Antes era Date

    public CapacityRequestDTO() {}
    
    // Constructor actualizado
    public CapacityRequestDTO(long plantId, String date) {
        this.plantId = plantId;
        this.date = date;
    }

    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    // Getter y Setter actualizados
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}