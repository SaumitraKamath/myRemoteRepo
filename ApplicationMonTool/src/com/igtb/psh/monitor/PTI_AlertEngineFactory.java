package com.igtb.psh.monitor;


public class PTI_AlertEngineFactory {
	
	public static PTI_AlertEngineImpl mWriteLog(){
		
		return new PTI_AlertEngineImpl();
	}
	
	public static boolean  mWriteMail(){
		
		return new PTI_AlertEngineImpl().mWriteEmail();
	}
	
	public boolean  mWriteDashboard(){
		
		return new PTI_AlertEngineImpl().mWriteDashboard();
	}
	

}
