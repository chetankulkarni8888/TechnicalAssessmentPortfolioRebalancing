package chetan.TechnicalAssessment.Models;

import chetan.TechnicalAssessment.PortfolioRebalancingApp;

import java.math.BigDecimal;

public class Security {
    private final String name;
    private final BigDecimal targetPercentage ;
    private final BigDecimal currentPercentage;
    private final BigDecimal targetVariance;
    private final BigDecimal unitPrice;
    private BigDecimal sharesTotalValue;
    private BigDecimal noOfShares;
    //private final  BigDecimal CurrentNumberOfShares;


    public Security(String name, String targetPercentage, String currentPercentage, String targetVariance, String unitPrice) {
        this.name = name;
        this.targetPercentage = toBigDecimal(targetPercentage);
        this.currentPercentage = toBigDecimal(currentPercentage);
        this.targetVariance = toBigDecimal(targetVariance);
        this.unitPrice = toBigDecimal(unitPrice);
    }

    private static BigDecimal toBigDecimal(String s) {
        return s == null ? null : new BigDecimal(s.trim());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTargetPercentage() {
        return targetPercentage;
    }

    public BigDecimal getCurrentPercentage() {
        return currentPercentage;
    }

    public BigDecimal getTargetVariance() {
        return targetVariance;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSharesTotalValue(BigDecimal totalAssets) {
      return  sharesTotalValue =  totalAssets.multiply(currentPercentage).divide(BigDecimal.valueOf(100), 2, PortfolioRebalancingApp.roundingMode);
    }

    public BigDecimal getNoOfShares(BigDecimal totalAssets) {
        return  noOfShares =  getSharesTotalValue(totalAssets).divide(unitPrice, 2, PortfolioRebalancingApp.roundingMode);
    }



}
