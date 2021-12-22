package uk.tw.energy.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserBillingServiceTest
{

    private static final String MOST_EVIL_SUPPLIER = "Dr Evil's Dark Energy";
    private static final String MOST_EVIL_PRICE_PLAN_ID = "price-plan-0";
    private static final String USER_NAME = "Sarah1";
    private static final String SMART_METER_ID = "smart-meter-0";


    PricePlanService pricePlanService;
    Map<String,String> userToSmartMeterId;
    Map<String, String> smartMeterToPricePlanAccounts;
    List<PricePlan> pricePlans;
    List<ElectricityReading> electricityReadingList;
    Map<String, List<ElectricityReading>> meterAssociatedReading;
    MeterReadingService meterReadingService;
    UserBillingService userBillingService;
    Instant instantDayOne;
    Instant instantDayTwo;

    @BeforeEach
    public void initializeTest()
    {
        pricePlans = new ArrayList<>();
        pricePlans.add(new PricePlan(MOST_EVIL_PRICE_PLAN_ID, MOST_EVIL_SUPPLIER, BigDecimal.TEN, emptyList()));

        electricityReadingList = new ArrayList<>();
        instantDayOne = Instant.now();
        BigDecimal electricityReadingDayOne = BigDecimal.valueOf(200.45);
        electricityReadingList.add(new ElectricityReading(instantDayOne, electricityReadingDayOne));
        instantDayTwo = instantDayOne.plusSeconds(120);
        BigDecimal electricityReadingDayTwo = BigDecimal.valueOf(324.45);
        electricityReadingList.add(new ElectricityReading(instantDayTwo, electricityReadingDayTwo));

        meterAssociatedReading = new HashMap<>();
        meterAssociatedReading.put("smart-meter-0",electricityReadingList);
        meterReadingService = new MeterReadingService(meterAssociatedReading);

        pricePlanService = new PricePlanService(pricePlans,meterReadingService);

        userToSmartMeterId = new HashMap<>();
        userToSmartMeterId.put(USER_NAME,SMART_METER_ID);

        smartMeterToPricePlanAccounts = new HashMap<>();
        smartMeterToPricePlanAccounts.put(SMART_METER_ID,MOST_EVIL_PRICE_PLAN_ID);

        userBillingService = new UserBillingService(pricePlanService,userToSmartMeterId,smartMeterToPricePlanAccounts);

    }

    @Test
    void getTheBillForTheGivenUsername()
    {
        Optional<BigDecimal> actualValue = userBillingService.billForTheGivenUser(USER_NAME);
        assertNotNull(actualValue);
    }

    @Test
    void getInvalidBillForTheInvalidUsername()
    {
        Optional<BigDecimal> actualValue = userBillingService.billForTheGivenUser(USER_NAME);
        assertEquals(ResponseEntity.notFound().build(),ResponseEntity.notFound().build());

    }
}