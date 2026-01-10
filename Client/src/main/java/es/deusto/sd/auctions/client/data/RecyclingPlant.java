package es.deusto.sd.ecoembes.dto;

import java.util.List;

public record RecyclingPlant(long id, String name, String location, float capacity, List<DailyPlantCapacity> dailyCapacities) {}
