new Vue({
    el: '#app',
    data:{
        today:new Date(),//当前日期
        currentDay: 1,
        currentMonth: 1,
        LocalMonth: 1,
        currentYear: 1970,
        currentWeek: 1,
        days: [],
        leftobj: []//用于装载页面显示的月份已经进行预约设置的数据
    },
    created: function () {//在vue初始化时调用
        this.initData(new Date());
    },
    methods: {
        //预约设置
        handleOrderSet(day){

            this.$prompt('请输入可预约人数', '预约设置', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputPattern: /^[0-9]*[1-9][0-9]*$/,
                inputErrorMessage: '只能输入正整数'

            }).then(({value})=>{
                var pam = {
                    number:value,
                    orderDate:this.formatDate(day.getFullYear(),day.getMonth()+1,day.getDate())
                }
                axios.post("/ordersetting/editNumberByDate.do",pam).then((res)=>{
                    if(res.data.flag){
                        this.initData(this.formatDate(day.getFullYear(), day.getMonth() + 1, 1));
                        this.$message({
                            message: res.data.message,
                            type: "success"
                        })
                    }else {
                        this.$message.error(res.data.message)
                    }
                })
            }).catch((e)=>{
                this.$message({
                    message:"操作已取消",
                    type:"info"
                })
            })

        },
        //上传之前进行文件格式校验
        beforeUpload(file){
            const isXLS = file.type === 'application/vnd.ms-excel';
            if(isXLS){
                return true;
            }
            const isXLSX = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            if (isXLSX) {
                return true;
            }
            this.$message.error('上传文件只能是xls或者xlsx格式!');
            return false;
        },
        //下载模板文件
        downloadTemplate(){
            window.location.href="../../template/ordersetting_template.xlsx";
        },
        //上传成功提示
        handleSuccess(response, file) {
            if(response.flag){
                this.$message({
                    message: response.message,
                    type: 'success'
                });
            }else{
                this.$message.error(response.message);
            }
            console.log(response, file, fileList);
        },
        //初始化当前页要展示的日期
        initData: function (cur) {
            var date;
            var index = 0;   //控制显示预定的天数
            if (cur) {
                date = new Date(cur);
            } else {
                var now = new Date();
                var d = new Date(this.formatDate(now.getFullYear(), now.getMonth(), 1));
                d.setDate(35);
                date = new Date(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
            }
            this.currentDay = date.getDate();
            this.currentYear = date.getFullYear();
            this.currentMonth = date.getMonth() + 1;
            this.currentWeek = date.getDay(); // //本月第一天是周几（周日0 周六 6）
            var today = new Date();
            this.LocalMonth = today.getMonth() + 1;
            if (this.currentWeek == 0) {
                this.currentWeek = 7;
            }
            var str = this.formatDate(this.currentYear, this.currentMonth, this.currentDay);
            this.days.length = 0;
            // 今天是周日，放在第一行第7个位置，前面6个
            //初始化本周
            for (var i = this.currentWeek - 1; i >= 0; i--) {
                var d = new Date(str);
                d.setDate(d.getDate() - i);
                var dayobject = {};
                dayobject.day = d;
                var now = new Date();
                if (d.getDate() === (now.getDate()) && d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear()) {
                    dayobject.index = index++;//从今天开始显示供预定的数量
                }
                else if (index != 0 && index < 3)
                    dayobject.index = index++;//从今天开始3天内显示供预定的数量
                this.days.push(dayobject);//将日期放入data 中的days数组 供页面渲染使用
            }
            //其他周
            for (var i = 1; i <= 35 - this.currentWeek; i++) {
                var d = new Date(str);
                d.setDate(d.getDate() + i);
                var dayobject = {};//dayobject   {day:date,index:2}
                dayobject.day = d;
                var now = new Date();
                if (d.getDate() === (now.getDate()) && d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear()) {
                    dayobject.index = index++;
                }
                else if (index != 0 && index < 3)
                    dayobject.index = index++;
                this.days.push(dayobject);
            }

            /* this.leftobj = [
                 { date: 1, number: 120, reservations: 1 },
                 { date: 3, number: 120, reservations: 1 },
                 { date: 4, number: 120, reservations: 120 },
                 { date: 6, number: 120, reservations: 1 },
                 { date: 8, number: 120, reservations: 1 }
             ];*/

            axios.post("/ordersetting/getOrderSettingByMonth.do?date="+this.currentYear+"-"+
                this.currentMonth).then((res)=>{
                if(res.data.flag){
                    this.leftobj=res.data.data
                }else {
                    this.$message.error(res.data.message)
                }
            })
        },
        //切换到当前月份
        goCurrentMonth: function (year, month) {
            var d = new Date();
            this.initData(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
        },
        //向前一个月
        pickPre: function (year, month) {
            // setDate(0); 上月最后一天
            // setDate(-1); 上月倒数第二天
            // setDate(dx) 参数dx为 上月最后一天的前后dx天
            var d = new Date(this.formatDate(year, month, 1));
            d.setDate(0);
            this.initData(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
        },
        //向后一个月
        pickNext: function (year, month) {
            var d = new Date(this.formatDate(year, month, 1));
            d.setDate(35);////获取指定天之后的日期
            this.initData(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
        },
        // 返回 类似 2016-01-02 格式的字符串
        formatDate: function (year, month, day) {
            var y = year;
            var m = month;
            if (m < 10) m = "0" + m;
            var d = day;
            if (d < 10) d = "0" + d;
            return y + "-" + m + "-" + d
        }
    }
})