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
			   case  ConstPart.MSG_CONNECT: //��½�����ѯ
					//21 display sucess
				 ///  
				   if(ConstPart.MSG_STATUS_SUCCESS==msg.arg1)
				    Log.i(TAG, "user success connect"+msg.arg1); 
					break;
					
			    case  ConstPart.MSG_DISCONNECT: //ע������鿴
			    	 Log.i(TAG, "user disconnect"+msg.arg1); 
				break;
			   case ConstPart.MSG_REG_STATUS: //ע�����鿴
				   
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
	 * ��һ��ʹ�õ�ʱ����Ҫ��ѯ��ǰ�����ϱ����ӵ��豸
	 */
	public void requestHostDevices() {
		
		
		ConnectHostDevicesBean connectHostDevicesBean=new ConnectHostDevicesBean();
		connectHostDevicesBean.isRequest = true;
		
		 igrsBaseProxyManager.sendQueryBaseBean(connectHostDevicesBean,myProcessHandler,connectHostDevicesBean.getNamespace());
	}
	/**
	 * ��һ��ʹ�õ�ʱ����Ҫ��ѯ��ǰ�����ϱ����ӵ��豸
	 */
	public void closingSeriveName(String serviceName) {
		
		
		 LanGoodByeBean  closingBean = new LanGoodByeBean();
		 closingBean.setTo(serviceName);
		 igrsBaseProxyManager.sendQueryBaseBean(closingBean,null,null);
	}
	
	
	/**
	 * ��һ��ʹ�õ�ʱ����Ҫ��ѯ��ǰ�����ϱ����ӵ��豸
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
						  //��Ϣ���ͳɹ�
							break;
							
						case  IgrsRet.IGRS_RET_USER_NOT_EXIST:
						 //�û�������	
							break;
						case  IgrsRet.IGRS_RET_FAIL:
							   //����ʧ��
								break;
								
					   
						default:
							break;
						}
					}
			}
			
		};
		
		//bind �豸 
	
		String deviceId ="";
		String verifyCode = "";
		Messenger messenger = new Messenger(myProcessHandler);
		try {
			appAccessAp.userBindDevice(deviceId, verifyCode, messenger);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//unbind  ȡ��bind
		
		try {
			appAccessAp.userUnBindDevice(deviceId, verifyCode, messenger);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * ��һ��ʹ�õ�ʱ����Ҫ��ѯ��ǰ�����ϱ����ӵ��豸
	 */
	public void requestLocalAddressBack() {
		
		
		LocalResourceTransformBean localResourceTransformBean = new LocalResourceTransformBean();
		
		localResourceTransformBean.inputStrOrAddress = "data/data/music/���Ķ�.mp3";
		
		igrsBaseProxyManager.sendQueryBaseBean(localResourceTransformBean,myProcessHandler,localResourceTransformBean.getNamespace());
	}
	
	 /**
	  * �û���¼
	  */
	public void userLoginByIgrs()
	{
		
		UserWanLoginBean  userWanLoginBean = new UserWanLoginBean();
		/**
		 * true ��ʾ��½  false ��ʾע����½
		 */
		userWanLoginBean.isLoginOrDisconnect = true ;
		userWanLoginBean.userName = "wangbo111x";
		userWanLoginBean.userPassword = "123456";
		Messenger callBackMessager = new Messenger(myProcessHandler);
		
		igrsBaseProxyManager.sendQueryBaseBeanByCallBackHandler(userWanLoginBean, callBackMessager);
		
		//ע����½
		//igrsBaseProxyManager.sendQueryBaseBeanByCallBackHandler(userWanLoginBean, callBackMessager);
	}
	
	/**
	 * �û�ע��
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