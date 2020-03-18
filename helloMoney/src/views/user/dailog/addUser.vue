<template>
  <el-dialog
    :visible.sync="isShow"
    :title="title"
    center
    @close = "closeDialog"
    @open = "openDialog"
  >
    <el-card>
      <el-form ref="addData" :model="addData" :rules="rules" label-width="100px" label-position="left">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="userName" label="账号:">
              <el-input v-model="addData.userName" placeholder="请输入账号" :disabled="isDisabled" @blur="userNameCheck"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="password" label="密码:">
              <el-input type="password" v-model="addData.password"  placeholder="请输入密码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="uName" label="用户名:">
              <el-input v-model="addData.uName"  placeholder="请输入用户号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="age" label="年龄:">
              <el-input v-model="addData.age"  placeholder="请输入密码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="uid" label="用户Id:">
              <el-input v-model="addData.uid" placeholder="用户Id" :disabled="isDisabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="sex" label="性别:">
              <el-select v-model="addData.sex" >
                <el-option v-for="item in sexSelect" :key="item.key"
                  :label="item.value"
                  :value="item.key">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div style="text-align:right;margin:20px 0">
        <el-button size="mini" @click="closeDialog">取消</el-button>
        <el-button type="primary"  size="mini" @click="addUser">确认</el-button>
    </div>
  </el-dialog>
</template>


<script>
import {commonAPI} from '@/api/commonAPI'
import crypto from 'crypto'
export default {
  props: {
    option: Object
  },
  data(){
    const checkuser = (rule,value,callback) =>{
      console.log(this.alreadyUser)
      if(value === this.alreadyUser){
        callback(new Error('用户已存在'))
      } else {
        callback()
      }
    }
    return{
      sexSelect:[
        {key:0,value:'男'},
        {key:1,value:'女'},
      ],
      tableData:'',
      addData:{
        userName:'',
        password:'',
        uid:'',
        uName:'',
        age:'',
        sex:'',
        pageNum:1,
        pageSize:10
      },
      formInline: {
        userName: '',
        sex: '',
        pageNum:1,
        pageSize:10
      },
      disabled:this.$store.state.token === "admin"?false:true,
      isDisabled:'',
      flag:false,
      alreadyUser:'',
      title:'',
      rules:{
        userName: [{required:true,message:'请输入账号',trigger:'blur'}],
        password:[{required:true,message:'请输入密码',trigger:'blur'},
                  {required:true,message:'密码不能小于5位',trigger:'change',min:5}],
        uName:[{required:true,message:'请输入用户名',trigger:'blur'}],
        age:[{required:true,message:'请输入年龄',trigger:'blur'}],
        uid:[{required:true,message:'请输入UId',trigger:'blur'}],
        sex:[{required:true,message:'请输入性别',trigger:'blur'}]
      }
    }
  },
  computed: {
    isShow: {
      // 修改用户弹窗是否显示
      get: function() {
        return this.option.isShow;
      },
      set: function(newValue) {
        this.option.isShow = newValue;
      }
    }
  },
  methods:{
    openDialog(){
      this.title =this.option.title
      this.isDisabled = this.option.isDisabled
      this.addData = JSON.parse(JSON.stringify(this.option.tableData))
    },
    userNameCheck(){
      this.loading = true,
        commonAPI('queryUserList',this.formInline)
        .then(res => {
          this.loading = false;
          this.tableData = res.data.data.rows;
          for(var i=0; i<this.tableData.length;i++){
            if(this.addData.userName === this.tableData[i].userName){
              this.$message.error('账号已存在');
              this.alreadyUser = this.tableData[i].userName;
              this.flag = true;
              break
            }
          }
        })
    },
    closeDialog(){
      this.$refs['addData'].resetFields();
      this.flag = false
      this.option.isShow = false;
      this.$emit('refreshTable');
    },
    addUser(){
      if(this.flag === false){
        this.$refs.addData.validate(valid => {
          if (valid) {
            var  md5 = crypto.createHash("md5");
            md5.update(this.addData.password) //需要加密的密码
            this.addData.password = md5.digest('hex');  //password 加密完的密码
            console.log(this.addData.password);
            if(this.option.isDisabled === false){
              this.loading = true,
              commonAPI('addUser',this.addData)
                .then(res => {
                  this.loading = false;
                  let data = res.data;
                  if (data.info.code === "0" && data.data === "OK") {
                    this.option.isShow = false;
                    this.$message({
                      message: "新增成功",
                      type: "success"
                    });
                  } else {
                    this.$message({
                      message: "新增失败",
                      type: "warning"
                    });
                  }
                })
            } else {
              this.loading = true;
              commonAPI('upUser',this.addData)
              .then(res => {
                this.loading = false;
                if (res.data.data === "OK") {
                  this.option.isShow = false;
                  this.$message({
                    message: "修改成功",
                    type: "success"
                  });
                } else {
                  this.$message({
                    message: "修改失败",
                    type: "warning"
                  });
                }
              })
            }
          }
        })
      } else {
        this.$message.error('账号已存在');
      }
    }
  }
}
</script>
