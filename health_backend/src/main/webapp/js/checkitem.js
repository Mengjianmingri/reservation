var vue = new Vue({
    el: '#app',
    data:{
        pagination: {//分页相关模型数据
            currentPage: 1,//当前页码
            pageSize:10,//每页显示的记录数
            total:0,//总记录数
            queryString:null//查询条件
        },
        dataList: [],//当前页要展示的分页列表数据
        formData: {},//表单数据
        dialogFormVisible: false,//增加表单是否可见
        dialogFormVisible4Edit:false,//编辑表单是否可见
        rules: {//校验规则
            code: [{ required: true, message: '项目编码为必填项', trigger: 'blur' }],
            name: [{ required: true, message: '项目名称为必填项', trigger: 'blur' }]
        }
    },
    //钩子函数，VUE对象初始化完成后自动执行
    created() {
        this.findPage()
    },
    methods: {
        //编辑
        handleEdit() {
            //表单校验 this.$refs
            this.$refs['dataEditForm'].validate((valid)=>{
                if(valid){
                    axios.post("/checkitem/edit.do",this.formData).then((res)=>{
                        this.dialogFormVisible4Edit = false;
                        if(res.data.flag){
                            //消息提示：成功与否message 类型 type：success成功绿色/warning警告红色/info无效灰色
                            //错误消息提示this.$message.error("错误信息")
                            this.$message({
                                message:res.data.message,
                                type:'success'
                            })
                        }else {
                            this.$message.error(res.data.message)
                        }
                    }).finally(()=>{
                        this.findPage()
                    })
                }else {
                    this.$message.error("系统错误！")
                    return false;
                }
            })
        },
        //添加
        handleAdd () {
            //表单校验 this.$refs
            this.$refs['dataAddForm'].validate((valid) =>{
                if(valid){
                    axios.post("/checkitem/add.do",this.formData).then((res)=>{
                            this.dialogFormVisible=false;
                            if(res.data.flag){
                                this.$message({
                                    message:res.data.message,
                                    type:'success'
                                })
                            }else {
                                this.$message.error(res.data.message)
                            }
                        }
                    ).catch((r)=>{
                        this.showmessage(r);
                    }).finally(()=>{
                        this. findPage();
                    })
                }else {
                    this.$message.error("表单校验失败");
                    return false
                }
            });
        },
        showmessage(r){
            if(r == 'Error: Request failed with status code 403'){
                this.$message.error('无访问权限');
                return;
            }else {
                this.$message.error('未知错误');
                return;
            }
        },

        //分页查询bug版
        _findPage(){
            this.pagination.currentPage=1,
                this.findPage()
        },
        //分页查询
        findPage() {
            var pam = {
                currentPage:this.pagination.currentPage,
                pageSize:this.pagination.pageSize,
                queryString:this.pagination.queryString
            }
            axios.post("/checkitem/findPage.do",pam).then((res)=>{
                this.dataList=res.data.rows;
                this.pagination.total=res.data.total;
            })
        },
        // 重置表单
        resetForm() {
            this.formData={};
        },
        // 弹出添加窗口
        handleCreate() {
            this.dialogFormVisible=true;
            this.resetForm();
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            axios.get("/checkitem/findByid.do?id="+row.id).then((res)=>{
                if(res.data.flag){
                    this.dialogFormVisible4Edit = true;
                    this.formData=res.data.data;
                }else {
                    this.$message.error("系统错误！")
                }
            })
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage=currentPage;
            this.findPage()
        },
        // 删除
        handleDelete(row) {
            //alert(row.id)

            this.$confirm("是真的要删除吗？","提示",{type:"warning"}).then(()=>{
                    axios.get("/checkitem/delete.do?id="+row.id).then((res)=>{

                        if(res.data.flag){
                            this.$message({
                                message:res.data.message,
                                type:'success'
                            })
                        }else {
                            this.$message.error(res.data.message)
                        }
                    }).catch((r)=>{
                        this.showmessage(r);
                    })
                }
            ).finally(()=>{
                this.findPage()
            })
        }
    }
})