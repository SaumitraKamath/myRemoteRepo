package com.igtb.psh.monitor;

import java.math.BigDecimal;
import java.util.List;

public class PTI_FileDetBean
{

    private BigDecimal workitemId;
    private List<BigDecimal> txnWidList;
    private int actualTxnCount;
    private String fileName;

    public PTI_FileDetBean()
    {

    }

    public BigDecimal getWorkitemId()
    {
	return workitemId;
    }

    public void setWorkitemId(BigDecimal workitemId)
    {
	this.workitemId = workitemId;
    }

    public List<BigDecimal> getTxnWidList()
    {
	return txnWidList;
    }

    public void setTxnWidList(List<BigDecimal> txnWidList)
    {
	this.txnWidList = txnWidList;
    }

    public int getActualTxnCount()
    {
	return actualTxnCount;
    }

    public void setActualTxnCount(int actualTxnCount)
    {
	this.actualTxnCount = actualTxnCount;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
	return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName)
    {
	this.fileName = fileName;
    }

}