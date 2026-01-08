package es.deusto.sd.ecoembes.entity;

// IMPORTANTE: Borra los imports de java.util.Date y jakarta.persistence.Temporal

// import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
// Borra @Temporal y TemporalType si los tienes

@Entity
public class FillLevelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // CAMBIO: De Date a String. Borramos @Temporal.
    private String date; 

    private float fillLevel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dumpster_id")
    private Dumpster dumpster;

    public FillLevelRecord() {}

    // Constructor actualizado a String
    public FillLevelRecord(String date, float fillLevel, Dumpster dumpster) {
        this.date = date;
        this.fillLevel = fillLevel;
        this.dumpster = dumpster;
    }

    // Getters y Setters actualizados
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // ... el resto de getters/setters igual ...
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }
    public Dumpster getDumpster() { return dumpster; }
    public void setDumpster(Dumpster dumpster) { this.dumpster = dumpster; }
}