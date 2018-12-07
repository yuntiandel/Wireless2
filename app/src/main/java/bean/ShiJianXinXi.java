package bean;

public class ShiJianXinXi {
    //编号
//    private String BianHao;
    //标记
    private String Tag;
    //事件类型
    private String ShiJianLeiXing;
    //事件代码
    private String ShiJianDaiMa;
    //事件说明--->事件描述
    private String ShiJianShuoMing;
    //地点---> 具体地点
    private String DiDian;
    //经度
    private String JingDu;
    //纬度
    private String WeiDu;
    //上报时间---->发生时间
    private String ShangBaoShiJian;
    //图片
    private String TuPian;
    //备注
    private String BeiZhu;
    //是否上报
    private boolean ShiFouShangBao;
    //网格员姓名
    private String WangGeYuan;
    //网格长姓名
    private String WangGeZhang;
    //所属网格
    private String SuoShuWangGe;
    //所属工作站
    private String SuoShuGongZuoZhan;
    //群众意见
    private String QunZhongYiJian;
    //处置过程
    private String ChuZhiGuoCheng;
    //处置结果
    private String ChuZhiJieGuo;

    public ShiJianXinXi(){

    }

    public ShiJianXinXi(String bianhao,String Tag,String ShiJianDaiMa,
                        String ShiJianShuoMing,String DiDian,String JingDu,String WeiDu,String ShangBaoShiJian,
                        String TuPian,String BeiZhu,boolean ShiFouShangBao,String WangGeYuan,String WangGeZhang,String SuoShuWangGe,
                        String SuoShuGongZuoZhan, String QunZhongYiJian,String ChuZhiGuoCheng,String ChuZhiJieGuo ){

        this.Tag = Tag;
        this.ShiJianDaiMa = ShiJianDaiMa;
        this.ShiJianShuoMing = ShiJianShuoMing;
        this.DiDian = DiDian;
        this.JingDu = JingDu;
        this.WeiDu = WeiDu;
        this.ShangBaoShiJian = ShangBaoShiJian;
        this.TuPian = TuPian;
        this.BeiZhu = BeiZhu;
        this.ShiFouShangBao = ShiFouShangBao;
        this.WangGeYuan = WangGeYuan;
        this.WangGeZhang = WangGeZhang;
        this.SuoShuWangGe = SuoShuWangGe;
        this.SuoShuGongZuoZhan = SuoShuGongZuoZhan;
        this.QunZhongYiJian = QunZhongYiJian;
        this.ChuZhiJieGuo = ChuZhiJieGuo;
        this.ChuZhiGuoCheng = ChuZhiGuoCheng;


    }

    public ShiJianXinXi(String time,String location,String shijianleixing){
        this.ShangBaoShiJian = time;
        this.DiDian = location;
        this.ShiJianLeiXing = shijianleixing;
    }

//    public String getBianHao() {
//        return BianHao;
//    }
//
//    public void setBianHao(String bianHao) {
//        BianHao = bianHao;
//    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getShiJianLeiXing(){
        return ShiJianLeiXing;
    }

    public void setShiJianLeiXing(String shijianleixing){
        ShiJianLeiXing = shijianleixing;
    }

    public String getShiJianDaiMa() {
        return ShiJianDaiMa;
    }

    public void setShiJianDaiMa(String shiJianDaiMa) {
        ShiJianDaiMa = shiJianDaiMa;
    }

    public String getShiJianShuoMing() {
        return ShiJianShuoMing;
    }

    public void setShiJianShuoMing(String shiJianShuoMing) {
        ShiJianShuoMing = shiJianShuoMing;
    }

    public String getDiDian() {
        return DiDian;
    }

    public void setDiDian(String diDian) {
        DiDian = diDian;
    }

    public String getJingDu() {
        return JingDu;
    }

    public void setJingDu(String jingDu) {
        JingDu = jingDu;
    }

    public String getWeiDu() {
        return WeiDu;
    }

    public void setWeiDu(String weiDu) {
        WeiDu = weiDu;
    }

    public String getShangBaoShiJian() {
        return ShangBaoShiJian;
    }

    public void setShangBaoShiJian(String shangBaoShiJian) {
        ShangBaoShiJian = shangBaoShiJian;
    }

    public boolean getShiFouShangBao() {return ShiFouShangBao;}

    public void setShiFouShangBao(boolean shifoushangbao) {ShiFouShangBao = shifoushangbao;}

    public String getWangGeYuan(){return WangGeYuan;}
    public void setWangGeYuan(String wanggeyuan){WangGeYuan = wanggeyuan;}

    public String getWangGeZhang(){return WangGeZhang;}
    public void setWangGeZhang(String wanggezhang){WangGeZhang = wanggezhang;}

    public String getSuoShuWangGe(){return SuoShuWangGe;}
    public void setSuoShuWangGe(String suoshuwangge){SuoShuWangGe = suoshuwangge;}

    public String getSuoShuGongZuoZhan(){return SuoShuGongZuoZhan;}
    public void setSuoShuGongZuoZhan(String suoshugongzuozhan){SuoShuGongZuoZhan = suoshugongzuozhan;}

    public String getQunZhongYiJian(){return QunZhongYiJian;}
    public void setQunZhongYiJian(String qunzhongyijian){QunZhongYiJian = qunzhongyijian;}

    public String getChuZhiGuoCheng(){return ChuZhiGuoCheng;}
    public void setChuZhiGuoCheng(String chuzhiguocheng){ChuZhiGuoCheng = chuzhiguocheng;}

    public String getChuZhiJieGuo(){return ChuZhiJieGuo;}
    public void setChuZhiJieGuo(String chuzhijieguo){ChuZhiJieGuo = chuzhijieguo;}

    public String getTuPian() {
        return TuPian;
    }

    public void setTuPian(String tuPian) {
        TuPian = tuPian;
    }

    public String getBeiZhu() {
        return BeiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        BeiZhu = beiZhu;
    }
}
