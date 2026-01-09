package es.deusto.sd.plassb.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
// Quitamos los imports de Date y Temporal porque ahora es un String simple

@Entity
public class DailyPlantCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // CAMBIO: Ahora es String. Guardará "2025-01-01" tal cual.
    private String date;

    private float capacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plant_id")
    private RecyclingPlant recyclingPlant;

    public DailyPlantCapacity() {}

    // CAMBIO EN CONSTRUCTOR
    public DailyPlantCapacity(String date, float capacity, RecyclingPlant recyclingPlant) {
        this.date = date;
        this.capacity = capacity;
        this.recyclingPlant = recyclingPlant;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // CAMBIO EN GETTER/SETTER
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }

    public RecyclingPlant getRecyclingPlant() { return recyclingPlant; }
    public void setRecyclingPlant(RecyclingPlant recyclingPlant) { this.recyclingPlant = recyclingPlant; }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DailyPlantCapacity)) return false;
        return id == ((DailyPlantCapacity) obj).id;
    }

    @Override
    public String toString() {
        return "DailyPlantCapacity [id=" + id + ", date=" + date + ", capacity=" + capacity + "]";
    }
}