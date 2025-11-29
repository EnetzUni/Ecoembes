package es.deusto.sd.ecoembes.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // Who makes the assignment
    @ManyToOne(optional = false)
    private Employee employee;

    // Destination recycling plant
    @ManyToOne(optional = false)
    private RecyclingPlant recyclingPlant;

    // Dumpsters assigned (each dumpster belongs to 1 assignment)
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<Dumpster> dumpsters;

    public Assignment() {}

    public Assignment(Date date, Employee employee, RecyclingPlant recyclingPlant) {
        this.date = date;
        this.employee = employee;
        this.recyclingPlant = recyclingPlant;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RecyclingPlant getRecyclingPlant() {
        return recyclingPlant;
    }

    public void setRecyclingPlant(RecyclingPlant recyclingPlant) {
        this.recyclingPlant = recyclingPlant;
    }

    public List<Dumpster> getDumpsters() {
        return dumpsters;
    }

    public void setDumpsters(List<Dumpster> dumpsters) {
        this.dumpsters = dumpsters;
    }
}
