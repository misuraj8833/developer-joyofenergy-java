package uk.tw.energy.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class UserBillingService
{
    private PricePlanService pricePlanService;
    private Map<String,String> userToSmartMeterId;
    private Map<String, String> smartMeterToPricePlanAccounts;

    public UserBillingService(PricePlanService pricePlanService, Map<String, String> userToSmartMeterId, Map<String, String> smartMeterToPricePlanAccounts) {
        this.pricePlanService = pricePlanService;
        this.userToSmartMeterId = userToSmartMeterId;
        this.smartMeterToPricePlanAccounts = smartMeterToPricePlanAccounts;
    }

    public Optional<BigDecimal> billForTheGivenUser(String userName)
    {
        Optional<Map<String,BigDecimal>> mapOfPlanNameToCost = Optional.of(new HashMap<>());
        Optional<BigDecimal> billAmount = Optional.empty();
        if(userToSmartMeterId.containsKey(userName))
        {
            String smartMeterId = userToSmartMeterId.get(userName);
            mapOfPlanNameToCost =
                    pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(smartMeterId);
            if(smartMeterToPricePlanAccounts.containsKey(smartMeterId))
            {
                String pricePlanName = smartMeterToPricePlanAccounts.get(smartMeterId);
                billAmount = Optional.of(mapOfPlanNameToCost.get().get(pricePlanName));
                return billAmount;
            }
            else
            {
                return billAmount;
            }
        }
        else
        {
            return billAmount;
        }


    }

}
