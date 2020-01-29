package com.yepit.flow.common.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * Created by qianlong on 2017/8/19.
 */
public enum DocTypeEnum implements EnumValue<Integer, String> {

    ProofOfId(1001, "身份证"), ProofOfHouseResidence(1002, "户口本"), ProofOfHouse(1003, "房产证"),
    ProofOfDriver(1004, "驾驶证"), ProofOfBankCard(1005, "银行卡"), ProofOfCar(1006, "车辆相关信息"),
    BankStatement(1007, "银行流水"), UserCreditReport(1008, "证信报告"), SystemAnalysisReport(1009, "系统分析报告"),
    FaceInterview(1010, "面签资料"), HomeVisit(1011, "家访资料"), PurchaseFirstMoney(1012, "客户购车首期款"),
    InsuranceConfirmPic(1013, "保险确认"), NameplateForCarPic(1014, "车辆铭牌照片"),CarVINPic(1015,"车架号"),
    ContactForCar(1016, "购车合同"),CertificateForCar(1017, "车辆合格证"), CarBargainMoneyPic(1018, "购车定金照片"),
    CarPurchasingTax(1019, "购车税"),PlateNumber(1020,"车牌"),ArrowDriving(1021,"行驶证"),
    VehicleRegistration(1022,"车辆登记证"),ExaminationOfCar(1023,"验车照片"),InstallLogOfCar(1024,"车辆安装记录"),
    GroupPhoto(1025,"人车合影");

    private int value;
    private String desc;

    private DocTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(Integer value) {
        for (DocTypeEnum enumObj : DocTypeEnum.values()) {
            if (enumObj.getValue() == value) {
                return enumObj.getDesc();
            }
        }
        return "";
    }

}
