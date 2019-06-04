package view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculator {

    private static SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat qrTime = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String nextDate(String date, int y, int m, int d){
        Calendar calendar = Calendar.getInstance();
        try {
            Date ref = dayTime.parse(date);
            calendar.setTime(ref);
            calendar.add(Calendar.YEAR, y);
            calendar.add(Calendar.MONTH, m);
            calendar.add(Calendar.DATE, d);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return dayTime.format(calendar.getTime());
    }

    public static String currentTimeDay(){
        return dayTime.format(new Date(System.currentTimeMillis()));
    }
    public static String currentTime(){
        return time.format(new Date(System.currentTimeMillis()));
    }
    public static String currentTime(String studentId){
        return studentId + qrTime.format(new Date(System.currentTimeMillis()));
    }
}
