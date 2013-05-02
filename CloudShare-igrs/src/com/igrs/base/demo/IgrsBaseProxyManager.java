/*******************************************************************************
 * Copyright (C) 2011 wangbo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.igrs.base.demo;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.igrs.base.IProviderExporterService;
import com.igrs.base.IgrsBaseConnectListener;
import com.igrs.base.IndependecServiceController;
import com.igrs.base.appcallbacks.IgrsBaseIgrsQueryCallback;
import com.igrs.base.beans.IgrsBaseBean;
import com.igrs.base.jingle.beans.JingleAccpetFileBean;
import com.igrs.base.lan.beans.CloseLanServiceBean;
import com.igrs.base.lan.beans.ConnectHostDevicesBean;
import com.igrs.base.lan.beans.EPGTvListSearchBean;
import com.igrs.base.lan.beans.LanLoadingResultBean;
import com.igrs.base.lan.beans.LocalResourceTransformBean;
import com.igrs.base.lan.beans.RenameDevicesBean;
import com.igrs.base.lenovo.netdesk.CurrentConnectUserInfoBean;
import com.igrs.base.pakects.iqs.EPGVersionBean;
import com.igrs.base.pakects.iqs.EpgTotalPagesBean;
import com.igrs.base.parcelable.IgrsBaseQuery;
import com.igrs.base.parcelable.ReferenceCmdInfoBean;
import com.igrs.base.protocol.IQImpl;
import com.igrs.base.services.lantransfer.IgrsBaseExporterLanService;
import com.igrs.base.util.IgrsTag;
/**
 *��Э��ջ����ͷ�װ
 */
public class IgrsBaseProxyManager implements IgrsBaseConnectListener{
	
	/** The TAG for the Log. */
	private String TAG =IgrsBaseProxyManager.class.getSimpleName();

	/** The instance. */
	private static IgrsBaseProxyManager instance;
	
	/** 
	 * The igrsServer result  handlers will callback the calling result. 
	 * 
	 * */
	private  ConcurrentHashMap<String,Handler> proxyResultConnectHandlers;
	
	/**
	 *  The IndependecServiceController for the communication between the android-client and application. 
	 * 
	 * dealing with bind and unbind servive
	 * */
	private IndependecServiceController independecServiceController;
	
	/** The igrsBase service. which is provider api and register callback */
	private IProviderExporterService igrsBasePService;
	

	private Handler serviConnecthandler;
	/** 
	 * Is true if we already registered the extensions. 
	 * 
	 * */
	private boolean extensionRegistered;
	
