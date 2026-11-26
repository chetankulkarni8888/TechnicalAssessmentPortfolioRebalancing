# Technical Assessment

As part of our technical interview process, we would like to ask each candidate to
take part in a technical assessment exercise.

Please read through the problem below and answer the highlighted questions.

## Problem

Account **ABC** holds these securities: IBM, MSFT, ORCL, AAPL and HD.

- **Account:** ABC
- **Total assets:** $100,000 (100% is vested)

| Security | Target % | Current % | Target variance | Unit price | Output: number of shares to buy/sell |
|----------|---------:|----------:|----------------:|-----------:|--------------------------------------|
| IBM      | 20       | 10        | -10             | 150        |                                      |
| MSFT     | 20       | 20        | 0               | 90         |                                      |
| ORCL     | 20       | 30        | 10              | 220        |                                      |
| AAPL     | 20       | 20        | 0               | 450        |                                      |
| HD       | 20       | 20        | 0               | 70         |                                      |

- **Target %** is the percentage of total assets you should hold in your account.
- **Current %** is the percentage of total assets you currently have in your account.
- **Target variance** is how far you are from the target. A negative variance means you
  need to buy; a positive variance means you need to sell.

> **Question:** What do you have to do to get to zero target variance?

*Information classification: General*

## Task

Assume that this rebalancing problem is your team's application, which you are going to
test. Write manual test cases and automated test cases to test the application.

1. The recommended time for this task is 4–5 hours. Feel free to research.
2. You can use any language or testing tool to code your automated test cases for this
   scenario, and make any reasonable assumptions. Upload them to GitHub and share the URL.
3. The tests should check that the total number of shares to buy and sell for each
   security is correct, as this is the application's output.
4. Please be prepared to discuss the logic, test cases and coded implementation
   during the interview.

> **Note:** If you make any assumptions, please write them down.
