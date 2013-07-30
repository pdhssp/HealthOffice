/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.data;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Buddhika
 */
public class CommonFunctions {

    public static Date calculateFirstDayOfMonth(Date monthDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static List<Date> datesBetween(Date fromDate, Date toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        List<Date> dates = new ArrayList<Date>();

        Integer ms;


        if (fromDate.getTime() < toDate.getTime()) {
            ms = (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
            System.out.println("Up for " + ms + " days.");
            for (int i = 0; i < ms; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fromDate);
                calendar.add(Calendar.DATE, i);
                Date newDate;
                newDate = calendar.getTime();
                dates.add(newDate);
            }
        } else {
            ms = (int) ((fromDate.getTime() - toDate.getTime()) / (1000 * 60 * 60 * 24));
            System.out.println("Down for " + ms + " days.");
            dates.add(fromDate);
            for (int i = ms; i > 0; i--) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(toDate);
                calendar.add(Calendar.DATE, i - 1);
                Date newDate;
                newDate = calendar.getTime();
                dates.add(newDate);
            }

        }
        return dates;
    }

    private static void funTwo() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date date1 = Calendar.getInstance().getTime();
        Date date2 = Calendar.getInstance().getTime();
        try {
            date1 = sdf.parse("10/01/2010");
            date2 = sdf.parse("10/02/2012");
        } catch (ParseException ex) {
            System.out.println(ex);
        }

        List<Date> months = getMonthsBetween(date1, date2);
        System.out.println("Inbetween Dates of " + sdf.format(date1) + " and " + sdf.format(date2) + " are listed below.");
        for (Date temMonth : months) {
            System.out.println("From " + sdf.format(firstDateOfMonth(temMonth)) + " to " + sdf.format(lastDateOfMonth(temMonth)));
        }
    }

    public static Date lastDateOfMonth(Date thisDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return (new GregorianCalendar(year, month, cal.getActualMaximum(Calendar.DATE))).getTime();
    }

    public static Date firstDateOfMonth(Date thisDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return (new GregorianCalendar(year, month, cal.getActualMinimum(Calendar.DATE))).getTime();
    }

    public static List<Date> getMonthsBetween(Date fromDate, Date toDate) {
        List<Date> temLst = new ArrayList<Date>();
        int monthCount = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        int c1date = cal.get(Calendar.DATE);
        int c1month = cal.get(Calendar.MONTH);
        int c1year = cal.get(Calendar.YEAR);
        cal.setTime(toDate);
        int c2date = cal.get(Calendar.DATE);
        int c2month = cal.get(Calendar.MONTH);
        int c2year = cal.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMMM yyyy");
        monthCount = ((c2year - c1year) * 12) + (c2month - c1month) + ((c2date >= c1date) ? 1 : 0);
        for (int i = 0; i <= monthCount; i++) {
            Calendar temCal = Calendar.getInstance();
            temCal.setTime(fromDate);
            temCal.add(Calendar.MONTH, i);
            temLst.add(temCal.getTime());
        }
        return temLst;
    }

    public static List<Date> getHoursBetween(Date fromDate, Date toDate) {
        List<Date> temLst = new ArrayList<Date>();
        int hourCount = 0;
        Calendar cal = Calendar.getInstance();

        cal.setTime(fromDate);
        int c1date = cal.get(Calendar.DATE);
        int c1month = cal.get(Calendar.MONTH);
        int c1year = cal.get(Calendar.YEAR);
        int c1Hour = cal.get(Calendar.HOUR);
        int c1Min = cal.get(Calendar.MINUTE);

        cal.setTime(toDate);
        int c2date = cal.get(Calendar.DATE);
        int c2month = cal.get(Calendar.MONTH);
        int c2year = cal.get(Calendar.YEAR);
        int c2Hour = cal.get(Calendar.HOUR);
        int c3Min = cal.get(Calendar.MINUTE);

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMMM yyyy");
        hourCount = ((c2year - c1year) * 12) + (c2month - c1month) + ((c2date >= c1date) ? 1 : 0);
        for (int i = 0; i <= hourCount; i++) {
            Calendar temCal = Calendar.getInstance();
            temCal.setTime(fromDate);
            temCal.add(Calendar.MONTH, i);
            temLst.add(temCal.getTime());
        }
        return temLst;
    }

    public static boolean isLongPositive(String str) {
        if (str==null){
            return false;
        }
        if(str.length()==0){
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) {
            return false;
        }

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }
}
