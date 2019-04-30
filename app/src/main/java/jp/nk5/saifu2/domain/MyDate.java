package jp.nk5.saifu2.domain;

public class MyDate {

    public static int getYear(int date)
    {
        return date / 10000;
    }

    public static int getMonth(int date)
    {
        return (date - getYear(date) * 10000) / 100;
    }

    public static int getDay(int date)
    {
        return (date - getYear(date) * 10000 - getMonth(date) * 100);
    }

    public static int generateDate(int year, int month, int day)
    {
        return year * 10000 + month * 100 + day;
    }

}
