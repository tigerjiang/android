package com.igrs.base.demo;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.igrs.base.IProviderExporterService;
import com.igrs.base.android.util.IgrsRet;
import com.igrs.base.lan.beans.ConnectHostDevicesBean;
import com.igrs.base.lan.beans.LanGoodByeBean;
import com.igrs.base.lan.beans.LocalResourceTransformBean;
import com.igrs.base.pakects.iqs.UserWanLoginBean;
import com.igrs.base.pakects.iqs.UserWanRegBean;
import com.igrs.base.util.ConstPart;

public class CommManager {

	
	private  IgrsBaseProxyManager  igrsBaseProxyManager=IgrsBaseProxyManager.getInstance();
	
	private String  TAG = CommManager.class.getSimpleName();
	private IProviderExporterService  appAccessAp;
	private Handler myProcessHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			
			Log.i(TAG, String.valueOf(msg.what));
			switch(msg.what)
			{
			   case GlobalControl.LOCAL_ADDRESS_BACK_RCV:
				    LocalResourceTransformBean localResourceTransformBean =(LocalResourceTransformBean) msg.obj;
				break;
			   case  ConstPart.MSG_CONNECT: //登陆结果查询
					//21 display sucess
				 ///  
				   if(ConstPart.MSG_STATUS_SUCCESS==msg.arg1)
				    Log.i(TAG, "user success connect"+msg.arg1); 
					break;
					
			    case  ConstPart.MSG_DISCONNECT: //注销结果查看
			    	 Log.i(TAG, "user disconnect"+msg.arg1); 
				break;
			   case ConstPart.MSG_REG_STATUS: //注册结果查看
				   
				   if (421==msg.arg1)
				   {
					   Log.i(TAG, "user exits"); 
				   } else if (200==msg.arg1)
				   {
					   Log.i(TAG, "user reg successful"); 
					   userLoginByIgrs();
				   }
			    break;
			}
		}
		
	};
	
	/**
	 * 第一次使用的时候，需要查询当前宿主上被连接的设备
	 */
	public void requestHostDevices() {
		
		
		ConnectHostDevicesBean connectHostDevicesBean=new ConnectHostDevicesBean();
		connectHostDevicesBean.isRequest = true;
		
		 igrsBaseProxyManager.sendQueryBaseBean(connectHostDevicesBean,myProcessHandler,connectHostDevicesBean.getNamespace());
	}
	/**
	 * 第一次使用的时候，需要查询当前宿主上被连接的设备
	 */
	public void closingSeriveName(String serviceName) {
		
		
		 LanGoodByeBean  closingBean = new LanGoodByeBean();
		 closingBean.setTo(serviceName);
		 igrsBaseProxyManager.sendQueryBaseBean(closingBean,null,null);
	}
	
	
	/**
	 * 第一次使用的时候，需要查询当前宿主上被连接的设备
	 */
	public void  bindDevice() {
		
		IProviderExporterService  appAccessAp;

		 appAccessAp = igrsBaseProxyManager.getConnectService();
		
		Handler myProcessHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				
				 if (msg.what == ConstPart.MSG_STATUS_IQ_RESULT) {
					 
			
					switch ( msg.arg1) {
						case  IgrsRet.IGRS_RET_OK:
						  //消息发送成功
							break;
							
						case  IgrsRet.IGRS_RET_USER_NOT_EXIST:
						 //用户不存在	
							break;
						case  IgrsRet.IGRS_RET_FAIL:
							   //发送失败
								break;
								
					   
						default:
							break;
						}
					}
			}
			
		};
		
		//bind 设备 
	
		String deviceId ="";
		String verifyCode = "";
		Messenger messenger = new Messenger(myProcessHandler);
		try {
			appAccessAp.userBindDevice(deviceId, verifyCode, messenger);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//unbind  取消bind
		
		try {
			appAccessAp.userUnBindDevice(deviceId, verifyCode, messenger);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * 第一次使用的时候，需要查询当前宿主上被连接的设备
	 */
	public void requestLocalAddressBack() {
		
		
		LocalResourceTransformBean localResourceTransformBean = new LocalResourceTransformBean();
		
		localResourceTransformBean.inputStrOrAddress = "data/data/music/在哪儿.mp3";
		
		igrsBaseProxyManager.sendQueryBaseBean(localResourceTransformBean,myProcessHandler,localResourceTransformBean.getNamespace());
	}
	
	 /**
	  * 用户登录
	  */
	public void userLoginByIgrs()
	{
		
		UserWanLoginBean  userWanLoginBean = new UserWanLoginBean();
		/**
		 * true 表示登陆  false 表示注销登陆
		 */
		userWanLoginBean.isLoginOrDisconnect = true ;
		userWanLoginBean.userName = "wangbo111x";
		userWanLoginBean.userPassword = "123456";
		Messenger callBackMessager = new Messenger(myProcessHandler);
		
		igrsBaseProxyManager.sendQueryBaseBeanByCallBackHandler(userWanLoginBean, callBackMessager);
		
		//注销登陆
		//igrsBaseProxyManager.sendQueryBaseBeanByCallBackHandler(userWanLoginBean, callBackMessager);
	}
	
	/**
	 * 用户注册
	 */
	public void userRegByigrs()
	{
		UserWanRegBean userWanRegBean = new UserWanRegBean();
		userWanRegBean.userName =  "wangbo111x";
		userWanRegBean.password = "123456";
		Messenger callBackMessager = new Messenger(myProcessHandler);
		igrsBaseProxyManager.sendQueryBaseBeanByCallBackHandler(userWanRegBean, callBackMessager);
		
	}
			
}
