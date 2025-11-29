package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Dumpster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    // Location as a human-readable address or description
    private String location;

    // Maximum capacity in liters
    private float maxCapacity;

    // Current fill level in liters
    private float fillLevel;

    //
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    // No-arg constructor required by JPA
    public Dumpster() {
    }

    public Dumpster(long id, String location, float maxCapacity, float fillLevel, Date date) {
        this.id = id;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.fillLevel = fillLevel;
        this.date = date;
    }

    // Getters and setters
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

    public float getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(float fillLevel) {
        this.fillLevel = fillLevel;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dumpster other = (Dumpster) obj;
        return id == other.id;
    }

	@Override
	public String toString() {
		return "Dumpster [id=" + id + ", location=" + location + ", maxCapacity=" + maxCapacity + ", fillLevel="
				+ fillLevel + ", date=" + date + "]";
	}


 
}