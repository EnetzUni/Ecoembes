package es.deusto.sd.ecoembes.dto;

public class CapacityRequestDTO {
    
    private long plantId;
    private String date; // IMPORTANTE: String para evitar problemas de formato con JSON

    // 1. Constructor vacío (Obligatorio para que Jackson pueda crear el objeto)
    public CapacityRequestDTO() {
    }

    // 2. Constructor con todo (Para que tú lo uses en el Service fácilmente)
    public CapacityRequestDTO(long plantId, String date) {
        this.plantId = plantId;
        this.date = date;
    }

    // 3. Getters y Setters
    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}