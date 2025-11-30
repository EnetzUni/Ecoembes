package es.deusto.sd.ecoembes.dto;

import java.util.Date;

public class FillLevelRecordDTO {
	//bueno q si lo terminamos cambiando d entity q no se nos pase cambiarlo aqui tambien
	
    private Date date;
    private float fillLevel;

    public FillLevelRecordDTO() {}

    public FillLevelRecordDTO(Date date, float fillLevel) {
        this.date = date;
        this.fillLevel = fillLevel;
    }

    // Getters y Setters
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }
}
