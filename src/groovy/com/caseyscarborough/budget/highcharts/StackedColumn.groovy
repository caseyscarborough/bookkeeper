package com.caseyscarborough.budget.highcharts

import org.apache.log4j.Logger

class StackedColumn {

  private static final Logger log = Logger.getLogger(StackedColumn.class)
  private Integer numberOfColumns

  List<String> xAxisCategories
  List<Map> series

  public StackedColumn(Integer numberOfColumns) {
    xAxisCategories = []
    series = []
    this.numberOfColumns = numberOfColumns
  }

  public void addToXAxisCategories(String category) {
    xAxisCategories << category
  }

  public void addToSeries(String value, BigDecimal data, Integer index) {
    for (int i = 0; i < series.size(); i++) {
      if (series[i].name == value) {
        series[i].data[index] += data
        return
      }
    }

    // If the category in the series doesn't yet exist, create it.
    series << [name: value, data: [(BigDecimal) 0] * numberOfColumns]
    series[series.size() - 1].data[index] += data
  }
}
