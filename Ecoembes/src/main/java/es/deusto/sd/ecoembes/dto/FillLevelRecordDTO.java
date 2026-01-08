package es.deusto.sd.ecoembes.dto;

public class FillLevelRecordDTO {

    // CAMBIO: Ahora es String
    private String date;
    private float fillLevel;

    public FillLevelRecordDTO() {}

    // ESTE ES EL CONSTRUCTOR QUE TE FALTABA (String, float)
    public FillLevelRecordDTO(String date, float fillLevel) {
        this.date = date;
        this.fillLevel = fillLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(float fillLevel) {
        this.fillLevel = fillLevel;
    }
}