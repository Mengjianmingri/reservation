var vue = new Vue({
    el: '#app',
    data:{
        autoUpload:true,//自动上传
        imageUrl:null,//模型数据，用于上传图片完成后图片预览
        activeName:'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize:10,
            total:100,
            queryString:null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData:[],//添加表单窗口中检查组列表数据
        checkgroupIds:[],//添加表单窗口中检查组复选框对应id
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit:false
    },
    created() {
        this.findPage();

    },
    methods: {
        //文件上传成功后的钩子，response为服务端返回的值，file为当前上传的文件封装成的js对象
        handleAvatarSuccess(response, file) {
            this.imageUrl="http://q399i9518.bkt.clouddn.com/"+response.data;
            this.$message({
                message:response.message,
                type:response.flag?"success":"error"
            })
            this.formData.img=response.data
        },
        //上传图片之前执行
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传套餐图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传套餐图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        //编辑数据回显
        handleUpdate(row){
            this.activeName='first'
            this.dialogFormVisible4Edit=true;
            axios.get("/setmeal/findSetmel.do?id="+row.id).then((res)=>{
                if(res.data.flag){
                    this.formData=res.data.data;
                    this.imageUrl="http://q399i9518.bkt.clouddn.com/"+res.data.data.img;
                    axios.post("/checkgroup/findAll.do").then((res)=>{
                        if(res.data.flag){
                            this.tableData=res.data.data
                        }else {
                            this.$message.error(res.data.message)
                        }
                    })
                    axios.post("/setmeal/findCheckGroups.do?id="+row.id).then((res)=>{
                        if(res.data.flag){
                            this.checkgroupIds=res.data.data;
                        }
                    })
                }
            })


        },
        //修改数据
        handleEdit(){
            this.dialogFormVisible4Edit=false;
            axios.post("/setmeal/edit.do?checkgroupIds="+this.checkgroupIds,this.formData).then((res)=>{
                if(res.data.flag){
                    this.$message({
                        message: res.data.message,
                        type:"success"
                    })
                }else {
                    this.$message.error(res.data.message)
                }
            }).finally(()=>{
                this.findPage()
            })

        },

        //添加
        handleAdd () {
            this.dialogFormVisible=false;
            axios.post("/setmeal/add.do?checkgroupIds="+this.checkgroupIds,this.formData).then((res)=>{
                if(res.data.flag){
                    this.$message({
                        message: res.data.message,
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

            axios.post("/setmeal/findPage.do",pam).then((res)=>{
                this.dataList=res.data.rows;
                this.pagination.total=res.data.total
            })
        },
        //删除套餐
        handleDelete(row){
            this.findPage()
            axios.get("/setmeal/delete.do?id="+row.id).then((res)=>{
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

        // 重置表单
        resetForm() {
            this.formData={};
            this.checkgroupIds=[];
            this.imageUrl=null;
        },
        // 弹出添加窗口
        handleCreate() {
            this.dialogFormVisible=true;
            this.activeName='first';
            this.resetForm();

            axios.post("/checkgroup/findAll.do").then((res)=>{
                if(res.data.flag){
                    this.tableData=res.data.data
                }else {
                    this.$message.error(res.data.message)
                }
            })
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage=currentPage;
            this.findPage()
        }
    }
})