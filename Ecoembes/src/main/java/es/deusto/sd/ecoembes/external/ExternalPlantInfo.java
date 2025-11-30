package es.deusto.sd.ecoembes.external;

public class ExternalPlantInfo {
    public long id;
    public String name;
    public float availableCapacity;

    public ExternalPlantInfo() {}

    public ExternalPlantInfo(long id, String name, float availableCapacity) {
        this.id = id;
        this.name = name;
        this.availableCapacity = availableCapacity;
    }
}
