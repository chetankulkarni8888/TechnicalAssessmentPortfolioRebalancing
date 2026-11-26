# CRD Technical Assessment: Manual Test Cases

## Assumptions

- Total assets are 100% vested before and after the trade.
- The rounding mode is `RoundingMode.HALF_UP` (for example, at 0 decimal places 1.4 → 1, 1.5 → 2 and 1.6 → 2).
- Shares can be traded in fractions.
- All calculated numbers are rounded to 2 decimal places (for example, 100.3465656456 → 100.35).
- The tolerance for share counts is 0.01.

## Formulas

| Value                          | Formula                                          |
|--------------------------------|--------------------------------------------------|
| Trade amount                   | `totalAssets × \|targetVariance\| / 100`           |
| Number of shares to trade      | `tradeAmount / unitPrice`                        |
| Expected value after trade     | `totalAssets × targetPercentage / 100`           |
| Expected shares after trade    | `expected value after trade / unitPrice`         |

## Test cases

### Data validation

| ID    | Test case                                                                                       | Automated test                    |
|-------|-------------------------------------------------------------------------------------------------|-----------------------------------|
| TC-01 | Sum of the target percentages of all securities matches the account's vested percentage.        | `verifyTargetPercentages`         |
| TC-02 | Sum of the current percentages of all securities matches the account's vested percentage.       | `verifyCurrentPercentages`        |
| TC-03 | Each target percentage is a valid percentage (0.0 to 100.0).                                    | `verifyTargetPercentageRange`     |
| TC-04 | Each current percentage is a valid percentage (0.0 to 100.0).                                   | `verifyCurrentPercentageRange`    |
| TC-05 | Each target variance is between -100 and 100.                                                   | `verifyTargetVarianceRange`       |
| TC-06 | Sum of the share amounts (current allocation) of all securities equals the total asset value.   | `verifyVestedAmountBeforeTrade`   |
| TC-15 | Each security's unit price is a valid amount (greater than 0).                                  | `verifyunitPriceRange`            |

### Trade

| ID    | Test case                                                                                                   | Automated test                         |
|-------|-------------------------------------------------------------------------------------------------------------|----------------------------------------|
| TC-07 | Sum of the share amounts (after rebalancing) of all securities equals the total asset value.                | `verifyVestedAmountAfterTrade`         |
| TC-08 | If the target variance is positive, the security is overweight and a **SELL** trade is made.                | `verifyTrades`                         |
| TC-09 | If the target variance is negative, the security is underweight and a **BUY** trade is made.                | `verifyTrades`                         |
| TC-10 | If the target variance is zero, the security is balanced and no trade is made (**HOLD**).                   | `verifyTrades`                         |
| TC-11 | The trade amount is calculated from the target variance (see [Formulas](#formulas)).                        | `verifyTrades`                         |
| TC-12 | The number of shares to trade is calculated from the trade amount (see [Formulas](#formulas)).              | `VerifySecurityNoOfSharesAfterTrade`   |
| TC-13 | Each security's total share value after the trade equals its expected vested amount.                        | `VerifySecurityTotalValueAfterTrade`   |
| TC-14 | Each security's number of shares after the trade equals the expected number of shares (within 0.01).        | `VerifySecurityNoOfSharesAfterTrade`   |
| TC-16 | The total value of all BUY trades is less than or equal to the total value of all SELL trades.              | `verifybuysIslessthanEqualToSell`      |

### Result

| ID    | Test case                                                                                  | Automated test           |
|-------|--------------------------------------------------------------------------------------------|--------------------------|
| TC-17 | The trades returned in the rebalancing result cannot be changed by callers (read-only).    | `resultIsUnmodifiable`   |
