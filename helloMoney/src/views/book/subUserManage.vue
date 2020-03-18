<template>
  <div>
    <el-card style="margin-top:20px" >
      <el-form :inline="true" :model="formQuery" class="demo-form-inline" size="mini" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="formQuery.userName" placeholder="用户名" ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData"><i class="el-icon-search"></i>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="resetting"><i class="el-icon-refresh"></i>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top:10px">
      <!-- <el-button type="primary" size="mini" :disabled="disabled" style="float:left" @click="addList()"><i class="el-icon-circle-plus-outline"></i>&nbsp;新增</el-button><br><br> -->
      <el-table
        v-loading="loading"
        :data="tableDataFinally"
        stripe
        border
        size="mini"
      >
        <el-table-column
          align="center"
          label="书名"
          prop="bookName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="用户名"
          prop="uName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="借阅时间"
          prop="subDateTime"
        ></el-table-column>
        <el-table-column
          align="center"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button size="mini" :disabled="changeFlag(scope)" @click="isCuiHuan(scope)">催还</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { commonAPI } from "@/api/commonAPI";
export default {
  data() {
    return {
      formQuery:{
        userName:'',
        uId:''
      },
      formInline: {
        pageNum: 1,
        pageSize: 10
      },
      loading: false,
      tableDataDemo: [],
      tableDataUser: [],
      tableDataBook: [],
      tableData: [],
      tableDataFinally: []
    };
  },
  created() {
    this.getCommonData();
    this.getData();
  },
  watch:{
    'formQuery.userName'(newValue,oldValue){
      for(var i=0;i<this.tableDataUser.length;i++){
        if(newValue == this.tableDataUser[i].uName){
          this.formQuery.uId = this.tableDataUser[i].id
        }
      }
    }
  },
  methods: {
    getCommonData() {
      commonAPI("queryUserList", this.formInline).then(res => {
        this.tableDataUser = res.data.data.rows;
      });
      commonAPI("queryBookList", this.formInline).then(res => {
        this.tableDataBook = res.data.data.rows;
      });
    },
    getData() {
      this.loading = true;
      this.tableDataFinally = []
      commonAPI("querySub", { bookType: 1 ,uId:this.formQuery.uId}).then(res => {
        this.tableDataDemo = res.data.data.rows;
        this.tableData = res.data.data.rows;
        for (var i = 0; i < this.tableDataDemo.length; i++) {
          for (var j = 0; j < this.tableDataUser.length; j++) {
            if (this.tableDataDemo[i].uId == this.tableDataUser[j].id) {
              // json里添加额外内容
              Object.assign(this.tableDataDemo[i], {uName:this.tableDataUser[j].uName});
            }
          }
        }
        for (var i = 0; i < this.tableDataDemo.length; i++) {
          for (var j = 0; j < this.tableDataBook.length; j++) {
            if (this.tableDataDemo[i].bId == this.tableDataBook[j].bId) {
              Object.assign(this.tableDataDemo[i], {bookName:this.tableDataBook[j].bookName});
            }
          }
        }
        this.loading = false;
        this.tableDataFinally = this.tableDataDemo;
        console.log(this.tableDataFinally)
        this.tableDataDemo = [];
      });
    },
    // returntype:1未催还，2已催还，3已归还
    changeFlag(scope){
       if(scope.row.returntype != 1){
        return true
      }else{
        return false
      }
    },
    isCuiHuan(scope){
      this.loading = true;
      commonAPI("returnSubBook", {uId:scope.row.uId,bId:scope.row.bId,returntype:2,bookType:1}).then(res => {
        this.loading = false;
        if(res.data.data === "OK"){
          this.$message({
            message: "催还成功",
            type: "success"
          });
        }else{
          this.$message({
            message: "催还失败，请再次催还",
            type: "warning"
          });
        }
      })
    },
    resetting() {
      this.formQuery.userName = "";
      this.formQuery.uId = ''
      this.getData();
    }
  }
};
</script>

<style>
</style>
