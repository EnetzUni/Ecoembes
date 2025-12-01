package es.deusto.sd.ecoembes.dto;

import java.util.Date;

public class CapacityResponseDTO {
    private long plantId;
    private float capacity;
    private Date date;

    public CapacityResponseDTO() {}

    public CapacityResponseDTO(long plantId, float capacity, Date date) {
        this.plantId = plantId;
        this.capacity = capacity;
        this.date = date;
    }

    // Getters & Setters
    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
