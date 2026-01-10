package es.deusto.sd.ecoembes.dto;

import java.util.Date;

public record CapacityResponse(long plantId, float capacity, Date date) {}
