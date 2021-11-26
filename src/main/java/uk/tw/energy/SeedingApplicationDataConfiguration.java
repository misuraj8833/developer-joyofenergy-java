package uk.tw.energy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.generator.ElectricityReadingsGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Configuration
public class SeedingApplicationDataConfiguration {

    private static final String MOST_EVIL_PRICE_PLAN_ID = "price-plan-0";
    private static final String RENEWABLES_PRICE_PLAN_ID = "price-plan-1";
    private static final String STANDARD_PRICE_PLAN_ID = "price-plan-2";
    private static final String MOST_EVIL_SUPPLIER = "Dr Evil's Dark Energy";
    private static final String RENEWABLES_SUPPLIER = "The Green Eco";
    private static final String STANDARD_SUPPLIER = "Power for Everyone";

    @Bean
    public List<PricePlan> pricePlans() {
        final List<PricePlan> pricePlans = new ArrayList<>();
        pricePlans.add(new PricePlan(MOST_EVIL_PRICE_PLAN_ID, MOST_EVIL_SUPPLIER, BigDecimal.TEN, emptyList()));
        pricePlans.add(new PricePlan(RENEWABLES_PRICE_PLAN_ID, RENEWABLES_SUPPLIER, BigDecimal.valueOf(2), emptyList()));
        pricePlans.add(new PricePlan(STANDARD_PRICE_PLAN_ID, STANDARD_SUPPLIER, BigDecimal.ONE, emptyList()));
        return pricePlans;
    }

    @Bean
    public Map<String, List<ElectricityReading>> perMeterElectricityReadings() {
        final Map<String, List<ElectricityReading>> readings = new HashMap<>();
        final ElectricityReadingsGenerator electricityReadingsGenerator = new ElectricityReadingsGenerator();
        smartMeterToPricePlanAccounts()
                .keySet()
                .forEach(smartMeterId -> readings.put(smartMeterId, electricityReadingsGenerator.generate(20)));
        return readings;
    }

    @Bean
    public Map<String, String> smartMeterToPricePlanAccounts() {
        final Map<String, String> smartMeterToPricePlanAccounts = new HashMap<>();
        smartMeterToPricePlanAccounts.put("smart-meter-0", MOST_EVIL_PRICE_PLAN_ID);
        smartMeterToPricePlanAccounts.put("smart-meter-1", RENEWABLES_PRICE_PLAN_ID);
        smartMeterToPricePlanAccounts.put("smart-meter-2", MOST_EVIL_PRICE_PLAN_ID);
        smartMeterToPricePlanAccounts.put("smart-meter-3", STANDARD_PRICE_PLAN_ID);
        smartMeterToPricePlanAccounts.put("smart-meter-4", RENEWABLES_PRICE_PLAN_ID);
        return smartMeterToPricePlanAccounts;
    }

    @Bean
    public Map<Integer, String> supplierIdToSupplier() {
        final Map<Integer, String> supplierIdToSupplier = new HashMap<>();
        supplierIdToSupplier.put(0, MOST_EVIL_SUPPLIER);
        supplierIdToSupplier.put(1, RENEWABLES_SUPPLIER);
        supplierIdToSupplier.put(2, STANDARD_SUPPLIER);
        return supplierIdToSupplier;
    }

    @Bean
    public Map<String, List<String>> supplierToSmartMeterId() {
        final Map<String, List<String>> supplierToSmartMeterId = new HashMap<>();

        List<String> listOfSmartMetersMostEvil = new ArrayList<>();
        listOfSmartMetersMostEvil.add("smart-meter-0");
        listOfSmartMetersMostEvil.add("smart-meter-2");
        supplierToSmartMeterId.put(MOST_EVIL_SUPPLIER, listOfSmartMetersMostEvil);

        List<String> listOfSmartMetersRenewalSupplier = new ArrayList<>();
        listOfSmartMetersRenewalSupplier.add("smart-meter-1");
        listOfSmartMetersRenewalSupplier.add("smart-meter-4");
        supplierToSmartMeterId.put(RENEWABLES_SUPPLIER,listOfSmartMetersRenewalSupplier);

        List<String> listOfSmartMeterStandardSupplier = new ArrayList<>();
        listOfSmartMeterStandardSupplier.add("smart-meter-3");
        supplierToSmartMeterId.put(STANDARD_SUPPLIER,listOfSmartMeterStandardSupplier);

        return supplierToSmartMeterId;
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
