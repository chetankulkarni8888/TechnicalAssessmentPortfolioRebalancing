# Portfolio Rebalancing

A small Java project that rebalances an investment account. Each security in the
account has a target allocation and a current allocation. The rebalancer works out
whether to **BUY**, **SELL** or **HOLD** each security, how much money and how many
shares to trade, and the share count and value after the trade.

## Requirements

- Java 27 (set in `pom.xml` via `maven.compiler.source` / `target`)
- Maven 3.x
- Dependencies (from `pom.xml`):
  - [opencsv](https://opencsv.sourceforge.net/) 5.12.0 for CSV import and export (`CSVUtil`)
  - JUnit Jupiter 6.1.3 for tests

## Project structure

```
src
├── main/java/chetan/TechnicalAssessment
│   ├── PortfolioRebalancingApp.java   # Entry point: sample account, prints holdings and trades
│   ├── Actions
│   │   ├── Rebalancing.java           # Core rebalancing logic
│   │   └── Validator.java             # Account validation (placeholder)
│   ├── Models
│   │   ├── Account.java               # Account: name, total assets, vested %, securities
│   │   ├── Security.java              # One holding: target %, current %, variance, unit price
│   │   ├── Trade.java                 # Result for one security
│   │   ├── TradeType.java             # BUY | SELL | HOLD
│   │   └── RebalancingResult.java     # Trades keyed by security name (read-only map)
│   └── Utilities
│       └── CSVUtil.java               # Read/write CSV rows and beans with opencsv
└── test/java/chetan/TechnicalAssessment/Actions
    └── RebalancingTest.java           # JUnit tests for the rebalancing rules
```

## How rebalancing works

The `Security` constructor takes all of its values as strings, which it converts to
`BigDecimal`:

```java
new Security(name, targetPercentage, currentPercentage, targetVariance, unitPrice)
```

`targetVariance` is passed in, not calculated. It uses the convention
**variance = current % − target %**:

| Variance | Meaning     | Trade  |
|----------|-------------|--------|
| `> 0`    | Overweight  | `SELL` |
| `< 0`    | Underweight | `BUY`  |
| `= 0`    | On target   | `HOLD` |

For each security, `Rebalancing.rebalance(account)` calculates:

| Value                    | Formula                                         |
|--------------------------|-------------------------------------------------|
| Current value            | `totalAssets × current% / 100`                  |
| Current shares           | `current value / unitPrice`                     |
| Trade amount             | `totalAssets × \|variance\| / 100`                |
| Shares traded            | `trade amount / unitPrice`                      |
| Shares after trade       | `current shares ± shares traded`                |
| Value after trade        | `current value ± trade amount`                  |

All amounts are rounded to 2 decimal places using `PortfolioRebalancingApp.roundingMode`
(`HALF_UP`). Current shares and shares traded are rounded separately, so the share
count after a trade can be 0.01 off the exact target (for example IBM: 133.34 instead of 133.33).

## Example

The sample account in `PortfolioRebalancingApp` has total assets of 100,000 and a
target of 20% for each of five securities:

| Security | Target % | Current % | Variance | Price | Shares before | Trade | Amount   | Shares traded | Shares after | Value after |
|----------|---------:|----------:|---------:|------:|--------------:|-------|---------:|--------------:|-------------:|------------:|
| IBM      | 20       | 10        | -10      | 150   | 66.67         | BUY   | 10000.00 | 66.67         | 133.34       | 20000.00    |
| MSFT     | 20       | 20        | 0        | 90    | 222.22        | HOLD  | 0        | 0             | 222.22       | 20000.00    |
| ORCL     | 20       | 30        | 10       | 220   | 136.36        | SELL  | 10000.00 | 45.45         | 90.91        | 20000.00    |
| AAPL     | 20       | 20        | 0        | 450   | 44.44         | HOLD  | 0        | 0             | 44.44        | 20000.00    |
| HD       | 20       | 20        | 0        | 70    | 285.71        | HOLD  | 0        | 0             | 285.71       | 20000.00    |

Usage from code:

```java
Account account = new Account("myAccount", new BigDecimal("100000"), new BigDecimal("100"), securities);
RebalancingResult result = new Rebalancing().rebalance(account);
Trade ibm = result.getTrade("ibm");   // lookup is trimmed and case-insensitive
```

## Build, run and test

Compile:

```bash
mvn compile
```

Run the sample app:

```bash
mvn exec:java -Dexec.mainClass=chetan.TechnicalAssessment.PortfolioRebalancingApp
```

(Or run `PortfolioRebalancingApp.main` from your IDE.)

Run the tests:

```bash
mvn test
```

## Tests

`RebalancingTest` checks that:

- target and current percentages add up to the account's vested percentage
- percentages are between 0 and 100, variances are between -100 and 100, and unit prices are valid
- each trade type matches the sign of its variance (SELL / BUY / HOLD)
- after the trade, each security's value equals its target value and its share count
  matches the target within 0.01
- the vested amount before and after rebalancing is consistent, and total buys do not exceed total sells
- `RebalancingResult.getTrades()` cannot be modified


