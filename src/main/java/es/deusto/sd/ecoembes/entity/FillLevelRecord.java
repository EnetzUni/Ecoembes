package es.deusto.sd.ecoembes.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class FillLevelRecord {  
	//eso para usar tipo historial, que como la funcion query dumspter usage filtra entre una fecha de inicio
	//y una de fin, pues para q sea mas facil y todo eso. si lo veis y creeis q no hace falta, lo quitamos o lo 
	//hacemos d otra manera
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date date;
    private float fillLevel;


    @ManyToOne
    @JoinColumn(name = "dumpster_id")
    private Dumpster dumpster;

    public FillLevelRecord() {}

    public FillLevelRecord(Date date, float fillLevel, Dumpster dumpster) {
        this.date = date;
        this.fillLevel = fillLevel;
        this.dumpster = dumpster;
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

	public float getFillLevel() {
		return fillLevel;
	}

	public void setFillLevel(float fillLevel) {
		this.fillLevel = fillLevel;
	}

	public Dumpster getDumpster() {
		return dumpster;
	}

	public void setDumpster(Dumpster dumpster) {
		this.dumpster = dumpster;
	}

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
}
