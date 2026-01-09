package es.deusto.sd.ecoembes.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String date;

    @ManyToOne(optional = false)
    private Employee employee;

    // Relación con Planta
    @ManyToOne(optional = true) 
    @JoinColumn(name = "recycling_plant_id", nullable = true)
    private RecyclingPlant recyclingPlant;

    // Relación con Contenedor
    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "dumpster_id")
    private Dumpster dumpster;

    public Assignment() {}

    // --- CONSTRUCTOR CORREGIDO (Ahora acepta los 4 campos) ---
    public Assignment(String date, Employee employee, Dumpster dumpster, RecyclingPlant recyclingPlant) {
        this.date = date;
        this.employee = employee;
        this.dumpster = dumpster;
        this.recyclingPlant = recyclingPlant;
    }

    // Getters y Setters
    public long getId() { return id; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public RecyclingPlant getRecyclingPlant() { return recyclingPlant; }
    public void setRecyclingPlant(RecyclingPlant recyclingPlant) { this.recyclingPlant = recyclingPlant; }

    public Dumpster getDumpster() { return dumpster; }
    public void setDumpster(Dumpster dumpster) { this.dumpster = dumpster; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Assignment)) return false;
        return id == ((Assignment) obj).id;
    }
}