package hse.kpo.services;

import hse.kpo.domains.Customer;
import hse.kpo.factories.cars.HandCarFactory;
import hse.kpo.factories.cars.PedalCarFactory;
import hse.kpo.factories.catamarans.HandCatamaranFactory;
import hse.kpo.factories.catamarans.PedalCatamaranFactory;
import hse.kpo.observers.SalesObserver;
import hse.kpo.params.EmptyEngineParams;
import hse.kpo.params.PedalEngineParams;
import hse.kpo.storages.CarStorage;
import hse.kpo.storages.CatamaranStorage;
import hse.kpo.storages.CustomerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hse {

    private final CustomerStorage customerStorage;
    private final CarStorage carStorage;
    private final CatamaranStorage catamaranStorage;

    private final PedalCarFactory pedalCarFactory;
    private final HandCarFactory handCarFactory;
    private final PedalCatamaranFactory pedalCatamaranFactory;
    private final HandCatamaranFactory handCatamaranFactory;

    private final HseCarService hseCarService;
    private final HseCatamaranService hseCatamaranService;

    private final SalesObserver salesObserver;

    @Autowired
    public Hse(CustomerStorage customerStorage, CarStorage carStorage, CatamaranStorage catamaranStorage,
               PedalCarFactory pedalCarFactory, HandCarFactory handCarFactory,
               PedalCatamaranFactory pedalCatamaranFactory, HandCatamaranFactory handCatamaranFactory,
               HseCarService hseCarService, HseCatamaranService hseCatamaranService,
               SalesObserver salesObserver) {
        this.customerStorage = customerStorage;
        this.carStorage = carStorage;
        this.catamaranStorage = catamaranStorage;
        this.pedalCarFactory = pedalCarFactory;
        this.handCarFactory = handCarFactory;
        this.pedalCatamaranFactory = pedalCatamaranFactory;
        this.handCatamaranFactory = handCatamaranFactory;
        this.hseCarService = hseCarService;
        this.hseCatamaranService = hseCatamaranService;
        this.salesObserver = salesObserver;

        hseCarService.addObserver(salesObserver);
    }

    public void addCustomer(String name, int legPower, int handPower, int iq) {
        customerStorage.addCustomer(Customer.builder()
                .name(name)
                .legPower(legPower)
                .handPower(handPower)
                .iq(iq)
                .build());
    }

    public void addPedalCar(int pedalSize) {
        carStorage.addCar(pedalCarFactory, new PedalEngineParams(pedalSize));
    }

    public void addHandCar() {
        carStorage.addCar(handCarFactory, EmptyEngineParams.DEFAULT);
    }

    public void addPedalCatamaran(int pedalSize) {
        catamaranStorage.addCatamaran(pedalCatamaranFactory, new PedalEngineParams(pedalSize));
    }

    public void addHandCatamaran() {
        catamaranStorage.addCatamaran(handCatamaranFactory, EmptyEngineParams.DEFAULT);
    }

    public void sell() {
        hseCarService.sellCars();
        hseCatamaranService.sellCatamarans();
    }

    public String generateReport() {
        return salesObserver.buildReport().toString();
    }
}