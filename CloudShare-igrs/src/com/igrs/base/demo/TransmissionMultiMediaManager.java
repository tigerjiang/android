package com.igrs.base.demo;
import java.util.Map;

import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Messenger;
import android.os.RemoteException;

import com.igrs.base.IProviderExporterService;
import com.igrs.base.android.util.IgrsType.DeviceType;
import com.igrs.base.android.util.IgrsType.FileType;
import com.igrs.base.lan.IgrsLanInfo;
import com.igrs.base.parcelable.BaseNetWorkInfoBean.NetworkType;
import com.igrs.base.parcelable.LanReferenceCmdInfoBean;
import com.igrs.base.parcelable.ReferenceCmdInfoBean;
import com.igrs.base.services.commend.CommonInfoRecommendBean;
import com.igrs.base.services.lantransfer.IgrsBaseExporterLanService;
import com.joy.cloudshare.common.MediaInfo;
/**
 * 定义推荐视频的统一处理方式,命令推送
 * @author wangbo
 */
public class TransmissionMultiMediaManager {

	private IProviderExporterService   commonNetExporterService;
	private IgrsBaseExporterLanService baseLanExporterService = null;
	private Context context=null;
	private   Handler netResponseHandler;
	public TransmissionMultiMediaManager(Context mainActivity,IProviderExporterService  commonNetExporterService, IgrsBaseExporterLanService baseLanExporterService,Handler netResponseHandler)
	{
		this.context=mainActivity;
		this.baseLanExporterService = baseLanExporterService;
	    this.commonNetExporterService= 	commonNetExporterService;
	    this.netResponseHandler=netResponseHandler;
	    
	  //  this.commonNetExporterService = commonNetExporterService.getLanService();
	}
	
