package chetan.TechnicalAssessment.Models;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    private final String accountName;
    private final BigDecimal totalAssets;
    private final BigDecimal vestedPercentage;
    private final List<Security> securities ;

    public Account(String accountName, BigDecimal totalAssets, BigDecimal vestedPercentage, List<Security> securities) {
        this.accountName = accountName;
        this.totalAssets = totalAssets;
        this.vestedPercentage = vestedPercentage;
        this.securities = securities == null ? null : List.copyOf(securities);
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public BigDecimal getVestedPercentage() {
        return vestedPercentage;
    }

    public List<Security> getSecurities() {
        return securities;
    }

    public BigDecimal getCalculatedCurrentVestedPercentage() {
        BigDecimal calVestedPercentage = BigDecimal.ZERO;
        for ( Security security : securities ) {
            calVestedPercentage = calVestedPercentage.add(security.getCurrentPercentage());
        }
        return calVestedPercentage;
    }

    public BigDecimal getCalculatedTargetVestedPercentage() {
        BigDecimal calVestedPercentage = BigDecimal.ZERO;
        for ( Security security : securities ) {
            calVestedPercentage = calVestedPercentage.add(security.getTargetPercentage());
        }
        return calVestedPercentage;
    }
}
