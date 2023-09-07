package arkiGame.components.vehicles;

public enum VehicleType {

    SEDAN(5),
    SUV(7),
    BUS(115);

    private final int maxCapacity;

    VehicleType(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

}
