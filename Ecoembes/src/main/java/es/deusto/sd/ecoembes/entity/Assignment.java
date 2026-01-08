package es.deusto.sd.ecoembes.entity;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // FECHA COMO STRING (Para evitar problemas de formato)
    private String date;

    @ManyToOne(optional = false)
    private Employee employee;

    // RELACIÓN BIDIRECCIONAL CON RECYCLINGPLANT
    // Este campo es el que busca el 'mappedBy' de RecyclingPlant
    @ManyToOne(optional = true) 
    @JoinColumn(name = "recycling_plant_id", nullable = true)
    private RecyclingPlant recyclingPlant;

    @ManyToMany
    @JoinTable(
        name = "assignment_dumpster",
        joinColumns = @JoinColumn(name = "assignment_id"),
        inverseJoinColumns = @JoinColumn(name = "dumpster_id")
    )
    private List<Dumpster> dumpsters;

    // ------------ //
    // Constructors //
    // ------------ //

    public Assignment() {}

    public Assignment(String date, Employee employee, List<Dumpster> dumpsters) {
        this.date = date;
        this.employee = employee;
        this.dumpsters = dumpsters;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //

    public long getId() { return id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public RecyclingPlant getRecyclingPlant() { return recyclingPlant; }
    public void setRecyclingPlant(RecyclingPlant recyclingPlant) { this.recyclingPlant = recyclingPlant; }

    public List<Dumpster> getDumpsters() { return dumpsters; }
    public void setDumpsters(List<Dumpster> dumpsters) { this.dumpsters = dumpsters; }

    // ----------------- //
    // hashCode & Equals //
    // ----------------- //

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Assignment)) return false;
        return id == ((Assignment) obj).id;
    }
}