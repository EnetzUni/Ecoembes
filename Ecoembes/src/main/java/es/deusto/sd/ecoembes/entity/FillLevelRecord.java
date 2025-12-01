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
public class FillLevelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private float fillLevel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dumpster_id")
    private Dumpster dumpster;

    // ------------ //
    // Constructors //
    // ------------ //

    public FillLevelRecord() {}

    public FillLevelRecord(Date date, float fillLevel, Dumpster dumpster) {
        this.date = date;
        this.fillLevel = fillLevel;
        this.dumpster = dumpster;
    }

    // ----------------- //
    // Getters & Setters //
    // ----------------- //

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }

    public Dumpster getDumpster() { return dumpster; }
    public void setDumpster(Dumpster dumpster) { this.dumpster = dumpster; }

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
        if (!(obj instanceof FillLevelRecord)) return false;
        return id == ((FillLevelRecord) obj).id;
    }

    // -------- //
    // toString //
    // -------- //

    @Override
    public String toString() {
        return "FillLevelRecord [id=" + id + ", date=" + date + ", fillLevel=" + fillLevel + "]";
    }
}
