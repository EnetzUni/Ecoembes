package es.deusto.sd.ecoembes.dto;

import java.util.Date;
import java.util.List;

public class AssignmentDTO {
    private long id;
    private Date date;
    private long employeeId;
    private long recyclingPlantId;
    private List<DumpsterDTO> dumpsters;

    public AssignmentDTO() {}

    public AssignmentDTO(long id, Date date, long employeeId, long recyclingPlantId, List<DumpsterDTO> dumpsters) {
        this.id = id;
        this.date = date;
        this.employeeId = employeeId;
        this.recyclingPlantId = recyclingPlantId;
        this.dumpsters = dumpsters;
    }

    // Getters y Setters
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

    public long getEmployeeId() { 
    	return employeeId; 
    	}
    public void setEmployeeId(long employeeId) { 
    	this.employeeId = employeeId; 
    	}

    public long getRecyclingPlantId() { 
    	return recyclingPlantId; 
    	}
    public void setRecyclingPlantId(long recyclingPlantId) { 
    	this.recyclingPlantId = recyclingPlantId; 
    	}

    public List<DumpsterDTO> getDumpsters() { 
    	return dumpsters; 
    	}
    public void setDumpsters(List<DumpsterDTO> dumpsters) { 
    	this.dumpsters = dumpsters; 
    	}
    
}
