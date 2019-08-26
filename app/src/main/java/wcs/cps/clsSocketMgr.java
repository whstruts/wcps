package wcs.cps;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class clsSocketMgr
{
	private Socket client;
	private int m_iRunSocketThreadState=1;
	public static String m_sktSrvIp="";
	public static int m_sktSrvPort=0;
	public String m_sktClnType="CPS";
	public static String m_sktDataEndChar="@^@";  //Sokect 通信字符串终止符号
	public static ArrayList<String> m_SendList=null;
	public static ArrayList<String> m_ReceList=null;
	public static ArrayList<String> m_PickDataList=null;
	public static Handler m_CurHandler; 
	public static int m_iCntState=0; 
	public static String m_SktErrData="";
	public static int m_iCheckCnnState=0;
	public static int m_iCurDoPickCount=500;
	
	public static Handler hd=new Handler()//声明消息处理器
	{
	
			@Override
			public void handleMessage(Message msg)
			{
				String sMess="";
				switch(msg.what)
				{
				case 99:
        			sMess=(String)msg.obj;
        			mDoSendData(0,sMess);
        			break;
				case 98:
        			sMess=(String)msg.obj;
        			mDoSendData(2,sMess);
        			break;
				case 97:
        			sMess=(String)msg.obj;
        			mDoSendData(3,sMess);
        			break;
        		default:
        			break;	
        		}
        	}
	};

	public static synchronized String mDoSendData(int _type,String _Data)
	{
		try
		{
			String sRlt="";
			
			if (_type==0)
			{
				String sMess=_Data;
				sMess+=m_sktDataEndChar;
				m_SendList.add(sMess);
			}
			else if(_type==1)
			{
				while(m_SendList.size()>0)
				{
					sRlt=m_SendList.get(0);
					m_SendList.remove(0);				
					break;
				}		
			}
			else if (_type==2)
			{
				m_PickDataList.clear();
			}
			else if (_type==3)
			{
				String sMess=_Data;
				sMess+=m_sktDataEndChar;
				m_PickDataList.add(sMess);
				if(m_PickDataList.size()==1)
				{
					m_iCurDoPickCount=500; //如果队列只有一个，马上确认
				}
			}
			else if(_type==4)
			{
				while(m_PickDataList.size()>0)
				{
					sRlt=m_PickDataList.get(0);			
					break;
				}		
			}
			else if (_type==5)
			{
				if (m_PickDataList.size()>0)
				{
					String _sfpd=clsMyPublic.GetStrInOfStr(_Data, 2);
					String _sHangh=clsMyPublic.GetStrInOfStr(_Data, 3);
					sRlt=m_PickDataList.get(0);	
					String _sCurFpd=clsMyPublic.GetStrInOfStr(sRlt, 2);
					String _sCurHangh=clsMyPublic.GetStrInOfStr(sRlt, 3);
					if (_sfpd.compareTo(_sCurFpd)==0 &&
						_sHangh.compareTo(_sCurHangh)==0)
						m_PickDataList.remove(0);
				}
			}
			return sRlt;
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog="clsSocketMgr;mDoSendData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
	        return "";
		}
	}
	
	public static synchronized String mDoReceData(int _type,String _Data)
	{
		try
		{
			String sRlt="";
			if (_type==0)
			{
				m_ReceList.add(_Data);
			}
			else if(_type==1)
			{
				while(m_ReceList.size()>0)
				{
					sRlt=m_ReceList.get(0);
					m_ReceList.remove(0);				
					break;
				}		
			}
			return sRlt;
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog="clsSocketMgr;mDoReceData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
	        return "";
		}
	}
	
	public clsSocketMgr()
	{
		m_SendList=new ArrayList<String>();
	    m_ReceList=new ArrayList<String>();
	    m_PickDataList=new ArrayList<String>();
	}  
	
	public static  String GetLocalIPAddress() 
	{
		String sRet="";						
		try
		{
			Enumeration<?> allNetInterfaces;
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements())
			{
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements())
				{
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address && ip.isLoopbackAddress()==false)
					{
						sRet=ip.getHostAddress();
						return sRet;
					} 
				}
			}
		
		} 
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return sRet;
		
	}
	public void mStart()
	{
		myCnntThread myCnntThread=new myCnntThread();
		myCnntThread.start();
		mySendThread mySendThread=new mySendThread();
		mySendThread.start();
		myReceThread myReceThread=new myReceThread();
		myReceThread.start();
		mDoDataThread myDoDataThread=new mDoDataThread();
		myDoDataThread.start();
	}
	
	public int SocketClient(String site, int port)
	{
		int iRet=0;
		try
		{	
			client =null;
            client = new Socket();            
            SocketAddress socketAddress = new InetSocketAddress(site, port);
            client.connect(socketAddress,3000);
            return 1;
	    }
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SckLog",clsMyPublic.g_WorkName,"创建连接失败;"+e.toString());
	    }
		catch (IOException e)
		{
	        e.printStackTrace();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SckLog",clsMyPublic.g_WorkName,"创建连接失败;"+e.toString());
	    }
		return iRet;
    }
	public int SetTimeOut()
	{
		try
        {
			client.setSoTimeout(10000);  //设置延时
			return 1;
        }
		catch (IOException e)
		{
	        e.printStackTrace();
	        return -1;
	    }
		
	}
    public int sendMsg(String msg)
    {
        int iRet=-1;
    	try
    	{
    		if (m_iCntState==0)
    			iRet=-1;
    		
    		PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"GBK")),true);
            out.println(msg);           
            iRet=1;
            
            return iRet;
		}
    	catch(UnknownHostException e)
    	{
    		m_iCntState=0;
    		e.printStackTrace();
    		LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SndLog",clsMyPublic.g_WorkName,e.toString());
    		iRet=-1;
    		return iRet;
    	}
    	catch(IOException e)
    	{
    		m_iCntState=0;
    		e.printStackTrace();
    		LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SndLog",clsMyPublic.g_WorkName,e.toString());
    		iRet=-1;
    		return iRet;
		}
		
    }
    public String receMsg()
    {
    	String sMess="";
    	try
    	{
            //接受服务器的信息    		
            BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream(),"GBK"));
            String mstr="";
            char[] c=new char[10240];
            br.read(c);
            mstr=new String(c);;
            return mstr;
		}
    	catch(UnknownHostException e)
    	{
    		m_iCntState=0;
    		e.printStackTrace();
    		sMess=e.toString();
    		LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SktErr",clsMyPublic.g_WorkName,sMess);
			if (m_SktErrData.indexOf(m_sktDataEndChar)>0)
			{
				LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"DerLog",clsMyPublic.g_WorkName,m_SktErrData+" 发送失败;");
				mDoReceData(0,m_SktErrData);
				m_SktErrData="";
			}
    	}
    	catch(IOException e)
    	{
    		m_iCntState=0;
    		e.printStackTrace();
    		sMess=e.toString();
    		LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SckLog",clsMyPublic.g_WorkName,sMess);
			if (m_SktErrData.indexOf(m_sktDataEndChar)>0)
			{
				LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"DerLog",clsMyPublic.g_WorkName,m_SktErrData+" 发送失败;");
				mDoReceData(0,m_SktErrData);
				m_SktErrData="";
			}
		}
		return "";
    }
    
    public class myCnntThread extends Thread
    {
    	public void run()
    	{

			String sLog="";
    		while(m_iRunSocketThreadState==1)
    		{
    			try
    			{
    				int iRet=0;
	    			switch(m_iCntState)
	    			{
	    			case 0: //待连接到服务端
	    				sLog="正在连接服务端,服务端IP地址:"+ m_sktSrvIp +";端口号:" + String.valueOf(m_sktSrvPort) +";请稍等....";
	    				clsMyPublic.gSendDisMess(sLog);
	    				LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"CntLog",clsMyPublic.g_WorkName,sLog);
	    				
	    				iRet=SocketClient(m_sktSrvIp, m_sktSrvPort);
	    				if (iRet==1)
	    				{
	    					iRet=sendMsg(m_sktClnType);
	    					if (iRet==1)
	    					{
	    						String _ReceData="";
	    						_ReceData=receMsg();
	    						_ReceData=_ReceData.trim();
	    						if (_ReceData.compareTo("OK")==0)
	    						{
	    							m_iCntState=1;//连接成功    
	    							SetTimeOut();
	    							//向服务端发送连线是否正常信号
	    							String _DoData="";
	    							_DoData=clsMyPublic.g_sktParamCnntState +";0;";
	    							mDoSendData(0,_DoData);
	    							sLog="连接服务端成功,服务端IP地址:"+ m_sktSrvIp +";端口号:" + String.valueOf(m_sktSrvPort) + ";接受:" + _ReceData;
	    							LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"CntLog",clsMyPublic.g_WorkName,sLog);
	    							clsMyPublic.gSendDisMess(sLog);
	    						}
	    						else
	    						{
	    							sLog="连接服务端握手失败，请联系系统管理员;"+ "接受:" + _ReceData;
	    							LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"CntLog",clsMyPublic.g_WorkName,sLog);
	    							clsMyPublic.gSendDisMess(sLog);	
	    						}  						
	    					}
	    				}
	    				SystemClock.sleep(100);
	    				break;    			
	    			default:
	    				SystemClock.sleep(50);
	    			}	
    			}
		        catch (Exception e) 
		        {
		        	String sErrLog="";
					sErrLog="clsSocketMgr;myCnntThread;" + e.toString();
			        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
		        	return ;
		        }
				catch(UnknownError e)
				{
		        	String sErrLog="";
					sErrLog="clsSocketMgr;myCnntThread;" + e.toString();
			        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
					return ;
				}
    		}
    	}	
	}
      
    public class myReceThread extends Thread
    {
    	public void run()
    	{
        	String sLog="";
    		while(m_iRunSocketThreadState==1)
    		{ 
    			try
    			{
    				if (m_iCntState==0)
	    			{
	    				if (m_SktErrData.length()>0 )
	    				{
	    					if (m_SktErrData.indexOf(m_sktDataEndChar)>0)
	        				{
	    						LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"DerLog",clsMyPublic.g_WorkName,m_SktErrData+" 发送失败;");
	    						mDoReceData(0,m_SktErrData);
	            				m_SktErrData="";
	        				}
	        			}
	    				//如果服务端连接不正常，则不接受服务端的数据
	    				SystemClock.sleep(5);   
	    				continue;
	    			}    				
					String _ReceData="";
					String sEndReceData="";
					_ReceData=receMsg().toString();
					_ReceData=_ReceData.trim();
					if (_ReceData.length()>0)
					{
						sEndReceData=sEndReceData+_ReceData;
						while(sEndReceData.indexOf(m_sktDataEndChar)>0)
						{
							int iPos=sEndReceData.indexOf(m_sktDataEndChar);
							String _DoData=sEndReceData.substring(0,iPos);
							String sBef="";
							sBef=_DoData.substring(0,7);
							if (sBef.compareTo(clsMyPublic.g_sktParamCnntState)==0)
							{
								m_iCheckCnnState=1;
							}
							else if (sBef.compareTo(clsMyPublic.g_SktParamQRPick)==0)
							{
								//后台已经确认拣货任务
								String sRet1=clsMyPublic.GetStrInOfStr(_DoData, 1);
								if (sRet1.compareTo("1")==0)
								{
									mDoSendData(5,_DoData);
									m_iCurDoPickCount=500;  //紧接着确认下一个任务
								}
							}
							else
							{
								mDoReceData(0,_DoData);	
								sLog="S->C:接受数据;"+ _DoData;
								clsMyPublic.gSendDisMess(sLog);
							}
							sEndReceData=sEndReceData.substring(iPos+3);												
						}
					}
					SystemClock.sleep(2);  
    			}
    	        catch (Exception e) 
    	        {
    	        	m_iCntState=0;
    	        	String sErrLog="";
    				sErrLog="clsSocketMgr;myReceThread;" + e.toString();
    		        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
    	        	return ;
    	        }
    			catch(UnknownError e)
    			{
    				m_iCntState=0;
    	        	String sErrLog="";
    				sErrLog="clsSocketMgr;myReceThread;" + e.toString();
    		        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
    				return ;
    			}
    		}
    	}
	}
        
    public class mySendThread extends Thread
    {
    	public void run()
    	{
    	    
    	    String sLog="";
    	    int _iCountCheckState=0;
    	    int _iCountChang=0;
    		while(m_iRunSocketThreadState==1)
    		{
    			try
    			{
    				
	    			String _sendData="";
	    			int iRet=0;	    			
	    			//修改拣货面的注意差错的颜色
	    			if (_iCountChang>=100)
	    			{
	    				_iCountChang=0;
	    				String _sChngMsg="";
	    				_sChngMsg=clsMyPublic.g_sktParamCountChg+";-1;";
	    				_sChngMsg+=m_sktDataEndChar;
	    				mDoReceData(0,_sChngMsg);
	    			}
	    			_iCountChang++;
	    			
	    			if (m_iCntState==0)
		    		{	    					    			
		    			SystemClock.sleep(10);
						continue;
		    		}
	    			
	    			
	    			if (m_iCheckCnnState==1)
					{
	    				_iCountCheckState=0;
	    				m_iCheckCnnState=2;
	    				
					}
	    			else if (m_iCheckCnnState==2)    			
	    			{
	    				_iCountCheckState++;
	    				if (_iCountCheckState>=200)
	    				{
	    					String _DoData=clsMyPublic.g_sktParamCnntState+";0;";
	    					mDoSendData(0,_DoData);
	    					m_iCheckCnnState=0;
	    				}
	    				
	    			}    			
					_sendData=mDoSendData(1,"");
					if (_sendData.length()<=0)
					{
						//是否有拣货任务的数据
						if (m_iCurDoPickCount>=500)
						{
							_sendData=mDoSendData(4,"");
							m_iCurDoPickCount=0;
						}
							
					}
					m_iCurDoPickCount++;
					
					if (_sendData.length()>0)
					{	
						String sBef=_sendData.substring(0,7);
						if (sBef.compareTo(clsMyPublic.g_sktParamCnntState)!=0)
						{
							m_SktErrData=_sendData.substring(0,7);
							m_SktErrData+=";-1;发送失败,请重新尝试....;";
							m_SktErrData+=m_sktDataEndChar;
						}
						
			    		if (m_iCntState==0)
			    		{	    					    			
			    			SystemClock.sleep(10);
							continue;
			    		}
						
						iRet=sendMsg(_sendData);
						if (iRet>0)
						{
							if (sBef.compareTo(clsMyPublic.g_sktParamCnntState)!=0)
							{
								sLog="C->S;发送数据;"+ _sendData + ";返回 " + String.valueOf(iRet) +";"+ String.valueOf(m_iCntState) +";";
								clsMyPublic.gSendDisMess(sLog);	
							}						
						}
						else 
						{
							LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"SndLog",clsMyPublic.g_WorkName,_sendData+" 发送失败;");						
							mDoReceData(0,m_SktErrData);
							clsMyPublic.gSendDisMess(m_SktErrData);
							m_SktErrData="";
						}
					}
					SystemClock.sleep(10);
    			}
    	        catch (Exception e) 
    	        {
    	        	m_iCntState=0;
    	        	String sErrLog="";
    				sErrLog="clsSocketMgr;mySendThread;" + e.toString();
    		        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
    	        	return ;
    	        }
    			catch(UnknownError e)
    			{
    				m_iCntState=0;
    	        	String sErrLog="";
    				sErrLog="clsSocketMgr;mySendThread;" + e.toString();
    		        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"clsSocketMgr",clsMyPublic.g_WorkName,sErrLog);
    				return ;
    			}
    		}
    	}
	
	}
    
    public class mDoDataThread extends Thread
    {
    	public void run()
    	{
    		while(m_iRunSocketThreadState==1)
    		{
				String _sDoData=mDoReceData(1,"");
				if (_sDoData.length()>0 && m_CurHandler!=null)
					m_CurHandler.obtainMessage(clsMyPublic.MsTypeReceMess, 100,-1, _sDoData).sendToTarget();
				SystemClock.sleep(10);
    		}
    	}	
	}
    
    public void closeSocket()
    {
         try
         {
             client.close();
         }
         catch(IOException e)
         {
             e.printStackTrace();
         }
    }
}
