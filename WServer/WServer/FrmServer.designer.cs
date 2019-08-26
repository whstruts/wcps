namespace WServer
{
    partial class FrmServer
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
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmServer));
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            this.dg_MainData = new System.Windows.Forms.DataGridView();
            this.toolStrip1 = new System.Windows.Forms.ToolStrip();
            this.ts_Refresh = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.ts_AddSvr = new System.Windows.Forms.ToolStripButton();
            this.ts_EditSvr = new System.Windows.Forms.ToolStripButton();
            this.ts_AddCln = new System.Windows.Forms.ToolStripButton();
            this.ts_EditCln = new System.Windows.Forms.ToolStripButton();
            this.ts_Save = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.ts_Exit = new System.Windows.Forms.ToolStripButton();
            this.dg_DetailData = new System.Windows.Forms.DataGridView();
            ((System.ComponentModel.ISupportInitialize)(this.dg_MainData)).BeginInit();
            this.toolStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dg_DetailData)).BeginInit();
            this.SuspendLayout();
            // 
            // dg_MainData
            // 
            this.dg_MainData.AllowUserToAddRows = false;
            dataGridViewCellStyle1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(128)))), ((int)(((byte)(255)))), ((int)(((byte)(128)))));
            this.dg_MainData.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            this.dg_MainData.BackgroundColor = System.Drawing.Color.White;
            this.dg_MainData.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dg_MainData.GridColor = System.Drawing.Color.White;
            this.dg_MainData.Location = new System.Drawing.Point(4, 61);
            this.dg_MainData.Name = "dg_MainData";
            this.dg_MainData.RowHeadersWidthSizeMode = System.Windows.Forms.DataGridViewRowHeadersWidthSizeMode.DisableResizing;
            this.dg_MainData.RowTemplate.Height = 20;
            this.dg_MainData.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dg_MainData.ShowRowErrors = false;
            this.dg_MainData.Size = new System.Drawing.Size(645, 119);
            this.dg_MainData.TabIndex = 13;
            this.dg_MainData.CellEnter += new System.Windows.Forms.DataGridViewCellEventHandler(this.dg_MainData_CellEnter);
            // 
            // toolStrip1
            // 
            this.toolStrip1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.toolStrip1.ImageScalingSize = new System.Drawing.Size(32, 32);
            this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.ts_Refresh,
            this.toolStripSeparator1,
            this.ts_AddSvr,
            this.ts_EditSvr,
            this.ts_AddCln,
            this.ts_EditCln,
            this.ts_Save,
            this.toolStripSeparator2,
            this.ts_Exit});
            this.toolStrip1.Location = new System.Drawing.Point(0, 0);
            this.toolStrip1.Name = "toolStrip1";
            this.toolStrip1.Size = new System.Drawing.Size(649, 56);
            this.toolStrip1.TabIndex = 14;
            this.toolStrip1.Text = "toolStrip1";
            // 
            // ts_Refresh
            // 
            this.ts_Refresh.Image = ((System.Drawing.Image)(resources.GetObject("ts_Refresh.Image")));
            this.ts_Refresh.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Refresh.Name = "ts_Refresh";
            this.ts_Refresh.Size = new System.Drawing.Size(50, 53);
            this.ts_Refresh.Text = "刷新[&F]";
            this.ts_Refresh.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Refresh.ToolTipText = "刷新";
            this.ts_Refresh.Click += new System.EventHandler(this.ts_Refresh_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(6, 56);
            // 
            // ts_AddSvr
            // 
            this.ts_AddSvr.Image = ((System.Drawing.Image)(resources.GetObject("ts_AddSvr.Image")));
            this.ts_AddSvr.ImageAlign = System.Drawing.ContentAlignment.TopCenter;
            this.ts_AddSvr.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_AddSvr.Name = "ts_AddSvr";
            this.ts_AddSvr.Size = new System.Drawing.Size(72, 53);
            this.ts_AddSvr.Text = "添加服务端";
            this.ts_AddSvr.TextAlign = System.Drawing.ContentAlignment.BottomCenter;
            this.ts_AddSvr.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_AddSvr.Click += new System.EventHandler(this.ts_Add_Click);
            // 
            // ts_EditSvr
            // 
            this.ts_EditSvr.Image = ((System.Drawing.Image)(resources.GetObject("ts_EditSvr.Image")));
            this.ts_EditSvr.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_EditSvr.Name = "ts_EditSvr";
            this.ts_EditSvr.Size = new System.Drawing.Size(72, 53);
            this.ts_EditSvr.Text = "编辑服务端";
            this.ts_EditSvr.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_EditSvr.Click += new System.EventHandler(this.ts_Edit_Click);
            // 
            // ts_AddCln
            // 
            this.ts_AddCln.Image = ((System.Drawing.Image)(resources.GetObject("ts_AddCln.Image")));
            this.ts_AddCln.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_AddCln.Name = "ts_AddCln";
            this.ts_AddCln.Size = new System.Drawing.Size(89, 53);
            this.ts_AddCln.Text = "添加客户端[&D]";
            this.ts_AddCln.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_AddCln.Click += new System.EventHandler(this.ts_AddCln_Click);
            // 
            // ts_EditCln
            // 
            this.ts_EditCln.Image = ((System.Drawing.Image)(resources.GetObject("ts_EditCln.Image")));
            this.ts_EditCln.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_EditCln.Name = "ts_EditCln";
            this.ts_EditCln.Size = new System.Drawing.Size(89, 53);
            this.ts_EditCln.Text = "编辑客户端[&D]";
            this.ts_EditCln.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_EditCln.Click += new System.EventHandler(this.ts_EditCln_Click);
            // 
            // ts_Save
            // 
            this.ts_Save.Enabled = false;
            this.ts_Save.Image = ((System.Drawing.Image)(resources.GetObject("ts_Save.Image")));
            this.ts_Save.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Save.Name = "ts_Save";
            this.ts_Save.Size = new System.Drawing.Size(51, 53);
            this.ts_Save.Text = "保存[&S]";
            this.ts_Save.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Save.Click += new System.EventHandler(this.ts_Save_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(6, 56);
            // 
            // ts_Exit
            // 
            this.ts_Exit.Image = ((System.Drawing.Image)(resources.GetObject("ts_Exit.Image")));
            this.ts_Exit.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Exit.Name = "ts_Exit";
            this.ts_Exit.Size = new System.Drawing.Size(51, 53);
            this.ts_Exit.Text = "退出[&T]";
            this.ts_Exit.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Exit.Click += new System.EventHandler(this.ts_Exit_Click);
            // 
            // dg_DetailData
            // 
            this.dg_DetailData.AllowUserToAddRows = false;
            dataGridViewCellStyle2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(255)))), ((int)(((byte)(192)))));
            this.dg_DetailData.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle2;
            this.dg_DetailData.BackgroundColor = System.Drawing.Color.White;
            this.dg_DetailData.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dg_DetailData.GridColor = System.Drawing.Color.White;
            this.dg_DetailData.Location = new System.Drawing.Point(4, 186);
            this.dg_DetailData.Name = "dg_DetailData";
            this.dg_DetailData.RowTemplate.Height = 20;
            this.dg_DetailData.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dg_DetailData.Size = new System.Drawing.Size(645, 190);
            this.dg_DetailData.TabIndex = 15;
            // 
            // FrmServer
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.ClientSize = new System.Drawing.Size(649, 382);
            this.Controls.Add(this.dg_DetailData);
            this.Controls.Add(this.toolStrip1);
            this.Controls.Add(this.dg_MainData);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmServer";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "服务端参数设置";
            this.Load += new System.EventHandler(this.FrmServer_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dg_MainData)).EndInit();
            this.toolStrip1.ResumeLayout(false);
            this.toolStrip1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dg_DetailData)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        public System.Windows.Forms.DataGridView dg_MainData;
        private System.Windows.Forms.ToolStrip toolStrip1;
        private System.Windows.Forms.ToolStripButton ts_AddSvr;
        private System.Windows.Forms.ToolStripButton ts_EditSvr;
        private System.Windows.Forms.ToolStripButton ts_AddCln;
        private System.Windows.Forms.ToolStripButton ts_Save;
        private System.Windows.Forms.ToolStripButton ts_Refresh;
        private System.Windows.Forms.ToolStripButton ts_Exit;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        public System.Windows.Forms.DataGridView dg_DetailData;
        private System.Windows.Forms.ToolStripButton ts_EditCln;
    }
}