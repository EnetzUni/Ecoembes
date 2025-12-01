package es.deusto.sd.ecoembes.entity;

import java.util.Date;
import java.util.Objects;

public class DailyPlantCapacity {

    private long id;
    private Date date;
    private float capacity;
    private RecyclingPlant recyclingPlant;

    // ------------ //
    // Constructors //
    // ------------ //

    public DailyPlantCapacity() {}

    public DailyPlantCapacity(Date date, float capacity, RecyclingPlant recyclingPlant) {
        this.date = date;
        this.capacity = capacity;
        this.recyclingPlant = recyclingPlant;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }

    public RecyclingPlant getRecyclingPlant() { return recyclingPlant; }
    public void setRecyclingPlant(RecyclingPlant recyclingPlant) { this.recyclingPlant = recyclingPlant; }

    // ----------------- //
    // hashCode & Equals //
    // ----------------- //

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