package uk.tw.energy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.UserBilling;
import uk.tw.energy.service.UserBillingService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/userBilling")
public class UserBillingController
{
    private final UserBillingService userBillingService;
    private final Map<String,String> userToSmartMeterId;

    public UserBillingController(UserBillingService userBillingService, Map<String, String> userToSmartMeterId) {
        this.userBillingService = userBillingService;
        this.userToSmartMeterId = userToSmartMeterId;
    }

    @GetMapping("/{userName}")
    public ResponseEntity billForUser(@PathVariable String userName)
    {
        Map<String ,BigDecimal> mapOfResult = new HashMap<>();
        UserBilling billAmountPerUser;
        if(userToSmartMeterId.containsKey(userName))
        {
            Optional<BigDecimal> bill = userBillingService.billForTheGivenUser(userName);
            if (bill.isPresent())
            {
                mapOfResult.put(userName,bill.get());
                billAmountPerUser = new UserBilling(userName,bill);
                return ResponseEntity.ok(billAmountPerUser);
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
