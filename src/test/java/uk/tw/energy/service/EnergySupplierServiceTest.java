package uk.tw.energy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class EnergySupplierServiceTest {

    private Map<Integer, String> supplierIdToSupplier;
    private Map<String, List<String>> supplierToSmartMeterId;
    private List<String> listOfSmartMeters;
    private List<ElectricityReading> electricityReadingList;
    private EnergySupplierService energySupplierService;
    private MeterReadingService meterReadingService;
    private Map<String, List<ElectricityReading>> meterAssociatedReading;
    private PricePlanService pricePlanService;
    private List<PricePlan> pricePlans;

    private static final String MOST_EVIL_SUPPLIER = "Dr Evil's Dark Energy";
    private static final String RENEWABLES_SUPPLIER = "The Green Eco";
    private static final String STANDARD_SUPPLIER = "Power for Everyone";
    private static final String MOST_EVIL_PRICE_PLAN_ID = "price-plan-0";


    @BeforeEach
    public void setUpSupplierIdToSupplier() {
        supplierIdToSupplier = new HashMap<>();
        supplierIdToSupplier.put(0, MOST_EVIL_SUPPLIER);
        supplierIdToSupplier.put(1, RENEWABLES_SUPPLIER);
        supplierIdToSupplier.put(2, STANDARD_SUPPLIER);

        listOfSmartMeters = new ArrayList<>();
        listOfSmartMeters.add("smart-meter-0");
        listOfSmartMeters.add("smart-meter-2");
        supplierToSmartMeterId = new HashMap<>();
        supplierToSmartMeterId.put(MOST_EVIL_SUPPLIER, listOfSmartMeters);

        electricityReadingList = new ArrayList<>();
        Instant instant = Instant.now();
        BigDecimal electricityReadingDayOne = BigDecimal.valueOf(200.45);
        electricityReadingList.add(new ElectricityReading(instant, electricityReadingDayOne));
        BigDecimal electricityReadingDayTwo = BigDecimal.valueOf(324.45);
        electricityReadingList.add(new ElectricityReading(instant, electricityReadingDayTwo));

        meterAssociatedReading = new HashMap<>();
        listOfSmartMeters.stream()
                .forEach(
                        meterReading ->
                                meterAssociatedReading.put(meterReading, electricityReadingList)
                );

        pricePlans = new ArrayList<>();
        pricePlans.add(new PricePlan(MOST_EVIL_PRICE_PLAN_ID, MOST_EVIL_SUPPLIER, BigDecimal.TEN, emptyList()));

        meterReadingService = new MeterReadingService(meterAssociatedReading);
        pricePlanService = new PricePlanService(pricePlans, meterReadingService);
        energySupplierService = new EnergySupplierService(supplierIdToSupplier, supplierToSmartMeterId, meterReadingService, pricePlanService);


    }

    @Test
    void givenValidSupplierIdShouldReturnElectricityDistributed() {

        BigDecimal stubTotalElectricityDistributed = BigDecimal.valueOf(524.90);

        Optional<BigDecimal> totalElectricityDistributed = energySupplierService.powerDistributed(0);

        Integer comparisonResult = stubTotalElectricityDistributed.compareTo(totalElectricityDistributed.get());
        assertEquals(comparisonResult,0);
    }

    @Test
    void givenSupplierIdNotInRangeSupplierIdShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            energySupplierService.powerDistributed(1);
        });
    }
}