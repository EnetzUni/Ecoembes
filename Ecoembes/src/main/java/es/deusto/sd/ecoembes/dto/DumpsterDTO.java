package es.deusto.sd.ecoembes.dto;

public class DumpsterDTO {

    private long id;
    private String location;
    private Integer containerCount;
    private float fillLevel;
    private float maxCapacity;

    // 1. Constructor vacío (Obligatorio)
    public DumpsterDTO() {
    }

    // 2. Constructor completo (AQUÍ ESTÁ LA CLAVE)
    public DumpsterDTO(long id, String location, Integer containerCount, float fillLevel, float maxCapacity) {
        this.id = id; // <--- ¡ASEGÚRATE DE QUE ESTA LÍNEA EXISTE!
        this.location = location;
        this.containerCount = containerCount;
        this.fillLevel = fillLevel;
        this.maxCapacity = maxCapacity;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getContainerCount() { return containerCount; }
    public void setContainerCount(int containerCount) { this.containerCount = containerCount; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }

    public float getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(float maxCapacity) { this.maxCapacity = maxCapacity; }
}