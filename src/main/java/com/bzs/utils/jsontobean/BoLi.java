package com.bzs.utils.jsontobean;

import java.io.Serializable;

public class BoLi  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int BaoE;
	private int BaoFei;
	public BoLi (){
		super();
	}
	public void setBaoE(int BaoE) {
		this.BaoE = BaoE;
	}

	public int getBaoE() {
		return BaoE;
	}

	public void setBaoFei(int BaoFei) {
		this.BaoFei = BaoFei;
	}

	public int getBaoFei() {
		return BaoFei;
	}
}
