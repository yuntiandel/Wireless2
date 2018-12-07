package bean;


public class MyEventShow {
    String Time;
    String ShuoMing;
    String ZhuangTai;

    public MyEventShow(String time,String shuoming,String zhuangtai){
        Time = time;
        ShuoMing = shuoming;
        ZhuangTai = zhuangtai;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getShuoMing() {
        return ShuoMing;
    }

    public void setShuoMing(String shuoMing) {
        ShuoMing = shuoMing;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }
}
