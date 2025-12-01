package es.deusto.sd.ecoembes.dto;

import java.util.Date;

public class CapacityRequestDTO {
    private long plantId;
    private Date date;

    public CapacityRequestDTO() {}
    public CapacityRequestDTO(long plantId, Date date) {
        this.plantId = plantId;
        this.date = date;
    }

    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}