package com.bzs.utils.jsontobean;

import java.util.List;

public class JsonToBeanNewVehicleInfo {
	private int BusinessStatus;
    private String StatusMessage;//信息描述
    private List<Items> Items;
    private String CustKey;
	private String MoldName;
	private String CheckMsg;//信息描述
	private String CarType;//大小车类型(0:小车 1：大车 -1：不可用）
	private String TypeName;//行驶证类型名称
	private Integer CheckCode;//0：可以全渠道报价 ，-1：不可以全渠道报价
	
	public JsonToBeanNewVehicleInfo() {
		super();
	}
	public int getBusinessStatus() {
		return BusinessStatus;
	}
	public void setBusinessStatus(int businessStatus) {
		BusinessStatus = businessStatus;
	}
	public String getStatusMessage() {
		return StatusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		StatusMessage = statusMessage;
	}
	public List<Items> getItems() {
		return Items;
	}
	public void setItems(List<Items> items) {
		Items = items;
	}
	public String getCustKey() {
		return CustKey;
	}
	public void setCustKey(String custKey) {
		CustKey = custKey;
	}
	public String getMoldName() {
		return MoldName;
	}
	public void setMoldName(String moldName) {
		MoldName = moldName;
	}
	public String getCheckMsg() {
		return CheckMsg;
	}
	public void setCheckMsg(String checkMsg) {
		CheckMsg = checkMsg;
	}
	public String getCarType() {
		return CarType;
	}
	public void setCarType(String carType) {
		CarType = carType;
	}
	public String getTypeName() {
		return TypeName;
	}
	public void setTypeName(String typeName) {
		TypeName = typeName;
	}
	public Integer getCheckCode() {
		return CheckCode;
	}
	public void setCheckCode(Integer checkCode) {
		CheckCode = checkCode;
	}
	
}
