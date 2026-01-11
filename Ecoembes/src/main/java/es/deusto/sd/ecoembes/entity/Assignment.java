package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String date;

    @ManyToOne(optional = false)
    private Employee employee;

    // Un assignment pertenece a una planta
    @ManyToOne(optional = true)
    @JoinColumn(name = "recycling_plant_id")
    private RecyclingPlant recyclingPlant;

    // Un assignment puede tener VARIOS dumpsters
    @OneToMany
    @JoinColumn(name = "assignment_id") // FK en la tabla dumpster
    private List<Dumpster> dumpsters = new ArrayList<>();

    public Assignment() {}

    //
    public Assignment(String date, Employee employee, RecyclingPlant recyclingPlant, List<Dumpster> dumpsters) {
        this.date = date;
        this.employee = employee;
        this.recyclingPlant = recyclingPlant;
        this.dumpsters = dumpsters;
    }

    public long getId() { return id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public RecyclingPlant getRecyclingPlant() { return recyclingPlant; }
    public void setRecyclingPlant(RecyclingPlant recyclingPlant) { this.recyclingPlant = recyclingPlant; }

    public List<Dumpster> getDumpsters() { return dumpsters; }
    public void setDumpsters(List<Dumpster> dumpsters) { this.dumpsters = dumpsters; }


    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Assignment)) return false;
        return id == ((Assignment) obj).id;
    }
}
