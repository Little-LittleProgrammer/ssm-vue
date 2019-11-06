<template>
  <el-dialog
    :visible.sync="isShow"
    title="修改密码"
    center
    @open="openDialog"
  >
    <el-form
      ref="loginData"
      :model="loginData"
      label-width="130px"
      :rules="rules"
    >
      <el-form-item
        label="密码："
        prop="password"
      >
        <el-input
          v-model="loginData.password"
          type="password"
          placeholder="请输入密码"
          style="width:77%"
        />
        <!-- <span class="show-pwd" @click="showPwd">
          <i class="el-icon-view" />
        </span> -->
      </el-form-item>
      <el-form-item
        label="请再次输入密码:"
        prop="repassWord"
      >
        <el-input
          v-model="loginData.repassWord"
          type="password"
          placeholder="请输入密码"
          style="width:77%"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="login"
        >修改密码</el-button>
        <el-button @click="resetForm('loginData')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
import crypto from 'crypto'
export default {
  props: {
    option: Object
  },
  data() {
    const validatePwd = (rule, value, callback) => {
      if (value != this.loginData.password) {
        callback(new Error("两次密码不一致"));
      } else {
        callback();
      }
    };
    return {
      title: "",
      loginData: {
        userName: "",
        password: "",
        repassWord:''
      },
      flag: false,
      msg: "",
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "不得少于六个字符", trigger: "blur" }
        ],
        repassWord: [{ required: true, validator: validatePwd }]
      }
    };
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
  methods: {
    openDialog() {
      this.loginData.userName = this.option.userName;
      console.log(this.loginData.userName);
    },
    closeDialog(formName) {
      this.option.isShow = false;
      this.$refs[formName].resetFields();
      this.regiestData = {
        userName: "",
        password: ""
      };
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    login() {
      this.$refs["loginData"].validate(valid => {
        if (valid) {
          this.$confirm("此操作将修改密码, 是否继续?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              console.log(this.loginData)
              this.option.isShow = false;
              var  md5 = crypto.createHash("md5");
              md5.update(this.loginData.password) //需要加密的密码
              this.loginData.password = md5.digest('hex');  //password 加密完的密码
              console.log(this.loginData.password);
              commonAPI("upUserPass", this.loginData).then(res => {
                let data = res.data;
                if (data.data === "OK") {
                  this.$message({
                    type: "success",
                    message: "修改成功!"
                  });
                }
              });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消修改"
              });
            });
        }
      });
    }
  }
};
</script>
