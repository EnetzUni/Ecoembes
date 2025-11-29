package es.deusto.sd.ecoembes.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Dumpster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String location;

    private float maxCapacity;

    private float fillLevel;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // -------------------------
    // RELACIÓN CON ASSIGNMENT
    // -------------------------
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    // -------------------------

    // No-arg constructor required by JPA
    public Dumpster() {}

    public Dumpster(long id, String location, float maxCapacity, float fillLevel, Date date) {
        this.id = id;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.fillLevel = fillLevel;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public float getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(float fillLevel) {
        this.fillLevel = fillLevel;
    }

    // Getter / Setter de Assignment
    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Dumpster other = (Dumpster) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "Dumpster [id=" + id + ", location=" + location + ", maxCapacity=" + maxCapacity 
                + ", fillLevel=" + fillLevel + ", date=" + date + "]";
    }
}
