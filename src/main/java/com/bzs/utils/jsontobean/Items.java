package com.bzs.utils.jsontobean;

public class Items {
	private String VehicleName;//品牌型号
	private String VehicleNo;//精友编码
	private String PurchasePrice;//新车购置价
	private Integer VehicleAlias;//动力别名
	private String VehicleSeat;//座位数
	private Double VehicleExhaust;//排气量
	private String VehicleYear;//购置年款
	private Integer  Source;//该车型支持的保险公司枚举之和
	private String SourceName;//支持的保险公司

	public Items() {
		super();
	}

	public String getVehicleName() {
		return VehicleName;
	}

	public void setVehicleName(String vehicleName) {
		VehicleName = vehicleName;
	}

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

	public Integer getVehicleAlias() {
		return VehicleAlias;
	}

	public void setVehicleAlias(Integer vehicleAlias) {
		VehicleAlias = vehicleAlias;
	}

	public String getVehicleSeat() {
		return VehicleSeat;
	}

	public void setVehicleSeat(String vehicleSeat) {
		VehicleSeat = vehicleSeat;
	}

	public Double getVehicleExhaust() {
		return VehicleExhaust;
	}

	public void setVehicleExhaust(Double vehicleExhaust) {
		VehicleExhaust = vehicleExhaust;
	}

	public String getVehicleYear() {
		return VehicleYear;
	}

	public void setVehicleYear(String vehicleYear) {
		VehicleYear = vehicleYear;
	}

	public Integer getSource() {
		return Source;
	}

	public void setSource(Integer source) {
		Source = source;
	}

	public String getSourceName() {
		return SourceName;
	}

	public void setSourceName(String sourceName) {
		SourceName = sourceName;
	}

}
