package es.deusto.sd.plassb.entity;

import jakarta.persistence.*; // Si usas Spring Boot 3
// import javax.persistence.*; // Si usas Spring Boot 2 (antiguo)

@Entity
@Table(name = "DAILY_PLAN") // Así se llamará la tabla en la consola H2
public class DailyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único autogenerado (1, 2, 3...)

    private Long plantId;
    private String date;
    private int totalDumpsters;
    private float totalWaste;

    public DailyPlan() {}

    // Constructor útil
    public DailyPlan(Long plantId, String date, int totalDumpsters, float totalWaste) {
        this.plantId = plantId;
        this.date = date;
        this.totalDumpsters = totalDumpsters;
        this.totalWaste = totalWaste;
    }

    // Getters
    public Long getId() { return id; }
    public Long getPlantId() { return plantId; }
    public String getDate() { return date; }
    public int getTotalDumpsters() { return totalDumpsters; }
    public float getTotalWaste() { return totalWaste; }
}
