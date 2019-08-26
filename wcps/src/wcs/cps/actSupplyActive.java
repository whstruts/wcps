package wcs.cps;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class actSupplyActive extends Activity
{
	private RadioButton[] rbline=null;  
	private RadioGroup radioGroup;  

	private Button bt_Return=null;
	private Button bt_Refresh=null;
	private Button bt_Dwcl=null;
	
	private ListView lv_mainTask=null;
	private TextView tv_MessTs=null;
	private TextView tv_Cur=null;
	private TextView txt_bhtm=null;
	
	private EditText edtInput=null;
	
	
	private String[][] m_receMainTask;
	private int m_iCurReceIndex=-1;
	private int m_iSumReceCount=-1;
	
	private String m_sBarCodeData="";
	private String m_sAreaNo="";
	
	private String m_sClsName="actSupplyActive";
	
	String[][]msg=null;
	
	public  Handler hd=new Handler()//������Ϣ������
	{
		public void handleMessage(Message msg)//��д����
		{			
			switch(msg.what)
			{
			case clsMyPublic.MsTypeDisMess:
				mDoDisMessage(msg);
				break;
			case clsMyPublic.MsTypeReceMess:
				mDoReturnData(msg);
				break;
		    case clsBluetoothMgr.STATE_SEND_DATA:
		    	mViewBlueData(msg);
		    	break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actsupplyactive);
		this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-���߼�ѡС��ϵͳ ��������ǩ��� ��" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"��");
		clsMyPublic.m_hdlSendMess=hd;
		clsBluetoothMgr.m_CurHandler=hd;
		mSetButtonIni();
		m_sAreaNo=clsMyPublic.g_WorkAreaNo;
	}
	private void mViewBlueData(Message msg)
	{
		byte[] readBuf = (byte[]) msg.obj;
		String readMessage = new String(readBuf, 0, msg.arg1);
		readMessage=readMessage.trim();
		m_sBarCodeData=readMessage;
		mSendData(2);
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mSetButtonIni()
	{
		rbline=new RadioButton[5];
		rbline[0]=(RadioButton)findViewById(R.id.radio0);
		rbline[1]=(RadioButton)findViewById(R.id.radio1);
		rbline[2]=(RadioButton)findViewById(R.id.radio2);
		rbline[3]=(RadioButton)findViewById(R.id.radio3);
		rbline[4]=(RadioButton)findViewById(R.id.radio4);
		
		radioGroup = (RadioGroup)findViewById(R.id.rdGrp);  
		radioGroup.setOnCheckedChangeListener(new RadioGroupListener()); 
		
		bt_Return=(Button)findViewById(R.id.bt_return);
		bt_Refresh=(Button)findViewById(R.id.bt_Refresh);
		bt_Dwcl=(Button)findViewById(R.id.bt_Dwcl);
		
		tv_MessTs=(TextView)findViewById(R.id.tv_ts);
		txt_bhtm=(TextView)findViewById(R.id.txt_bhtm);
		lv_mainTask=(ListView)findViewById(R.id.lst_bhData);
		
		
		edtInput=(EditText)findViewById(R.id.edtInput);
		edtInput.setInputType(InputType.TYPE_NULL);

		edtInput.setOnKeyListener
    	(
    		new View.OnKeyListener()
	    	{
	    		public boolean onKey(View v, int keyCode, KeyEvent event)
	    		{
	    			if (keyCode==KeyEvent.KEYCODE_ENTER)
	    			{	
	    				String s="";
	    				s=edtInput.getText().toString();
	    				s=s.trim();
	    				if (s.length()==0) 
	    				{
	    					edtInput.setText("");
	    					return true;
	    				}	  
	    				m_sBarCodeData=edtInput.getText().toString();
	    				edtInput.setText("");  
	    				mSendData(2);
	    			}
	    			return false;
	    		}
	    	}
    	);
		
		bt_Dwcl.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mCharKeys();
					return;
				}
		    }
        );
		
		//�����¼�
    	bt_Return.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					finish();
				}
		    }
        );

    	//ˢ�������¼�
    	bt_Refresh.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					m_receMainTask=null;
					mSendData(1);
				    v.setEnabled(false);
				    v.setBackgroundResource(R.drawable.clrunenabled);	
				}
		    }
        );
		
	}
	class RadioGroupListener implements RadioGroup.OnCheckedChangeListener
	{   
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{  
			if(checkedId == rbline[0].getId())
			{  
				m_sAreaNo="01";
			}
		    else if(checkedId == rbline[1].getId())
		    {  
		    	m_sAreaNo="02";
		    }  
		    else if(checkedId == rbline[2].getId())
		    {  
		    	m_sAreaNo="03";
		    }  
		    else if(checkedId == rbline[3].getId())
		    {  
		    	m_sAreaNo="04";
		    }  
		    else if(checkedId == rbline[4].getId())
		    {  
		    	m_sAreaNo="05";
		    }  			
			
			m_receMainTask=null;
			lv_mainTask.removeAllViewsInLayout();

			mSendData(1);

			
			bt_Refresh.setEnabled(false);
			bt_Refresh.setBackgroundResource(R.drawable.clrunenabled);	
			
			rbline[0].setEnabled(false);
			rbline[1].setEnabled(false);
			rbline[2].setEnabled(false);
			rbline[3].setEnabled(false);
			rbline[4].setEnabled(false);
		}  
        
    }  
	
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
		if (_Type==1)
		{
			_sendData=clsMyPublic.g_sktParamBhActiv +";" +
					  m_sAreaNo +";";
		}
		else if (_Type==2)
		{			
			_sendData=clsMyPublic.g_sktParamBhDanwei +";" +
					  m_sBarCodeData +";";
		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	
	private void mFillMainTaskData(String[][] _sData)
	{
		msg=_sData;
        BaseAdapter ba_detail=new BaseAdapter()//�½�������
        {
			public int getCount() 
			{
				return msg[0].length;//�õ��б�ĳ���
			}
			public Object getItem(int arg0){return null;}
			public long getItemId(int arg0){return 0;}
			public View getView(int arg0, View arg1, ViewGroup arg2)//Ϊÿһ���������
			{
				LinearLayout ll_detail=new LinearLayout(actSupplyActive.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//���ó���	
				ll_detail.setPadding(5,5,5,5);//��������

				for(int i=0;i<msg.length;i++)//Ϊÿһ��������ʾ������
				{					    
					TextView s= new TextView(actSupplyActive.this);
					s.setText(msg[i][arg0]+" ");//TextView����ʾ������
					s.setTextSize(20);//�����С
					s.setTextColor(Color.RED);
					s.setPadding(1,2,2,1);//��������
					if (i==0)
						s.setWidth(400);//���
					else if (i==1)
						s.setWidth(300);
					else
						s.setWidth(300);
					
				    ll_detail.addView(s);//����LinearLayout
				}
				return ll_detail;//����LinearLayout����
			}        	
        };        
        lv_mainTask.setAdapter(ba_detail);
        
        
        
        lv_mainTask.setOnItemClickListener//Ϊ�б���Ӽ���
        (
           new OnItemClickListener()
           {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) //arg2Ϊ����ĵڼ���
			{
				m_sBarCodeData=msg[0][arg2];
				mShowMessageII("��ȷ��������Ʒ�Ѿ�������������ȷ��Ҫ������?");
			}        	   
           }
        );
	}
	

	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_sktParamBhActiv)==0)
			{			
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0||sRet1.compareTo("0")==0)
				{
					tv_MessTs.setText(sReceData);
					bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
					bt_Refresh.setEnabled(true);
					
					if (m_receMainTask==null)
					{

						m_receMainTask=new String[4][1];				
						for(int i=0;i<4;i++)
						{
							m_receMainTask[i]=new String[1];
						}
					}
					
					for(int i=0;i<4;i++)
					{
						m_receMainTask[i][0]="";
					}
						
					mFillMainTaskData(m_receMainTask);	
					
					rbline[0].setEnabled(true);
					rbline[1].setEnabled(true);
					rbline[2].setEnabled(true);
					rbline[3].setEnabled(true);
					rbline[4].setEnabled(true);
					
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(sReceData);
				}
				else
				{				
					tv_MessTs.setText(sReceData);
					if (m_receMainTask==null)
					{
						m_iSumReceCount=Integer.parseInt(sRet1);
						m_receMainTask=new String[4][m_iSumReceCount];				
						for(int i=0;i<4;i++)
						{
							m_receMainTask[i]=new String[m_iSumReceCount];
						}
						m_iCurReceIndex=-1;
					}
					m_iCurReceIndex++;
					for(int i=0;i<4;i++)
					{
						m_receMainTask[i][m_iCurReceIndex]=clsMyPublic.GetStrInOfStr(sReceData, i+2);
					}
						
					mFillMainTaskData(m_receMainTask);		
					
					if (m_iSumReceCount==(m_iCurReceIndex+1))
					{
						//��֤һ�β�ѯ���޺󣬲ſ�ʼ�´β�ѯ
						bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
						bt_Refresh.setEnabled(true);
						
						rbline[0].setEnabled(true);
						rbline[1].setEnabled(true);
						rbline[2].setEnabled(true);
						rbline[3].setEnabled(true);
						rbline[4].setEnabled(true);
					}
				}
				
			}
			else
			{
				tv_MessTs.setText(sReceData);
			}
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
	        mShowMessage(sErrLog);
	    }
		catch(UnknownError e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    switch (keyCode)
	    {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actSupplyActive.this)
	        .setTitle("��ܰ��ʾ")
	        .setIcon(android.R.drawable.alert_dark_frame)
	        .setMessage(_sMess)
	        .setNegativeButton("ȷ��", new DialogInterface.OnClickListener()
	 	    {
	 		   public void onClick(DialogInterface dialog, int which) 
	 		   {
	 			  
	 		   }
	 		 }).show();
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mShowMessage;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
	}
	
	public  void mShowMessageII(String _sMess)
	{
		new AlertDialog.Builder(actSupplyActive.this)
        .setTitle("��ܰ��ʾ")
        .setIcon(android.R.drawable.alert_dark_frame)
        .setMessage(_sMess+"\nȷ��Ҫ�˳���������?")
        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
		   {
			   public void onClick(DialogInterface dialog, int which)
			   {
				   
			   }
		   }
		   ).setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
 	    {
 		   public void onClick(DialogInterface dialog, int which) 
 		   {
 			  mSendData(2);
 		   }
 		 }).show();
	}
	
	private void mCharKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actcharkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setTitle("����").setView(loginLayout).create();
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=-500;
		lp.y=500;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
		tv_Cur=txt_bhtm;
		for (int i = 0; i < loginLayout.getChildCount(); i++)
		{  
			View v=loginLayout.getChildAt(i);
			if (v instanceof RelativeLayout)
			{
				RelativeLayout KeyLayout=(RelativeLayout)v;
				for(int k=0;k<KeyLayout.getChildCount();k++)
				{
					View x=KeyLayout.getChildAt(k);
					x.setOnClickListener
			    	(
		    			new OnClickListener()
		    		    {
		    				public void onClick(View v) 
		    				{
		    					CharSequence sTmp="";
		    					String sKey="";
		    					Button btn=(Button)v;
		    					sTmp=btn.getText();
		    					sKey=(String)sTmp;
		    					if (sKey.compareTo("�س�")==0)
		    					{
		    						m_sBarCodeData=clsMyPublic.m_sKeys;
		    						mSendData(2);
		    						adNumKey.cancel();
		    						return;
		    					}
		    					else if (sKey.compareTo("ɾ��")==0)
		    					{
		    						int iLen=-1;
		        					iLen=clsMyPublic.m_sKeys.length();
		        					if (iLen>0)
		        					{
		        						clsMyPublic.m_sKeys=clsMyPublic.m_sKeys.substring(0,iLen-1);
		        						tv_Cur.setText(clsMyPublic.m_sKeys);
		        					}
		    						return;
		    					}
		    					else if (sKey.compareTo("���")==0)
		    					{
		    						clsMyPublic.m_sKeys="";
		    						tv_Cur.setText("");
		    						return;
		    					}
		    					else if(sKey.compareTo("ȡ��")==0)
		    					{
		    						adNumKey.cancel();
		    						return;
		    					}
		    					clsMyPublic.m_sKeys+=sKey;
		    					tv_Cur.setText(clsMyPublic.m_sKeys);
		    				}
		    		    }
			    	);
				}
			}
	    }
		
		adNumKey.show();		
	}
}
