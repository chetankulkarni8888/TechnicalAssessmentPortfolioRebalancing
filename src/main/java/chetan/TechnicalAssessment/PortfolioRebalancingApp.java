package chetan.TechnicalAssessment;

import chetan.TechnicalAssessment.Actions.Rebalancing;
import chetan.TechnicalAssessment.Models.Account;
import chetan.TechnicalAssessment.Models.RebalancingResult;
import chetan.TechnicalAssessment.Models.Security;
import chetan.TechnicalAssessment.Models.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PortfolioRebalancingApp {
    public static final  RoundingMode roundingMode = RoundingMode.HALF_UP;
    private static final BigDecimal TOTAL_ASSETS = new BigDecimal("100000");
    private static final BigDecimal VESTED_PERCENTAGE = new BigDecimal("100");
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        List<Security> securities = new ArrayList<>(Arrays.asList(
                new Security("IBM",  "20", "10", "-10", "150"),
                new Security("MSFT", "20", "20", "0", "90"),
                new Security("ORCL", "20", "30", "10", "220"),
                new Security("AAPL", "20", "20", "0", "450"),
                new Security("HD",   "20", "20", "0", "70")));
        Account testAccount = new Account("testAccount", TOTAL_ASSETS, VESTED_PERCENTAGE, securities);

        BigDecimal totalAssets =testAccount.getTotalAssets();
        for ( Security security : securities ) {
//            BigDecimal currentAmount = totalAssets.multiply(security.getCurrentPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//            BigDecimal currentNoOfShares = currentAmount.divide(security.getUnitPrice(), 2, RoundingMode.HALF_UP);
            System.out.println("---------------");
            System.out.println("Security name " + security.getName());
            System.out.println("current Amount " + security.getSharesTotalValue(totalAssets));
            System.out.println("current no of shares " + security.getNoOfShares(totalAssets));
            System.out.println("---------------");
        }

        Rebalancing rebalancing = new Rebalancing();
        System.out.println("Calculated Current Vested Percentage " + testAccount.getCalculatedCurrentVestedPercentage());
        System.out.println("Calculated target Vested Percentage " + testAccount.getCalculatedTargetVestedPercentage());
        RebalancingResult result = rebalancing.rebalance(testAccount);
        System.out.println("Account " + testAccount.getAccountName());
        for (Trade trade : result.getTrades().values()) {
            System.out.println("---------------");
            System.out.println("Trade " + trade.getName());
            System.out.println("Trade Type " + trade.getTradeType());
            System.out.println("shares " + trade.getShares());
            System.out.println("trade amount  " + trade.getAmount());
            System.out.println("No of shares after trade  " + trade.getNoOfSharesAfterTrade());
            System.out.println("shares total value after trade  " + trade.getShareTotalValueAfterTrade());
            System.out.println("---------------");
        }
    }
}
