package es.deusto.sd.ecoembes.client.data;

import java.util.List;

public record RecyclingPlant(long id, String name, String location, float capacity, List<DailyPlantCapacity> dailyCapacities) {}
