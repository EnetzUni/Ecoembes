package es.deusto.ingenieria.sd.Sockets;

import java.util.Date;

public class CapacityyRequestDTO {
    private long plantId;
    private Date date;

    public CapacityyRequestDTO() {}
    public CapacityyRequestDTO(long plantId, Date date) {
        this.plantId = plantId;
        this.date = date;
    }

    public long getPlantId() { return plantId; }
    public void setPlantId(long plantId) { this.plantId = plantId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}