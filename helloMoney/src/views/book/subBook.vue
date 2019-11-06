<template>
  <div>
    <el-card style="margin-top:20px">
      <el-form :inline="true" :model="formQuery" size="mini" label-width="80px">
        <el-form-item label="书名">
          <el-input v-model="formQuery.bookName" placeholder="书名" ></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="formQuery.author" placeholder="作者" ></el-input>
        </el-form-item>
       <el-form-item>
          <el-button type="primary" @click="getData"><i class="el-icon-search"></i>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="resetting"><i class="el-icon-refresh"></i>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="elMain">
      <el-button type="primary" size="mini" @click="openDialog"><i class="el-icon-printer"></i>&nbsp;打印</el-button><br><br>
      <el-table v-loading="loading" :data="tableData" stripe border size="mini">
        <el-table-column align="center" label="编号" prop="bId"></el-table-column>
        <el-table-column align="center" label="书名" prop="bookName"></el-table-column>
        <el-table-column align="center" label="作者" prop="author"></el-table-column>
        <el-table-column align="center" label="库存" prop="inventory"></el-table-column>
        <el-table-column align="center" label="单价" prop="price"></el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="getDate(scope)">借阅</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div style="padding: 15px 0;text-align: right;">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="formQuery.pageNum"
        :page-sizes="pageSizes" :page-size="formQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pageTotal">
      </el-pagination>
    </div>

    <printSubBook :option="printDialog"></printSubBook>
  </div>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
import printSubBook from './dialog/printSubBook'

export default {
   components:{
      printSubBook
    },
  data(){
    return {
      formQuery:{
        bookName:'',
        author:'',
        pageNum:1,
        pageSize:10
      },
      tableData:[],
      pageSizes:[5,10,20,40],
      subManage:{
        uId:Number,
        bId:Number,
        bookType: 1,// 1为借阅，2为归还
        subDateTime:'',
        returntype: 1//1为未催还，2为催还，3为已归还
      },
      bookTotal:Number,
      pageTotal:Number,
      printDialog:{
        isShow:false,
        tableData:[]
      }
    }
  },
  created(){
    this.getData()
  },
  methods:{
    getData(){
      this.loading = true;
        commonAPI('queryBookList',this.formQuery)
        .then(res => {
          console.log(res.data)
          this.loading = false;
          this.tableData = res.data.data.rows;
          this.pageTotal=res.data.data.total;
        })
    },
    handleSizeChange(val){
      this.formQuery.pageSize=val;
      this.formQuery.pageNum=1;
      this.getData();
    },
    handleCurrentChange(val) {
      //console.log(`当前页: ${val}`);
      this.formQuery.pageNum=val;
      this.getData();
    },
    getDate(val){
      var now =new Date();
      var year =now.getFullYear();
      var month =now.getMonth();
      var date =now.getDate();
      var hour =now.getHours();
      var minu =now.getMinutes();
      var sec = now.getSeconds();//得到秒
      month =month+1;
      if(month<10) month= "0"+month;
      if (date < 10) date = "0" + date;
      if (hour < 10) hour = "0" + hour;
      if (minu < 10) minu = "0" + minu;
      if (sec < 10) sec = "0" + sec;
      var time = '';
      time = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec
      commonAPI('queryUserList',{userName:this.$store.state.userName,pageNum:1, pageSize:10})
      .then(ress =>{
        if(ress.data.data.rows[0].score < 25){
          this.$notify({
            title:'紧急消息',
            message:'您已被列入失信用户，请联系管理员',
            type:'warning',
            duration: 0,
          })
        }else{
          commonAPI('querySub',{uId:this.$store.state.uId,bId:val.row.bId})
          .then(res =>{
            var i =res.data.data.rows.length-1
              if(res.data.data.rows[i].bookType === 1){
                this.$message({
                  message: "已借阅此书，上次借阅时间："+res.data.data.rows[i].subDateTime,
                  type: "warning"
                })
              }else if(val.row.inventory <= 0){
                this.$message({
                  message: "书籍已没有库存",
                  type: "warning"
                })
              }else{
                this.subBook(val,time)
              }
          })
          .catch(error => {
            this.subBook(val,time)
          })
        }
      })
    },
    subBook(val,time){
      this.loading = true;
      this.subManage.uId = this.$store.state.uId
      this.subManage.bId = val.row.bId
      this.subManage.subDateTime = time
      commonAPI('addSubBook',this.subManage)
      .then(res => {
        var total = val.row.inventory -1
        commonAPI('upInventories',{inventory:total,bId:val.row.bId})
        .then(res1 => {
          this.loading = false;
          console.log("res",res1)
          if(res1.data.data === "OK"){
            this.$message({
              message: "借阅成功，请记得归还",
              type: "success"
            });
          }else{
            this.$message({
              message: "借阅失败，请联系管理员",
              type: "warning"
            });
          }
        })
        this.getData()
      })
      .catch(error => {
        this.loading = false;
        this.$notify({
          //这里采用element ui的一个错误显示效果模板
          title: "温馨提示",
          message: "借阅失败，请联系管理员",
          position: "bottom-right",
          type: "error"
        });
      });
    },
    resetting() {
      this.formQuery.bookName = '';
      this.formQuery.author = '';
      this.getData();
    },
    openDialog(){
      this.printDialog.isShow = true
      this.printDialog.tableData = this.tableData
    }
  }
}
</script>

<style>
  .elMain{
    height: 468px;
    margin-top:10px;
  }
</style>
