package com.mdi.response;

public enum StatusResponse {
	SUCCESS ("Success"),
    ERROR ("Error");
 
    private String status;       
    

	private StatusResponse(String string) {
		this.status=string;
	}
	
	public String getStatusResponse() {
		return this.status;
	}
}