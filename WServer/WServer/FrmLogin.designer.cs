namespace WServer
{
    partial class FrmLogin
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmLogin));
            this.label1 = new System.Windows.Forms.Label();
            this.txt_WMSDBUserPWD = new System.Windows.Forms.TextBox();
            this.gb_Set = new System.Windows.Forms.GroupBox();
            this.txt_KeyCnntCount = new System.Windows.Forms.TextBox();
            this.label9 = new System.Windows.Forms.Label();
            this.txt_SvrPort = new System.Windows.Forms.TextBox();
            this.label8 = new System.Windows.Forms.Label();
            this.txt_SvrIP = new System.Windows.Forms.TextBox();
            this.label7 = new System.Windows.Forms.Label();
            this.txt_wmsDbName = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.txt_WMSDBUserID = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.bt_Set = new System.Windows.Forms.Button();
            this.lbl_Mess = new System.Windows.Forms.Label();
            this.bt_Ok = new System.Windows.Forms.Button();
            this.label6 = new System.Windows.Forms.Label();
            this.txt_SysUserPWD = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.bt_Exit = new System.Windows.Forms.Button();
            this.txt_SysUserID = new System.Windows.Forms.TextBox();
            this.gb_SetLogin = new System.Windows.Forms.GroupBox();
            this.gb_Set.SuspendLayout();
            this.gb_SetLogin.SuspendLayout();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(21, 24);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(59, 12);
            this.label1.TabIndex = 0;
            this.label1.Text = "用户编号:";
            // 
            // txt_WMSDBUserPWD
            // 
            this.txt_WMSDBUserPWD.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_WMSDBUserPWD.Location = new System.Drawing.Point(96, 72);
            this.txt_WMSDBUserPWD.MaxLength = 20;
            this.txt_WMSDBUserPWD.Name = "txt_WMSDBUserPWD";
            this.txt_WMSDBUserPWD.PasswordChar = '*';
            this.txt_WMSDBUserPWD.Size = new System.Drawing.Size(175, 22);
            this.txt_WMSDBUserPWD.TabIndex = 7;
            // 
            // gb_Set
            // 
            this.gb_Set.BackColor = System.Drawing.Color.Transparent;
            this.gb_Set.Controls.Add(this.txt_KeyCnntCount);
            this.gb_Set.Controls.Add(this.label9);
            this.gb_Set.Controls.Add(this.txt_SvrPort);
            this.gb_Set.Controls.Add(this.label8);
            this.gb_Set.Controls.Add(this.txt_SvrIP);
            this.gb_Set.Controls.Add(this.label7);
            this.gb_Set.Controls.Add(this.txt_wmsDbName);
            this.gb_Set.Controls.Add(this.txt_WMSDBUserPWD);
            this.gb_Set.Controls.Add(this.label5);
            this.gb_Set.Controls.Add(this.txt_WMSDBUserID);
            this.gb_Set.Controls.Add(this.label3);
            this.gb_Set.Controls.Add(this.label4);
            this.gb_Set.Location = new System.Drawing.Point(6, 187);
            this.gb_Set.Name = "gb_Set";
            this.gb_Set.Size = new System.Drawing.Size(349, 193);
            this.gb_Set.TabIndex = 9;
            this.gb_Set.TabStop = false;
            this.gb_Set.Text = "设置";
            // 
            // txt_KeyCnntCount
            // 
            this.txt_KeyCnntCount.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_KeyCnntCount.Location = new System.Drawing.Point(96, 160);
            this.txt_KeyCnntCount.MaxLength = 20;
            this.txt_KeyCnntCount.Name = "txt_KeyCnntCount";
            this.txt_KeyCnntCount.Size = new System.Drawing.Size(175, 22);
            this.txt_KeyCnntCount.TabIndex = 10;
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(23, 164);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(71, 12);
            this.label9.TabIndex = 14;
            this.label9.Text = "可用连接数:";
            // 
            // txt_SvrPort
            // 
            this.txt_SvrPort.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_SvrPort.Location = new System.Drawing.Point(96, 132);
            this.txt_SvrPort.Name = "txt_SvrPort";
            this.txt_SvrPort.Size = new System.Drawing.Size(175, 22);
            this.txt_SvrPort.TabIndex = 9;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(23, 138);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(71, 12);
            this.label8.TabIndex = 13;
            this.label8.Text = "服务端端口:";
            // 
            // txt_SvrIP
            // 
            this.txt_SvrIP.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_SvrIP.Location = new System.Drawing.Point(96, 105);
            this.txt_SvrIP.MaxLength = 20;
            this.txt_SvrIP.Name = "txt_SvrIP";
            this.txt_SvrIP.Size = new System.Drawing.Size(175, 22);
            this.txt_SvrIP.TabIndex = 8;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(24, 110);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(71, 12);
            this.label7.TabIndex = 11;
            this.label7.Text = "服务端地址:";
            // 
            // txt_wmsDbName
            // 
            this.txt_wmsDbName.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_wmsDbName.Location = new System.Drawing.Point(96, 19);
            this.txt_wmsDbName.MaxLength = 20;
            this.txt_wmsDbName.Name = "txt_wmsDbName";
            this.txt_wmsDbName.Size = new System.Drawing.Size(175, 22);
            this.txt_wmsDbName.TabIndex = 5;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(23, 77);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(71, 12);
            this.label5.TabIndex = 8;
            this.label5.Text = "数据库密码:";
            // 
            // txt_WMSDBUserID
            // 
            this.txt_WMSDBUserID.Font = new System.Drawing.Font("宋体", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_WMSDBUserID.Location = new System.Drawing.Point(96, 45);
            this.txt_WMSDBUserID.MaxLength = 20;
            this.txt_WMSDBUserID.Name = "txt_WMSDBUserID";
            this.txt_WMSDBUserID.Size = new System.Drawing.Size(175, 22);
            this.txt_WMSDBUserID.TabIndex = 6;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(23, 52);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(71, 12);
            this.label3.TabIndex = 6;
            this.label3.Text = "数据库用户:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(22, 24);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(71, 12);
            this.label4.TabIndex = 4;
            this.label4.Text = "数据库服务:";
            // 
            // bt_Set
            // 
            this.bt_Set.Location = new System.Drawing.Point(140, 142);
            this.bt_Set.Name = "bt_Set";
            this.bt_Set.Size = new System.Drawing.Size(70, 32);
            this.bt_Set.TabIndex = 2;
            this.bt_Set.Text = "设置";
            this.bt_Set.UseVisualStyleBackColor = true;
            this.bt_Set.Click += new System.EventHandler(this.bt_Set_Click);
            // 
            // lbl_Mess
            // 
            this.lbl_Mess.AutoSize = true;
            this.lbl_Mess.ForeColor = System.Drawing.Color.Maroon;
            this.lbl_Mess.Location = new System.Drawing.Point(94, 97);
            this.lbl_Mess.Name = "lbl_Mess";
            this.lbl_Mess.Size = new System.Drawing.Size(125, 12);
            this.lbl_Mess.TabIndex = 5;
            this.lbl_Mess.Text = "请输入用户编号和密码";
            // 
            // bt_Ok
            // 
            this.bt_Ok.Location = new System.Drawing.Point(212, 142);
            this.bt_Ok.Name = "bt_Ok";
            this.bt_Ok.Size = new System.Drawing.Size(70, 32);
            this.bt_Ok.TabIndex = 3;
            this.bt_Ok.Text = "登录";
            this.bt_Ok.UseVisualStyleBackColor = true;
            this.bt_Ok.Click += new System.EventHandler(this.bt_Ok_Click);
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(21, 97);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(59, 12);
            this.label6.TabIndex = 4;
            this.label6.Text = "温馨提示:";
            // 
            // txt_SysUserPWD
            // 
            this.txt_SysUserPWD.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_SysUserPWD.Location = new System.Drawing.Point(96, 56);
            this.txt_SysUserPWD.MaxLength = 20;
            this.txt_SysUserPWD.Name = "txt_SysUserPWD";
            this.txt_SysUserPWD.PasswordChar = '*';
            this.txt_SysUserPWD.Size = new System.Drawing.Size(175, 23);
            this.txt_SysUserPWD.TabIndex = 1;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(21, 59);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(59, 12);
            this.label2.TabIndex = 2;
            this.label2.Text = "用户密码:";
            // 
            // bt_Exit
            // 
            this.bt_Exit.Location = new System.Drawing.Point(284, 142);
            this.bt_Exit.Name = "bt_Exit";
            this.bt_Exit.Size = new System.Drawing.Size(70, 32);
            this.bt_Exit.TabIndex = 4;
            this.bt_Exit.Text = "退出";
            this.bt_Exit.UseVisualStyleBackColor = true;
            this.bt_Exit.Click += new System.EventHandler(this.bt_Exit_Click);
            // 
            // txt_SysUserID
            // 
            this.txt_SysUserID.Font = new System.Drawing.Font("宋体", 10.5F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.txt_SysUserID.Location = new System.Drawing.Point(96, 21);
            this.txt_SysUserID.MaxLength = 20;
            this.txt_SysUserID.Name = "txt_SysUserID";
            this.txt_SysUserID.Size = new System.Drawing.Size(175, 23);
            this.txt_SysUserID.TabIndex = 0;
            // 
            // gb_SetLogin
            // 
            this.gb_SetLogin.BackColor = System.Drawing.Color.Transparent;
            this.gb_SetLogin.Controls.Add(this.lbl_Mess);
            this.gb_SetLogin.Controls.Add(this.label6);
            this.gb_SetLogin.Controls.Add(this.txt_SysUserPWD);
            this.gb_SetLogin.Controls.Add(this.label2);
            this.gb_SetLogin.Controls.Add(this.txt_SysUserID);
            this.gb_SetLogin.Controls.Add(this.label1);
            this.gb_SetLogin.Location = new System.Drawing.Point(6, 1);
            this.gb_SetLogin.Name = "gb_SetLogin";
            this.gb_SetLogin.Size = new System.Drawing.Size(348, 136);
            this.gb_SetLogin.TabIndex = 5;
            this.gb_SetLogin.TabStop = false;
            this.gb_SetLogin.Text = "请登录";
            this.gb_SetLogin.Enter += new System.EventHandler(this.gb_SetLogin_Enter);
            // 
            // FrmLogin
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(128)))), ((int)(((byte)(128)))), ((int)(((byte)(255)))));
            this.ClientSize = new System.Drawing.Size(360, 410);
            this.Controls.Add(this.gb_Set);
            this.Controls.Add(this.bt_Set);
            this.Controls.Add(this.bt_Ok);
            this.Controls.Add(this.bt_Exit);
            this.Controls.Add(this.gb_SetLogin);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmLogin";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "登录";
            this.Load += new System.EventHandler(this.FrmLogin_Load);
            this.gb_Set.ResumeLayout(false);
            this.gb_Set.PerformLayout();
            this.gb_SetLogin.ResumeLayout(false);
            this.gb_SetLogin.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txt_WMSDBUserPWD;
        private System.Windows.Forms.GroupBox gb_Set;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox txt_WMSDBUserID;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button bt_Set;
        private System.Windows.Forms.Label lbl_Mess;
        private System.Windows.Forms.Button bt_Ok;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.TextBox txt_SysUserPWD;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button bt_Exit;
        private System.Windows.Forms.TextBox txt_SysUserID;
        private System.Windows.Forms.GroupBox gb_SetLogin;
        private System.Windows.Forms.TextBox txt_wmsDbName;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.TextBox txt_SvrIP;
        private System.Windows.Forms.TextBox txt_KeyCnntCount;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.TextBox txt_SvrPort;
    }
}