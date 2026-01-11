package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    //vale, he añadido esto q si no es imposible asignar dumpsters xq pone x defecto 0 asiq nunca tienen espacio
    private float capacity;
    
    
    //cambaido a uno a muchos
    @OneToMany(mappedBy = "recyclingPlant")
    @JsonIgnore // <--- AÑADE ESTO AQUÍ
    private List<Assignment> assignments = new ArrayList<>();


    public RecyclingPlant() {}

    public RecyclingPlant(String name, String location, float capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }
    

    
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

 
    public float getCapacity() { return capacity; }
    public void setCapacity(float capacity) { this.capacity = capacity; }


    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }




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
        return "RecyclingPlant [id=" + id + ", name=" + name + ", location=" + location + ", capacity=" + capacity + "]";
    }
}