	/**
	 * 局域网到局域网视频互相推荐
	 * @param fileType 播放类型
	 * @param toTerminalType 终端类型
	 * @param condtions 传递参数值
	 */
	public void sendLanResourceToLanFriends(FileType fileType,Map<String,String> condtions)
	{
		
			CommonInfoRecommendBean commonInfoRecommendBean=new CommonInfoRecommendBean();
			commonInfoRecommendBean.setReceiveDeviceId(condtions.get("deviceid"));
			commonInfoRecommendBean.setTo(condtions.get("deviceid"));
			commonInfoRecommendBean.setFilesize(condtions.get("filesize")==null?"50mb":condtions.get("filesize"));
			commonInfoRecommendBean.setTitle(condtions.get("title")==null?"":condtions.get("title"));
			commonInfoRecommendBean.setImgurl(condtions.get("imgurl")==null?"":condtions.get("imgurl"));
			String offset=condtions.get("offset")==null?"0":condtions.get("offset");
			commonInfoRecommendBean.setTimestamp(Long.parseLong(offset));
			commonInfoRecommendBean.setPlayType(fileType);
			commonInfoRecommendBean.setCid(fileType.name());
			commonInfoRecommendBean.setPlayNow("100");
			commonInfoRecommendBean.setGid(condtions.get("gid")==null?"":condtions.get("gid"));
			if (baseLanExporterService!= null) {
			
					Messenger msg = new Messenger(netResponseHandler);
					try {
						 baseLanExporterService.sendLanDevicePlayerMessage(commonInfoRecommendBean, msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			}
	}
	
	
	
	/**
	 * 局域网到局域网视频互相推荐
	 * @param mediaData 本地资源
	 * @param serviceName 终端serviceNmae
	 */
	public void sendLocalResourceToLanFriends(MediaInfo mediaData,String serviceName)
	{
		
			CommonInfoRecommendBean commonInfoRecommendBean=new CommonInfoRecommendBean();

			commonInfoRecommendBean.setTo(serviceName);
			commonInfoRecommendBean.setFilesize(mediaData.getFileSize()==null?"50mb":mediaData.getFileSize());
			commonInfoRecommendBean.setTitle(mediaData.getName()==null?"":mediaData.getName());
			commonInfoRecommendBean.setImgurl(mediaData.getUrl()==null?"":mediaData.getUrl());
//			String offset=condtions.get("offset")==null?"0":condtions.get("offset");
			commonInfoRecommendBean.setTimestamp(0);
			commonInfoRecommendBean.setPlayType(FileType.localBesideResource);
			commonInfoRecommendBean.setGid(mediaData.getPath()); 
			//如mnt/sdcard 为共享目录，那么 下面的 文件夹   music/那些年.mp3  mnt/sdcard/   music/ddd.mps
			if (baseLanExporterService!= null) {
			
					Messenger msg = new Messenger(netResponseHandler);
					try {
						 baseLanExporterService.sendLanDevicePlayerMessage(commonInfoRecommendBean, msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			}
	}
	
	
	
	/**
	 * 局域网到局域网视频互相推荐
	 * @param fileType 播放类型
	 * @param toTerminalType 终端类型
	 * @param condtions 传递参数值
	 */
	public void sendLocalResourceToLanFriends(Map<String,String> condtions)
	{
		
			CommonInfoRecommendBean commonInfoRecommendBean=new CommonInfoRecommendBean();

			commonInfoRecommendBean.setTo("wangbo");
			commonInfoRecommendBean.setFilesize(condtions.get("filesize")==null?"50mb":condtions.get("filesize"));
			commonInfoRecommendBean.setTitle(condtions.get("title")==null?"":condtions.get("title"));
			commonInfoRecommendBean.setImgurl(condtions.get("imgurl")==null?"":condtions.get("imgurl"));
			String offset=condtions.get("offset")==null?"0":condtions.get("offset");
			commonInfoRecommendBean.setTimestamp(Long.parseLong(offset));
			commonInfoRecommendBean.setPlayType(FileType.localBesideResource);
			commonInfoRecommendBean.setGid("music/那些年.mp3"); 
			//如mnt/sdcard 为共享目录，那么 下面的 文件夹   music/那些年.mp3  mnt/sdcard/   music/ddd.mps
			if (baseLanExporterService!= null) {
			
					Messenger msg = new Messenger(netResponseHandler);
					try {
						 baseLanExporterService.sendLanDevicePlayerMessage(commonInfoRecommendBean, msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			}
	}
	
	/**
	 *  互联网资源推荐到局域网视频播放
	 * @param fileTpe 播放类型
	 * @param condtions 传递参数值
	 */
	public void sendNetResourceToLanFriends(FileType fileTpe,Map<String,String> condtions)
	{
			
			CommonInfoRecommendBean commonInfoRecommendBean=new CommonInfoRecommendBean();
			commonInfoRecommendBean.setReceiveDeviceId(condtions.get("deviceid"));
			commonInfoRecommendBean.setTo(condtions.get("deviceid"));
			commonInfoRecommendBean.setFilesize(condtions.get("filesize")==null?"":condtions.get("filesize"));
			commonInfoRecommendBean.setTitle(condtions.get("title")==null?"":condtions.get("title"));
			commonInfoRecommendBean.setImgurl(condtions.get("imgurl")==null?"":condtions.get("imgurl"));
			String offset=condtions.get("offset")==null?"0":condtions.get("offset");
			commonInfoRecommendBean.setTimestamp(Long.parseLong(offset));
			commonInfoRecommendBean.setPlayType(fileTpe);
			commonInfoRecommendBean.setCid(fileTpe.name());
			commonInfoRecommendBean.setGid(condtions.get("gid")==null?"":condtions.get("gid"));
		
			if (baseLanExporterService!= null) {
				
			
					Messenger msg = new Messenger(netResponseHandler);
					try {
						 baseLanExporterService.sendLanDevicePlayerMessage(commonInfoRecommendBean, msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			}
			
		}
	
	/**
	 *  互联网资源推荐到互联网视频播放
	 * @param fileTpe 播放类型
	 * @param condtions 传递参数值
	 */
	public void sendNetResourceToNetFriends(FileType fileTpe,Map<String,String> condtions)
	{
		
		CommonInfoRecommendBean commonInfoRecommendBean=new CommonInfoRecommendBean();
		commonInfoRecommendBean.setReceiveDeviceId(condtions.get("deviceid"));
		commonInfoRecommendBean.setTo(condtions.get("deviceid"));
		commonInfoRecommendBean.setTitle(condtions.get("title")==null?"":condtions.get("title"));
		commonInfoRecommendBean.setImgurl(condtions.get("imgurl")==null?"":condtions.get("imgurl"));
		commonInfoRecommendBean.setCid(fileTpe.name());
		commonInfoRecommendBean.setGid(condtions.get("gid")==null?"":condtions.get("gid"));
		String offset=condtions.get("offset")==null?"0":condtions.get("offset");
		commonInfoRecommendBean.setTimestamp(Long.parseLong(offset));
		commonInfoRecommendBean.setFilesize(condtions.get("filesize")==null?"":condtions.get("filesize"));
		commonInfoRecommendBean.setPlayType(fileTpe);
	
		if (commonNetExporterService != null) {
			
			Messenger msg = new Messenger(netResponseHandler);
			try {
				commonNetExporterService.recommonedMultiMediaToTerminal(commonInfoRecommendBean,msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关于网命令推送
	 * @param condtions
	 */
	public void sendCmdToTerminalByNet(Map<String,String> condtions) {
		
		ReferenceCmdInfoBean referenceCmdInfoBean =new ReferenceCmdInfoBean ();
		referenceCmdInfoBean.setTo(condtions.get("to"));//要操作的设备
		referenceCmdInfoBean.setCmdClass("input");
		referenceCmdInfoBean.setCmdType("input");//命令类型
		referenceCmdInfoBean.setCmdCtrl("set");
		referenceCmdInfoBean.setCmdParam("");//命令参数
		referenceCmdInfoBean.setCmdValue("KEY_RIGHT");//命令值
		if (commonNetExporterService != null) {
			
			Messenger msg = new Messenger(netResponseHandler);
			try {
				commonNetExporterService.recommonedCommandToTerminal(referenceCmdInfoBean, msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void sendBigContent(String to,String content,boolean isWan) {
		
		if (isWan)
		{
			ReferenceCmdInfoBean referenceCmdInfoBean = new ReferenceCmdInfoBean();
			referenceCmdInfoBean.setCmdClass("msgInputMethod");
			referenceCmdInfoBean.setCmdType("track");
			referenceCmdInfoBean.setTo(StringUtils.parseName(to));
			referenceCmdInfoBean.setCmdCtrl("set");
			referenceCmdInfoBean.setCmdParam("igrs");
			referenceCmdInfoBean.setCmdValue(content);
			  Messenger msg = new Messenger(netResponseHandler);
			try {
				commonNetExporterService.recommonedCommandToTerminal(referenceCmdInfoBean, msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else
		{
			
			 LanReferenceCmdInfoBean lanReferenceCmdInfoBean=new LanReferenceCmdInfoBean();
	          lanReferenceCmdInfoBean.setCmdClass("msgInputMethod");
		         lanReferenceCmdInfoBean.setTo(to);
	          lanReferenceCmdInfoBean.setCmdType("track");
	          lanReferenceCmdInfoBean.setCmdCtrl("set");
	          lanReferenceCmdInfoBean.setCmdParam("igrs");
	          lanReferenceCmdInfoBean.setCmdValue(content);
	          lanReferenceCmdInfoBean.setNetworkType(NetworkType .LANNETWORK);
				  
	          Messenger msg = new Messenger(netResponseHandler);
	       
	        
				 try {
					baseLanExporterService.sendLanCmdMessage(lanReferenceCmdInfoBean,  msg);
				  } catch (RemoteException e) {
					 e.printStackTrace();
				  }	
			
		}
		 	
	}
}