	/**
	 * Gets the single instance of IgrsBaseProxyManager.
	 * 
	 * @return single instance of IgrsBaseProxyManager
	 */
	public static IgrsBaseProxyManager getInstance(){
		if(instance == null){
			synchronized (IgrsBaseProxyManager.class) {
				if(instance==null)
				{
				 instance = new IgrsBaseProxyManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Instantiates a new IgrsService manager.
	 */
	private IgrsBaseProxyManager(){
		proxyResultConnectHandlers = new ConcurrentHashMap<String,Handler>();
		extensionRegistered = false;
	}
	
	/**
	 * ע��ص�����
	 * @throws RemoteException the remote exception
	 */
	private void registerHisenseExtensions(){
	  /**
	   * �û�ע���Լ��Ļص�����ӿ�
	   */
	   if(!extensionRegistered)
	    try {
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.RELOCAL_ADDRESS);
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.EPG_TV_LIST_RESPONSE);
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.EPG_CHANNEL_RESPONSE);
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.TLS_URL_RESPONSE);
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.START_LAN_STATUS);
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.GET_LAN_RUNNING_STATE);
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.CLOSE_LAN_SERVICE);
	   
		
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.IGRS_USER_INFO_REQUEST);
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.JINGLE_RECEIVED_FILE);
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag. GET_EPG_CHANNEL_VERSION_REPLY);
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.GET_EPG_CHANNEL_TOTALS_REPLY);

		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.SEND_VEDIO_REROUCE);
		
		/**
		 * ��ǰ�豸��������
		 */
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.CURRENT_CONNECT_DEVICES_RESPONSE);
		
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.SEND_COMMAND_CONTROL_RESP);
		
		/**
		 * ���ص�ַ����ע��
		 */
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.LAN_RESOURCE_TRANSFER);
		
		/**
		 * �������ص�ַ
		 */
		igrsBasePService.registerIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY, IgrsTag.EPG_TV_PROGRAM_RESPONSE);

	    
	    } catch (RemoteException e) {
		e.printStackTrace();
	    }
	}
	/**
	* ע��ص��ӿ�
	*
	 */
    private IgrsBaseIgrsQueryCallback igrsBaseIgrsQueryCallback = new IgrsBaseIgrsQueryCallback.Stub(){

        public void processIgrsQuery(IgrsBaseQuery igrsBaseQuery) throws RemoteException{
          //  Handler handler=proxyResultConnectHandlers.get(IgrsTag.RELOCAL_ADDRESS);
          
            /** The igrs basic protocol don't filter the igrs error messages clearly and just send an error back
           *  without the error message itself **/
          if(igrsBaseQuery.type == IgrsBaseQuery.TYPE_ERROR){
              Log.e(TAG, "From: " + igrsBaseQuery.from);
              Log.e(TAG, "PacketID: " + igrsBaseQuery.packetID);
              Log.e(TAG, "Payload: " + igrsBaseQuery.payload);
              return;
          }
          
          /** Convert a igrsBase  to an IgrsBaseQuery because we work with the  IgrsBaseQuery format.
           *  This should be changed but at the moment it was the only known way
           *  to implement this kind of handling association handler**/
          IQImpl impl = new IQImpl();
          impl.fromXMPPIQ(igrsBaseQuery);

          XmlPullParserFactory factory;
          XmlPullParser xpp = null;
          try {
              
              factory = XmlPullParserFactory.newInstance();
              factory.setNamespaceAware(true);
              xpp = factory.newPullParser(); 
              xpp.setInput( new StringReader ( impl.toXML() ) );
              
           } catch (XmlPullParserException e) {
              e.printStackTrace();
           }
           
           if(igrsBaseQuery.namespace.equals(IgrsTag.RENAME))
           {
                  /*******************************************************
                   *      ����Ļص����
                   * 
                   * ****************************************************/ 
                RenameDevicesBean renameDevicesBean=new RenameDevicesBean();
                try {
                    renameDevicesBean.fromXML(xpp);
                   } catch (Exception e) {
                      e.printStackTrace();
                  }
           }else 
               //�򿪾�����ص�
              if(igrsBaseQuery.namespace.equals(IgrsTag.START_LAN_STATUS)) {
                  /*******************************************************
                   *    ����������Ļص����
                   * 
                   * ****************************************************/ 
                 final LanLoadingResultBean lanLoadingResultBean=new LanLoadingResultBean();
                   try {
                       lanLoadingResultBean.fromXML(xpp);
                   } catch (Exception e) {
                      e.printStackTrace();
                  }
                   /**
                    * ������״̬
                    */
                  lanLoadingResultBean.isLanSerivceRunning();
           }else
               //�رվ�����״̬
               if(igrsBaseQuery.namespace.equals(IgrsTag.CLOSE_LAN_SERVICE))
           {
               CloseLanServiceBean closeLanServiceBean=new CloseLanServiceBean();
               try {
                   closeLanServiceBean.fromXML(xpp);
               } catch (Exception e) {
                  e.printStackTrace();
              }
               closeLanServiceBean.isLanSerivceClosing();
           }
           
           
           else if(igrsBaseQuery.namespace.equals(IgrsTag.IGRS_USER_INFO_REQUEST))
           {
               Handler  handler=proxyResultConnectHandlers.get(IgrsTag.IGRS_USER_INFO_REQUEST);
               
               CurrentConnectUserInfoBean regUserBean=new CurrentConnectUserInfoBean();
               try {
                  regUserBean.fromXML(xpp);
              } catch (Exception e) {
                  e.printStackTrace();
              }
               Message message=new Message();
               message.obj=regUserBean.getToken();
               handler.sendMessage(message);
           }else if(igrsBaseQuery.namespace.equals(IgrsTag.JINGLE_RECEIVED_FILE))
           {
               JingleAccpetFileBean jingleAccpetFileBean=new JingleAccpetFileBean();
               try {
                  jingleAccpetFileBean.fromXML(xpp);
              } catch (Exception e) {
                  e.printStackTrace();
              }
               Handler  handler=proxyResultConnectHandlers.get(IgrsTag.JINGLE_RECEIVED_FILE);
               Message message=new Message();
               message.what=GlobalControl.File_JINGLE_RECEIVE;
               message.obj=jingleAccpetFileBean;
               handler.sendMessage(message);
              
           } else if (igrsBaseQuery.namespace.equals(IgrsTag.GET_EPG_CHANNEL_TOTALS_REPLY))
           {
               EpgTotalPagesBean epgTotalPagesBean=new EpgTotalPagesBean();
               try {
                   epgTotalPagesBean.fromXML(xpp);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
               Log.e(TAG, " version :" + epgTotalPagesBean.payloadToXML());
           } else if (igrsBaseQuery.namespace.equals(IgrsTag. GET_EPG_CHANNEL_VERSION_REPLY))
           {
               
                  EPGVersionBean versionBean=new EPGVersionBean();
                   try {
                       versionBean.fromXML(xpp);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
               Log.e(TAG, " version :" + versionBean.payloadToXML());
           } else if (igrsBaseQuery.namespace.equals(IgrsTag.CURRENT_CONNECT_DEVICES_RESPONSE))
           {
                ConnectHostDevicesBean connectHostDevicesBean = new ConnectHostDevicesBean();
                 try {
                   connectHostDevicesBean.fromXML(xpp);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                
              List<String>  list= connectHostDevicesBean.deviceList;
              
              boolean isadd = connectHostDevicesBean.isAddOrRemove;
              String deivesName = connectHostDevicesBean.prviousServiceName;
              Handler  handler = proxyResultConnectHandlers.get(connectHostDevicesBean.getNamespace());
               
                if (handler != null) {
                   
                    Message message = new Message();
                    message.obj = connectHostDevicesBean;
                    handler.sendMessage(message);
                }
           } else if (igrsBaseQuery.namespace.equals(IgrsTag.SEND_COMMAND_CONTROL_RESP))
           {
               
               ReferenceCmdInfoBean  referenceCmdInfoBean = new ReferenceCmdInfoBean();
               referenceCmdInfoBean.setId(igrsBaseQuery.packetID);
               referenceCmdInfoBean.setTo(igrsBaseQuery.to);
               referenceCmdInfoBean.setFrom(igrsBaseQuery.from);
               try {
                   referenceCmdInfoBean.fromXML(xpp);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
               Handler  handler = proxyResultConnectHandlers.get(referenceCmdInfoBean.getNamespace());
               
                if (handler != null) {
                   
                    Message message = new Message();
                    message.what = GlobalControl.SEND_CONTROL_CMD_RCV;
                    message.obj = referenceCmdInfoBean;
                    handler.sendMessage(message);
                }
           } else if (igrsBaseQuery.namespace.equals(IgrsTag.LAN_RESOURCE_TRANSFER))
           {
              
                 LocalResourceTransformBean localResourceTransformBean = new LocalResourceTransformBean();
                 try {
                     localResourceTransformBean.fromXML(xpp);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                   Handler  handler = proxyResultConnectHandlers.get(localResourceTransformBean.getNamespace());
                   
                    if (handler != null) {
                       
                        Message message = new Message();
                        message.what = GlobalControl.LOCAL_ADDRESS_BACK_RCV;
                        message.obj = localResourceTransformBean;
                        handler.sendMessage(message);
              }
           } else if (igrsBaseQuery.namespace.equals(IgrsTag.EPG_TV_PROGRAM_RESPONSE)) {
               
               EPGTvListSearchBean epgListSearchBean = new EPGTvListSearchBean();
                 try {
                     epgListSearchBean.fromXML(xpp);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                   Handler  handler = proxyResultConnectHandlers.get(epgListSearchBean.getNamespace());
                   
                    if (handler != null) {
                       
                        Message message = new Message();
                        message.what = GlobalControl.LOCAL_EPG_SERACH_BACK_RCV;
                        message.obj = epgListSearchBean;
                        handler.sendMessage(message);
                      
              }       
           }
        }
	 
     };
	
	/**
	 * bind ��Э��ջ
	 * @param ctx the context to bind on
	 * @param serviConnecthandler
	 * 
	 * @throws RemoteException the remote exception
	 */
	public void connectToIgrsBaseService(Context ctx,Handler serviConnecthandler) throws RemoteException{
		if(igrsBasePService == null){
			independecServiceController = IndependecServiceController.get();
			this.serviConnecthandler=serviConnecthandler;
			independecServiceController.connectService(ctx, this);
		}
		else if(!igrsBasePService.isConnected()){
			onServiceConnected();
		}
	}
	/**
	 * ������Э��ջ��bind
	 * @param ctx
	 */
	public void unBindOndestry(Context ctx)
	{
		if (independecServiceController != null &&igrsBasePService!=null) {
		    try {
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.RELOCAL_ADDRESS);
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.EPG_TV_LIST_RESPONSE);
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.EPG_CHANNEL_RESPONSE);
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.TLS_URL_RESPONSE);
				
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.START_LAN_STATUS);
				
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.GET_LAN_RUNNING_STATE);
			   
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.IGRS_USER_INFO_REQUEST);
				
				igrsBasePService.unregisterIgrsBaseQueryCallback(igrsBaseIgrsQueryCallback,IgrsTag.QUERY,IgrsTag.JINGLE_RECEIVED_FILE);
				
		    } catch (RemoteException e) {
				e.printStackTrace();
			}
		    independecServiceController.unbindService(ctx);
		    igrsBasePService = null;
		}
	}
	
	/**
	 * ��Э��ջbind �ɹ���Ļص�
	 */
	public void onServiceConnected() {
		Log.i(TAG,"Connection to igrsBase Remote proxy Service established.");
		igrsBasePService = independecServiceController.getIgrsBaseService();
		registerHisenseExtensions();
		Message msg=new Message();
		if(igrsBasePService!=null)
		{
		   msg.arg1=1;
		   msg.arg2=20;
		}else
		{
		  msg.arg1=1;
		  msg.arg2=10;
		}
		serviConnecthandler.sendMessage(msg);
	}
	
	/**
	 * Called when the igrsBase stub Service gets unbound.
	 */
	public void  onServiceDisconnected() {
		Log.e(TAG,"Connection to igrsBase Remote  Proxy Service broken.");
	}
	
	/**
	 * 
	 *��ȡ��Э��ջ�ľ�����������
	 * @return IgrsBaseExporterLanService
	 * @throws RemoteException the remote exception
	 */
	public IgrsBaseExporterLanService  getLanNetWorkService()throws RemoteException{
		return igrsBasePService.getLanService();
	}
	
	/**
	 * ��ȡ��Э������
	 * @return IProviderExporterService
	 * @throws RemoteException
	 */
	public IProviderExporterService getConnectService() {
		return igrsBasePService;
	}
	/**
	 * Send igrsBaseQuery  to theigrsBase stub  Service
	 * @param igrsBasicBean the IgrsBaseQuery 
	 */
	private  void  sendIgrsBaseQuery (IgrsBaseQuery  igrsBasicBean){
		if(igrsBasicBean!= null){
			try {
				igrsBasePService.sendIgrsBaseQuery(sendIgrsBaseQueryAckMessenger, sendIgrsBaseQueryResMessenger, 0, igrsBasicBean);
			} catch (RemoteException e1) {
				Log.e(TAG, "ERROR while sending IgrsBaseQuery : " + igrsBasicBean.payload);
			}
		}
	}
	/**
	 * Send igrsBaseQuery  to theigrsBase Remote Proxy Service.
	 * 
	 * @param igrsBasicBean the IgrsBaseQuery 
	 */
	public void  sendQueryBaseBean (IgrsBaseBean igrsBasicBean,Handler responseHandler,String callKey){
		if(igrsBasicBean!= null){
			IgrsBaseQuery igrsBaseQuery=new IgrsBaseQuery();
			igrsBaseQuery.namespace=igrsBasicBean.getNamespace();
			igrsBaseQuery.element=igrsBasicBean.getChildElement();
			igrsBaseQuery.payload=igrsBasicBean.payloadToXML();
			igrsBaseQuery.to= igrsBasicBean.getTo();
			igrsBaseQuery.from= igrsBasicBean.getFrom();
			igrsBaseQuery.packetID=igrsBasicBean.getId();
			igrsBaseQuery.type=igrsBasicBean.getType();
			igrsBaseQuery.token="lancmd";
			sendIgrsBaseQuery(igrsBaseQuery);
			if(callKey!=null)
				proxyResultConnectHandlers.put(callKey, responseHandler);
		}
	}
	
	
	
	/**
	 * Send igrsBaseQuery  to theigrsBase Remote Proxy Service.
	 * 
	 * @param igrsBasicBean the IgrsBaseQuery 
	 */
	public void  sendQueryBaseBeanByCallBackHandler (IgrsBaseBean igrsBasicBean, Messenger callBackMessenger){
		if(igrsBasicBean!= null){
			IgrsBaseQuery igrsBaseQuery=new IgrsBaseQuery();
			igrsBaseQuery.namespace=igrsBasicBean.getNamespace();
			igrsBaseQuery.element=igrsBasicBean.getChildElement();
			igrsBaseQuery.payload=igrsBasicBean.payloadToXML();
			igrsBaseQuery.to= igrsBasicBean.getTo();
			igrsBaseQuery.from= igrsBasicBean.getFrom();
			igrsBaseQuery.packetID=igrsBasicBean.getId();
			igrsBaseQuery.type=igrsBasicBean.getType();
			igrsBaseQuery.token="lancmd";
			try {
				igrsBasePService.sendIgrsBaseQuery(callBackMessenger, sendIgrsBaseQueryResMessenger,0,igrsBaseQuery);
			} catch (RemoteException e1) {
				Log.e(TAG, "ERROR while sending IgrsBaseQuery : " + igrsBaseQuery.payload);
			}
			
		}
	}
	
	/**
	 * Register igrsBase proxy result connect handler.
	 * 
	 * @param h the handler which should be registered
	 */
	public void registerIgrsProxyResultConnectHandler(String callBackKey,Handler handler){
		this.proxyResultConnectHandlers.put(callBackKey,  handler);
	}
	
	/**
	 * Unregister igrsBase proxy result connect handler.
	 * 
	 * @param key which should be unregistered
	 */
	public void unregisterIgrsProxyResultConnectHandler(String callBackKey){
		this.proxyResultConnectHandlers.remove(callBackKey);
	}
	
	/** The send IgrsBaseQuery ack handler. */
	private Handler sendIgrsBaseQueryAckHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {}
	};
	
	/** The send IgrsBaseQuery result handler. */
	private Handler sendIgrsBaseQueryResHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {}
	};
	
	/** The send IgrsBaseQuery ack messenger. */
	private Messenger sendIgrsBaseQueryAckMessenger = new Messenger(sendIgrsBaseQueryAckHandler);
	
	 /** The send IgrsBaseQuery res messenger. */
	private Messenger sendIgrsBaseQueryResMessenger = new Messenger(sendIgrsBaseQueryResHandler);
    
    
}