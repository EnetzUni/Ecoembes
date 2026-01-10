package es.deusto.sd.ecoembes.dto;

public class RecyclingPlantDTO {
    private long id;
    private String name;
    private String location;
    private float capacity;
    

    public RecyclingPlantDTO() {}

    public RecyclingPlantDTO(long id, String name, String location, float capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;

    }

    // Getters y Setters
    public long getId() {
    	return id; 
    	}
    public void setId(long id) {
    	this.id = id; 
    	}

    public String getName() { 
    	return name; 
    }
    public void setName(String name) { 
    	this.name = name; 
    	}

    public String getLocation() { 
    	return location; 
    	}
    public void setLocation(String location) { 
    	this.location = location; 
    	}

    public float getCapacity() { 
    	return capacity; 
    	}
    public void setCapacity(float capacity) { 
    	this.capacity = capacity;
    	}
    }