package es.deusto.sd.ecoembes.external;

import java.time.LocalDate;

public class ExternalPlantCapacityDTO {
    private long id;
    private String name;
    private String location;
    private LocalDate date;
    private float capacity;

    public ExternalPlantCapacityDTO() {}

    public ExternalPlantCapacityDTO(long id, String name, String location, LocalDate date, float capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.capacity = capacity;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }
}
