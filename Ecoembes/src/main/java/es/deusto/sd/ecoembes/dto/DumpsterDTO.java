package es.deusto.sd.ecoembes.dto;

import java.util.Date;
import java.util.List;

public class DumpsterDTO {

    private long id;
    private String location;
    private float maxCapacity;
    
    // Campos añadidos para dar coherencia al Service
    private float currentFillLevel;
    private Date lastUpdate;
    private List<FillLevelRecordDTO> history;

    public DumpsterDTO() {}

    // Constructor completo usado por el Service
    public DumpsterDTO(long id, String location, float maxCapacity, 
                       float currentFillLevel, Date lastUpdate, List<FillLevelRecordDTO> history) {
        this.id = id;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentFillLevel = currentFillLevel;
        this.lastUpdate = lastUpdate;
        this.history = history;
    }

    // ---------- //
    // Getters & Setters
    // ---------- //

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public float getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(float maxCapacity) { this.maxCapacity = maxCapacity; }

    public float getCurrentFillLevel() { return currentFillLevel; }
    public void setCurrentFillLevel(float currentFillLevel) { this.currentFillLevel = currentFillLevel; }

    public Date getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public List<FillLevelRecordDTO> getHistory() { return history; }
    public void setHistory(List<FillLevelRecordDTO> history) { this.history = history; }

    @Override
    public String toString() {
        return "DumpsterDTO [id=" + id + ", location=" + location + 
               ", currentFillLevel=" + currentFillLevel + "]";
    }
}