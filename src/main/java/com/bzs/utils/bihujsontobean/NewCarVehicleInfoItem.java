package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description: 新车车辆信息Item
 * @author: dengl
 * @create: 2019-06-10 10:52
 */
public class NewCarVehicleInfoItem {
    private String VehicleNo;//精友编码
    private String PurchasePrice;//购置价格
    private String VehicleAlias;//动力类型
    private String VehicleName;//品牌型号
    private String VehicleSeat;//座位数
    private String VehicleExhaust;//排气量
    private String VehicleYear;//年款

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public String getVehicleAlias() {
        return VehicleAlias;
    }

    public void setVehicleAlias(String vehicleAlias) {
        VehicleAlias = vehicleAlias;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getVehicleSeat() {
        return VehicleSeat;
    }

    public void setVehicleSeat(String vehicleSeat) {
        VehicleSeat = vehicleSeat;
    }

    public String getVehicleExhaust() {
        return VehicleExhaust;
    }

    public void setVehicleExhaust(String vehicleExhaust) {
        VehicleExhaust = vehicleExhaust;
    }

    public String getVehicleYear() {
        return VehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        VehicleYear = vehicleYear;
    }
}
