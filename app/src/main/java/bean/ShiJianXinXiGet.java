package bean;

import java.io.Serializable;
import java.sql.Date;

public class ShiJianXinXiGet implements Serializable{
        private String ShiJianBianHao;//编号
        private String XunChaJiLu;//巡查记录
        private String ShiJianXiaoLei;//小类
        private String ShiJianShuoMing;//说明
        private String ShiJianDiDian;//地点
        private String JingDu;//经度
        private String WeiDu;//纬度
        private String ShiFouShangBao;//上报
        private String ShangBaoShiJian;//上报时间
        private String ShangBaoBuMen;//部门
        private String ChuZhiGuoCheng;//过程
        private String ChuLiJieGuo;//结果
        private String QunZhongYiJian;//群众意见
        private String GenJinCuoShi;//跟进措施
        private String TuPian;//图片
        private String BeiZhu;//备注
        private String BMFZRSHYiJian;//？？？
        private String Name;//上报人员姓名

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getShiJianBianHao() {
        return ShiJianBianHao;
    }

    public void setShiJianBianHao(String shiJianBianHao) {
        ShiJianBianHao = shiJianBianHao;
    }

    public String getXunChaJiLu() {
        return XunChaJiLu;
    }

    public void setXunChaJiLu(String xunChaJiLu) {
        XunChaJiLu = xunChaJiLu;
    }

    public String getShiJianXiaoLei() {
        return ShiJianXiaoLei;
    }

    public void setShiJianXiaoLei(String shiJianXiaoLei) {
        ShiJianXiaoLei = shiJianXiaoLei;
    }

    public String getShiJianShuoMing() {
        return ShiJianShuoMing;
    }

    public void setShiJianShuoMing(String shiJianShuoMing) {
        ShiJianShuoMing = shiJianShuoMing;
    }

    public String getShiJianDiDian() {
        return ShiJianDiDian;
    }

    public void setShiJianDiDian(String shiJianDiDian) {
        ShiJianDiDian = shiJianDiDian;
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

    public String getShiFouShangBao() {
        return ShiFouShangBao;
    }

    public void setShiFouShangBao(String shiFouShangBao) {
        ShiFouShangBao = shiFouShangBao;
    }

    public String getShangBaoShiJian() {
        return ""+ShangBaoShiJian;
    }

    public void setShangBaoShiJian(String shangBaoShiJian) {
        ShangBaoShiJian = shangBaoShiJian;
    }

    public String getShangBaoBuMen() {
        return ShangBaoBuMen;
    }

    public void setShangBaoBuMen(String shangBaoBuMen) {
        ShangBaoBuMen = shangBaoBuMen;
    }

    public String getChuZhiGuoCheng() {
        return ChuZhiGuoCheng;
    }

    public void setChuZhiGuoCheng(String chuZhiGuoCheng) {
        ChuZhiGuoCheng = chuZhiGuoCheng;
    }

    public String getChuLiJieGuo() {
        return ChuLiJieGuo;
    }

    public void setChuLiJieGuo(String chuLiJieGuo) {
        ChuLiJieGuo = chuLiJieGuo;
    }

    public String getQunZhongYiJian() {
        return QunZhongYiJian;
    }

    public void setQunZhongYiJian(String qunZhongYiJian) {
        QunZhongYiJian = qunZhongYiJian;
    }

    public String getGenJinCuoShi() {
        return GenJinCuoShi;
    }

    public void setGenJinCuoShi(String genJinCuoShi) {
        GenJinCuoShi = genJinCuoShi;
    }

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

    public String getBMFZRSHYiJian() {
        return BMFZRSHYiJian;
    }

    public void setBMFZRSHYiJian(String BMFZRSHYiJian) {
        this.BMFZRSHYiJian = BMFZRSHYiJian;
    }
}
