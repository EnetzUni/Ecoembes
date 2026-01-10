package es.deusto.sd.ecoembes.client.model;

import java.util.Date;

public record CapacityResponse(long plantId, float capacity, Date date) {}
