package com.joy.cloudshare.common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DeviceTypeVO implements Serializable{
	

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name = "";
	private String ip = "";
	private String playerType = "";
	private String screenSupport = "";
	private String irSupported = "";
	private String appType = "";
	private String type = "";
	private String pad2tvtype = "";
	
	private List<Map<String, String>> sourceDesp  = new ArrayList<Map<String, String>>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPad2tvtype() {
		return pad2tvtype;
	}
	public void setPad2tvtype(String pad2tvtype) {
		this.pad2tvtype = pad2tvtype;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlayerType() {
		return playerType;
	}
	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}
	public String getIrSupported() {
		return irSupported;
	}
	public void setIrSupported(String irSupported) {
		this.irSupported = irSupported;
	}
	
	public String getScreenSupport() {
		return screenSupport;
	}
	public void setScreenSupport(String screenSupport) {
		this.screenSupport = screenSupport;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public List<Map<String, String>> getSourceDesp() {
		return sourceDesp;
	}
	public void setSourceDesp(List<Map<String, String>> sourceDesp) {
		this.sourceDesp = sourceDesp;
	}
	
	public void setValue(DeviceTypeVO value)
	{
		if(value == null)
			return;
		name = value.name;
		ip = value.ip;
		playerType = value.playerType;
		screenSupport = value.screenSupport;
		irSupported = value.irSupported;
		appType = value.appType;
		type = value.type;
		pad2tvtype = value.pad2tvtype;
		sourceDesp = value.sourceDesp;
	}
}
