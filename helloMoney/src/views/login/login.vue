<template>
  <div class="login-container">
    <div v-if="flag" >
      <div style="margin-top:3%;margin-left:6%">
        <span class="title"><h1>图书统一管理系统</h1></span>
    </div>
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        autocomplete="on"
        label-position="left"
      >
        <div class="title-container">
          <h3 class="title">登 &nbsp; 录 &nbsp; 界 &nbsp; 面</h3>
        </div>
        <!-- tabindex :是定义用于tab键切换输入框的序号 -->
        <el-form-item prop="userName">
          <span class="svg-container">
            <i class="el-icon-user-solid"></i>
          </span>
          <el-input
            ref="userName"
            v-model="loginForm.userName"
            placeholder="用户名"
            name="userName"
            type="text"
            tabindex="1"
            autocomplete="on"
          />
        </el-form-item>

        <el-tooltip
          v-model="capsTooltip"
          content="大写开启"
          placement="right"
          manual
        >
          <el-form-item prop="passWord">
            <span class="svg-container">
              <i class="el-icon-lock"></i>
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.passWord"
              :type="passwordType"
              placeholder="密码"
              name="password"
              tabindex="2"
              autocomplete="on"
              @keyup.native="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleLogin"
            />
            <span
              class="show-pwd"
              @click="showPwd"
            >
              <i class="el-icon-view"></i>
            </span>
          </el-form-item>
        </el-tooltip>

        <el-form-item prop="verificationCode">
          <span class="svg-container">
            <i class="el-icon-edit" />
          </span>
          <el-input
            v-model="loginForm.verificationCode"
            name="verification-code"
            type="text"
            placeholder="验证码"
            @keyup.enter.native="handleLogin"
          />
          <span class="code">
            <verification-code @rendom="value=>rendomStr = value" />
          </span>
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;margin-bottom:30px;"
          @click="handleLogin"
        >登录</el-button>
      </el-form>
    </div>
    <div v-else class="imgDiv"  @click="change">
      <el-carousel height="866px">
        <el-carousel-item v-for="(item,index) in image" :key="index">
          <img v-bind:src="item.src" alt="图片" style="max-width: 100%;max-height: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
import layout from '../layout/Layout'
import crypto from 'crypto'
import VerificationCode from '@/components/VerificationCode/index'

export default {
  components:{layout,VerificationCode},
  data() {
    const validateCode = (rule, value, callback) => {
      if (value.toUpperCase() !== this.rendomStr.toUpperCase()) {
        callback(new Error('验证码有误！'))
      } else {
        callback()
      }
    }
    return {
      rendomStr: '',
      watchAds:Object,
      watchAdss:Object,
      flag:false,
      loginForm: {
        userName: "",
        password: "",
        passWord: "",
        verificationCode: ""
      },
      image:[
        {src:require('./static/1.jpg')},
        {src:require('./static/2.jpg')},
        {src:require('./static/3.jpg')},
        {src:require('./static/4.jpg')}
      ],
      capsTooltip: false,
      passwordType: "password",
      loading: false,
      loginRules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            message: "请填写用户名"
          }
        ],
        passWord: [
          {
            required: true,
            trigger: "blur",
            message: "请填写密码"
          },
          { type: "string", min: 6, message: "密码至少为6位", trigger: "blur" }
        ],
        verificationCode: [{ required: true, trigger: 'blur', validator: validateCode }]
      }
    };
  },
  watch: {
    'loginForm.userName'(newName, oldName) {
      console.log(this.watchAds)
      let _this=this
      clearTimeout(this.watchAds)
      clearTimeout(this.watchAdss)
      this.watchAdss = setTimeout(function()  {
          _this.changeAds()
        },30000)
    }
  },
  methods: {
    change(){
      this.flag =true;
      let _this=this
      this.watchAds = setTimeout(function()  {
          _this.changeAds()
        },30000)
    },
    changeAds(){
      this.flag =false
      this.loginForm.userName = ''
      this.loginForm.password = ''
    },
    checkCapslock({ shiftKey, key } = {}) {
      console.log('shiftKey',shiftKey)
      console.log('key',key)
      if (key && key.length === 1) {
        if (
          (shiftKey && (key >= "a" && key <= "z")) ||
          (!shiftKey && (key >= "A" && key <= "Z"))
        ) {
          this.capsTooltip = true;
        } else {
          this.capsTooltip = false;
        }
      }
      if (key === "CapsLock" && this.capsTooltip === true) {
        this.capsTooltip = false;
      }
    },
    showPwd(e) {
      console.log(e.target.style)
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      var el = e.target;
      this.passwordType == ""
        ? el.setAttribute("style", "color: #409EFF")
        : el.setAttribute("style", "color: #c0c4cc");
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          var  md5 = crypto.createHash("md5");
          md5.update(this.loginForm.passWord) //需要加密的密码
          this.loginForm.password = md5.digest('hex');  //password 加密完的密码
          console.log(this.loginForm.password);
            //封装的公共请求API
            commonAPI("queryUser", this.loginForm)
              .then(res => {
                let data = res.data;
                console.log(data)
                if (data.info.code === "0" && data.data.status === "OK") {
                  this.$store.dispatch("changeUserName",this.loginForm.userName)
                  this.$store.dispatch("saveToken",data.data.token)
                  this.loading = false;
                  setTimeout(() => {
                    this.$router.push({
                      name: "layoutYHGL"
                    });
                  },2000)
                } else {
                  this.$message({
                    message: "用户名或密码错误",
                    type: "warning"
                  });
                  this.loading = false;
                }
              })
              .catch(error => {
                this.loading = false;
                this.$notify({
                  //这里采用element ui的一个错误显示效果模板
                  title: "登录提示",
                  message: error.message,
                  position: "bottom-right",
                  type: "error"
                });
              });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>

<style lang="scss">

/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 5px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 16px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .code {
    position: absolute;
    right: 0px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }

  .title {
        font-size: 20px;
        color: #eeeeee;
      }
}
</style>
