var vue = new Vue({
    el: '#app',
    data: {
        activeName: 'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize: 10,
            total: 100,
            queryString: null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableDataForMenu: [], //新增和编辑表单中对应的菜单列表数据
        tableDataForPermission: [], //新增和编辑表单中对应的权限列表数据
        menuIds: [],//新增和编辑表单中菜单对应的复选框，基于双向绑定可以进行回显和数据提交
        permessionIds: [],//新增和编辑表单中权限对应的复选框，基于双向绑定可以进行回显和数据提交
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit: false,//控制编辑窗口显示/隐藏
        rules: {//校验规则
            name: [{required: true, message: '角色名称为必填项', trigger: 'blur'}],
            keyword: [{required: true, message: '角色关键词为必填项', trigger: 'blur'}],
            description: [{required: true, message: '角色描述为必填项', trigger: 'blur'}]
        }
    },
    created() {
        this.findPage();
    },
    methods: {
        //编辑
        handleEdit() {
            this.$refs['dataEditForm'].validate((valid) => {
                if (valid) {
                    axios.post("/role/editRole.do?permessionIds=" + this.permessionIds + "&menuIds=" + this.menuIds, this.formData,).then((res) => {
                        this.dialogFormVisible4Edit = false;
                        if (res.data.flag) {
                            this.$message({
                                message: res.data.message,
                                type: "success"
                            })
                        } else {
                            this.$message.error(res.data.message)
                        }
                    }).finally(() => {
                        this.findPage()
                    })
                } else {
                    this.$message.error("数据校验失败，请检查输入是否正确!")
                    return false;
                }
            })
        },
        //删除检查组
        handleDelete(row) {
            this.$confirm("你确定要删除吗？", "提示", {
                type: 'warning'
            }).then(() => {
                axios.get("/role/delRoleById.do?id=" + row.id).then((res) => {
                    if (res.data.flag) {
                        this.$message({
                            message: res.data.message,
                            type: 'success'
                        })
                    } else {
                        this.$message.error(res.data.message);
                    }
                }).finally(() => {
                    this.findPage();
                });
            }).catch(() => {
                this.$message({
                    message: "操作取消",
                    type: 'info'
                })
            })
        },
        //添加
        handleAdd() {
            this.$refs['dataAddForm'].validate((valid) => {
                if (valid) {
                    axios.post("/role/addRole.do?permessionIds=" + this.permessionIds + "&menuIds=" + this.menuIds, this.formData,).then((res) => {
                        this.dialogFormVisible = false;
                        if (res.data.flag) {
                            this.$message({
                                message: res.data.message,
                                type: "success"
                            })
                        } else {
                            this.$message.error(res.data.message)
                        }
                    }).finally(() => {
                        this.findPage()
                    })
                } else {
                    this.$message.error("数据校验失败，请检查输入是否正确!")
                    return false;
                }
            })
        },


        _findPage() {
            this.pagination.currentPage = 1;
            this.findPage()
        },
        //分页查询
        findPage() {
            var pam = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            }
            axios.post("/role/findRolePage.do", pam).then((res) => {
                this.dataList = res.data.rows;
                this.pagination.total = res.data.total;
            })
        },
        // 重置表单
        resetForm() {
            this.formData = {};
        },
        // 弹出添加窗口
        handleCreate() {
            this.dialogFormVisible = true;
            this.activeName = 'first';
            this.resetForm();
            this.menuIds = [];
            this.permessionIds = [];
            //表单校验
            this.$nextTick(() => {
                this.$refs['dataAddForm'].clearValidate(() => {
                });
                // 回显菜单信息
                axios.post("/role/findMenu.do").then((res) => {
                    if (res.data.flag) {
                        this.tableDataForMenu = res.data.data;
                    } else {
                        this.$message.error(res.data.message)
                    }
                });

                // 回显权限信息
                axios.post("/role/findPermission.do").then((res) => {
                    if (res.data.flag) {
                        this.tableDataForPermission = res.data.data;
                    } else {
                        this.$message.error(res.data.message)
                    }
                });
            });
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            this.dialogFormVisible4Edit = true;
            this.activeName = 'first';
            this.$nextTick(()=>{
                this.$refs['dataEditForm'].clearValidate();
            })
            axios.post("/role/findByRoleId.do?id=" + row.id).then((res) => {
                if (res.data.flag) {
                    this.formData = res.data.data;
                    // 回显菜单信息
                    axios.post("/role/findMenu.do").then((res) => {
                        if (res.data.flag) {
                            this.tableDataForMenu = res.data.data;

                            axios.post("/role/findRoleAndMenuById.do?id=" + row.id).then((res) => {
                                if (res.data.flag) {
                                    this.menuIds = res.data.data;
                                } else {
                                    this.$message.error(res.data.message)
                                }
                            });
                        } else {
                            this.$message.error(res.data.message)
                        }
                    });

                    // 回显权限信息
                    axios.post("/role/findPermission.do").then((res) => {
                        if (res.data.flag) {
                            this.tableDataForPermission = res.data.data;

                            axios.post("/role/findRoleAndPermessionById.do?id=" + row.id).then((res) => {
                                if (res.data.flag) {
                                    this.permessionIds = res.data.data;
                                } else {
                                    this.$message.error(res.data.message)
                                }
                            });
                        } else {
                            this.$message.error(res.data.message)
                        }
                    });
                } else {
                    this.$message.error(res.data.message)
                }
            })

        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage()
        },
    }
})