package uk.tw.energy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.service.EnergySupplierService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class EnergySupplierController
{
    private final EnergySupplierService energySupplierService;
    private final Map<Integer, String> supplierIdToSupplier;

    public EnergySupplierController(EnergySupplierService energySupplierService, Map<Integer, String> supplierIdToSupplier)
    {
        this.energySupplierService = energySupplierService;
        this.supplierIdToSupplier = supplierIdToSupplier;
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity getElectricityDistributedBySupplierId(@PathVariable Integer supplierId)
    {

        Map<String, BigDecimal> mapOfSupplierToPowerDistributed = new HashMap<>();
        try
        {
            Optional<BigDecimal> totalPowerDistributed = energySupplierService.powerDistributed(supplierId);
            if (totalPowerDistributed.isPresent())
            {
                mapOfSupplierToPowerDistributed.put(supplierIdToSupplier.get(supplierId), totalPowerDistributed.get());
                return ResponseEntity.ok(mapOfSupplierToPowerDistributed);
            }

        }
        catch (NullPointerException nullPointerException)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}
