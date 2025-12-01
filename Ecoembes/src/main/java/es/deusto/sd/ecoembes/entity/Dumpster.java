package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Dumpster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String location;

    private float maxCapacity;

    @OneToMany(mappedBy = "dumpster", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public List<FillLevelRecord> getFillHistory() { return fillHistory; }
    public void setFillHistory(List<FillLevelRecord> fillHistory) { this.fillHistory = fillHistory; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

    // Convenience method to add a fill level record
    public void addFillLevelRecord(FillLevelRecord record) {
        record.setDumpster(this);
        fillHistory.add(record);
    }

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
                + ", fillHistorySize=" + fillHistory.size() + "]";
    }
}
