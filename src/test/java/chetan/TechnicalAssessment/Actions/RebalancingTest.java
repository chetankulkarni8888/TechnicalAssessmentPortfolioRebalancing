package chetan.TechnicalAssessment.Actions;

import chetan.TechnicalAssessment.Models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RebalancingTest {

    private static final BigDecimal TOTAL_ASSETS = new BigDecimal("100000");
    private static final BigDecimal VESTED_PERCENTAGE = new BigDecimal("100");
    private static final BigDecimal SHARE_TOLERANCE = new BigDecimal("0.01");
    private static List<Security> getSecurities() {
        List<Security> securities = new ArrayList<>(Arrays.asList(
                new Security("IBM", "20", "10", "-10", "150"),
                new Security("MSFT", "20", "20", "0", "90"),
                new Security("ORCL", "20", "30", "10", "220"),
                new Security("AAPL", "20", "20", "0", "450"),
                new Security("HD", "20", "20", "0", "70")));
        return securities;
    }

    private static Account getAccount() {
        return new Account("testAccount", TOTAL_ASSETS, VESTED_PERCENTAGE, getSecurities());
    }

    Account testAccount = getAccount();

    private Rebalancing rebalancing;
    @BeforeEach
    void setUp() {
        rebalancing = new Rebalancing();
    }

    @AfterEach
    void tearDown() {
        rebalancing = null;
    }

    private RebalancingResult rebalancePortfolio() {
        return rebalancing.rebalance(testAccount);
    }

    private static void assertAmount(String expected, BigDecimal actual) {
        assertNotNull(actual);
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                "expected " + expected + " but was " + actual);
    }

    private static void assertAmount(BigDecimal expected, BigDecimal actual) {
        assertNotNull(actual);
        assertEquals(0, expected.compareTo(actual),
                "expected " + expected + " but was " + actual);
    }

    private static void assertTrade(Trade trade, TradeType type, String shares, String amount) {
        assertNotNull(trade);
        assertEquals(type, trade.getTradeType(), "trade type for " + trade.getName());
        assertAmount(shares, trade.getShares());
        assertAmount(amount, trade.getAmount());
    }

    @Test
    @DisplayName("Data Validation : Sum of target percentages of all securities should match account vested percentage")
    void verifyTargetPercentages() {
        assertAmount(testAccount.getVestedPercentage(), testAccount.getCalculatedTargetVestedPercentage());
    }

    @Test
    @DisplayName("Data Validation : Sum of current percentages of all securities should match account vested percentage")
    void verifyCurrentPercentages() {
        assertAmount(testAccount.getVestedPercentage(), testAccount.getCalculatedCurrentVestedPercentage());
    }

    @Test
    @DisplayName("Data Validation : Target percentage should be valid percentage between 0.0 and 100.0)")
    void verifyTargetPercentageRange() {
        for (Security security : getSecurities()) {
            BigDecimal targetPercentage = security.getTargetPercentage();
            assertTrue(targetPercentage.compareTo(BigDecimal.ZERO) >=0
            && targetPercentage.compareTo(BigDecimal.valueOf(100)) <= 0,
                    () -> "Expected value between 0 and 100 but was " + targetPercentage);
        }
    }

    @Test
    @DisplayName("Data Validation : Current percentage should be valid percentage between 0.0 and 100.0)")
    void verifyCurrentPercentageRange() {
        for (Security security : getSecurities()) {
            BigDecimal currentPercentage = security.getCurrentPercentage();
            assertTrue(currentPercentage.compareTo(BigDecimal.ZERO) >=0
                            && currentPercentage.compareTo(BigDecimal.valueOf(100)) <= 0,
                    () -> "Expected value between 0 and 100 but was " + currentPercentage);
        }
    }

    @Test
    @DisplayName("Data Validation : Share unit price should be valid amount")
    void verifyunitPriceRange() {
        for (Security security : getSecurities()) {
            BigDecimal unitPrice = security.getUnitPrice();
            assertTrue(unitPrice.compareTo(BigDecimal.ZERO) > 0,
                    () -> security.getName() + " security unit price should be grater than 0 ");
        }
    }

    @Test
    @DisplayName("Data Validation : Target variance should be valid  between -100.0  and 100.0)")
    void verifyTargetVarianceRange() {
        for (Security security : getSecurities()) {

            BigDecimal targetVariance = security.getTargetVariance();
            assertTrue(targetVariance.compareTo(BigDecimal.valueOf(-100)) >=0
                            && targetVariance.compareTo(BigDecimal.valueOf(100)) <= 0,
                    () -> "Expected value between -100 and 100 but was " + targetVariance);
        }
    }

    @Test
    @DisplayName("Data Validation : Sum of all security total amount before trade should be equal to total asset value")
    void verifyVestedAmountBeforeTrade() {
        BigDecimal beforeTradesVestedTotalAssets = BigDecimal.ZERO;
        for (Security security : getSecurities()) {
            BigDecimal beforeTradeShareTotalValue = security.getSharesTotalValue(TOTAL_ASSETS);
            beforeTradesVestedTotalAssets =  beforeTradesVestedTotalAssets.add(beforeTradeShareTotalValue);

        }
        assertAmount(TOTAL_ASSETS, beforeTradesVestedTotalAssets);
    }

    @Test
    @DisplayName("Trade : Verify trade are performed as per target variance")
    void verifyTrades() {
        RebalancingResult rebalancingResult = rebalancePortfolio();
        for (Security security : getSecurities()) {
            BigDecimal targetVariance = security.getTargetVariance();
            Trade trade =  rebalancingResult.getTrade(security.getName());
            if(targetVariance.compareTo(BigDecimal.ZERO) >0) {
                assertEquals(TradeType.SELL,trade.getTradeType());
            }
            else if(targetVariance.compareTo(BigDecimal.ZERO) < 0) {
                assertEquals(TradeType.BUY,trade.getTradeType());
            }
            else {
                assertEquals(TradeType.HOLD,trade.getTradeType());
            }
        }
    }

    @Test
    @DisplayName("Trade : Verify security no of shares after trade is same as expected no of shares")
    void VerifySecurityNoOfSharesAfterTrade() {
        RebalancingResult rebalancingResult = rebalancePortfolio();
        for (Security security : getSecurities()) {
            BigDecimal targetPercentage = security.getTargetPercentage();
            BigDecimal expectedSharesTotalValue = TOTAL_ASSETS.multiply(targetPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal expectedNoOfSharesAfterTrade = expectedSharesTotalValue.divide(security.getUnitPrice(), 2, RoundingMode.HALF_UP);
            Trade trade =  rebalancingResult.getTrade(security.getName());
            BigDecimal actualNoOfSharesAfterTrade = trade.getNoOfSharesAfterTrade();
            // before-trade shares and traded shares are each rounded to 2dp, so allow a 0.01 rounding difference
            assertTrue(expectedNoOfSharesAfterTrade.subtract(actualNoOfSharesAfterTrade).abs().compareTo(SHARE_TOLERANCE) <= 0,
                    "incorrect no of shares for security " + security.getName()
                            + ": expected " + expectedNoOfSharesAfterTrade + " but was " + actualNoOfSharesAfterTrade);
        }
    }

    @Test
    @DisplayName("Trade : Verify security total shares amount after trade is same as expected vested amount for security")
    void VerifySecurityTotalValueAfterTrade() {
        RebalancingResult rebalancingResult = rebalancePortfolio();
        for (Security security : getSecurities()) {
            BigDecimal targetPercentage = security.getTargetPercentage();
            BigDecimal expectedSharesTotalValue = TOTAL_ASSETS.multiply(targetPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            Trade trade =  rebalancingResult.getTrade(security.getName());
            BigDecimal actualSharesTotalValue = trade.getShareTotalValueAfterTrade();
            assertAmount(expectedSharesTotalValue, actualSharesTotalValue);
        }
    }

    @Test
    @DisplayName("Trade : Sum of all security total amount after trade should be equal to total asset value")
    void verifyVestedAmountAfterTrade() {
        RebalancingResult rebalancingResult = rebalancePortfolio();
        BigDecimal afterTradesVestedTotalAssets = BigDecimal.ZERO;
         for (Security security : getSecurities()) {
             Trade trade =  rebalancingResult.getTrade(security.getName());
             BigDecimal AfterTradeShareTotalValue = trade.getShareTotalValueAfterTrade();
             afterTradesVestedTotalAssets =  afterTradesVestedTotalAssets.add(AfterTradeShareTotalValue);
         }
         assertAmount(TOTAL_ASSETS, afterTradesVestedTotalAssets);
    }

    @Test
    @DisplayName("Trade : Sum of  purchase value of all trade should be less than sum of sell of all tarde")
    void verifybuysIslessthanEqualToSell() {
        BigDecimal buysTotalAmount = BigDecimal.ZERO;
        BigDecimal sellsTotalAmount = BigDecimal.ZERO;

        RebalancingResult rebalancingResult = rebalancePortfolio();
        for (Trade trade : rebalancingResult.getTrades().values()) {
            TradeType tradeType = trade.getTradeType();
            if(tradeType.equals(TradeType.BUY)) {
                buysTotalAmount  = buysTotalAmount.add(trade.getAmount());

            } else if (tradeType.equals(TradeType.SELL)) {
                sellsTotalAmount = sellsTotalAmount.add(trade.getAmount());
            }
        }
        assertTrue(buysTotalAmount.compareTo(sellsTotalAmount) <= 0,
                "shares over bought by "+ buysTotalAmount.subtract(sellsTotalAmount));
    }

    @Test
    @DisplayName("Result map cannot be modified by callers")
    void resultIsUnmodifiable() {
        RebalancingResult result = rebalancePortfolio();
        assertThrows(UnsupportedOperationException.class, () -> result.getTrades().clear());
    }
}