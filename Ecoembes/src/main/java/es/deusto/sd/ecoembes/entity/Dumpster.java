package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private Date lastUpdate;

    @OneToMany(mappedBy = "dumpster", cascade = CascadeType.ALL)
    private List<FillLevelRecord> fillHistory = new ArrayList<>();

    @ManyToMany(mappedBy = "dumpsters")
    private List<Assignment> assignments;

    // ------------ //
    // Constructors //
    // ------------ //

    public Dumpster() {}

    public Dumpster(String location, float maxCapacity) {
        this.location = location;
        this.maxCapacity = maxCapacity;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public float getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(float maxCapacity) { this.maxCapacity = maxCapacity; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }

    public Date getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public List<FillLevelRecord> getFillHistory() { return fillHistory; }
    public void setFillHistory(List<FillLevelRecord> fillHistory) { this.fillHistory = fillHistory; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

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
        if (!(obj instanceof Dumpster)) return false;
        return id == ((Dumpster) obj).id;
    }

    // -------- //
    // toString //
    // -------- //

    @Override
    public String toString() {
        return "Dumpster [id=" + id + ", location=" + location + ", maxCapacity=" + maxCapacity
                + ", currentFillLevel=" + fillLevel + ", lastUpdate=" + lastUpdate + "]";
    }
}
