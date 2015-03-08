package com.caseyscarborough.budget.highcharts

class DateUtils {

  private final static long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

  static Integer getDaysInPast(Date date) {
    def now = new Date()
    def daysInPast = (int) ((now.getTime() - date.getTime()) / DAY_IN_MILLIS)
    return daysInPast
  }

  static Calendar getCalendarAtMidnight() {
    def cal = Calendar.instance
    setCalendarToMidnight(cal)
    return cal
  }

  static void setCalendarToMidnight(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
  }

  static Calendar getCalendarFromCalendar(Calendar cal1) {
    def cal2 = Calendar.instance
    cal2.setTime(cal1.time)
    return cal2
  }
}
