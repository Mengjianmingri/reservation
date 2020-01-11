var vue = new Vue({
    el: '#app',
    data:{
        reportData:{
            reportDate:null,
            todayNewMember :0,
            totalMember :0,
            thisWeekNewMember :0,
            thisMonthNewMember :0,
            todayOrderNumber :0,
            todayVisitsNumber :0,
            thisWeekOrderNumber :0,
            thisWeekVisitsNumber :0,
            thisMonthOrderNumber :0,
            thisMonthVisitsNumber :0,
            hotSetmeal :[
                {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
                {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
            ]
        }
    },
    created() {
        axios.get("/report/getBusinessReportData.do").then((res)=>{
            this.reportData = res.data.data;
        });
    },
    methods:{
        exportExcel(){
            window.location.href = '/report/exportBusinessReport.do';
        },
        exportPDF(){
            window.location.href = '/report/exportBusinessReport4PDF.do';
        }
    }
})