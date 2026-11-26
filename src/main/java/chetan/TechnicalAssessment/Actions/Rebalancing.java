package chetan.TechnicalAssessment.Actions;

import chetan.TechnicalAssessment.Models.*;
import chetan.TechnicalAssessment.PortfolioRebalancingApp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Rebalancing {

    public Rebalancing() {
    }

    public RebalancingResult rebalance(Account account) {

        BigDecimal assets = account.getTotalAssets();
        List<Trade> trades = new ArrayList<>();

        for (Security security : account.getSecurities()) {
            String name = security.getName().trim().toUpperCase();
            BigDecimal targetVariance = security.getTargetVariance();
            BigDecimal noOfSharesBeforeTrade = security.getNoOfShares(assets);
            BigDecimal sharesBeforeTradeTotalValue = security.getSharesTotalValue(assets);
            BigDecimal noOfSharesAfterTrade;
            BigDecimal sharesAfterTradeTotalValue;

            TradeType tradeType;
            BigDecimal shares;
            BigDecimal tradeAmount;

            if (targetVariance.signum() == 0) {
                tradeType = TradeType.HOLD;
                shares = BigDecimal.ZERO;
                tradeAmount = BigDecimal.ZERO;
                noOfSharesAfterTrade = noOfSharesBeforeTrade;
                sharesAfterTradeTotalValue = sharesBeforeTradeTotalValue;
            } else {
                tradeAmount = this.getTradeAmount(assets, targetVariance.abs());
                shares = this.getShares(tradeAmount, security.getUnitPrice());
                if (targetVariance.signum() > 0) {
                    tradeType = TradeType.SELL;
                    noOfSharesAfterTrade  = noOfSharesBeforeTrade.subtract(shares);
                    sharesAfterTradeTotalValue = sharesBeforeTradeTotalValue.subtract(tradeAmount);
                } else {
                    tradeType = TradeType.BUY;
                    noOfSharesAfterTrade  = noOfSharesBeforeTrade.add(shares);
                    sharesAfterTradeTotalValue = sharesBeforeTradeTotalValue.add(tradeAmount);
                }
            }

            trades.add(new Trade(name, tradeType, shares, tradeAmount, targetVariance, sharesAfterTradeTotalValue, noOfSharesAfterTrade));
        }
        return new RebalancingResult(trades);
    }

    public BigDecimal getTradeAmount(BigDecimal assets, BigDecimal targetVariance) {
        return targetVariance.multiply(assets).divide(BigDecimal.valueOf(100), 2, PortfolioRebalancingApp.roundingMode);
    }

    public BigDecimal getShares(BigDecimal tradeAmount, BigDecimal unitPrice) {
        return tradeAmount.divide(unitPrice, 2, PortfolioRebalancingApp.roundingMode);
    }

}
