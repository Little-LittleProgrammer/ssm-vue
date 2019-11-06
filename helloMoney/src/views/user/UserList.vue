<template>
  <el-container>
    <el-card style="margin-top:20px" >
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="formInline.userName" placeholder="用户名" ></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="formInline.sex" >
            <el-option label="全部" value=""></el-option>
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit"><i class="el-icon-search"></i>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="resetting"><i class="el-icon-refresh"></i>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="elMain" style="margin-top:10px">
      <el-button type="primary" size="mini" :disabled="disabled" style="float:left" @click="addList()"><i class="el-icon-circle-plus-outline"></i>&nbsp;新增</el-button><br><br>
      <el-table v-loading="loading" :data="tableData" stripe border size="mini">
        <el-table-column align="center" label="编号" prop="uid"></el-table-column>
        <el-table-column align="center" label="用户名" prop="userName"></el-table-column>
        <el-table-column align="center" label="姓名" prop="uName"></el-table-column>
        <el-table-column align="center" label="年龄" prop="age"></el-table-column>
        <el-table-column align="center" label="征信分" prop="score">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.score >=25 ? 'success' : 'danger'"
              disable-transitions>{{scope.row.score}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="性别">
          <template slot-scope="scope">
            <span v-if="scope.row.sex===0">男</span>
            <span v-if="scope.row.sex===1">女</span>
            <span v-if="scope.row.sex===6">***</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" :disabled="isYou(scope)" @click="addList(scope)">编辑</el-button>
            <el-button size="mini" :disabled="disabled" @click="delUser(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-footer>
      <div style="padding: 15px 0;text-align: right;">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="formInline.pageNum"
          :page-sizes="pageSizes" :page-size="formInline.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pageTotal">
        </el-pagination>
      </div>
    </el-footer>

    <!-- dialog -->
    <addUser :option="addIn" @refreshTable="getData"></addUser>
  </el-container>
</template>

<script>
  import {commonAPI} from '@/api/commonAPI'
  import addUser from './dailog/addUser'
  export default {
    components:{
      addUser
    },
    data() {
      return {
        disabled:this.$store.state.token === "admin"?false:true,
        tableData: [],
        loading: false,
        formInline: {
          userName: '',
          sex: '',
          pageNum:1,
          pageSize:10
        },
        addIn:{
          isShow:false,
          title:"",
          flag:'',
          tableData:'',
          isDisabled:''
        },
        pageSizes:[5, 10, 15, 20],
        pageTotal:0
      }
    },
    created() {
      this.getData();
    },
    methods: {
      getData() {
        this.loading = true;
        commonAPI('queryUserList',this.formInline)
        .then(res => {
          console.log(res.data)
          this.loading = false;
          this.tableData = res.data.data.rows
          for(var i=0;i<this.tableData.length;i++){
            if(this.$store.state.token == "editor"&& this.tableData[i].userName !=this.$store.state.userName){
              this.tableData[i].uid ="***"
              this.tableData[i].userName ="***"
              this.tableData[i].uName ="***"
              this.tableData[i].score = "***"
              this.tableData[i].age ="***"
              this.tableData[i].sex =6
            }
          }
          this.pageTotal=res.data.data.total;
        })
      },
      onSubmit() {
        this.getData();
      },
      resetting() {
        this.formInline.userName = '';
        this.formInline.sex = '';
        this.getData();
      },
      handleSizeChange(val) {
        //console.log(`每页 ${val} 条`);
        this.formInline.pageSize=val;
        this.formInline.pageNum=1;
        this.getData();
      },
      handleCurrentChange(val) {
        //console.log(`当前页: ${val}`);
        this.formInline.pageNum=val;
        this.getData();
      },
      isYou(val){
        if (this.$store.state.userName != val.row.userName && this.$store.state.token == "editor"){
          return true
        } else {
          return false
        }
      },
      addList(val){
        this.addIn.isShow=true
        if(val){
          this.addIn.title ='修改用户'
          this.addIn.tableData = val.row
          this.addIn.isDisabled =true
        } else {
          this.addIn.title ='新增用户'
          this.addIn.tableData ={}
          this.addIn.isDisabled = false
        }
      },
      delUser(val){
        this.loading = true;
        var usNm = val.row.userName
        commonAPI('delUser',{userName:usNm})
        .then(res => {
          this.loading = false;
          if (res.data.data === "OK") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.getData()
          } else {
            this.$message({
              message: "删除失败",
              type: "warning"
            });
          }
        })
      },
    }
  }
</script>

<style scoped>
  .elInput,
  .btn {
    margin-top: 18px;
  }
</style>
<style>
  .el-main {
    padding: 5px 10px;
  }
  .elMain{
    height: 468px;
  }
  .el-table th {
    height: 45px;
    font-size: 16px;
    font-family: 微软雅黑;
    font-weight: 500;
    color: darkblue;
  }
</style>
