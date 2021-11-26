package uk.tw.energy.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class EnergySupplierService
{

    private final Map<Integer, String> supplierIdToSupplier;
    private final Map<String, List<String>> supplierToSmartMeterId;
    private final MeterReadingService meterReadingService;
    private final PricePlanService pricePlanService ;

    public EnergySupplierService(Map<Integer, String> supplierIdToSupplier,  Map<String, List<String>> supplierToSmartMeterId, MeterReadingService meterReadingService, PricePlanService pricePlanService) {
        this.supplierIdToSupplier = supplierIdToSupplier;
        this.supplierToSmartMeterId = supplierToSmartMeterId;
        this.meterReadingService = meterReadingService;
        this.pricePlanService = pricePlanService;
    }

    public Optional<BigDecimal> powerDistributed(Integer supplierId) throws NullPointerException
    {
        String supplierName = supplierIdToSupplier.get(supplierId);
        if(!(supplierName ==null)){
        List<String> smartMeterIdsFromSupplier = supplierToSmartMeterId.get(supplierName);
        Optional<List<String>> smartMeterIds = Optional.of(smartMeterIdsFromSupplier);
        return powerDistributedBySmartMeters(smartMeterIds);
        }
        else
            return Optional.empty();
    }

    private Optional<BigDecimal> powerDistributedBySmartMeters(Optional<List<String>> smartMeterIds)
    {
        List<BigDecimal> listTotalElectricityDistributed = new ArrayList<>();
        if(smartMeterIds.isPresent()) {
            smartMeterIds.get()
                    .forEach(smartMeterId ->
                    {
                        listTotalElectricityDistributed.add(pricePlanService.calculateAverageReading(meterReadingService.getReadings(smartMeterId).get()));
                    });
            Optional<BigDecimal> totalElectricityUsedBySuppliersSmartMeters;
            totalElectricityUsedBySuppliersSmartMeters =
                    Optional.ofNullable(listTotalElectricityDistributed.stream()
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
            ;
            return totalElectricityUsedBySuppliersSmartMeters;
        }
        else
            return Optional.empty();

    }
}
