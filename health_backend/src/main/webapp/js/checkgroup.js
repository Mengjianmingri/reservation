var vue = new Vue({
    el: '#app',
    data:{
        activeName:'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize:10,
            total:100,
            queryString:null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData:[],//新增和编辑表单中对应的检查项列表数据
        checkitemIds:[],//新增和编辑表单中检查项对应的复选框，基于双向绑定可以进行回显和数据提交
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit:false//控制编辑窗口显示/隐藏
    },
    created() {
        this.findPage();
    },
    methods: {
        //编辑
        handleEdit() {
            this.dialogFormVisible4Edit=false;
            axios.post("/checkgroup/edit.do?checkitemIds="+this.checkitemIds,this.formData).then((res)=>{
                if(res.data.flag){
                    this.$message({
                        message:res.data.message,
                        type:"success"
                    })
                }else {
                    this.$message.error(res.data.message)
                }
            }).finally(()=>{
                this.findPage()
            })
        },
        //删除检查组
        handleDelete(row){
            this.findPage()
            axios.get("/checkgroup/delete.do?id="+row.id).then((res)=>{
                if(res.data.flag){
                    this.$message({
                        message:res.data.message,
                        type:"success"
                    })
                    this.findPage()
                }else {
                    this.$message.error(res.data.message)
                }
            })
        },
        //添加
        handleAdd () {
            this.dialogFormVisible=false;
            axios.post("/checkgroup/add.do?checkitemIds="+this.checkitemIds,this.formData).then((res)=>{
                if(res.data.flag){
                    this.$message({
                        message:res.data.message,
                        type:"success"
                    })
                }else {
                    this.$message.error(res.data.message)
                }
            }).finally(()=>{
                this.findPage()
            })
        },

        _findPage(){
            this.pagination.currentPage=1;
            this.findPage()
        },
        //分页查询
        findPage() {
            var pam = {
                currentPage:this.pagination.currentPage,
                pageSize:this.pagination.pageSize,
                queryString:this.pagination.queryString
            }
            axios.post("/checkgroup/findPage.do",pam).then((res)=>{
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
            this.activeName='first';
            this.resetForm();
            this.checkitemIds=[];
            axios.post("/checkitem/findAll.do").then((res)=>{
                if (res.data.flag){
                    this.tableData=res.data.data;
                } else {
                    this.$message.error(res.data.message)
                }
            })
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            this.dialogFormVisible4Edit=true;
            this.activeName='first';
            axios.post("/checkgroup/findCheckGroupByid.do?id="+row.id).then((res)=>{
                if(res.data.flag){
                    this.formData=res.data.data;
                    axios.post("/checkitem/findAll.do").then((res)=>{
                        if (res.data.flag){
                            this.tableData=res.data.data;
                            axios.post("/checkgroup/findCheckItemByCheckGroupId.do?id="+row.id).then((res=>{
                                if(res.data.flag){
                                    this.checkitemIds=res.data.data;
                                }else {
                                    this.$message.error(res.data.message)
                                }
                            }))
                        } else {
                            this.$message.error(res.data.message)
                        }
                    })
                }else {
                    this.$message.error(res.data.message)
                }
            })

        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage=currentPage;
            this.findPage()
        },
    }
})