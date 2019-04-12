package com.bzs.utils.jsontobean;

import java.io.Serializable;
import java.util.List;

public class JsonRootBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfo UserInfo;
	private Item Item;
	private String CustKey;
	private int BusinessStatus;
	private String StatusMessage;
	
	public void setUserInfo(UserInfo UserInfo) {
		this.UserInfo = UserInfo;
	}

	public UserInfo getUserInfo() {
		return UserInfo;
	}

	public void setItem(Item Item) {
		this.Item = Item;
	}

	public Item getItem() {
		return Item;
	}

	public void setCustKey(String CustKey) {
		this.CustKey = CustKey;
	}

	public String getCustKey() {
		return CustKey;
	}

	public void setBusinessStatus(int BusinessStatus) {
		this.BusinessStatus = BusinessStatus;
	}

	public int getBusinessStatus() {
		return BusinessStatus;
	}

	public void setStatusMessage(String StatusMessage) {
		this.StatusMessage = StatusMessage;
	}

	public String getStatusMessage() {
		return StatusMessage;
	}

}
