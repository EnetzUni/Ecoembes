package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class RecyclingPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String location;

    // Histórico de capacidades diarias
    @OneToMany(mappedBy = "recyclingPlant", cascade = CascadeType.ALL)
    private List<DailyPlantCapacity> dailyCapacities;

    public RecyclingPlant() {}

    public RecyclingPlant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // -----------------------
    // Getters y Setters
    // -----------------------
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<DailyPlantCapacity> getDailyCapacities() { return dailyCapacities; }
    public void setDailyCapacities(List<DailyPlantCapacity> dailyCapacities) { this.dailyCapacities = dailyCapacities; }

    // -----------------------
    // Método para obtener capacidad del día actual
    // -----------------------
    public float getCapacityForToday() {
        LocalDate today = LocalDate.now();
        if (dailyCapacities != null) {
            for (DailyPlantCapacity dpc : dailyCapacities) {
                if (dpc.getDate().equals(today)) {
                    return dpc.getCapacity();
                }
            }
        }
        // Si no hay registro para hoy, asumimos 0
        return 0f;
    }

    // -----------------------
    // hashCode y equals
    // -----------------------
    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RecyclingPlant other = (RecyclingPlant) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "RecyclingPlant [id=" + id + ", name=" + name + ", location=" + location + "]";
    }
}
