package hse.kpo.domains;

import hse.kpo.interfaces.Engine;

public class CatamaranOnWheels extends Car {


    private final Catamaran catamaran;

    public CatamaranOnWheels(int number, Engine engine) {
        super(99000 + number, engine);

        this.catamaran = new Catamaran(number, engine);
    }

    @Override
    public boolean isCompatible(Customer customer) {
        return super.isCompatible(customer);
    }

    @Override
    public String toString() {
        return String.format("CatamaranOnWheels[Adapter for: %s, Adapter VIN: %d]",
                this.catamaran.toString(), super.getVin());
    }
}