package chetan.TechnicalAssessment.Models;
import java.math.BigDecimal;

public final class Trade {
    private final String name;
    private final TradeType tradeType;
    private final BigDecimal shares;
    private final BigDecimal amount;
    private final BigDecimal targetVariance;
    private final BigDecimal shareTotalValueAfterTrade;
    private final BigDecimal noOfSharesAfterTrade;

    public Trade(String name, TradeType tradeType, BigDecimal shares, BigDecimal amount, BigDecimal targetVariance, BigDecimal shareTotalValueAfterTrade, BigDecimal noOfSharesAfterTrade) {
        this.shareTotalValueAfterTrade = shareTotalValueAfterTrade;
        this.noOfSharesAfterTrade = noOfSharesAfterTrade;
        this.name = name;
        this.tradeType = tradeType;
        this.shares = shares;
        this.amount = amount;
        this.targetVariance = targetVariance;
    }

    public String getName() {
        return name;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getShareTotalValueAfterTrade() {
        return shareTotalValueAfterTrade;
    }

    public BigDecimal getNoOfSharesAfterTrade() {
        return noOfSharesAfterTrade;
    }

    public BigDecimal getTargetVariance() {
        return targetVariance;
    }
}
