package wcs.cps;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Handler;
import android.os.SystemClock;




public  class clsSocketCln
{
	private static Socket client;
	
	public static String m_SvrIp="10.46.8.229";
	public static int m_SvrPort=9045;
	
	
	public static  String g_WorkNo="";   //Ա�����
	public static  String g_WorkName="North"; //Ա������
	public static  String g_WorkPass="";  //Ա������
	public static  String g_ZcdNo="";
	public static  String g_GsNo="";
	
	public static Handler m_CurHandler; 
	public static String m_sktDataEndChar="@^@";  //Sokect ͨ���ַ�����ֹ����
	public static String m_sKeys="";
	
	
	public static final int MsTypeDisMess=0;  //��ʾ��ʾ��Ϣ
	public static final int MsTypeSendMess=1;  //������Ϣ
	public static final int MsTypeReceMess=2;  //������Ϣ
	public static final int LyTypeReceMess=3;  //������Ϣ
	
	
	public static String m_SendData="";
	public static String m_Welcomestr="��ܰ��ʾ:��ӭʹ������׷�������ն�...";


	public static Integer m_iStartThreadStatus=0;
	public static Integer m_iConnetTime=0;
	public static String  m_sktClnType="CPS";
	
	public static final String  g_SktParamCts="WCTS";
	public static final String  g_SktParamLogin="CTS0001";  //��½
	public static final String  g_SktParamExit="CTS0002";   //�˳�
	public static final String  g_SktParamMain = "CTS0003";   //�������������
	public static final String  g_SktParamBoxStatus = "CTS0004";   //������¶Ȼ�������
	public static final String  g_SktParamPsYwdj = "CTS0005";   //������¶Ȼ�������
	public static final String  g_SktParamPsDjMx = "CTS0006";   //�����ҵ�񵥾���ϸ  
	public static final String  g_SktParamPsWdMx = "CTS0007";   //�������Ʒ�¶���ϸ  
	public static final String  g_SktParamPsZJiao = "CTS0008";  //�������Ʒ׼������ 
	public static final String  g_SktParamPsAct = "CTS0009";  //װ����׷��ȫ������ 
	public static final String  g_SktParamCsRece = "CTS0010";   //װ�����ͻ�ǩ��
	public static final String  g_SktParamPsFinish = "CTS0011";   //װ����׷�����
	public static final String  g_SktParamPintHzData = "CTS0012";  //��ӡͷ
	
	
	
	public clsSocketCln()
	{
	
	}
	
	public void mStart()
	{		
		CnntThread myCnntThread=new CnntThread();
		myCnntThread.start();
		m_iStartThreadStatus=1;
		m_iConnetTime=0;
	}
	
	public  int mConnectSvr()
	{
		String sMess="";
		String sMainReceData="";
		String[] _sResult=null;
		String[] _sSendData=m_SendData.split(";");
		try
		{	
			sMess=_sSendData[0]+";-2;���ڳ�������Զ�̷����...;";
			
			if (m_CurHandler!=null)
				m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
			
			client =null;			
            client = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(m_SvrIp, m_SvrPort);
            client.connect(socketAddress,3000);
            
            int iRet=-1;
            client.setSoTimeout(10000);

            //��������;
            m_SendData=m_SendData+";"+m_sktDataEndChar;
            iRet=sendMsg(m_SendData);
            if (iRet==1)
            {
            	sMess=_sSendData[0]+";-2;��������ɹ������ڵȴ���Ӧ...;";
            	if (m_CurHandler!=null)
            		m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
            }
            else
            {
            	sMess=_sSendData[0]+";-1;��������ʧ�ܣ������³���...;";
            	if (m_CurHandler!=null)
            		m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
            	m_iStartThreadStatus=0;
            	return -1;
            	
            }
            
            //��������
            int iMaxRowCount=0;
            int iCurRowCount=0;
            ArrayList<String> m_ReceList=null;
            m_ReceList=new ArrayList<String>();
            while(1==1)
            {
            	String _sReceData="";
            	_sReceData=receMsg(); 
            	if (_sReceData.length()==0)
            	{
            		sMess=_sSendData[0]+";-1;���ܳ�ʱ�������³���...;";
            		if (m_CurHandler!=null)
            			m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
                	m_iStartThreadStatus=0;
                	return -1;
            	}
            	_sReceData=_sReceData.trim();
            	sMainReceData+=_sReceData;
            	while(sMainReceData.indexOf(m_sktDataEndChar)>0)
            	{
            		
            		int iPos=sMainReceData.indexOf(m_sktDataEndChar);
					String _DoData=sMainReceData.substring(0,iPos);
					_sResult=_DoData.split(";");
					if (_sResult[1].compareTo("-1")==0)
					{
						if (m_CurHandler!=null)
							m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, _DoData).sendToTarget();
						return -1;
					}
					sMainReceData=sMainReceData.substring(iPos+3);
					m_ReceList.add(_DoData);					
	            	iCurRowCount++;
	            	iMaxRowCount=Integer.parseInt(_sResult[1]);
	            	if(iMaxRowCount==0)
	            	{
	            		iMaxRowCount=1;
	            	}
	            	SystemClock.sleep(10);	            	
            	}

            	if (iCurRowCount==iMaxRowCount)
            	{
            		m_iStartThreadStatus=0;
            		break;
            	}
            }
            
            if (m_ReceList.size()==0)
            {
        		sMess=_sSendData[0]+";-1;������Ӧʧ�ܣ������³���...;";
        		if (m_CurHandler!=null)
        			m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
            	m_iStartThreadStatus=0;
            	return -1;

            }
            
            while(m_ReceList.size()>0)
            {
            	String _sData="";
            	_sData=m_ReceList.get(0);
				m_ReceList.remove(0);					
				if (m_CurHandler!=null)
					m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, _sData).sendToTarget();				
            	SystemClock.sleep(10);
            	
            }            
            return 1;
	    }
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			sMess=_sSendData[0]+";-1;"+e.toString()+";";
			if (m_CurHandler!=null)
				m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
			return -1;
	    }
		catch (IOException e)
		{
	        e.printStackTrace();
	        sMess=_sSendData[0]+";-1;"+e.toString()+";";
	        if (m_CurHandler!=null)
	        	m_CurHandler.obtainMessage(clsSocketCln.MsTypeReceMess, 100,-1, sMess).sendToTarget();
	        return -1;
	    }
	}
	
	public int sendMsg(String msg)
    {
        int iRet=-1;
    	try
    	{
    		PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"GBK")),true);
            out.println(msg);           
            iRet=1;
		}
    	catch(UnknownHostException e)
    	{
    		e.printStackTrace();
    		
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
		}

		return iRet;
    }
	
	public String receMsg()
    {
    	String sMess="";
    	try
    	{
            //���ܷ���������Ϣ    
    		if (client==null) return "";
            BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream(),"GBK"));
            String mstr="";
            char[] c=new char[10240];
            br.read(c);
            mstr=new String(c);;
            return mstr;
		}
    	catch(UnknownHostException e)
    	{
    		e.printStackTrace();
    		sMess=e.toString();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		sMess=e.toString();

		}
		return "";
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
	
	
	public class CnntThread extends Thread
    {
    	public void run()
    	{
    		while(1==1)
    		{
    			mConnectSvr();
    			return;
    		}
    	}
    }
		
}
