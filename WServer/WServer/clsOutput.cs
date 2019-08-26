using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace WServer
{
    //下架作业
    class clsOutput
    {

        public clsMyAccDB mLmisDB = new clsMyAccDB();
        private string m_sClsName = "clsOutput";


        public string[] mDoCPSWorkData(string _WorkData)
        {
            string sMess="";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                int iRet=-1;
                //先连接数据库
                mLmisDB.m_DbCnntStr = MyPublic.m_DbLMISCnntStr;
                iRet = mLmisDB.mConnectDB(ref sMess);
                if (iRet == -1)
                {
                    _sResult = new string[1];
                    _sResult[0] = string.Format("{0};-1;{1};", _sParam[0], sMess);
                    return _sResult;
                }

                
                if (_sParam[0]==MyPublic.g_SktParamLogin)
                {
                    //处理登陆数据
                    _sResult = mDoLoginData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamExit)
                {
                    //处理退出数据
                    _sResult = mDoLoginOutData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamMainTask)
                {
                    //小车任务查看
                    _sResult = mGetSumWorkData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamSQWrkType)
                {
                    //索取作业类型
                    _sResult = mGetWorkType(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamSQPick)
                {
                    //索取预关联任务
                    _sResult = mGetProTask(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamMatch)
                {
                    //关联任务
                    _sResult = mMatchTask(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamPickTask)
                {
                    //索取拣货作业数据
                    _sResult = mGetPickWorkData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamFpdGroup)
                {
                    //索取拣货周转箱数据
                    _sResult = mGetPickBoxData(_WorkData);
                }
                else if (_sParam[0]==MyPublic.g_SktParamQRPick ||
                         _sParam[0] == MyPublic.g_SktParamQRZDBH)
                {
                    //拣货确认
                    _sResult=mConfirgWorkData(_WorkData);
                }
                else if (_sParam[0]==MyPublic.g_SktParamCXPick)
                {
                    //拣货任务查询
                    _sResult=mQueryBoxTaskData(_WorkData);
                }
                 else if (_sParam[0]==MyPublic.g_sktParamCXWork)
                {
                     //工作记录查询
                    _sResult = mQueryYuanGWorkData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamCXStock)
                {
                    //库存查询
                    _sResult=mQueryStockData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_SktParamSQZDBh)
                {
                    //主动补货
                    _sResult = mGegBhTaskData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamBhActiv)
                {
                    _sResult = mQueryBhDataActive(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamBhDanwei)
                {
                    _sResult = mBhCodeDaiWei(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamHwPc)
                {
                    //货位盘查
                    _sResult = mHwPcData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamHwTz)
                {
                    //货位调整
                    _sResult = mHwTzData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamHwDp)
                {
                    //动盘
                    _sResult = mHwDpData(_WorkData);
                }
                else if (_sParam[0] == MyPublic.g_sktParamDpQr)
                {
                    //动盘确认
                    _sResult = mDpQrData(_WorkData);
                }
                return _sResult;

            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mDoCPSWorkData ", sMess);
                _sResult = new string[1];
                _sResult[0] = string.Format("{0};-1;{1};", _sParam[0], sMess);
                return _sResult;
 
            }
        }




        public string[] mGetSumWorkData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');

            try
            {
                string _sSQL = "", _sRowCount = "";
                DataSet _Ds = null;
                _sSQL = " select a.QUYU_NO,PDCOUNT,LSCOUNT,ZJJHCOUNT, " +
                      "          ZTCKCOUNT,PTCKCOUNT,BDBHCOUNT,ZDBHCOUNT,RKSJCOUNT,FHHKCOUNT " +
                      "  from viw_aio_renws_an a ";
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);
                if (_Ds ==null)
                {

                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    _sResult = new string[_Ds.Tables[0].Rows.Count];
                    if (_Ds.Tables[0].Rows.Count > 0)
                    {
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["QUYU_NO"].ToString() + ";" +
                                         
                                          _Ds.Tables[0].Rows[i]["LSCOUNT"].ToString() + ";" +
                                          
                                          _Ds.Tables[0].Rows[i]["ZTCKCOUNT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["PTCKCOUNT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["BDBHCOUNT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZDBHCOUNT"].ToString() + ";" +
                                          
                                          _Ds.Tables[0].Rows[i]["FHHKCOUNT"].ToString() + ";"+
                                          _Ds.Tables[0].Rows[i]["PDCOUNT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["RKSJCOUNT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZJJHCOUNT"].ToString() + ";" ;
                        }
                    }
                    else
                    {
                        _sResult = new string[1];
                        _sResult[0] = _sParam[0] + ";" + "-1;无查询任务数据;";
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetSumWorkData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }

        public string[] mGetWorkType(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');

            try
            {

                string _sWrkName = "";
                _sWrkName = _sParam[1];
                string[,] ProcPara = new string[2, 4];
                int iRet = -1;
                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sWrkName;
                ProcPara[1, 0] = "ov_cztype";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_OutPut;
                iRet = mLmisDB.mRunProcedure("PKG_AIO_TASK.P_OBTAIN", ProcPara,ref sMess);
                _sResult = new string[1];
                if (iRet == 0)
                    _sResult[0] = _sParam[0] + ";" + "1;" + ProcPara[1, 3] + ";";
                else
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                    return _sResult;
                }

                ProcPara[1, 3] = ProcPara[1, 3].Trim();

                if (ProcPara[1, 3] == "2" ||
                    ProcPara[1, 3] == "3" ||
                    ProcPara[1, 3] == "4" ||
                    ProcPara[1, 3] == "5" ||
                    ProcPara[1, 3] == "TQ" ||
                    ProcPara[1, 3] == "5TQ")
                {
                    //判断是否需要关联
                    DataSet _Ds = null;
                    ProcPara[0, 0] = "iv_zystaff";
                    ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                    ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                    ProcPara[0, 3] = _sWrkName;
                    ProcPara[1, 0] = "oc_set";
                    ProcPara[1, 1] = clsMyAccDB.g_Oracle_Cursor;
                    ProcPara[1, 2] = clsMyAccDB.g_Oracle_OutPut;
                    _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.p_bindrq", ProcPara, ref sMess);
                    if (_Ds == null)
                    {
                        _sResult[0] = MyPublic.g_sktParamSFWGL + ";" + "-1;" + sMess + ";";
                        return _sResult;
                    }
                    if (_Ds.Tables[0].Rows.Count > 0)
                    {
                        int iFind = -1;
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            if (_Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString().Length == 0 &&
                                _Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString().Length == 0)
                            {
                                iFind = 1;
                                break;
                            }

                            if (_Ds.Tables[0].Rows[i]["ZHANT_NO"].ToString() ==
                                _Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString())
                            {
                                iFind = 1;
                                break;
                            }
                            if (_Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString().Length == 1)
                            {
                                iFind = 1;
                                break;
                            }
                        }
                        if (iFind == 1)
                            _sResult[0] = MyPublic.g_sktParamSFWGL + ";" + "1;";
                        else
                            _sResult[0] = MyPublic.g_sktParamSFWGL + ";" + "0;";
                    }
                    else
                    {
                        _sResult[0] = _sParam[0] + ";" + "0;";
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetWorkType", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mDoLoginData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {   

                string[,] ProcPara = new string[3, 4];
                DataSet _Ds=null;


                string _sSQL = "";
                //_sSQL = "delete from jc_pdazlb t where t.pdaip_addr='" +_sParam[3] + "'";

                //mLmisDB.mExecuteSQL(_sSQL);


                ProcPara[0, 0] = "iv_code";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1]; 

                ProcPara[1, 0] = "iv_parm";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];

                ProcPara[2, 0] = "oc_set";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("prc_utl_execsql", ProcPara, ref sMess);
                if (_Ds == null)
                {
                    _sResult = new string[1];
                    _sResult[0]=_sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else if (_Ds.Tables[0].Rows.Count > 0)
                {
                    _sResult = new string[1];
                    _sResult[0] = _sParam[0]+ ";" + _Ds.Tables[0].Rows[0]["col_01"].ToString() + ";" +
                                  _Ds.Tables[0].Rows[0]["col_02"].ToString() + ";" +
                                  _Ds.Tables[0].Rows[0]["col_03"].ToString() + ";";

                     _sSQL = " select a.quyu_no " +
                                   "   from jc_zhiydoc_jhy a " +
                                   "  where zhiy_name='" + _Ds.Tables[0].Rows[0]["col_03"].ToString() + "'" +
                                   "    and exists(select '1' from jc_kfbmdyb b  "+
                                   "                where a.quyu_no=b.quyu_no and b.jianxc_flg='Y') "+
                                   "  order by jianhyjh_order";
                    _Ds.Clear();
                    _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);
                    if (_Ds == null)
                        _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                    else
                    {
                        if (_Ds.Tables[0].Rows.Count > 0)
                            _sResult[0] += _Ds.Tables[0].Rows[0]["quyu_no"].ToString() + ";";
                        else
                            _sResult[0] = _sParam[0] + ";" + "-1;无小车拣货权限;";

                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mDoLoginData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }


        public string[] mDoLoginOutData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {

                string[,] ProcPara = new string[7, 4];
                ProcPara[0, 0] = "iv_code";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_parm";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];
                ProcPara[2, 0] = "ov_var1";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[2, 3] = "";
                ProcPara[3, 0] = "ov_var2";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[3, 3] = "";
                ProcPara[4, 0] = "ov_var3";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[4, 3] = "";
                ProcPara[5, 0] = "ov_var4";
                ProcPara[5, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[5, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[5, 3] = "";
                ProcPara[6, 0] = "ov_var5";
                ProcPara[6, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[6, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[6, 3] = "";
                int iRet = mLmisDB.mRunProcedure("PRC_UTL_EXECVAR", ProcPara, ref sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "1;1;";
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mDoLoginOutData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }



        public string[] mGetPickWorkData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            bool _isBoxIsNull = false;

            _sParam = _WorkData.Split(';');

            try
            {
                //获取当前拣货员的已经索取的拣货任务且未完成
                string _sWrkName = "";
                _sWrkName = _sParam[1];
                DataSet _Ds = null;
                string[,] ProcPara = new string[2, 4];
                string _sRowCount = "0";
                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sWrkName;
                ProcPara[1, 0] = "oc_set";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.P_OBTAIN_DISP", ProcPara, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    if (_Ds.Tables[0].Rows.Count == 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;您所操作的任务，正在确认中...请稍等;";
                        return _sResult;
                    }
                    /*
                    for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                    {
                        if (_Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString().Length<=0 && _Ds.Tables[0].Rows[i]["ZUOY_CATEGORY"].ToString() != "1")
                        {
                            _sResult[0] = _sParam[0] + ";" + "-1;获取的拣货任务中有周转箱信息为空，请退出进行关联;";
                            return _sResult;
                        }
                    }*/

                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    _sResult = new string[_Ds.Tables[0].Rows.Count];
                    for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                    {
                        if (_Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString().Length <= 0 && _Ds.Tables[0].Rows[i]["ZUOY_CATEGORY"].ToString() != "1")
                        {
                            _isBoxIsNull = true;
                            break;
                        }

                        _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                      _Ds.Tables[0].Rows[i]["DANJ_NO"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["HANGHAO"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["XIANS_LOC"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["CHINESE_NAME"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["YAOP_GUIG"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["BAOZ_NUM"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["BAOZ_DANW"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["LOT"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["MAKER"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["JIHUA_NUM"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["SHIJ_NUM"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["IS_TQ"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["TQ_HW"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["zhant_no"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["ZUOY_CATEGORY"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["FENPD_NO"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["ZHONGBZ"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["ZHONGBZ_CK"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["CHAIF_LID"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["QUYU_NO"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["fenpd_group"].ToString() + ";"+
                                      _Ds.Tables[0].Rows[i]["YFC_FLG"].ToString() + ";" +
                                      _Ds.Tables[0].Rows[i]["YFCQK"].ToString() + ";"+
                                      _Ds.Tables[0].Rows[i]["FUHT_NO"].ToString() + ";";
                    }
                }

                if (_isBoxIsNull)
                {
                    _sResult = new string[1];
                    _sResult[0] = _sParam[0] + ";" + "-1;获取的拣货任务中有周转箱信息为空，请退出进行关联任务;";
                    return _sResult;
                }
                return _sResult;
            }     
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetPickWorkData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }

        public string[] mGetPickBoxData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');

            try
            {
                DataSet _Ds = null;

                string _sFenpGroup = _sParam[1];
                string _sAreaNo = _sParam[2];
                //获取周转箱分配单数据
                string _sSQL = " select tb_fpd.fenpd_no,tb_fpd.zhant_no,tb_fpd.zhouzx_no,tb_zl.jianxl_no,tb_fpd.huow_addr,count(tb_zl.hanghao) rwsl " +
                          "   from  " +
                          "   (select distinct a.fenpd_no,a.zhant_no,a.quyu_no,nvl(a.zhouzx_no,a.jianxl_no)zhouzx_no, " +
                          "           SUBSTR(b.CENG,2,1)||SUBSTR(b.LIE,2,1)||SUBSTR(c.CENG,2,1)||SUBSTR(c.LIE,2,1) huow_addr " +
                          "     from yw_xjzl a " +
                          "     left join jc_hwzd b on substr(a.huow_buffer,1,11)=b.huow_id " +
                          "     left join jc_hwzd c on substr(a.huow_buffer,13,11)=c.huow_id  " +
                          "   where a.fenpd_group = '" + _sFenpGroup + "' and a.quyu_no='" + _sAreaNo + "') tb_fpd left join yw_xjzl tb_zl  " +
                          "   on tb_fpd.fenpd_no=tb_zl.fenpd_no " +
                          "     and tb_fpd.quyu_no=tb_zl.quyu_no " +
                          "    and tb_zl.renw_state='C4' " +
                          "   group by tb_fpd.fenpd_no,tb_fpd.zhant_no,tb_zl.jianxl_no,tb_fpd.zhouzx_no,tb_fpd.huow_addr " +
                          "   order by tb_fpd.zhant_no";
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);

                _sResult=new string[1];

                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {

                    if (_Ds.Tables[0].Rows.Count == 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;当前员工无未完成的任务，请索取任务;";
                    }
                    else
                    {
                        string _sRowCount = (_Ds.Tables[0].Rows.Count).ToString();
                        _sResult=new string[_Ds.Tables[0].Rows.Count];
                        for (int k =0; k < _Ds.Tables[0].Rows.Count; k++)
                        {
                            _sResult[k] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[k]["zhant_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[k]["fenpd_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[k]["zhouzx_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[k]["jianxl_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[k]["huow_addr"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[k]["rwsl"].ToString() + ";";
                        }
                    }
                }
                return _sResult;                
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetPickBoxData", sMess);
                _sResult=new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }



        public string[] mGetProTask(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
                
            try
            {
                ///////
                string _sWrkName = "";
                string _sRowCount = "";
                _sWrkName = _sParam[1];
                DataSet _Ds = null;
                string[,] ProcPara = new string[2, 4];
                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sWrkName;
                ProcPara[1, 0] = "oc_set";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.p_bindrq", ProcPara, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count > 0)
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["FENPD_NO"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZUOY_LB"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZHANT_NO"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString() + ";"+
                                          _Ds.Tables[0].Rows[i]["QUYU_NO"].ToString() + ";";                                            
                        }
                    }
                    else
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;无可关联任务;";
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetProTask", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mMatchTask(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
  
            try
            {
                string[,] ProcPara = new string[5, 4];
                string _sZylb = "";
                _sZylb = _sParam[3];
                int iRet = -1;

                //判断此分配单是否已经关联
                string _sFpdNo = _sParam[2];
                string _sRqNo = _sParam[1];
                string _sSQL = " select a.bianhao from jc_rqzlb a " +
                               "  where a.rongq_type='2' " +
                               "    and a.rongq_state='1' " +
                               "    and a.fenpd_no='" + _sFpdNo + "'";
                DataSet _Ds = null;
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);
                _sResult = new string[1];
                if (_Ds != null)
                {
                    if (_Ds.Tables[0].Rows.Count > 0)
                    {
                        if (_Ds.Tables[0].Rows[0]["bianhao"].ToString() == _sRqNo)
                        {
                            _sResult[0] = _sParam[0] + ";1;";
                        }
                        else
                        {
                            _sResult[0] = _sParam[0] + ";-1;此分配单已关联周转箱【" + _Ds.Tables[0].Rows[0]["bianhao"].ToString() + "】请再次扫描此周转箱;";
                        }
                    }
                }
                ProcPara[0, 0] = "iv_rqno";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1]; 
                ProcPara[1, 0] = "iv_fpdh";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2]; 
                ProcPara[2, 0] = "iv_zycate";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[2, 3] = _sParam[3];
                ProcPara[3, 0] = "iv_zyry";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[3, 3] = _sParam[4];
                ProcPara[4, 0] = "iv_quyu";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[4, 3] = _sParam[5];
                iRet = mLmisDB.mRunProcedure("pkg_aio_task.p_match", ProcPara, ref sMess);
                if (iRet == -1)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else if (iRet == 0)
                    _sResult[0] = _sParam[0] + ";1;";
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mMatchTask", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mConfirgWorkData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');

            try
            {

                string m_sCurDanjNo = "", m_sCurHangNo = "", m_sZuoyCateType = "", m_sCurJhnum = "", m_UserName = "";
                string m_sCurXshw = "";

               

                m_sZuoyCateType = _sParam[1];
                m_sCurDanjNo = _sParam[2];
                m_sCurHangNo = _sParam[3];
                m_sCurJhnum = _sParam[4];
                m_UserName = _sParam[5];
                m_sCurXshw = _sParam[6];

                sMess = "拣货确认开始：" + m_UserName + " " + m_sCurDanjNo + " " + m_sCurHangNo + " " + m_sCurJhnum + " " + m_sCurXshw;
                MyPublic.gWriteLog(MyPublic.g_LogTime, m_sClsName, "mConfirgWorkData", sMess);
/*
                string sSQL = string.Format("select * from yw_pad_jhqrdata a " +
                                    " where a.danj_no='{0}' and a.hanghao ='{1}'", m_sCurDanjNo, m_sCurHangNo);
                DataSet _Ds = mLmisDB.mOpenDataSet(sSQL, ref sMess);

                int iRet = 0;

                if (_Ds.Tables[0].Rows.Count == 1)
                    iRet = 1;
                else
                {
                    sSQL = " insert into yw_pad_jhqrdata(danj_no,hanghao,huow_id, " +
                           " shij_num,zuoy_category,zhiy_name,CRT_DATE,do_status)" +
                           " values('" + m_sCurDanjNo + "','" + m_sCurHangNo + "','" +
                                         m_sCurXshw + "'," + m_sCurJhnum + ",'" +
                                         m_sZuoyCateType + "','" +m_UserName + "'," +
                                        " to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'0')";

                    iRet = mLmisDB.mExecuteSQL(sSQL);
                }
                */
                string[,] ProcPara = new string[6, 4];
                int iRet = -1;
                ProcPara[0, 0] = "iv_cztype";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = m_sZuoyCateType;
                ProcPara[1, 0] = "iv_danjno";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = m_sCurDanjNo;
                ProcPara[2, 0] = "in_rowno";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[2, 3] = m_sCurHangNo;
                ProcPara[3, 0] = "in_num";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[3, 3] = m_sCurJhnum;
                ProcPara[4, 0] = "iv_zystaff";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[4, 3] = m_UserName;
                ProcPara[5, 0] = "iv_xshw";
                ProcPara[5, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[5, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[5, 3] = m_sCurXshw;
                iRet = mLmisDB.mRunProcedure("pkg_aio_task.P_CONFIRM", ProcPara, ref sMess);

                _sResult=new string[1];
                if (iRet <0)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;确认失败 " + sMess + ";";
                }
                else 
                    _sResult[0] = _sParam[0] + ";" + "1;" + m_sCurDanjNo + ";" +m_sCurHangNo + ";";

                sMess = "拣货确认结束：" + m_UserName + " " + m_sCurDanjNo + " " + m_sCurHangNo + " " + m_sCurJhnum + " " + m_sCurXshw;
                MyPublic.gWriteLog(MyPublic.g_LogTime, m_sClsName, "mConfirgWorkData", sMess);
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mConfirgWorkData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;

            }
        }
        public string[] mQueryBoxTaskData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "";
                DataSet _Ds = null;
                string[,] ProcPara = new string[3, 4];
                ProcPara[0, 0] = "iv_fenpd_no";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_quyu_no";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];
                ProcPara[2, 0] = "oc_set";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.P_ZZXRWCX", ProcPara, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0]= _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "0;当前员工无未完成的任务，请索取任务;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["XIANS_LOC"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["CHINESE_NAME"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["YAOP_GUIG"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["JIHUA_NUM"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["SHIJ_NUM"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZHONGBZ"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["ZHONGBZ_CK"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["LOT"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["MAKER"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["RENW_STATE"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["IS_TQ"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString() + ";";
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mQueryBoxTaskData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }

        }
        public string[] mQueryYuanGWorkData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "";
                DataSet _Ds = null;
                string[,] ProcPara = new string[5, 4];
                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_begdate";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];
                ProcPara[2, 0] = "iv_enddate";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[2, 3] = _sParam[3];
                ProcPara[3, 0] = "iv_rqbh";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[3, 3] = _sParam[4];
                ProcPara[4, 0] = "oc_set";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.P_ZYJLCX", ProcPara, ref sMess);
                _sResult=new string[1];
                if (_Ds == null)
                {
                    _sResult[0]= _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;当前员工无未完成的任务，请索取任务;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                            (i + 1) + ";" +
                                            _Ds.Tables[0].Rows[i]["ZHOUZX_NO"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["XIANS_LOC"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["CHINESE_NAME"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["SHIJ_NUM"].ToString() + ";" +

                                            
                                             _Ds.Tables[0].Rows[i]["LOT"].ToString() + ";" +

                                            
                                            _Ds.Tables[0].Rows[i]["DANJ_NO"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["ZHID_CONTENT"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["SHANGP_NO"].ToString() + ";" +
                                            
                                            _Ds.Tables[0].Rows[i]["MAKER"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["BAOZ_DANW"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["BAOZ_NUM"].ToString() + ";" +



                                            _Ds.Tables[0].Rows[i]["JIANXL_NO"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["JIANS"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["LINGS_NUM"].ToString() + ";" +
                                            
                                            _Ds.Tables[0].Rows[i]["HUOW_ADDR"].ToString() + ";"+
                                            _Ds.Tables[0].Rows[i]["ZUOYWC_DATE"].ToString() + ";" ;
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mQueryYuanGWorkData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mGegBhTaskData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "";
                DataSet _Ds = null;
                string[,] ProcPara = new string[4, 4];
                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_barcode";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2].ToUpper();
                ProcPara[2, 0] = "IV_ZYTYPE";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[2, 3] = _sParam[3];
                ProcPara[3, 0] = "oc_set";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_OutPut;
                _Ds = mLmisDB.mGetTaskDataSet("PKG_AIO_TASK.p_getbhzydata", ProcPara, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0]= _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0]= _sParam[0] + ";" + "-1;当前员工无未完成的任务，请索取任务;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                            _Ds.Tables[0].Rows[i]["xians_loc"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["sj_js"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["chinese_name"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["maker"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["yaop_guig"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["lot"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["baoz_num"].ToString() +
                                            _Ds.Tables[0].Rows[i]["baoz_danw"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["shengchan_date"].ToString() + " 效期 " +
                                            _Ds.Tables[0].Rows[i]["youx_date"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["liush_barcode"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["danj_no"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["hanghao"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["zuoy_category"].ToString() + ";";
                        }
                    }

                }

                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGegBhTaskData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mQueryStockData(string _WorkData)
       {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "",_sSQL="",_sWhere="";
                DataSet _Ds = null;
                _sWhere=_sParam[1];
                _sWhere = _sWhere.Replace("-", "");
                _sSQL = "select c.xians_loc,b.shangp_no,b.chinese_name,b.baoz_danw,b.baoz_num,a.lot, " +
                      "         b.yaop_guig,b.maker,d.zhid_content kuc_state,(a.num-mod(a.num,b.baoz_num))/b.baoz_num zjsl, " +
                      "         a.num lssl,a.rukyz_num,a.chukyk_num,a.buhyz_num, " +
                      "         a.buhyk_num,a.zait_num,a.suod_flg " +
                      "    from kc_spphhw a,jc_spzl b,jc_hwzd c,jc_zdwh_mx d " +
                      "   where a.yez_id=b.yez_id " +
                      "     and a.shangp_id=b.shangp_id " +
                      "     and a.huow_id=c.huow_id " +
                      "     and c.kub='LHK' " +
                      "     and d.english_name='KUC_STATE' and d.zhidz=a.kuc_state" +
                      "     and rownum<=20" +
                      "     and (replace(c.xians_loc,'-','') like '%" + _sWhere.ToUpper() + "%'" +
                      "          or c.xians_loc like '%" + _sWhere.ToUpper() + "%'" +                 //20170314 whstruts 长春增加
                      "          or b.zhuj_code like '%" + _sWhere.ToUpper() + "%')" +
                      "  order by c.xians_loc,b.shangp_no";
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;无库存数据;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["xians_loc"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shangp_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["chinese_name"].ToString() + ";" +
                                           _Ds.Tables[0].Rows[i]["lssl"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["yaop_guig"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["baoz_danw"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["baoz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["maker"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["lot"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["kuc_state"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["zjsl"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["rukyz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["chukyk_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["buhyz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["buhyk_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["zait_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["suod_flg"].ToString() + ";";
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mQueryStockData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }
        public string[] mBhCodeDaiWei(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string[,] ProcPara = new string[1, 4];
                int iRet = -1;
                ProcPara[0, 0] = "iv_lsbar";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                iRet = mLmisDB.mRunProcedure("PKG_WCS_MCS.P_ARRIVAL", ProcPara, ref sMess);
                _sResult = new string[1];
                if (iRet == -1)
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                else if (iRet == 0)
                    _sResult[0] = _sParam[0]+ ";1;ok;";
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mBhCodeDaiWei", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;

            }
        }

        public string[] mHwPcData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "", _sSQL = "", _sWhere = "";
                DataSet _Ds = null;
                _sWhere = _sParam[1];
                _sWhere = _sWhere.Replace("-", "");
                _sSQL = "select c.xians_loc,b.shangp_no,b.shangp_id,b.chinese_name,b.baoz_danw,b.zhongbz,a.lot, " +
                      "         b.yaop_guig,b.maker,d.zhid_content kuc_state,(a.num-mod(a.num,b.baoz_num))/b.baoz_num zjsl, " +
                      "         a.num lssl,a.rukyz_num,a.chukyk_num,a.buhyz_num, " +
                      "         a.buhyk_num,a.zait_num,a.suod_flg ,to_char(e.shengchan_date,'yyyy-mm-dd') shengchan_date, to_char(e.youx_date,'yyyy-mm-dd') youx_date " +
                      "    from kc_spphhw a,jc_spzl b,jc_hwzd c,jc_zdwh_mx d ,jc_phwhb e" +
                      "   where a.yez_id=b.yez_id " +
                      "     and a.shangp_id=b.shangp_id " +
                      "     and a.huow_id=c.huow_id " +
                      "     and a.shangp_id = e.shangp_id and a.lot = e.lot "+
                      "     and c.kub='LHK' " +
                      "     and d.english_name='KUC_STATE' and d.zhidz=a.kuc_state" +
                      "     and rownum <= 1" +
                      "     and (replace(c.xians_loc,'-','') like '%" + _sWhere.ToUpper() + "%'" +
                      "          or b.zhuj_code like '%" + _sWhere.ToUpper() + "%'" +
                      "          or c.xians_loc like '%" + _sWhere.ToUpper() + "%')" +
                      "  order by c.xians_loc,b.shangp_no";
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);
                _sResult = new string[1];
                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;无库存数据;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["xians_loc"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shangp_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["chinese_name"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["yaop_guig"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["baoz_danw"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["zhongbz"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["maker"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["lot"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["lssl"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shengchan_date"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["youx_date"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shangp_id"].ToString() + ";" +

                                          _Ds.Tables[0].Rows[i]["kuc_state"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["zjsl"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["rukyz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["chukyk_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["buhyz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["buhyk_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["zait_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["suod_flg"].ToString() + ";";
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mQueryStockData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }

        public string[] mHwDpData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {
                string _sRowCount = "";
                DataSet _Ds = null;
                string[,] ProcPara = new string[2, 4];
                ProcPara[0, 0] = "iv_xians_loc";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];

                ProcPara[1, 0] = "oc_set";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Cursor;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_OutPut;

                _Ds = mLmisDB.mGetTaskDataSet("pkg_aio_task.p_pd", ProcPara, ref sMess);
                _sResult = new string[1];

                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "-1;无动盘数据;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                          _Ds.Tables[0].Rows[i]["xians_loc"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shangp_no"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["chinese_name"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["yaop_guig"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["baoz_danw"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["baoz_num"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["maker"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["lot"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["NUM"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["shengchan_date"].ToString() + ";" +
                                          _Ds.Tables[0].Rows[i]["youx_date"].ToString() + ";";
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mHwDpData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }

        private string[] mDpQrData(string _WorkData)
        {
        string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;
            _sParam = _WorkData.Split(';');
            try
            {

                string[,] ProcPara = new string[7, 4];
                ProcPara[0, 0] = "iv_code";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_parm";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];
                ProcPara[2, 0] = "ov_var1";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[2, 3] = "";
                ProcPara[3, 0] = "ov_var2";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[3, 3] = "";
                ProcPara[4, 0] = "ov_var3";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[4, 3] = "";
                ProcPara[5, 0] = "ov_var4";
                ProcPara[5, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[5, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[5, 3] = "";
                ProcPara[6, 0] = "ov_var5";
                ProcPara[6, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[6, 2] = clsMyAccDB.g_Oracle_OutPut;
                ProcPara[6, 3] = "";
                int iRet = mLmisDB.mRunProcedure("PRC_UTL_EXECVAR", ProcPara, ref sMess);
                _sResult = new string[1];
                if (iRet == -1)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sResult[0] = _sParam[0] + ";" + "1;1;";
                }                
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mDoLoginOutData", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }

        public string[] mHwTzData(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;

            _sParam = _WorkData.Split(';');

            try
            {
                int iRet = -1;

                string[,] ProcPara = new string[6, 4];

                ProcPara[0, 0] = "iv_zystaff";
                ProcPara[0, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[0, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[0, 3] = _sParam[1];
                ProcPara[1, 0] = "iv_shangp_id";
                ProcPara[1, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[1, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[1, 3] = _sParam[2];
                ProcPara[2, 0] = "iv_lot";
                ProcPara[2, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[2, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[2, 3] = _sParam[3];
                ProcPara[3, 0] = "iv_xians_loc_s";
                ProcPara[3, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[3, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[3, 3] = _sParam[4];
                ProcPara[4, 0] = "iv_xians_loc_t";
                ProcPara[4, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[4, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[4, 3] = _sParam[5];
                ProcPara[5, 0] = "in_num";
                ProcPara[5, 1] = clsMyAccDB.g_Oracle_Char;
                ProcPara[5, 2] = clsMyAccDB.g_Oracle_InPut;
                ProcPara[5, 3] = _sParam[6];
                iRet = mLmisDB.mRunProcedure("pkg_aio_task.P_HWTZCONFIRM", ProcPara, ref sMess);

                _sResult = new string[1];
                if (iRet == -1)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else if (iRet == 0)
                    _sResult[0] = _sParam[0] + ";1;";
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mMatchTask", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }


        public string[] mQueryBhDataActive(string _WorkData)
        {
            string sMess = "";
            string[] _sParam = null;
            string[] _sResult = null;

            _sParam = _WorkData.Split(';');

            try
            {
                string _sSQL = "";
                string _sRowCount = "";
                DataSet _Ds = null;


                _sSQL = "select a.liush_barcode,b.zhid_content zylb,c.quyu_no,c.xians_loc " +
                      "  from yw_sjzl a,jc_zdwh_mx b,jc_hwzd c  " +
                      " where a.zuoy_category in('6','7') " +
                      "   and b.english_name='ZUOY_CATEGORY' " +
                      "   and b.zhidz=a.zuoy_category " +
                      "   and a.shijhw_id=c.huow_id " +
                      "   AND A.RENW_STATE < 'C5' " +
                      "   and c.shusx_no='" + _sParam[1] + "'";
                _Ds = mLmisDB.mOpenDataSet(_sSQL, ref sMess);

                _sResult = new string[1];

                if (_Ds == null)
                {
                    _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                }
                else
                {
                    _sRowCount = _Ds.Tables[0].Rows.Count.ToString();
                    if (_Ds.Tables[0].Rows.Count <= 0)
                    {
                        _sResult[0] = _sParam[0] + ";" + "0;无补货数据;";
                    }
                    else
                    {
                        _sResult = new string[_Ds.Tables[0].Rows.Count];
                        for (int i = 0; i < _Ds.Tables[0].Rows.Count; i++)
                        {
                            _sResult[i] = _sParam[0] + ";" + _sRowCount + ";" +
                                            _Ds.Tables[0].Rows[i]["liush_barcode"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["zylb"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["quyu_no"].ToString() + ";" +
                                            _Ds.Tables[0].Rows[i]["xians_loc"].ToString() + ";";
                        }
                    }
                }
                return _sResult;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mQueryBhDataActive", sMess);
                _sResult = new string[1];
                _sResult[0] = _sParam[0] + ";" + "-1;" + sMess + ";";
                return _sResult;
            }
        }


    }

}
