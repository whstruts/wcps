package wcs.cps;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class actSupply extends Activity
{
	private TextView[] m_WorkBt;
	private int m_iSumPickBtCount=10;
	private int m_iSupTaskCount=0;
	private int m_iCurDoIndex=-1;
	private int m_iCurViewIndex=0;
	private int m_iUnDoTaskCount=-1;
	private Button bt_Return=null;
	private Button bt_Log=null;	
	private Button bt_SQ=null;
	private Button bt_Save=null;
	private TextView edt_Bar=null;
	private TextView edt_Loc=null;
	private TextView edt_Sl=null;
	private Intent m_actBlnMgr=null;	
	private Intent m_actLog=null;
	private TextView tv_Cur=null;
	private TextView txt_zyts=null;
	private EditText edt_ScnData=null;

	private TextView tv_PlanLoc = null;// 计划货位
	private TextView tv_PlanCount = null;// 计划数量
	private TextView tv_ChinaName = null;// 中文名
	private TextView tv_Manu = null;// 制造商信息
	private TextView tv_PackUnit = null;// 包装单位
	private TextView tv_DrugSpec = null;// 药品规格
	private TextView tv_LotNumber = null;// 批号
	private TextView tv_ValidUntil = null;// 有效期
	private TextView tv_RunNum = null;// 流水号
	private TextView tv_MessTs=null;
	
	private String m_sZylb="";   //作业类别
	private String m_sDjbh="";   //单据编号
	private String m_sDjhh="";   //单据行号
	private String m_sJhsl="";   //拣货数量
	private String m_sJhhw="";   //拣货货位
	
	private String m_sCurBarCode="";
	public static int m_iWorkMode=0;  //作业模式  
	private String m_sClsName="actSupply";
	
	private static ArrayList<clsSupTask> m_SupTaskList=null;
	
	public  Handler hd=new Handler()//声明消息处理器
	{
		public void handleMessage(Message msg)//重写方法
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
		setContentView(R.layout.actsupply);
		this.setTitleColor(Color.BLUE);
		mSetSysData();
		if (m_iWorkMode==clsMyPublic.SJZY_ZDBH)
		{
			this.setTitle("WCPS-主动补货作业  【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
			m_sZylb="7";
			txt_zyts.setText("主动补货");
		}
		else if (m_iWorkMode==clsMyPublic.SJZY_RK)
		{
			this.setTitle("WCPS-入库上架作业  【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
			m_sZylb="9";
		}
		else if (m_iWorkMode==clsMyPublic.SJZY_BDBH)
		{
			this.setTitle("WCPS-被动补货作业  【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
			m_sZylb="6";
			txt_zyts.setText("被动补货");
		}
		m_SupTaskList=new ArrayList<clsSupTask>();
		clsMyPublic.m_hdlSendMess=hd;
		clsBluetoothMgr.m_CurHandler=hd;		
		mSetButtonIni();	
		mSendData(0);
	}
	
	
	public class clsSupTask 
	{
		String _sDanjNo="";
		String _sHangHao="";
		String _sBoxNo="";
		String _sPickHw="";
		String _sArtName;
		String _sArtGg;
		String _sArtZbz;
		String _sArtBzdw;
		String _sArtPh;
		String _sArtCj;
		String _sJhsl;
		String _sSjsl;
		String _sZylb;
		String _sFpdno;
		String _sBarCode;
		String _sArtDate;   //有效日期
		int   _iDoStatus=0;  // 0 初始化 2 索取成功 3 上架确认
	}
	
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;

		if (_Type==0)
		{
			m_SupTaskList=null;
			m_SupTaskList=new ArrayList<clsSupTask>();
			m_iSupTaskCount=0;
			m_iCurViewIndex=0;
			m_iCurDoIndex=-1;
			_sendData=clsMyPublic.g_SktParamSQZDBh +";" +
					  clsMyPublic.g_WorkName +";" +
					  "A" +";" +
					  m_sZylb +";";
		}
		else if (_Type==1)
		{
			m_SupTaskList=null;
			m_SupTaskList=new ArrayList<clsSupTask>();
			m_iSupTaskCount=0;
			m_iCurViewIndex=0;
			m_iCurDoIndex=-1;
			_sendData=clsMyPublic.g_SktParamSQZDBh +";" +
					  clsMyPublic.g_WorkName +";" +
					  m_sCurBarCode +";" +
					  m_sZylb +";";
		}
		else if (_Type==2)
		{
			_sendData=clsMyPublic.g_SktParamQRZDBH +";" +
		              m_sZylb +";" +
		              m_sDjbh +";" +
		              m_sDjhh +";" +
		              m_sJhsl +";" +
					  clsMyPublic.g_WorkName+";" +
		              m_sJhhw +";";
		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	
	private void mSetButtonIni()
	{
		for(int i=0;i<m_iSumPickBtCount;i++)
		{
			m_WorkBt[i].setEnabled(false);
			m_WorkBt[i].setBackgroundResource(R.drawable.clrunenabled);
		}
		
		tv_PlanLoc.setText("");
		tv_PlanCount.setText("");	
		tv_ChinaName.setText(""); //商品名称		
		tv_Manu.setText("");  //生产厂家		
		tv_DrugSpec.setText("");  //药品规格		
		tv_LotNumber.setText("");  //药品批号		
		tv_PackUnit.setText("");   //药品包装单位数量		
		tv_ValidUntil.setText("");    //包转数量
		tv_RunNum.setText(""); 
		
		bt_Save.setEnabled(false);
		bt_Save.setBackgroundResource(R.drawable.clrunenabled);
		
		bt_SQ.setEnabled(true);
		bt_SQ.setBackgroundResource(R.drawable.clsbtnselect);
	}
	
	private void mSetSysData()
	{
		bt_Return=(Button)findViewById(R.id.bt_return);
		bt_Log=(Button)findViewById(R.id.bt_log);
		bt_SQ=(Button)findViewById(R.id.bt_boxtake);
		bt_Save=(Button)findViewById(R.id.bt_save);
		edt_Bar=(TextView)findViewById(R.id.et_barcode);
		edt_Loc=(TextView)findViewById(R.id.et_realloc);
		edt_Sl=(TextView)findViewById(R.id.edt_sjsl);

    	edt_ScnData=(EditText)findViewById(R.id.edt_ScnData);
    	edt_ScnData.setInputType(InputType.TYPE_NULL);
    	
    	tv_PlanLoc = (TextView) findViewById(R.id.tv_taskloc_v);
		tv_PlanCount = (TextView) findViewById(R.id.tv_plancount_v);
		tv_ChinaName = (TextView) findViewById(R.id.tv_chinaname_v);
		tv_Manu = (TextView) findViewById(R.id.tv_manu_v);
		tv_PackUnit = (TextView) findViewById(R.id.tv_bzdw);
		tv_DrugSpec = (TextView) findViewById(R.id.tv_ypgg);
		tv_LotNumber = (TextView) findViewById(R.id.tv_spph);
		tv_ValidUntil = (TextView) findViewById(R.id.tv_yxDate);
		tv_RunNum = (TextView) findViewById(R.id.tv_BarNo);
		txt_zyts= (TextView) findViewById(R.id.txt_zyts);
		
		
		tv_MessTs=(TextView)findViewById(R.id.tv_pi);
    	//拣货货位按钮初始化
    	m_WorkBt=new TextView[m_iSumPickBtCount];
    	m_WorkBt[0]=(TextView)findViewById(R.id.tv_hw01);
    	m_WorkBt[1]=(TextView)findViewById(R.id.tv_hw02);
    	m_WorkBt[2]=(TextView)findViewById(R.id.tv_hw03);
    	m_WorkBt[3]=(TextView)findViewById(R.id.tv_hw04);
    	m_WorkBt[4]=(TextView)findViewById(R.id.tv_hw05);
    	m_WorkBt[5]=(TextView)findViewById(R.id.tv_hw06);
    	m_WorkBt[6]=(TextView)findViewById(R.id.tv_hw07);
    	m_WorkBt[7]=(TextView)findViewById(R.id.tv_hw08);
    	m_WorkBt[8]=(TextView)findViewById(R.id.tv_hw09);
    	m_WorkBt[9]=(TextView)findViewById(R.id.tv_hw10);
    	
    	for(int i=0;i<10;i++)
    	{
    		m_WorkBt[i].setVisibility(View.INVISIBLE);
    	}
    	
    	for(int i=0;i<this.m_iSumPickBtCount;i++)
		{
    		m_WorkBt[i].setEnabled(false);
    		m_WorkBt[i].setOnClickListener
			(
				new OnClickListener()
			    {
					public void onClick(View v) 
					{
						mViewSupData(v);
						m_sCurBarCode="";
						edt_Bar.setText("");
					}
			    }
			);
        }	
		
		tv_PlanLoc.setOnClickListener (new OnClickListener(){public void onClick(View v) {}});   	
		tv_PlanCount.setOnClickListener (new OnClickListener(){public void onClick(View v) {}});  
		tv_ChinaName.setOnClickListener (new OnClickListener(){public void onClick(View v) {}});   	
		tv_Manu.setOnClickListener (new OnClickListener(){public void onClick(View v) {}}); 
		tv_PackUnit.setOnClickListener (new OnClickListener(){public void onClick(View v) {}});   	
		tv_DrugSpec.setOnClickListener (new OnClickListener(){public void onClick(View v) {}}); 
		tv_LotNumber.setOnClickListener (new OnClickListener(){public void onClick(View v) {}});   	
		tv_ValidUntil.setOnClickListener (new OnClickListener(){public void onClick(View v) {}}); 
		tv_RunNum.setOnClickListener (new OnClickListener(){public void onClick(View v) {}}); 
		
		
		edt_ScnData.setOnKeyListener
    	(
    		new View.OnKeyListener()
	    	{
	    		public boolean onKey(View v, int keyCode, KeyEvent event)
	    		{
	    			if (keyCode==KeyEvent.KEYCODE_ENTER)
	    			{		    				
	    				String s="";
	    				keyCode=0;
	    				s=edt_ScnData.getText().toString();
	    				s=s.trim();
	    				if (s.length()==0) 
	    				{  
	    					return true;
	    				}
	    				edt_Bar.setText(s);
	    				edt_ScnData.setText("");
	    				m_sCurBarCode=s;
	    				mSQBhZyData(s);
	    			}
	    			return false;
	    		}
	    	}
    	);
		
		//返回事件
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

    	//日志查看
    	bt_Log.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mViewUi(4);
    				}
    		    }	
    	);
    	//日志查看
    	bt_SQ.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					m_sCurBarCode=edt_Bar.getText().toString();
    					if (m_sCurBarCode.length()==0)
    					{
    						mShowMessage("请扫描或者输入作业标签条码......");
    						return;
    					}
    					mSQBhZyData(m_sCurBarCode);
    				}
    		    }	
    	);
    	bt_Save.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					mSendData(2);
    					v.setEnabled(false);
    					v.setBackgroundResource(R.drawable.clrunenabled);
    				}
    		    }	
    	);
    	edt_Bar.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{  
    					v.setBackgroundResource(R.drawable.clrpressyes);
    					edt_Loc.setBackgroundResource(R.drawable.clspickmsgselect);
    					edt_Sl.setBackgroundResource(R.drawable.clspickmsgselect);
    					mCharKeys();
    				}
    		    }		
    	);
    	edt_Loc.setOnClickListener(new OnClickListener(){public void onClick(View v) {}});
    	edt_Sl.setOnClickListener(new OnClickListener(){public void onClick(View v) {}});

	}
	private void mViewUi(int _UiNo)
	{
		switch(_UiNo)
		{
		case 1:
			
			break;
		case 2:
			m_actBlnMgr=new Intent(this,actBlueMgr.class);
			startActivityForResult(m_actBlnMgr,2);
			break;
		case 3:
			break;
		case 4:
			m_actLog=new Intent(this,LogDBActivity.class);
			startActivityForResult(m_actLog,4);
			break;
		}
	}
	
	private void mViewSupData(View v)
	{
		clsSupTask _SupTask=new clsSupTask();
    	int tag = (Integer) v.getTag();
    	_SupTask=m_SupTaskList.get(tag);
    	
		if (m_iCurDoIndex>=0)
		{
			int curOldIndex=(Integer) m_WorkBt[m_iCurDoIndex].getTag();
			clsSupTask _SupTask1=new clsSupTask();
			_SupTask1=m_SupTaskList.get(curOldIndex);
			if (_SupTask1._iDoStatus==1)
				m_WorkBt[m_iCurDoIndex].setBackgroundResource(R.drawable.clrendboxback);
			else
				m_WorkBt[m_iCurDoIndex].setBackgroundResource(R.drawable.clsbtnselect);
		}   	
    	m_iCurDoIndex=tag;
		tv_PlanLoc.setText(_SupTask._sPickHw);
		edt_Loc.setText(_SupTask._sPickHw);
		tv_PlanCount.setText(_SupTask._sJhsl);
		edt_Sl.setText(_SupTask._sJhsl);
		tv_ChinaName.setText(_SupTask._sArtName); //商品名称		
		tv_Manu.setText(_SupTask._sArtCj);  //生产厂家		
		tv_DrugSpec.setText(_SupTask._sArtGg);  //药品规格		
		tv_LotNumber.setText(_SupTask._sArtPh);  //药品批号		
		tv_PackUnit.setText(_SupTask._sArtBzdw);   //药品包装单位数量		
		tv_ValidUntil.setText(_SupTask._sArtDate);    //包转数量
		tv_RunNum.setText(_SupTask._sBarCode);     //条码				

		m_sDjbh=_SupTask._sDanjNo;   //单据编号
		m_sDjhh=_SupTask._sHangHao;   //单据行号
		m_sJhsl=_SupTask._sJhsl;   //拣货数量
		m_sJhhw=_SupTask._sPickHw;   //拣货货位_SupTask._sDanjNo
		
		//正在作业
		if (_SupTask._iDoStatus==0)
		{	
			edt_Bar.setText(_SupTask._sBarCode);
			bt_Save.setEnabled(true);
			bt_Save.setBackgroundResource(R.drawable.clsbtnselect);
			bt_SQ.setEnabled(false);
			bt_SQ.setBackgroundResource(R.drawable.clrunenabled);
			v.setBackgroundResource(R.drawable.clrcurworkhw);
		}
		else if (_SupTask._iDoStatus==1)
		{
			v.setBackgroundResource(R.drawable.clrunenabled);
			mShowMessage("已经上架确认，请核对....");
			bt_SQ.setEnabled(false);
			bt_SQ.setBackgroundResource(R.drawable.clrunenabled);
			bt_Save.setEnabled(false);
			bt_Save.setBackgroundResource(R.drawable.clrunenabled);
		}	
	}
	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_SktParamSQZDBh)==0)
			{
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0)
				{
					edt_Bar.setText("");
					bt_SQ.setEnabled(true);
					bt_SQ.setBackgroundResource(R.drawable.clsbtnselect);
					tv_MessTs.setText(sReceData);
					return;
				}
				else if (sRet1.compareTo("0")==0)
				{
					edt_Bar.setText("");
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
				}
				else
				{
					tv_MessTs.setText(sReceData);
					m_iSupTaskCount=Integer.parseInt(sRet1);
					m_iUnDoTaskCount=m_iSupTaskCount;
					clsSupTask _SupTask=new clsSupTask();
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 2);
					
					_SupTask._sPickHw=sRet1;  //拣货货位
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 3);
					_SupTask._sJhsl=sRet1;  //拣货数量
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 4);
					_SupTask._sArtName=sRet1; //商品名称
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 5);
					_SupTask._sArtCj=sRet1;//生产厂家
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 6);
					_SupTask._sArtGg=sRet1;//药品规格
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 7);
					_SupTask._sArtPh=sRet1; //药品批号
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 8);
					_SupTask._sArtBzdw=sRet1; //药品包装单位数量
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 9);
					_SupTask._sArtDate=sRet1; //包转数量
					
					sRet1 = clsMyPublic.GetStrInOfStr(sReceData, 10);
					_SupTask._sBarCode=sRet1;   //上架标签
					
					_SupTask._sDanjNo=clsMyPublic.GetStrInOfStr(sReceData, 11);
					_SupTask._sHangHao=clsMyPublic.GetStrInOfStr(sReceData, 12);
					_SupTask._sZylb=clsMyPublic.GetStrInOfStr(sReceData, 13);
					_SupTask._iDoStatus=0; //索取补货任务
					
		
					if(_SupTask._sZylb.compareTo("6") ==0)
					{
						txt_zyts.setText("被动补货");
					}
					else if(_SupTask._sZylb.compareTo("7") ==0)
					{
						txt_zyts.setText("主动补货");
					}else if(_SupTask._sZylb.compareTo("9") ==0)
					{
						txt_zyts.setText("入库上架");
						m_sZylb="9";
					}
					
					m_WorkBt[m_iCurViewIndex].setText(_SupTask._sPickHw);
					m_WorkBt[m_iCurViewIndex].setTag(m_iCurViewIndex);
					m_WorkBt[m_iCurViewIndex].setEnabled(true);
					m_WorkBt[m_iCurViewIndex].setVisibility(View.VISIBLE);
					m_WorkBt[m_iCurViewIndex].setBackgroundResource(R.drawable.clsbtnselect); 
					m_SupTaskList.add(_SupTask);
					
					m_iCurViewIndex++;
					
					bt_SQ.setEnabled(true);
					bt_SQ.setBackgroundResource(R.drawable.clsbtnselect);
					bt_Save.setEnabled(false);
					bt_Save.setBackgroundResource(R.drawable.clrunenabled);
					
					if (m_iCurViewIndex==m_iSupTaskCount)
					{					
						if (m_iWorkMode==clsMyPublic.SJZY_BDBH)
							mViewSupData(m_WorkBt[0]);   //指哪扫哪 被动补货默认第一个箱子
						else
							mViewSupData(m_WorkBt[m_iCurViewIndex-1]); //主动补货默认最后一个箱子
					}				
				}
			}
			else if (sFunNo.compareTo(clsMyPublic.g_SktParamQRZDBH)==0)
			{
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0)
				{
					mShowMessage(clsMyPublic.GetStrInOfStr(sReceData, 2));
					bt_SQ.setEnabled(true);
					bt_SQ.setBackgroundResource(R.drawable.clsbtnselect);
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(sReceData);
				}
				else if (sRet1.compareTo("1")==0)
		    	{
					tv_MessTs.setText(sReceData);
					bt_Save.setEnabled(true);
					bt_Save.setBackgroundResource(R.drawable.clsbtnselect);
					m_WorkBt[m_iCurDoIndex].setBackgroundResource(R.drawable.clrendboxback);
					clsSupTask _CurSupTask=new clsSupTask();
					_CurSupTask=m_SupTaskList.get(m_iCurDoIndex);
					_CurSupTask._iDoStatus=1;
					m_SupTaskList.set(m_iCurDoIndex, _CurSupTask);
					mClrea();
					m_iUnDoTaskCount--;
					if (m_iUnDoTaskCount==0)
						mShowMessageII("全部作业确认成功，请继续作业;是否要退出本界面?");
					else
					{
						//自动显示下一个货位
						if(m_iCurDoIndex<(m_iSupTaskCount-1))
							mViewSupData(m_WorkBt[m_iCurDoIndex+1]);
					}
		    	}
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
	private void mSQBhZyData(String _BarCode)
	{
		int iFind=-1;
		for (int i=0;i<m_SupTaskList.size();i++)
		{
			clsSupTask _SupTask=new clsSupTask();
			_SupTask=m_SupTaskList.get(i);
			if (_SupTask._sBarCode.compareTo(_BarCode)==0)
			{	
				m_WorkBt[i].setBackgroundResource(R.drawable.clrcurworkhw);
				mViewSupData(m_WorkBt[i]);
				iFind=1;
				break;
			}
		}		
		//如果查找		
		if (iFind==-1)
		{
			m_sDjbh="";   //单据编号
			m_sDjhh="";   //单据行号
			m_sJhsl="";   //拣货数量
			m_sJhhw="";   //拣货货位_SupTask._sDanjNo
			bt_SQ.setEnabled(true);
			bt_SQ.setBackgroundResource(R.drawable.clsbtnselect);
			bt_Save.setEnabled(false);
			bt_Save.setBackgroundResource(R.drawable.clrunenabled);
			//如果没有找到则向LMIS索取任务
			if (m_sZylb=="7")
			{
				mSendData(1);	
				bt_SQ.setEnabled(false);
				bt_SQ.setBackgroundResource(R.drawable.clrunenabled);
			}
			else if (m_sZylb=="6")
			{
				edt_Bar.setText("");
				//如果是被动补货则提示不符合条件
				mShowMessage("请查找指定范围内的作业标签，然后作业......");
			}
		}
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mViewBlueData(Message msg)
	{
		byte[] readBuf = (byte[]) msg.obj;
		String readMessage = new String(readBuf, 0, msg.arg1);
		readMessage=readMessage.trim();
		edt_Bar.setText(readMessage);
		m_sCurBarCode=readMessage;
		mSQBhZyData(m_sCurBarCode);
	}
	
	private void mClrea()
	{
		tv_PlanLoc.setText("");
		tv_PlanCount.setText("");
		tv_ChinaName.setText("");
		tv_Manu.setText("");
		tv_DrugSpec.setText("");
		tv_LotNumber.setText("");
		tv_PackUnit.setText("");
		tv_ValidUntil.setText("");
		tv_RunNum.setText("");
		edt_Bar.setText("");
		edt_Loc.setText("");
		edt_Sl.setText("");
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
	
	private void mCharKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actcharkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setTitle("键盘").setView(loginLayout).create();
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=-500;
		lp.y=500;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
		tv_Cur=edt_Bar;
		clsMyPublic.m_sKeys=tv_Cur.getText().toString();
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
		    						m_sCurBarCode=clsMyPublic.m_sKeys;
		    						if(m_sCurBarCode.length()>0)
		    						{
		    							//m_sCurBarCode="";
		    							mSQBhZyData(m_sCurBarCode);
		    						}
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
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actSupply.this)
	        .setTitle("温馨提示")
	        .setIcon(android.R.drawable.alert_dark_frame)
	        .setMessage(_sMess)
	        .setNegativeButton("确定", new DialogInterface.OnClickListener()
	 	    {
	 		   public void onClick(DialogInterface dialog, int which) 
	 		   {
	 			   if (m_iUnDoTaskCount==0)
	 				   finish();
	 		   }
	 		 }).show();
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mShowMessage;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
		catch(UnknownError e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mShowMessage;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
	}
	
	public  void mShowMessageII(String _sMess)
	{
		new AlertDialog.Builder(actSupply.this)
        .setTitle("温馨提示")
        .setIcon(android.R.drawable.alert_dark_frame)
        .setMessage(_sMess+"\n确定要退出本界面吗?")
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
				finish();
 		   }
 		 }).show();
	}
	

}
