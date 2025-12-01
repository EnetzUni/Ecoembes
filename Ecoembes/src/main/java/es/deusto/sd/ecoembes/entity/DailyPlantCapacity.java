package es.deusto.sd.ecoembes.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class DailyPlantCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private float capacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plant_id")
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

    // -------- //
    // toString //
    // -------- //

    @Override
    public String toString() {
        return "DailyPlantCapacity [id=" + id + ", date=" + date + ", capacity=" + capacity + "]";
    }
}
