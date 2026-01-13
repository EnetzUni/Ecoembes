package es.deusto.sd.ecoembes.client.model;

public record Dumpster(
    long id,
    String location,
    float maxCapacity,
    int containerCount,
    float fillLevel
) {

    // --- INSERT THIS CONSTRUCTOR ---
    public Dumpster {
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("Max capacity cannot be negative");
        }
        if (fillLevel < 0 || fillLevel > maxCapacity) {
            throw new IllegalArgumentException("Fill level must be between 0 and max capacity");
        }
        // You can also trim strings or normalize data here
        if (location == null) {
            location = "Unknown";
        }
    }
    // -------------------------------

    // Your existing custom getters (Note: Records provide id(), location() etc. by default)
    public long getId() { return id; }
    public String getLocation() { return location; }
    public float getMaxCapacity() { return maxCapacity; }
    public int getContainerCount() { return containerCount; }
    public float getFillLevel() { return fillLevel; }
}