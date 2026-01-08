package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    // RELACIÓN NUEVA: Una planta tiene muchos Assignments
    // 'mappedBy = "recyclingPlant"' indica que la clave ajena está en la clase Assignment
    @OneToMany(mappedBy = "recyclingPlant")
    private List<Assignment> assignments = new ArrayList<>();

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

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

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

    @Override
    public String toString() {
        return "RecyclingPlant [id=" + id + ", name=" + name + ", location=" + location + "]";
    }
}