package es.deusto.sd.ecoembes.client.model;

public record RecyclingPlant(long id, String name, String location, float capacity) {
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public float getCapacity() {
        return capacity;
    }
}
