package org.testing_Automation.utility;
import java.util.ArrayList;
import java.util.HashMap;

// This class contains all the getters and setters for excel sheet 1 which contains primary details
public class InputOutputDaoObject  {

	ArrayList<Object> arrInputData = null;
	String   sTestCaseId =           null;
	String   sMainEntityName =   	 null;
	String   sChildEntityName =      null;
	String   sMenuEntityName =       null;
	String   sSubMenuEntityName =    null;
	String   sUserName =             null;
	String   sOperation =            null;
	boolean  bOperationStatus =      false;
	String   sOperationMessage =     null;
	String   primaryKey =            null;
	String   tabToSwitch =           null;
	ArrayList<String> sNavigationEntityName = null;
	

	public String toString(){
		StringBuffer buff = new StringBuffer();
		
		buff.append("sTestCaseId =").append(sTestCaseId).append("|")
		.append("sUserName =").append(sUserName).append("|")
		.append("sMenuEntityName =").append(sMenuEntityName).append("|")
		.append("sMainEntityName =").append(sMainEntityName).append("|")
		.append("sChildEntityName =").append(sChildEntityName).append("|")
		.append("arrInputData =").append(arrInputData);
		return buff.toString();
	}
	
	public void setsTestCaseId(String sTestCaseId) {
		this.sTestCaseId = sTestCaseId;
	}

	public InputOutputDaoObject(){
		arrInputData = new ArrayList<Object>();
		
	}
	
	public void addObject(Object object){
		arrInputData.add(object);
	}
	
	public ArrayList<Object> getDataDao(){
		return arrInputData;
	}

	public String getMainEntityName() {
		return sMainEntityName;
	}

	public void setMainEntityName(String mainEntityName) {
		sMainEntityName = mainEntityName;
	}
	
	public void setNavigationEntityName(ArrayList<String> navigationEntityName) {
		sNavigationEntityName = navigationEntityName;
		
	}
	
	public ArrayList<String> getNavigationEntityname(){
		return sNavigationEntityName;
	}

	public String getChildEntityName() {
		return sChildEntityName;
	}

	public void setChildEntityName(String childEntityName) {
		sChildEntityName = childEntityName;
	}

	public String getMenuEntityName() {
		return sMenuEntityName;
	}

	public void setMenuEntityName(String menuEntityName) {
		sMenuEntityName = menuEntityName;
	}
	
	public void setSubMenuEntityName(String subMenuEntityName) {
		sSubMenuEntityName = subMenuEntityName;
		
	}

	public String getTestCaseId() {
		return sTestCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		sTestCaseId = testCaseId;
	}

	public String getUserName() {
		return sUserName;
	}

	public void setUserName(String userName) {
		sUserName = userName.toUpperCase();
	}

	public String getOperation() {
		return sOperation;
	}

	public void setOperation(String operation) {
		sOperation = operation;
	}

	public boolean isOperationStatus() {
		return bOperationStatus;
	}

	public void setOperationStatus(boolean operationStatus) {
		bOperationStatus = operationStatus;
	}

	public String getOperationMessage() {
		return sOperationMessage;
	}

	public void setOperationMessage(String operationReqId) {
		sOperationMessage = operationReqId;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getTabToSwitch() {
		return tabToSwitch;
	}

	public void setTabToSwitch(String tabToSwitch) {
		this.tabToSwitch = tabToSwitch;
	}

	

	
	
}
