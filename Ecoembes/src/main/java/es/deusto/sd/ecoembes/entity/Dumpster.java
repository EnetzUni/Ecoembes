package es.deusto.sd.ecoembes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Dumpster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String location;

    private float maxCapacity;

    // ---------- //
    // Constructores
    // ---------- //

    public Dumpster() {}

    public Dumpster(String location, float maxCapacity) {
        this.location = location;
        this.maxCapacity = maxCapacity;
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

    @Override
    public String toString() {
        return "Dumpster [id=" + id + ", location=" + location + ", maxCapacity=" + maxCapacity + "]";
    }
}