package jp.nk5.saifu2.domain;

import java.time.LocalDate;
import java.util.Locale;

import lombok.Getter;

/**
 * yyyymmおよびyyyymmdd形式の日付を取り扱うクラス
 */
public class MyDate {

    @Getter
    private int fullDate;
    @Getter
    private int year;
    @Getter
    private int month;
    @Getter
    private int day;

    public MyDate(int year, int month, int day)
    {
        this.fullDate = year * 10000 + month * 100 + day;
        this.year = year;
        this.month = month;
        this.day = day;
        isCorrectFormat();
    }

    public MyDate(int year, int month)
    {
        this.fullDate = year * 100 + month;
        this.year = year;
        this.month = month;
        this.day = 0;
        isCorrectFormat();
    }

    public MyDate(int fullDate)
    {
        this.fullDate = fullDate;
        if (fullDate >= 1000000) {
            this.year = fullDate / 10000;
            this.month = (fullDate - this.year * 10000) / 100;
            this.day = (fullDate - this.year * 10000 - this.month * 100);
        } else {
            this.year = fullDate / 100;
            this.month = (fullDate - this.year * 100);
            this.day = 0;
        }
        isCorrectFormat();
    }

    /**
     * 日付が存在することを判定する
     */
    private void isCorrectFormat()
    {
        if (fullDate > 1000000) LocalDate.of(this.year, this.month, this.day);
        else LocalDate.of(this.year, this.month, 1);
    }

    /**
     * yyyy/mmもしくはyyyy/mm/ddの文字列を返却する
     * @return format化された日付
     */
    public String getFullDateWithFormat()
    {
        if (this.day != 0)
            return String.format(
                Locale.JAPAN,
                "%04d/%02d/%02d",
                this.year,
                this.month,
                this.day
            );
        else
            return String.format(
                    Locale.JAPAN,
                    "%04d/%02d",
                    this.year,
                    this.month
            );
    }

}
