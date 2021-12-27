package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class UserBilling
{
    private String userName;
    private final Character currencySymbol = '$';
    private Optional<BigDecimal> billAmount;

    public UserBilling(String userName, Optional<BigDecimal> billAmount)
    {
        this.userName = userName;
        this.billAmount = billAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Character getCurrencySymbol() {
        return currencySymbol;
    }

    public Optional<BigDecimal> getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Optional<BigDecimal> billAmount) {
        this.billAmount = billAmount;
    }
}
