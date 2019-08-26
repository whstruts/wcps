package wcs.cps;
import wcs.cps.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class actMain extends Activity
{
	private Intent m_actPick=null;
	private Intent m_actBlnMgr=null;	
	private Intent m_actSupply=null;
	private Intent m_actLog=null;
	private Intent m_actStock=null;
	private Intent m_actWorkLog=null;
	private Intent mactPickBoxMatch=null;
	private Intent mactSupplyAvtive=null;
	private Button bt_refresh=null;
	private Button bt_Exit=null;
	private Button bt_Pick=null;
	private Button bt_BlueMgr=null;
	private Button bt_BhWork=null;
	private Button bt_RkWork=null;
	private Button bt_Log=null;
	private Button bt_Pdwork=null;
	private Button bt_kccx=null;
	private Button bt_zyjl=null;
	private Button bt_pczy=null;
	private Button bt_zzxcx = null;
	private TextView tv_MessTs=null;
	private TextView tv_Cur=null;
	private ListView lv_mainTask=null;
	private ListView lv_SumTask=null;
	private String[][] m_receMainTask;
	private String[][] m_receSumData;
	private int m_iCurReceIndex=-1;
	private int m_iSumReceCount=-1;
	private String m_sPrnBoxNo="";  //打印标签条码
	private String m_sClsName="actMain";
	
	public  Handler hd=new Handler()//声明消息处理器
	{
			@Override
			public void handleMessage(Message msg)//重写方法
        	{			
				switch(msg.what)
				{
				case clsMyPublic.MsTypeDisMess:
					mDoDisMessage(msg);
					break;
				case clsMyPublic.MsTypeReceMess:
					mDoReturnData(msg);
				}
        	}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.main);
        this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-无线拣选小车系统 主界面 【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】" +
                      "首选拣货区域【" + clsMyPublic.g_WorkAreaNo +"】");
        //clsMyPublic.m_hdlSendMess=hd;
        
        mSetSysData();
        mSendData(1);
    }
	@Override
	protected void onResume()
	{
	    super.onResume();
	    clsMyPublic.m_hdlSendMess=hd;
	}
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
		if (_Type==1)
		{
			m_receMainTask=null;
			_sendData=clsMyPublic.g_SktParamMainTask +";" +
					  clsMyPublic.g_WorkName +";";       
		    mSetButton(1);
		}
		else if  (_Type==2)
		{
			//索取作业任务类型 拣货作业和被动补货作业都是从此处开始
			_sendData=clsMyPublic.g_sktParamSQWrkType +";" +
					  clsMyPublic.g_WorkName +";";    
		    mSetButton(1);
		}
		else if (_Type==3)
		{
			_sendData=clsMyPublic.g_sktParamSFWGL +";"+
					  clsMyPublic.g_WorkName+";";
			mSetButton(1);
		}
		else if (_Type==4)
		{
			_sendData=clsMyPublic.g_sktParamPrnTest +";"+"1;" + clsMyPublic.g_WorkAreaNo +";";
		}
		else if (_Type==5)
		{
			_sendData=clsMyPublic.g_sktParamPrnData +";"+
		              clsMyPublic.g_sktParamLsZtData +";"+"1;"+m_sPrnBoxNo+";"+
		              clsMyPublic.g_WorkName+";";			
		}
		else if (_Type==6)
		{
			_sendData=clsMyPublic.g_SktParamExit +";PDALOGOUT;'"+
					  clsMyPublic.g_WorkName +"','"+
					  clsMyPublic.g_LocalIp+"';";
		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	
	private void mSetSysData()
	{
    	bt_refresh=(Button)findViewById(R.id.bt_refresh);
    	bt_Exit=(Button)findViewById(R.id.bt_Exit);
    	bt_Pick=(Button)findViewById(R.id.bt_pickwork);
    	//bt_BlueMgr=(Button)findViewById(R.id.bt_blueteeth);
    	bt_BhWork=(Button)findViewById(R.id.bt_bhwork);
    	//bt_Log=(Button)findViewById(R.id.bt_Log);
    	//bt_RkWork=(Button)findViewById(R.id.bt_rkwork);
    	//bt_Pdwork=(Button)findViewById(R.id.bt_pdwork);
    	bt_kccx=(Button)findViewById(R.id.bt_kccx);
        bt_zyjl=(Button)findViewById(R.id.bt_zyjl);
        bt_pczy=(Button)findViewById(R.id.bt_pc);
        bt_zzxcx=(Button)findViewById(R.id.bt_zzxcx);
    	tv_MessTs=(TextView)findViewById(R.id.tv_ts);
    	lv_mainTask=(ListView)findViewById(R.id.listView1);
    	lv_SumTask=(ListView)findViewById(R.id.listView2);
    	//刷新事务事件
    	bt_refresh.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mSendData(1);
				}
		    }
        );
    	//退出事件
    	bt_Exit.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mQueryExitSys();
    				}
    		    }	
    	);
    	
    	//拣货作业
    	bt_Pick.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					//mViewUi(1);
    					mSetButton(1);
    			    	mSendData(2);
    				}
    		    }	
        );
    	/*
    	bt_Pdwork.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(12);
    				}
    		    }	
        );*/
    	//蓝牙管理
    	/*
    	bt_BlueMgr.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(2);
    				}
    		    }	
        );*/
    	bt_zzxcx.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					//mViewUi(3);
    					//mSendData(4);
    					mViewUi(14);  //主动补货
    				}
    		    }	
    	);
    	
    	//补货作业
    	bt_BhWork.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					//mViewUi(3);
    					//mSendData(4);
    					mViewUi(11);  //主动补货
    				}
    		    }	
        );
    	/*
    	bt_RkWork.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					//mViewUi(5);
    					//mNumKeys();
    				}
    		    }	
        );*/
    	//日志查看
    	/*
    	bt_Log.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(4);
    				}
    		    }	
    	);*/
    	bt_pczy.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(13);
    				}
    		    }	
    	);
    	bt_kccx.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(7);
    				}
    		    }	
    	);
    	bt_zyjl.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(8);
    				}
    		    }	
    	);

	}
	
	private void mSetButton(int _Type)
	{
		if (_Type==1)
		{
			bt_refresh.setEnabled(false);
			bt_refresh.setBackgroundResource(R.drawable.clrunenabled);	
	    	bt_Pick.setEnabled(false);
	    	bt_Pick.setBackgroundResource(R.drawable.clrunenabled);	
	    	bt_BhWork.setEnabled(false);
	    	bt_BhWork.setBackgroundResource(R.drawable.clrunenabled);	
	    	//bt_RkWork.setEnabled(false);
	    	//bt_RkWork.setBackgroundResource(R.drawable.clrunenabled);	
	    	//bt_Pdwork.setEnabled(false);
	    	//bt_Pdwork.setBackgroundResource(R.drawable.clrunenabled);	
	    	bt_kccx.setEnabled(false);
	    	bt_kccx.setBackgroundResource(R.drawable.clrunenabled);	
	    	bt_zyjl.setEnabled(false);
	    	bt_zyjl.setBackgroundResource(R.drawable.clrunenabled);	
		}
		else if(_Type==2)
		{
			bt_refresh.setEnabled(true);
			bt_refresh.setBackgroundResource(R.drawable.clsbtnselect);
			bt_Pick.setEnabled(true);
			bt_Pick.setBackgroundResource(R.drawable.clsbtnselect);	
	    	bt_BhWork.setEnabled(true);
	    	bt_BhWork.setBackgroundResource(R.drawable.clsbtnselect);	
	    	//bt_RkWork.setEnabled(true);
	    	//bt_RkWork.setBackgroundResource(R.drawable.clsbtnselect);	
	    	//bt_Pdwork.setEnabled(true);
	    	//bt_Pdwork.setBackgroundResource(R.drawable.clsbtnselect);	
	    	bt_kccx.setEnabled(true);
	    	bt_kccx.setBackgroundResource(R.drawable.clsbtnselect);	
	    	bt_zyjl.setEnabled(true);
	    	bt_zyjl.setBackgroundResource(R.drawable.clsbtnselect);
		}
	}
	
	private void mFillMainTaskData(String[][] _sData)
	{
		final String[][]msg=_sData;
        BaseAdapter ba_detail=new BaseAdapter()//新建适配器
        {
			public int getCount() 
			{
				return msg[0].length;//得到列表的长度
			}
			public Object getItem(int arg0){return null;}
			public long getItemId(int arg0){return 0;}
			public View getView(int arg0, View arg1, ViewGroup arg2)//为每一项添加内容
			{
				LinearLayout ll_detail=new LinearLayout(actMain.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
				ll_detail.setPadding(5,5,5,5);//四周留白

				for(int i=0;i<msg.length;i++)//为每一行设置显示的数据
				{					    
					TextView s= new TextView(actMain.this);
					s.setText(msg[i][arg0]+" ");//TextView中显示的文字
					s.setTextSize(30);//字体大小
					s.setTextColor(Color.BLUE);
					s.setPadding(1,2,2,1);//四周留白
					s.setWidth(205);//宽度
					s.setHeight(100);
					if (i==0)
						s.setGravity(Gravity.CENTER);	
					else
						s.setGravity(Gravity.CENTER);
				    ll_detail.addView(s);//放入LinearLayout
				}
				return ll_detail;//将此LinearLayout返回
			}        	
        };        
        lv_mainTask.setAdapter(ba_detail);
	}	
	private void mFillSumTaskData(String[][] _sData)
	{
		final String[][]msg=_sData;
        BaseAdapter ba_detail=new BaseAdapter()//新建适配器
        {
			public int getCount() 
			{
				return msg[0].length;//得到列表的长度
			}
			public Object getItem(int arg0){return null;}
			public long getItemId(int arg0){return 0;}
			public View getView(int arg0, View arg1, ViewGroup arg2)//为每一项添加内容
			{
				LinearLayout ll_detail=new LinearLayout(actMain.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
				ll_detail.setPadding(5,5,5,5);//四周留白

				for(int i=0;i<msg.length;i++)//为每一行设置显示的数据
				{					    
					TextView s= new TextView(actMain.this);
					s.setText(msg[i][arg0]);//TextView中显示的文字
					s.setTextSize(30);//字体大小
					s.setTextColor(Color.RED);
					s.setPadding(1,2,2,1);//四周留白
					s.setWidth(205);//宽度
					s.setHeight(80);
					if (i==0)
						s.setGravity(Gravity.CENTER);	
					else
						s.setGravity(Gravity.CENTER);
				    ll_detail.addView(s);//放入LinearLayout
				}
				return ll_detail;//将此LinearLayout返回
			}        	
        };        
        lv_SumTask.setAdapter(ba_detail);
	}
	
	private void mViewUi(int _UiNo)
	{
		switch(_UiNo)
		{
		case 1:
			actPickWork.m_iWorkMode=clsMyPublic.JHZY_XJ;
			m_actPick=new Intent(this,actPickWork.class);
			startActivityForResult(m_actPick,1);
			break;
		case 2:
			m_actBlnMgr=new Intent(this,actBlueMgr.class);
			startActivityForResult(m_actBlnMgr,2);
			break;
		case 3:
			actSupply.m_iWorkMode=clsMyPublic.SJZY_BH;
			m_actSupply=new Intent(this,actSupply.class);
			startActivityForResult(m_actSupply,3);
			break;
		case 4:
			m_actLog=new Intent(this,LogDBActivity.class);
			startActivityForResult(m_actLog,4);
			break;
		case 5:
			actSupply.m_iWorkMode=clsMyPublic.SJZY_RK;
			m_actSupply=new Intent(this,actSupply.class);
			startActivityForResult(m_actSupply,5);
			break;
		case 6:
			actPickWork.m_iWorkMode=clsMyPublic.JHZY_PD;
			m_actPick=new Intent(this,actPickWork.class);
			startActivityForResult(m_actPick,6);
			break;
		case 7:
			m_actStock=new Intent(this,actStock.class);
			startActivityForResult(m_actStock,7);
			break;
		case 8:
			m_actWorkLog=new Intent(this,actWorkLog.class);
			startActivityForResult(m_actWorkLog,8);
			break;
		case 9:			
			actSupply.m_iWorkMode=clsMyPublic.SJZY_BDBH;
			m_actSupply=null;
			m_actSupply=new Intent(this,actSupply.class);
			startActivityForResult(m_actSupply,9);
			break;
		case 10:
			mactPickBoxMatch=null;
			mactPickBoxMatch=new Intent(this,actPickBoxMatch.class);
			startActivityForResult(mactPickBoxMatch,10);
			break;
		case 11:			
			actSupply.m_iWorkMode=clsMyPublic.SJZY_ZDBH;
			m_actSupply=null;
			m_actSupply=new Intent(this,actSupply.class);
			startActivityForResult(m_actSupply,11);
			break;
		case 12:
			mactSupplyAvtive=new Intent(this,actSupplyActive.class);
			startActivityForResult(mactSupplyAvtive,12);
			break;
		case 13:
			mactSupplyAvtive=new Intent(this,actCheckStock.class);
			startActivityForResult(mactSupplyAvtive,13);
			break;
		case 14:
			mactSupplyAvtive=new Intent(this,actQuery.class);
			startActivityForResult(mactSupplyAvtive,13);
			break;
		}

	}
	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_SktParamMainTask)==0)
			{				
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0)
				{
					tv_MessTs.setText(sReceData);
					mSetButton(2);
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					mSetButton(2);
				}
				else
				{	
					tv_MessTs.setText(sReceData);
					if (m_receMainTask==null)
					{
						m_iSumReceCount=Integer.parseInt(sRet1);
						m_receMainTask=new String[10][m_iSumReceCount];
						m_receSumData=new String[10][1];					
						for(int i=0;i<10;i++)
						{
							m_receMainTask[i]=new String[m_iSumReceCount];
							m_receSumData[i]=new String[1];
							m_receSumData[i][0]="0";
						}
						m_iCurReceIndex=-1;
						m_receSumData[0][0]="合计:";
					}
					m_iCurReceIndex++;
					for(int i=0;i<10;i++)
					{
						m_receMainTask[i][m_iCurReceIndex]=clsMyPublic.GetStrInOfStr(sReceData, i+2);
						if (i>0)
						{
							int iSum=0,iTmp=0;
							iSum=Integer.parseInt(m_receSumData[i][0]);
							iTmp=Integer.parseInt(m_receMainTask[i][m_iCurReceIndex]);
							m_receSumData[i][0]=String.valueOf(iSum+iTmp);
						}
	
					}						
					mFillMainTaskData(m_receMainTask);
					mFillSumTaskData(m_receSumData);
				}
				if (m_iSumReceCount==(m_iCurReceIndex+1))
				{
					mSetButton(2);
				}
			}
			else if(sFunNo.compareTo(clsMyPublic.g_sktParamSQWrkType)==0)
			{
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				
				if (sRet1.compareTo("-1")==0)
				{
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					mSetButton(2);
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{		
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					mSetButton(2);
				}
				else
				{					
					tv_MessTs.setText(sReceData);
					String sWrkType="";
					sWrkType=clsMyPublic.GetStrInOfStr(sReceData, 2);
					sWrkType=sWrkType.trim();
					if(sWrkType.compareTo(clsMyPublic.g_SqWorkType_Lstd)==0||
					   sWrkType.compareTo(clsMyPublic.g_SqWorkType_Zjjh)==0||
					   sWrkType.compareTo(clsMyPublic.g_SqWorkType_Ztzy)==0||
					   sWrkType.compareTo(clsMyPublic.g_SqWorkType_Ptzy)==0||
					   sWrkType.compareTo(clsMyPublic.g_SqWorkType_Tqjx)==0||
					   sWrkType.compareTo(clsMyPublic.g_SqWorkType_Tqjx2)==0)
					{
						//判断是否先关联周转箱，还是可以拣货作业
						mSendData(3);
						return;
					}
					else if (sWrkType.compareTo(clsMyPublic.g_SqWorkType_Pdzy)==0)
					{
						//盘点作业
						mViewUi(6);					
					}
					else if (sWrkType.compareTo(clsMyPublic.g_SqWorkType_Bdbh)==0)
					{
						mViewUi(9);
					}
					else if (sWrkType.compareTo(clsMyPublic.g_SqWorkType_Zdbh)==0)
					{
						mViewUi(11);
					}
					else if (sWrkType.compareTo(clsMyPublic.g_SqWorkType_Rksj)==0)
					{
						//入库上架
						mViewUi(5);					
					}
				}
			}
			else if (sFunNo.compareTo(clsMyPublic.g_sktParamSFWGL)==0)
			{
				
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0)
				{
					tv_MessTs.setText(sReceData);
					mSetButton(2);
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					mSetButton(2);
				}
				else if (sRet1.compareTo("1")==0)
				{
					//索取分配单、关联周转箱作业
					mViewUi(10);
					
				}
				else if (sRet1.compareTo("0")==0)
				{
					mViewUi(1);
				}
			}
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
		catch(UnknownError e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
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
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		mSetButton(2);
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case 10:
			switch(resultCode)
			{
			case 2:
				//关联周转箱任务结束，直接进入拣货界面
				mViewUi(1);
				break;
			}			
		}
	}
	private void mNumKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actnumkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setTitle("键盘").setView(loginLayout).create();
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=200;
		lp.y=200;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";		
		tv_Cur=tv_MessTs;
		
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
		    					if (sKey.compareTo("回车")==0)
		    					{
		    						m_sPrnBoxNo=clsMyPublic.m_sKeys;
		    						mSendData(5);
		    						adNumKey.cancel();
		    						return;
		    					}
		    					else if (sKey.compareTo("删除")==0)
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
		    					else if (sKey.compareTo("清除")==0)
		    					{
		    						clsMyPublic.m_sKeys="";
		    						tv_Cur.setText("");
		    						return;
		    					}
		    					else if(sKey.compareTo("取消")==0)
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
	private void mQueryExitSys()
	{
        new AlertDialog.Builder(actMain.this)
               .setTitle("温馨提示")
               .setIcon(android.R.drawable.alert_dark_frame)
               .setMessage("您确定要退出系统吗？")
               .setNegativeButton("取消", new DialogInterface.OnClickListener()
			   {
				   public void onClick(DialogInterface dialog, int which)
				   {
					   
				   }
			   }
			   ).setPositiveButton("确定", new DialogInterface.OnClickListener()
	    	    {
	    		   public void onClick(DialogInterface dialog, int which) 
	    		   {
	    			    mSendData(6);
	   					Intent intent=new Intent();
	   					intent.putExtra("UINO", "MainUI");
	   					setResult(3,intent);
	      				finish();
	    		   }
	    		 }).show();
	}
}
