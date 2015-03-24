package com.caseyscarborough.bookkeeper.highcharts

import org.apache.log4j.Logger

class TimeSeries extends HighChart {

  private static final Logger log = Logger.getLogger(TimeSeries.class)

  String type
  String name
  Long pointInterval
  Long pointStart
  List<BigDecimal> data

  public TimeSeries(String title, String name, Long pointInterval, Date startDate) {
    super(title)
    this.type = 'area'
    this.name = name
    this.pointInterval = pointInterval
    this.pointStart = startDate.time
    this.data = [(BigDecimal) 0] * DateUtils.getDaysInPast(startDate)
  }

  public void addToDataForDate(BigDecimal amount, Date date) {
    try {
      data[data.size() - DateUtils.getDaysInPast(date)] += amount
    } catch (NullPointerException e) {
      log.error(e)
    }
  }

}
