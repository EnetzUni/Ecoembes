package es.deusto.sd.ecoembes.dto;

public class DumpsterDTO {

    private long id;
    private String location;
    private float maxCapacity;

    public DumpsterDTO() {}

    public DumpsterDTO(long id, String location, float maxCapacity) {
        this.id = id;
        this.location = location;
        this.maxCapacity = maxCapacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}