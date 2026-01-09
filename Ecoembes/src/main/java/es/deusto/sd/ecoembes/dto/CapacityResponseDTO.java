package es.deusto.sd.ecoembes.dto;

public class CapacityResponseDTO {
    
    private long plantId;
    private float capacity;
    private String date;

    public CapacityResponseDTO() {
    }

    public CapacityResponseDTO(long plantId, float capacity, String date) {
        this.plantId = plantId;
        this.capacity = capacity;
        this.date = date;
    }

    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}