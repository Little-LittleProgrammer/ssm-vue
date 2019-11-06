<template>
  <div>
    <!-- <el-card style="margin-top:20px">
      <el-form :inline="true" :model="formQuery" size="mini" label-width="80px">
        <el-form-item label="书名">
          <el-input v-model="formQuery.bookName" placeholder="书名" ></el-input>
        </el-form-item>
       <el-form-item>
          <el-button type="primary" @click="getData"><i class="el-icon-search"></i>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="resetting"><i class="el-icon-refresh"></i>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card> -->
        <el-table v-loading="loading" :data="newList" stripe border size="mini">
          <el-table-column align="center" label="书名" prop="bookName"></el-table-column>
          <el-table-column align="center" label="作者" prop="author"></el-table-column>
          <el-table-column align="center" label="借阅时间" prop="subDateTime"></el-table-column>
          <el-table-column align="center" label="状态" :sortable="true" :sort-method="sortSubBook">
            <template slot-scope="scope">
              <span v-if="scope.row.bookType===1">借阅</span>
              <span v-if="scope.row.bookType===2">已归还</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button size="mini" :disabled="isReturnBook(scope)" @click="getDate(scope)">归还</el-button>
            </template>
          </el-table-column>
        </el-table>
      <div style="padding: 15px 0;text-align: right;">
        <el-pagination
          background
          style="float:right"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 40,1000]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
  </div>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
export default {
  data(){
    return{
      formQuery:{
        bookName:''
      },
      loading:false,
      loading:false,
      tableData:[],
      tableSubData:[],
      currentPage: 1,
      newList:[],
      pageSize:undefined,
      total:undefined,
    }
  },
  created(){
    this.getData()
  },
  watch: {
    loading: {
    　　handler() {
      　　this.tableData = []
    　　},
    　　immediate: true
    }
  },
  methods:{
    getData(){
      this.loading =true
      commonAPI('querySub',{uId:this.$store.state.uId})
      .then(res =>{
        for(var i=0;i<res.data.data.rows.length;i++){
          this.tableData.push(res.data.data.rows[i])
          var j = 0
          commonAPI('queryBookList',{bId:res.data.data.rows[i].bId,pageNum:1,pageSize: 10})
          .then(res1 =>{
            Object.assign(this.tableData[j], res1.data.data.rows[0]);
            j++
            if( j === i){
              this.loading =false
              this.tableSubData =this.tableData
              this.total =this.tableSubData.length
              this.pageSize =5
              this.newList = this.tableSubData.slice((this.currentPage-1)*this.pageSize,this.pageSize*this.currentPage)
            }
          })
        }
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.newList = this.tableSubData.slice((this.currentPage-1)*this.pageSize,this.pageSize*this.currentPage)
    },
    handleCurrentChange(val) {
      this.currentPage =val
      this.newList = this.tableSubData.slice((this.currentPage-1)*this.pageSize,this.pageSize*this.currentPage)
    },
    getDate(scope){
      var now =new Date();
      var year =now.getFullYear();
      var month =now.getMonth();
      var date =now.getDate();
      var hour =now.getHours();
      var minu =now.getMinutes();
      var sec = now.getSeconds();//得到秒
      month =month+1;
      if(month < 10) month= "0"+month;
      if (date < 10) date = "0" + date;
      if (hour < 10) hour = "0" + hour;
      if (minu < 10) minu = "0" + minu;
      if (sec < 10) sec = "0" + sec;
      var time = '';
      time = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec
      console.log(scope)
      this.returnBook(scope,time)
    },
    returnBook(scope,time){
      this.loading = true
      commonAPI('returnSubBook',{bId:scope.row.bId,uId:scope.row.uId,bookType:'1',returnDateTime:time}).then(
        commonAPI('returnSubBook',{bId:scope.row.bId,bookType:'2',returntype:3})
        .then(res =>{
          var total = scope.row.inventory +1
          commonAPI('upInventories',{inventory:total,bId:scope.row.bId})
          .then(res1=>{
            this.loading = false;
            console.log("res",res1)
            if(res1.data.data === "OK"){
              this.$message({
                message: "归还成功",
                type: "success"
              });
            }else{
              this.$message({
                message: "归还失败，请联系管理员",
                type: "warning"
              });
            }
          })
          this.getData()
        })
      )
    },
    isReturnBook(scope){
      if(scope.row.bookType === 2){
        return true
      }else{
        return false
      }
    },
    sortSubBook(a,b){
      return a.bookType-b.bookType
    }
  }
}
</script>

<style>

</style>
