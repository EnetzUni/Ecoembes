package es.deusto.sd.ecoembes.entity;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(optional = false)
    private Employee employee;

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

    public Assignment(Date date, Employee employee, Dumpster dumpster) {
        this.date = date;
        this.employee = employee;
        this.dumpster = dumpster;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //

    public long getId() { return id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Dumpster getDumpster() { return dumpster; }
    public void setDumpster(Dumpster dumpster) { this.dumpster = dumpster; }

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

    // -------- //
    // toString //
    // -------- //

    @Override
    public String toString() {
        return "Assignment [id=" + id + ", date=" + date + ", employee=" + employee 
                + ", dumpster=" + dumpster + "]";
    }
}
