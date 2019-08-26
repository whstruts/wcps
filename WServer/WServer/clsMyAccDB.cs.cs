using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Security.Cryptography;
using System.IO;
using System.Xml;
using System.Xml.Linq;
using System.Reflection;
using System.Data.OracleClient;
using System.Data.OleDb;

namespace WServer
{
    public class clsMyAccDB
    {
        public static string m_sClsName = "MyPublic";

        //定义数据库连接

        public static string g_Oracle_Char = "1";   //字符类型
        public static string g_Oracle_Int = "2";    //数字类型
        public static string g_Oracle_Cursor = "3";  //返回游标
        public static string g_Oracle_InPut = "1";  //输入类型
        public static string g_Oracle_OutPut = "2";  //输出类型



        public  OracleConnection m_oraCon = new OracleConnection();      //数据库连接
        public  OracleTransaction m_oraTact = null;                      //数据库事务
        public  OracleCommand m_oraCmd = new OracleCommand();
        public  OracleCommand m_oraPro = new OracleCommand();
        public  string m_DbCnntStr = null;  //数据库连接字符串



        //定义数据库连接函数
        public  int mConnectDB( ref string _errMess)
        {
            try
            {
                if (m_oraCon.State == ConnectionState.Open)
                    m_oraCon.Close();
                m_oraCon.ConnectionString = m_DbCnntStr;
                m_oraCon.Open();
                return 0;
            }
            catch (System.Exception ex)
            {
                _errMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mConnectWCSDB" + m_DbCnntStr, _errMess);
                return -1;
            }
        }

        //打开数据集
        public  DataSet mOpenDataSet(string _SQL,ref string _errMess)
        {
            string sMess = null;
            try
            {
                //新建一个DataAdapter用于填充DataSet
                OracleDataAdapter oraDap = new OracleDataAdapter(_SQL, m_oraCon);
                //新建一个DataSet
                DataSet ds = new DataSet();
                //填充DataSet
                oraDap.Fill(ds);
                return ds;
            }
            catch (System.Exception ex)
            {
                _errMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mOpenDataSetStatic", sMess + _SQL);
                mConnectDB(ref _errMess);
                return null;
            }
        }

        //写数据
        public  int mExecuteSQL(string _SQL)
        {
            string sMess = null;
            try
            {
                int iRow = 0;
                m_oraCmd.CommandText = _SQL;
                m_oraCmd.Connection = m_oraCon;
                iRow = m_oraCmd.ExecuteNonQuery();
                return iRow;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mExecuteSQL", sMess);
                return -1;
            }
        }

        //开始事务
        public  int mBeginTran()
        {
            string sMess = null;
            try
            {
                m_oraTact = m_oraCon.BeginTransaction();
                m_oraCmd.Transaction = m_oraTact;
                m_oraPro.Transaction = m_oraTact;
                return 0;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mBeginTran", sMess);
                return -1;
            }
        }
        //提交事务
        public  int mCommitTran()
        {
            string sMess = null;
            try
            {
                m_oraTact.Commit();
                return 0;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mBeginTran", sMess);
                return -1;
            }
        }

        //回滚事务
        public int mRollbackTran()
        {
            string sMess = null;
            try
            {
                m_oraTact.Rollback();
                return 0;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mRollbackTran", sMess);
                return -1;
            }
        }

        //执行存储过程
        public int mRunProcedure(string _PreName, string[,] _VarrData, ref string _errMess)
        {
            try
            {
                int iArrCount = -1, iRow = -1, i = 0;
                OracleParameter[] vParam = null;
                m_oraPro.Connection = m_oraCon;
                m_oraPro.CommandText = _PreName;
                m_oraPro.CommandType = CommandType.StoredProcedure;
                iArrCount = _VarrData.GetLength(0);
                vParam = new OracleParameter[iArrCount];
                m_oraPro.Parameters.Clear();
                for (i = 0; i < iArrCount; i++)
                {
                    //字段输入/输出
                    if (_VarrData[i, 2] == g_Oracle_InPut)
                    {
                        //字段类型
                        if (_VarrData[i, 1] == g_Oracle_Char)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.VarChar, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Int)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Float, 1000);
                        vParam[i].Direction = ParameterDirection.Input;
                        //字段值
                        vParam[i].Value = _VarrData[i, 3];
                    }
                    else if (_VarrData[i, 2] == g_Oracle_OutPut)
                    {
                        //字段类型
                        if (_VarrData[i, 1] == g_Oracle_Char)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.VarChar, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Int)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Float, 1000);
                        vParam[i].Direction = ParameterDirection.Output;
                    }
                    m_oraPro.Parameters.Add(vParam[i]);
                }
                //执行存储过程
                iRow = m_oraPro.ExecuteNonQuery();
                for (i = 0; i < iArrCount; i++)
                {
                    if (_VarrData[i, 2] == g_Oracle_OutPut)
                    {
                        _VarrData[i, 3] = vParam[i].Value.ToString();
                    }
                }
                return 0;
            }
            catch (System.Exception ex)
            {
                _errMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mRunProcedure", _errMess);
                mConnectDB(ref _errMess);
                return -1;
            }
        }


        public  DataSet mGetTaskDataSet(string _PreName, string[,] _VarrData,ref string _errMess)
        {
            try
            {
                int iArrCount = -1, i = 0;
                OracleParameter[] vParam = null;
                m_oraPro.Connection = m_oraCon;
                m_oraPro.CommandText = _PreName;
                m_oraPro.CommandType = CommandType.StoredProcedure;
                iArrCount = _VarrData.GetLength(0);
                vParam = new OracleParameter[iArrCount];
                m_oraPro.Parameters.Clear();
                for (i = 0; i < iArrCount; i++)
                {
                    //字段输入/输出
                    if (_VarrData[i, 2] == g_Oracle_InPut)
                    {
                        //字段类型
                        if (_VarrData[i, 1] == g_Oracle_Char)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.VarChar, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Int)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Float, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Cursor)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Cursor, 1000);
                        vParam[i].Direction = ParameterDirection.Input;
                        //字段值
                        vParam[i].Value = _VarrData[i, 3];
                    }
                    else if (_VarrData[i, 2] == g_Oracle_OutPut)
                    {
                        //字段类型
                        if (_VarrData[i, 1] == g_Oracle_Char)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.VarChar, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Int)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Float, 1000);
                        else if (_VarrData[i, 1] == g_Oracle_Cursor)
                            vParam[i] = new OracleParameter(_VarrData[i, 0], OracleType.Cursor, 1000);
                        vParam[i].Direction = ParameterDirection.Output;
                    }
                    m_oraPro.Parameters.Add(vParam[i]);
                }
                //执行存储过程               
                DataSet ds = new DataSet();
                OracleDataAdapter da = new OracleDataAdapter(m_oraPro);
                da.Fill(ds);
                return ds;
            }
            catch (System.Exception ex)
            {
                _errMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mRunProcedure", _errMess);
                mConnectDB(ref _errMess);
                return null;
            }
        }
       
    }
}
