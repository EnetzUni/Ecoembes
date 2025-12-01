package es.deusto.sd.ecoembes.entity;

import java.util.List;
import java.util.Objects;

public class RecyclingPlant {

    private long id;
    private String name;
    private String location;

    private List<DailyPlantCapacity> dailyCapacities;

    // ------------ //
    // Constructors //
    // ------------ //

    public RecyclingPlant() {}

    public RecyclingPlant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //
    
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<DailyPlantCapacity> getDailyCapacities() { return dailyCapacities; }
    public void setDailyCapacities(List<DailyPlantCapacity> dailyCapacities) { this.dailyCapacities = dailyCapacities; }

    // ----------------- //
    // hashCode & Equals //
    // ----------------- //

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RecyclingPlant other = (RecyclingPlant) obj;
        return id == other.id;
    }

    // -------- //
    // toString //
    // -------- //

    @Override
    public String toString() {
        return "RecyclingPlant [id=" + id + ", name=" + name + ", location=" + location + "]";
    }
}
