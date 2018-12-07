package bean;

/**
 * Created by 86188 on 2018/8/21.
 */

public class XunChaJiLu {
    //编号
    private Integer BianHao;
    //巡查日期
    private String XunChaShiJian;
    //巡查区域
    private String QuYu;
    //事件数量
    private Integer number;
    //网格员姓名
    private String WGYXingming;
    //网格长姓名
    private String WGZXingming;
    //所属网格
    private String SSWangGe;
    //所属工作站
    private String SSGongZuoZhan;

    public Integer getBianHao() {
        return BianHao;
    }

    public void setBianHao(Integer bianHao) {
        BianHao = bianHao;
    }

    public String getXunChaShiJian() {
        return XunChaShiJian;
    }

    public void setXunChaShiJian(String xunChaShiJian) {
        XunChaShiJian = xunChaShiJian;
    }

    public String getQuYu() {
        return QuYu;
    }

    public void setQuYu(String quYu) {
        QuYu = quYu;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWGYXingming() {
        return WGYXingming;
    }

    public void setWGYXingming(String WGYXingming) {
        this.WGYXingming = WGYXingming;
    }

    public String getWGZXingming() {
        return WGZXingming;
    }

    public void setWGZXingming(String WGZXingming) {
        this.WGZXingming = WGZXingming;
    }

    public String getSSWangGe() {
        return SSWangGe;
    }

    public void setSSWangGe(String SSWangGe) {
        this.SSWangGe = SSWangGe;
    }

    public String getSSGongZuoZhan() {
        return SSGongZuoZhan;
    }

    public void setSSGongZuoZhan(String SSGongZuoZhan) {
        this.SSGongZuoZhan = SSGongZuoZhan;
    }


}
