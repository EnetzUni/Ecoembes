package es.deusto.sd.ecoembes.client.model;

public record Dumpster(
    long id,
    String location,
    float maxCapacity,
    int containerCount,
    float fillLevel
) {}
