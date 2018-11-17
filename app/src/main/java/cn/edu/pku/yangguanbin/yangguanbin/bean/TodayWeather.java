package cn.edu.pku.yangguanbin.yangguanbin.bean;

import java.util.Stack;

/**
 * Created by yangg on 2018/10/10.
 */

public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String fengli_1;
    private String fengli_2;
    private String fengli_3;
    private String fengli_4;
    private String fengli_5;
    private String fengli_6;

    private String date;
    private String week_1;
    private String week_2;
    private String week_3;
    private String week_4;
    private String week_5;
    private String week_6;

    private String high;
    private String high_1;
    private String high_2;
    private String high_3;
    private String high_4;
    private String high_5;
    private String high_6;

    private String low;
    private String low_1;
    private String low_2;
    private String low_3;
    private String low_4;
    private String low_5;
    private String low_6;



    private String type;
    private String type_1;
    private String type_2;
    private String type_3;
    private String type_4;
    private String type_5;
    private String type_6;

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }
    public String getWeek_1() { return week_1;}
    public String getWeek_2() { return week_2;}
    public String getWeek_3() { return week_3;}
    public String getWeek_4() { return week_4;}
    public String getWeek_5() { return week_5;}
    public String getWeek_6() { return week_6;}

    public String getPm25() {
        return pm25;
    }

    public String getFengli() {
        return fengli;
    }
    public String getFengli_1() {
        return fengli_1;
    }
    public String getFengli_2() {
        return fengli_2;
    }
    public String getFengli_3() {
        return fengli_3;
    }
    public String getFengli_4() {
        return fengli_4;
    }
    public String getFengli_5() {
        return fengli_5;
    }
    public String getFengli_6() {
        return fengli_6;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public String getHigh() {
        return high;
    }
    public String getHigh_1() {
        return high_1;
    }
    public String getHigh_2() {
        return high_2;
    }
    public String getHigh_3() {
        return high_3;
    }
    public String getHigh_4() {
        return high_4;
    }
    public String getHigh_5() {
        return high_5;
    }
    public String getHigh_6() {
        return high_6;
    }

    public String getQuality() {
        return quality;
    }

    public String getLow() {
        return low;
    }
    public String getLow_1() { return low_1;}
    public String getLow_2() { return low_2;}
    public String getLow_3() { return low_3;}
    public String getLow_4() { return low_4;}
    public String getLow_5() { return low_5;}
    public String getLow_6() { return low_6;}


    public String getShidu() {
        return shidu;
    }

    public String getType() {
        return type;
    }
    public String getType_1() {
        return type_1;
    }
    public String getType_2() {
        return type_2;
    }
    public String getType_3() {
        return type_3;
    }
    public String getType_4() {
        return type_4;
    }
    public String getType_5() {
        return type_5;
    }
    public String getType_6() {
        return type_6;
    }


    public String getUpdatetime() {
        return updatetime;
    }

    public String getWendu() {
        return wendu;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public  void setWeek_1(String week_1){this.week_1=week_1;}
    public  void setWeek_2(String week_2){this.week_2=week_2;}
    public  void setWeek_3(String week_3){this.week_3=week_3;}
    public  void setWeek_4(String week_4){this.week_4=week_4;}
    public  void setWeek_5(String week_5){this.week_5=week_5;}
    public  void setWeek_6(String week_6){this.week_6=week_6;}


    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }
    public void setFengli_1(String fengli_1) {
        this.fengli_1 = fengli_1;
    }
    public void setFengli_2(String fengli_2) {
        this.fengli_2 = fengli_2;
    }
    public void setFengli_3(String fengli_3) {
        this.fengli_3 = fengli_3;
    }
    public void setFengli_4(String fengli_4) {
        this.fengli_4 = fengli_4;
    }
    public void setFengli_5(String fengli_5) {
        this.fengli_5 = fengli_5;
    }
    public void setFengli_6(String fengli_6) {
        this.fengli_6 = fengli_6;
    }

    public void setHigh(String high) {
        this.high = high;
    }
    public void setHigh_1(String high_1) {
        this.high_1 = high_1;
    }
    public void setHigh_2(String high_2) {
        this.high_2 = high_2;
    }
    public void setHigh_3(String high_3) {
        this.high_3 = high_3;
    }
    public void setHigh_4(String high_4) {
        this.high_4 = high_4;
    }
    public void setHigh_5(String high_5) {
        this.high_5 = high_5;
    }
    public void setHigh_6(String high_6) {
        this.high_6 = high_6;
    }

    public void setLow(String low) {
        this.low = low;
    }
    public void setLow_1(String low_1) {
        this.low_1 = low_1;
    }
    public void setLow_2(String low_2) {
        this.low_2 = low_2;
    }
    public void setLow_3(String low_3) {
        this.low_3 = low_3;
    }
    public void setLow_4(String low_4) {
        this.low_4 = low_4;
    }
    public void setLow_5(String low_5) {
        this.low_5 = low_5;
    }
    public void setLow_6(String low_6) {
        this.low_6 = low_6;
    }



    public void setType(String type) {
        this.type = type;
    }
    public void setType_1(String type_1) {this.type_1 = type_1;}
    public void setType_2(String type_2) {this.type_2 = type_2;}
    public void setType_3(String type_3) {this.type_3 = type_3;}
    public void setType_4(String type_4) {this.type_4 = type_4;}
    public void setType_5(String type_5) {this.type_5 = type_5;}
    public void setType_6(String type_6) {this.type_6 = type_6;}

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", fengli='" + fengli + '\'' +
                ", date='" + date + '\'' +
                ", weeks[]='" + week_1+ '\'' +
                ", weeks[]='" + week_2+ '\'' +
                ", weeks[]='" + week_3+ '\'' +
                ", weeks[]='" + week_4+ '\'' +
                ", weeks[]='" + week_5+ '\'' +
                ", weeks[]='" + week_6+ '\'' +
                ", high='" + high + '\'' +
                ", high='" + high_1 + '\'' +
                ", high='" + high_2 + '\'' +
                ", high='" + high_3 + '\'' +
                ", high='" + high_4 + '\'' +
                ", high='" + high_5 + '\'' +
                ", high='" + high_6 + '\'' +
                ", low='" + low + '\'' +
                ", low='" + low_1 + '\'' +
                ", low='" + low_2 + '\'' +
                ", low='" + low_3 + '\'' +
                ", low='" + low_4 + '\'' +
                ", low='" + low_5 + '\'' +
                ", low='" + low_6 + '\'' +
                ", type='" + type + '\'' +
                ", type='" + type_1 + '\'' +
                ", type='" + type_2 + '\'' +
                ", type='" + type_3 + '\'' +
                ", type='" + type_4 + '\'' +
                ", type='" + type_5 + '\'' +
                ", type='" + type_6 + '\'' +
                '}';
    }

}
