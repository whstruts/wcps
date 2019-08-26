using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace WServer
{
    public partial class FrmServer : Form
    {
        private string m_sClsName = "FrmServer";
        private DataSet m_dsMain;
        private DataSet m_dsDetail;
        private int m_CurMainRowIndex = -1;
        private int m_CurDetailRowIndex = -1;
        private int m_UpdateStatus = -1; //1 添加 2 修改 3 删除 

        public FrmServer()
        {
            InitializeComponent();
        }

        private void bt_Exit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void FrmServer_Load(object sender, EventArgs e)
        {
            DataGridViewTextBoxColumn dgc_Txt0 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(0, dgc_Txt0); 
            dg_MainData.Columns[0].HeaderText = "分组编号";
            dg_MainData.Columns[0].DataPropertyName = "GROUPNO";
            dg_MainData.Columns[0].Width = 100;
            dg_MainData.Columns[0].ReadOnly = true;
            dg_MainData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt1 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(1, dgc_Txt1);
            dg_MainData.Columns[1].HeaderText = "IP地址";
            dg_MainData.Columns[1].DataPropertyName = "IP";
            dg_MainData.Columns[1].Width = 150;
            dg_MainData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt2 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(2, dgc_Txt2);
            dg_MainData.Columns[2].HeaderText = "端口号";
            dg_MainData.Columns[2].DataPropertyName = "PORT";
            dg_MainData.Columns[2].Width = 100;
            dg_MainData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt3 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(3, dgc_Txt3);
            dg_MainData.Columns[3].HeaderText = "描述";
            dg_MainData.Columns[3].DataPropertyName = "mark";
            dg_MainData.Columns[3].Width = 150;
            dg_MainData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt4 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(4, dgc_Txt4);
            dg_MainData.Columns[4].HeaderText = "连接数";
            dg_MainData.Columns[4].DataPropertyName = "cnntnum";
            dg_MainData.Columns[4].Width = 100;
            dg_MainData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;


            DataGridViewTextBoxColumn dgc_Txt5 = new DataGridViewTextBoxColumn();
            dg_MainData.Columns.Insert(5, dgc_Txt5);
            dg_MainData.Columns[5].HeaderText = "IP编号";
            dg_MainData.Columns[5].DataPropertyName = "ipno";
            dg_MainData.Columns[5].Width = 100;
            dg_MainData.Columns[5].Visible = false;

            DataGridViewTextBoxColumn dgc_Txt20 = new DataGridViewTextBoxColumn();
            dg_DetailData.Columns.Insert(0, dgc_Txt20);
            dg_DetailData.Columns[0].HeaderText = "分组编号";
            dg_DetailData.Columns[0].DataPropertyName = "GROUPNO";
            dg_DetailData.Columns[0].Width = 100;
            dg_DetailData.Columns[0].ReadOnly = true;
            dg_DetailData.Columns[0].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt21 = new DataGridViewTextBoxColumn();
            dg_DetailData.Columns.Insert(1, dgc_Txt21);
            dg_DetailData.Columns[1].HeaderText = "客户端IP";
            dg_DetailData.Columns[1].DataPropertyName = "IP";
            dg_DetailData.Columns[1].Width = 150;
            dg_DetailData.Columns[1].SortMode = DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt22 = new DataGridViewTextBoxColumn();
            dg_DetailData.Columns.Insert(2, dgc_Txt22);
            dg_DetailData.Columns[2].HeaderText = "描述";
            dg_DetailData.Columns[2].DataPropertyName = "MARK";
            dg_DetailData.Columns[2].Width = 200;
            dg_DetailData.Columns[2].SortMode=DataGridViewColumnSortMode.NotSortable;

            DataGridViewComboBoxColumn dgc_Txt23 = new DataGridViewComboBoxColumn();
            dgc_Txt23.Items.Add("DPS");
            dgc_Txt23.Items.Add("MCS");
            dgc_Txt23.Items.Add("ECS");
            dgc_Txt23.Items.Add("PDA");
            dgc_Txt23.Items.Add("CPS");
            dgc_Txt23.Items.Add("PRN");
            dg_DetailData.Columns.Insert(3, dgc_Txt23);
            dg_DetailData.Columns[3].HeaderText = "类型";
            dg_DetailData.Columns[3].DataPropertyName = "WTYPE";
            dg_DetailData.Columns[3].Width = 100;
            dg_DetailData.Columns[3].SortMode=DataGridViewColumnSortMode.NotSortable;

            DataGridViewTextBoxColumn dgc_Txt24 = new DataGridViewTextBoxColumn();
            dg_DetailData.Columns.Insert(4, dgc_Txt24);
            dg_DetailData.Columns[4].HeaderText = "IP编号";
            dg_DetailData.Columns[4].DataPropertyName = "ipno";
            dg_DetailData.Columns[4].Width = 100;
            dg_DetailData.Columns[4].Visible = false;
            dg_DetailData.Columns[4].SortMode=DataGridViewColumnSortMode.NotSortable;

            mIniData();
        }
     
        private void mIniData()
        {
            string sMess = null;
            try
            {
                string sSQL = null;
                sSQL = " select t.groupno,t.ip,t.Port,mark,t.cnntnum,t.ipno from wcs_ip t where t.type='S' and t.groupno='" + MyPublic.m_SysGroupNo + "'";
                m_dsMain = FrmLogin.m_LWcsDB.mOpenDataSet(sSQL,ref sMess);
                this.dg_MainData.DataSource = m_dsMain.Tables[0];

                if (m_dsMain.Tables[0].Rows.Count == 0)
                {
                    ts_AddSvr.Enabled = true;
                    ts_EditSvr.Enabled = false;
                }
                else
                    ts_AddSvr.Enabled = false;

                sSQL = " select t.groupno,t.ip,t.MARK,t.WTYPE,ipno from wcs_ip t " +
                       "  where t.type='C' and t.groupno='" + MyPublic.m_SysGroupNo + "'" +
                       "  order by WTYPE,ip";
                m_dsDetail = FrmLogin.m_LWcsDB.mOpenDataSet(sSQL, ref sMess);
                this.dg_DetailData.DataSource = m_dsDetail.Tables[0];
                if (m_dsDetail.Tables[0].Rows.Count==0)
                {
                    ts_EditCln.Enabled=false;
                }
                
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mIniServerParam", sMess);
            }
        }

        private void ts_Exit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void ts_Add_Click(object sender, EventArgs e)
        {
            //如果服务端参数为0，则先添加服务数据
            if (dg_MainData.Rows.Count == 0)
            {
                dg_DetailData.Enabled = false;
                dg_MainData.Enabled = true;
                dg_MainData.SelectionMode = DataGridViewSelectionMode.CellSelect;
                m_UpdateStatus = 11;
            }
            else
                return;
            ts_EditSvr.Enabled = false;
            ts_AddCln.Enabled = false;
            ts_AddSvr.Enabled = false;
            ts_EditCln.Enabled = false;
            ts_Save.Enabled = true;
            m_dsMain.Tables[0].Rows.Add();
            dg_MainData.Rows[0].Cells[0].Value = MyPublic.m_SysGroupNo;
            dg_MainData.EndEdit();
            m_CurMainRowIndex = dg_MainData.Rows.Count - 1;

        }

        private void ts_Save_Click(object sender, EventArgs e)
        {
            string sMess = "";
            try
            {
                string _sSQL = "";
                int iRow = 0;
                if (m_UpdateStatus == 11 || m_UpdateStatus==12)
                {
                    //保存服务端数据
                    dg_MainData.EndEdit();
                    if (dg_MainData.Rows[0].Cells[0].Value.ToString().Length == 0 ||
                        dg_MainData.Rows[0].Cells[1].Value.ToString().Length == 0 ||
                        dg_MainData.Rows[0].Cells[2].Value.ToString().Length == 0 ||
                        dg_MainData.Rows[0].Cells[3].Value.ToString().Length == 0 ||
                        dg_MainData.Rows[0].Cells[4].Value.ToString().Length == 0)
                        return;
                    if(m_UpdateStatus==11)
                    {
                        _sSQL = " insert into wcs_ip(ip,type,port,srvip,mark,ustatus,groupno,cnntnum,wtype,ipno) " +
                              " select '" + dg_MainData.Rows[0].Cells[1].Value.ToString() + "'," +
                              "'S'," +
                              "'" + dg_MainData.Rows[0].Cells[2].Value.ToString() + "'," +
                              "'-1',"+
                              "'" + dg_MainData.Rows[0].Cells[3].Value.ToString() + "'," +
                              "'1',"+
                              "'" + dg_MainData.Rows[0].Cells[0].Value.ToString() + "'," +
                              " " + dg_MainData.Rows[0].Cells[4].Value.ToString() + "," +
                              "'-1',"+
                              "(select nvl(max(ipno),0)+1 from wcs_ip) ipno from dual ";
                        iRow = FrmLogin.m_LWcsDB.mExecuteSQL(_sSQL);
                    }
                    else if (m_UpdateStatus==12)
                    {
                        _sSQL=" update wcs_ip set " +
                              "           ip='" + dg_MainData.Rows[0].Cells[1].Value.ToString() + "'," +
                              "         port='"  + dg_MainData.Rows[0].Cells[2].Value.ToString() + "'," +
                              "         mark='"  + dg_MainData.Rows[0].Cells[3].Value.ToString() + "'," +
                              "      cnntnum="   + dg_MainData.Rows[0].Cells[4].Value.ToString() + 
                              " where groupno='" + dg_MainData.Rows[0].Cells[0].Value.ToString() + "'" +
                              "   and ipno='" +m_dsMain.Tables[0].Rows[0]["ipno"].ToString() +"'";
                        iRow = FrmLogin.m_LWcsDB.mExecuteSQL(_sSQL);

                        _sSQL=" update wcs_ip set " +
                              "         srvip='" + dg_MainData.Rows[0].Cells[1].Value.ToString() + "'," +
                              "         port='"  + dg_MainData.Rows[0].Cells[2].Value.ToString() + "'" +
                              " where groupno='" + dg_MainData.Rows[0].Cells[0].Value.ToString() + "'" +
                              "   and type='C'";
                        iRow = FrmLogin.m_LWcsDB.mExecuteSQL(_sSQL);

                    }
                }
                else if (m_UpdateStatus == 21 || m_UpdateStatus==22)
                {
                    //保存客户端数据
                    dg_DetailData.EndEdit();
                    if (dg_DetailData.Rows[m_CurDetailRowIndex].Cells[0].Value.ToString().Length == 0 ||
                        dg_DetailData.Rows[m_CurDetailRowIndex].Cells[1].Value.ToString().Length == 0 ||
                        dg_DetailData.Rows[m_CurDetailRowIndex].Cells[2].Value.ToString().Length == 0 ||
                        dg_DetailData.Rows[m_CurDetailRowIndex].Cells[3].Value.ToString().Length == 0)
                    {
                        MessageBox.Show("您输入的数据不完整，请输入...", "注册");
                        return;
                    }
                    if (m_UpdateStatus == 21)
                    {
                        _sSQL = " insert into wcs_ip(ip,type,port,srvip,mark,ustatus,groupno,wtype,ipno) " +
                              " select '" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[1].Value.ToString() + "'," +
                              "'C'," +
                              "'" + dg_MainData.Rows[m_CurMainRowIndex].Cells[2].Value.ToString() + "'," +
                              "'" + dg_MainData.Rows[m_CurMainRowIndex].Cells[1].Value.ToString() + "'," +
                              "'" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[2].Value.ToString() + "'," +
                              "'1'," +
                              "'" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[0].Value.ToString() + "'," +
                              "'" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[3].Value.ToString() + "'," +
                              "(select nvl(max(ipno),0)+1 from wcs_ip) ipno from dual ";
                        iRow = FrmLogin.m_LWcsDB.mExecuteSQL(_sSQL);
                    }
                    else  if (m_UpdateStatus == 22)
                    {
                        _sSQL = " update wcs_ip set " +
                               "           ip='" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[1].Value.ToString() + "'," +
                               "         mark='" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[2].Value.ToString() + "'," +
                               "        wtype='" + dg_DetailData.Rows[m_CurDetailRowIndex].Cells[3].Value.ToString() +"'" +
                               " where groupno='" + dg_MainData.Rows[0].Cells[0].Value.ToString() + "'" +
                               "   and ipno='" + m_dsDetail.Tables[0].Rows[m_CurDetailRowIndex]["ipno"].ToString() + "'";
                        iRow = FrmLogin.m_LWcsDB.mExecuteSQL(_sSQL);
                    }

                }
                ts_Refresh_Click(null, null);
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();    
            }

        }

        private void ts_Refresh_Click(object sender, EventArgs e)
        {
            ts_AddSvr.Enabled = true;
            ts_EditSvr.Enabled = true;
            ts_AddCln.Enabled = true;
            ts_EditCln.Enabled = true;
            ts_AddSvr.Enabled = true;
            ts_Save.Enabled = false;
            mIniData();
            dg_DetailData.Enabled = true;
            dg_MainData.Enabled = true;
            dg_DetailData.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dg_MainData.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        }

        private void ts_Edit_Click(object sender, EventArgs e)
        {
            dg_DetailData.Enabled = false;
            dg_MainData.Enabled = true;
            dg_MainData.SelectionMode = DataGridViewSelectionMode.CellSelect;
            m_UpdateStatus = 12;
            m_CurMainRowIndex = dg_MainData.CurrentRow.Index;
            ts_Save.Enabled = true;
            ts_AddCln.Enabled=false;
            ts_AddSvr.Enabled = false;
            ts_EditSvr.Enabled = false;
            ts_EditCln.Enabled = false;
            
        }

        private void ts_AddCln_Click(object sender, EventArgs e)
        {
            dg_DetailData.Enabled = true;
            dg_MainData.Enabled = false;
            dg_DetailData.SelectionMode = DataGridViewSelectionMode.CellSelect;
            m_UpdateStatus = 21;
            ts_EditSvr.Enabled = false;
            ts_AddCln.Enabled = false;
            ts_AddSvr.Enabled = false;
            ts_EditCln.Enabled = false;
            ts_Save.Enabled = true;
            DataRow _DataRow=m_dsDetail.Tables[0].NewRow();
            m_dsDetail.Tables[0].Rows.InsertAt(_DataRow, 0);
            m_CurDetailRowIndex = 0;
            m_CurMainRowIndex = 0;
            dg_DetailData.Rows[m_CurDetailRowIndex].Cells[0].Value = MyPublic.m_SysGroupNo;
            dg_DetailData.EndEdit();

        }

        private void ts_EditCln_Click(object sender, EventArgs e)
        {
            dg_DetailData.Enabled = true;
            dg_MainData.Enabled = false;
            dg_DetailData.SelectionMode = DataGridViewSelectionMode.CellSelect;
            m_UpdateStatus = 22;          
            m_CurMainRowIndex = dg_MainData.CurrentRow.Index;
            m_CurDetailRowIndex = dg_DetailData.CurrentRow.Index;
            ts_EditSvr.Enabled = false;
            ts_AddCln.Enabled = false;
            ts_AddSvr.Enabled = false;
            ts_EditCln.Enabled = false;
            ts_Save.Enabled = true;
        }

        private void dg_MainData_CellEnter(object sender, DataGridViewCellEventArgs e)
        {
            //dg_MainData.Rows[e.RowIndex].Cells[e.ColumnIndex].Style.SelectionForeColor(Color.Red);
        }
        

    }
}
