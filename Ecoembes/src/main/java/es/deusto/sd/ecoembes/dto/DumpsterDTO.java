package es.deusto.sd.ecoembes.dto;

public class DumpsterDTO {

    private long id;
    private String location;
    private float maxCapacity;
    //miramos luego a ver si los quitamos
    private int containerCount;    
    private float fillLevel;       
    
    public DumpsterDTO() {}

    public DumpsterDTO(long id, String location, int containerCount, float fillLevel, float maxCapacity) {
        this.id = id;
        this.location = location;
        this.containerCount = containerCount;
        this.fillLevel = fillLevel;
        this.maxCapacity = maxCapacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    
    //el otro cambio. si al final todo va mal quitar estos dos
    public float getFillLevel() { 
    	return fillLevel; 
    	}
    
    public void setFillLevel(float fillLevel) { 
    	this.fillLevel = fillLevel; 
    	}

    public int getContainerCount() { 
    	return containerCount; 
    	}
    
    public void setContainerCount(int containerCount) { 
    	this.containerCount = containerCount; 
    	}

   
}
