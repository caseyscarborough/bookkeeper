# Budget Application

This is a small Grails app that allows you to track and analyze your spending over time.

Features:

* Tracking account balances
* Transfers between accounts
* Inputting transactions with category
* Receipt uploading with transactions
* Graphs and spending analysis:
  * Spending by day for the past year
  * Spending by category for the past year
  * Side-by-side comparisons of monthly spending
  * Total spending by payee
* Importing transactions from Excel spreadsheet (works with export from Mint.com)

## Requirements

* Java 7 or 8

## Running the Application

Clone the repository, and rename the example DataSource.groovy file.

```bash
git clone https://github.com/caseyscarborough/budget.git
mv grails-app/conf/DataSource.example.groovy grails-app/conf/DataSource.groovy
```

Update your initial user information in [BootStrap.groovy](https://github.com/caseyscarborough/budget/blob/master/grails-app/conf/BootStrap.groovy#L14), then run one of the following from the root of the project:

```bash
# Windows
grailsw.bat run-app

# Linux or Mac OS X
./grailsw run-app
```

For development, receipts are uploaded to the `/tmp` directory. If you want to use this application in production, you'll need to update your receipt storage location in [Config.groovy](https://github.com/caseyscarborough/budget/blob/master/grails-app/conf/Config.groovy#L154).

## Example Screenshots

![](https://raw.githubusercontent.com/caseyscarborough/budget/master/grails-app/assets/images/example-1.png)

![](https://raw.githubusercontent.com/caseyscarborough/budget/master/grails-app/assets/images/example-2.png)