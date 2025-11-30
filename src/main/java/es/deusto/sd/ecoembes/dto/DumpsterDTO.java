package es.deusto.sd.ecoembes.dto;

import java.util.Date;
import java.util.List;

public class DumpsterDTO {
    private long id;
    private String location;
    private float maxCapacity;
    private float fillLevel;
    private Date lastUpdate;
    private List<FillLevelRecordDTO> fillHistory;

    public DumpsterDTO() {}

    public DumpsterDTO(long id, String location, float maxCapacity, float fillLevel, Date lastUpdate, List<FillLevelRecordDTO> fillHistory) {
        this.id = id;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.fillLevel = fillLevel;
        this.lastUpdate = lastUpdate;
        this.fillHistory = fillHistory;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public float getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(float maxCapacity) { this.maxCapacity = maxCapacity; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }

    public Date getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public List<FillLevelRecordDTO> getFillHistory() { return fillHistory; }
    public void setFillHistory(List<FillLevelRecordDTO> fillHistory) { this.fillHistory = fillHistory; }
}
