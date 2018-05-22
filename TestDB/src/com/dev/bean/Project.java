package com.dev.bean;

public class Project {

	private String loginUser;//SESSION_USER userID
	private String projectNumber;//PRJ_NO
	private String startDate;//START_DATE
	private String endDate;//END_DATE
	private String clientId;//CLIENT_ID
	private String posRequest;//POS_REQ--int
	//private Integer posRequest;
	
	private String mrgNumber;//MRG_NO
	private String projectcost;//PRJ_COST--int
	//private Integer projectcost;
	
	private String projectlocation;//PRJ_LOC
	private String projectStatus;//PRJ_STATUS
	private String projectDes;//PRJ_DES
	private String empNumber;//EMP_NO
	
	
	
	/*public Integer getProjectcost() {
		return projectcost;
	}
	public void setProjectcost(Integer projectcost) {
		this.projectcost = projectcost;
	}*/
	/*public Integer getPosRequest() {
		return posRequest;
	}
	public void setPosRequest(Integer posRequest) {
		this.posRequest = posRequest;
	}*/
	
	public String getEmpNumber() {
		return empNumber;
	}
	public String getPosRequest() {
		return posRequest;
	}
	public void setPosRequest(String posRequest) {
		this.posRequest = posRequest;
	}
	public String getProjectcost() {
		return projectcost;
	}
	public void setProjectcost(String projectcost) {
		this.projectcost = projectcost;
	}
	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/*public String getPosRequest() {
		return posRequest;
	}
	public void setPosRequest(String posRequest) {
		this.posRequest = posRequest;
	}*/
	public String getMrgNumber() {
		return mrgNumber;
	}
	public void setMrgNumber(String mrgNumber) {
		this.mrgNumber = mrgNumber;
	}
	/*public String getProjectcost() {
		return projectcost;
	}
	public void setProjectcost(String projectcost) {
		this.projectcost = projectcost;
	}*/
	public String getProjectlocation() {
		return projectlocation;
	}
	public void setProjectlocation(String projectlocation) {
		this.projectlocation = projectlocation;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getProjectDes() {
		return projectDes;
	}
	public void setProjectDes(String projectDes) {
		this.projectDes = projectDes;
	}
}
