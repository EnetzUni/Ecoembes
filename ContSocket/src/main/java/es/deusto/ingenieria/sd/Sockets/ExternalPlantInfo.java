package es.deusto.ingenieria.sd.Sockets;

public class ExternalPlantInfo {

    private long id;
    private String name;
    private String location;
    private float capacity;

    public ExternalPlantInfo() {}

    public ExternalPlantInfo(long id, String name, String location, float capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public float getCapacity() { return capacity; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setCapacity(float capacity) { this.capacity = capacity; }
}
