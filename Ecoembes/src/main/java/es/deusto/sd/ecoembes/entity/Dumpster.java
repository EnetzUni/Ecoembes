package es.deusto.sd.ecoembes.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Dumpster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String location;

    private float maxCapacity;
    
    //añado estos dos ahora q no se si los necesitamos para q lo vean los empleados y todo eso
    private int containerCount;      
    private float fillLevel;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    //constructores


    public Dumpster() {}

    public Dumpster(String location, float maxCapacity) {
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.containerCount = 0; 
        this.fillLevel = 0.0f;    
    }

    public Dumpster(String location, float maxCapacity, int containerCount, float fillLevel) {
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.containerCount = containerCount;
        this.fillLevel = fillLevel;
    }


    //getters y setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public float getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(float maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getContainerCount() { return containerCount; }
    public void setContainerCount(int containerCount) { this.containerCount = containerCount; }

    public float getFillLevel() { return fillLevel; }
    public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }

    @Override
    public String toString() {
        return "Dumpster [id=" + id + ", location=" + location + ", maxCapacity=" + maxCapacity
               + ", containerCount=" + containerCount + ", fillLevel=" + fillLevel + "]";}
}
