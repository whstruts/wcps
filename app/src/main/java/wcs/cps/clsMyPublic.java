package wcs.cps;


import android.os.Handler;


public class clsMyPublic
{

	public static  Handler m_hdlSendMess;
	public static final int MsTypeDisMess=0;  //��ʾ��ʾ��Ϣ
	public static final int MsTypeSendMess=1;  //������Ϣ
	public static final int MsTypeReceMess=2;  //������Ϣ
	public static final int LyTypeReceMess=3;  //������Ϣ
	
	public static final int SJZY_BH=1;  //������ҵ
	public static final int SJZY_RK=2;  //�����ҵ
	public static final int SJZY_BDBH=3;  //����������ҵ
	public static final int SJZY_ZDBH=4;  //����������ҵ
	
	public static final int JHZY_XJ=1;   //�¼���ҵ
	public static final int JHZY_PD=2;   //�̵���ҵ
	public static final int JHZY_TQ=3;   //��ǰ��ѡ��ҵ
	
	public static  String m_sKeys="";
	public static  String g_WorkNo="";   //Ա�����
	public static  String g_WorkName="North"; //Ա������
	public static  String g_WorkAreaNo="";   //��ҵ����
	public static  String g_WorkPass="";  //Ա������
	public static  String g_LocalIp="";   //����IP
	
	
	//�����˷�����Ϣ�ĸ�ʽ
	// ��ʾ + ���ظ�ʽ + ��������+ ���� ��
	public static final String  g_ResultTypeStr="01";    //�����ַ�����
	public static final String  g_ResultTypeCln="02";    //���ؽ����
	
	public static final String  g_SktParamLogin="PBS0001";  //��½
	public static final String  g_SktParamExit="PBS0002";   //�˳�
	public static final String  g_SktParamMainTask="PBS0003";     //�鿴����
	public static final String  g_SktParamPickTask="PBS0004";     //��������ҵ����
	public static final String  g_SktParamQRPick="PBS0005";       //���ȷ����ҵ
	public static final String  g_SktParamSQPick="PBS0006";       //��ȡ�����ҵ����
	public static final String  g_SktParamMatch="PBS0007";        //������ת����ҵ����
	public static final String  g_SktParamSQZDBh="PBS0008";       //��ȡ����������ҵ����
	public static final String  g_SktParamQRZDBH="PBS0009";       //ȷ������������ҵ����
	public static final String  g_SktParamCXPick="PBS0010";       //��ѯ�����ҵ��ϸ
	public static final String  g_sktParamSQWrkType="PSB0011";    //��ȡ��ҵ����
	public static final String  g_sktParamSFWGL="PSB0012";        //�ж��Ƿ���δ����������
	public static final String  g_sktParamCXWork="PSB0013";       //��ѯ��ҵ��¼
	public static final String  g_sktParamCXStock="PSB0014";      //��ѯ���
    public static final String  g_sktParamFpdGroup="PSB0015";   //���䵥��
    public static final String  g_sktParamBhActiv="PSB0016";      //�������뼤��
    public static final String  g_sktParamBhDanwei="PSB0017";     //������λ����
    
    public static final String  g_sktParamHwPc="PSB0018";      //��λ�̲�
    public static final String  g_sktParamHwTz="PSB0019";      //��λ����
    public static final String  g_sktParamHwDp="PSB0020";      //��λ����
    public static final String  g_sktParamDpQr="PSB0021";

    public static final String  g_sktParamPrnData = "PSB9999";      //���Դ�ӡ
    public static final String  g_sktParamPrnTest = "PSB9998";      //���Դ�ӡ
    public static final String  g_sktParamLsZtData = "PSB9001";   //��ɫ�����ӡ
    public static final String  g_sktParamCnntState = "CPS0000";    //�ж������Ƿ�����
    
    public static final String  g_sktParamCountChg="CPS0001";    //������ʾ��ɫ
	
	
	//��ҵ����
	public static final String g_SqWorkType_Tqjx="TQ";    //��ǰ��ѡ
	public static final String g_SqWorkType_Pdzy="1";     //�̵���ҵ
	public static final String g_SqWorkType_Lstd="2";     //��ɫͨ��
	public static final String g_SqWorkType_Zjjh="3";     //׷�Ӽ��
	public static final String g_SqWorkType_Ztzy="4";     //������ҵ
	public static final String g_SqWorkType_Ptzy="5";     //��ͨ����
	public static final String g_SqWorkType_Bdbh="6";     //����������ҵ
	public static final String g_SqWorkType_Zdbh="7";     //����������ҵ	
	public static final String g_SqWorkType_Rksj="9";     //����������ҵ	
	public static final String g_SqWorkType_Gjtc="10";     //�����˳�	
	public static final String g_SqWorkType_Tqjx2="5TQ";  //��ǰ��ѡ
	
	
	public clsMyPublic()
	{
		
	}
	public static String GetZylbName(String _No)
	{
		String sRlt="";
		if(_No.compareTo(g_SqWorkType_Tqjx)==0)
			sRlt="��ǰ��ѡ";
		else if (_No.compareTo(g_SqWorkType_Pdzy)==0)
			sRlt="�̵���ҵ";
		else if (_No.compareTo(g_SqWorkType_Lstd)==0)
			sRlt="��ɫͨ��";
		else if (_No.compareTo(g_SqWorkType_Zjjh)==0)
			sRlt="׷�Ӽ��";
		else if (_No.compareTo(g_SqWorkType_Ztzy)==0)
			sRlt="�������";
		else if (_No.compareTo(g_SqWorkType_Ptzy)==0)
			sRlt="��ͨ����";
		else if (_No.compareTo(g_SqWorkType_Bdbh)==0)
			sRlt="��������";
		else if (_No.compareTo(g_SqWorkType_Gjtc)==0)
			sRlt="�����˳�";
		return sRlt;
	}
	public static String GetZylbNo(String _Name)
	{
		String sRlt="";
		if(_Name.compareTo("��ǰ��ѡ")==0)
			sRlt=g_SqWorkType_Tqjx;
		else if (_Name.compareTo("�̵���ҵ")==0)
			sRlt=g_SqWorkType_Pdzy;
		else if (_Name.compareTo("��ɫͨ��")==0)
			sRlt=g_SqWorkType_Lstd;
		else if (_Name.compareTo("׷�Ӽ��")==0)
			sRlt=g_SqWorkType_Zjjh;
		else if (_Name.compareTo("�������")==0)
			sRlt=g_SqWorkType_Ztzy;
		else if (_Name.compareTo("��ͨ����")==0)
			sRlt=g_SqWorkType_Ptzy;
		else if (_Name.compareTo("��������")==0)
			sRlt=g_SqWorkType_Bdbh;
		else if (_Name.compareTo("�����˳�")==0)
			sRlt=g_SqWorkType_Ptzy;
		return sRlt;
	}
	//����ָ��λ�õ��ַ���
	public static String GetStrInOfStr(String _str1,int _startIndex)
	{
		String sRet="";
		String stmpStr="";
		int iCurIndex=-1;
		int iCurPos=-1;
		stmpStr=_str1;
	
		while(stmpStr.indexOf(";")>=0)
		{
			iCurPos=stmpStr.indexOf(";");
			iCurIndex++;
			if (iCurIndex==_startIndex)
			{	
				sRet=stmpStr.substring(0,iCurPos);
				break;
			}
			stmpStr=stmpStr.substring(iCurPos+1);
		}
		return sRet;
	}
	public final static int gSendDisMess(String _sData)
	{
		try
		{
			if (m_hdlSendMess==null) return 0;
			m_hdlSendMess.obtainMessage(MsTypeDisMess, 100,-1, _sData).sendToTarget();
			//LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,"UI001","0000",_sData);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
}
