<template>
  <el-dialog
    width = '250px'
    :visible.sync="isShow"
    @open="openDialog"
    @close="closeDialog"
    :show-close ="false"
    title="扫码登陆"
    center>
      <!-- <wxlogin  :appid="'wx8ab28949b96fac23'" :scope="'snsapi_login'"  :redirect_uri="'http://localhost:8686/#/user'"  ></wxlogin> -->
      <div class="div_box">
        <div id="qrCode" ref="qrCodeDiv" class="qr_code"></div>
      </div>
  </el-dialog>
</template>

<script>
/*
  先是发通讯获取二维码展示地址和token，一般是QRcodeurl
  展示出来
  扫码后发送通讯,（app确定登录，向后端发送通讯），管理端将QRcodeurl发送，每隔一秒去查询接口，根据后台返回的数据去判断app端是否登录
  清空轮询发通讯

*/
  import QRCode from 'qrcodejs2';  //在指定页面引入，也可以在main.js里面全局引用
  import {commonAPI} from '@/api/commonAPI'
export default {
  name:'qrCode',
   props: {
    option: Object
  },
  data() {
    return {
      QRcodeurl:'',
      timer:'',
      token:'',
      loginForm:{
        userName:'',
        password:'',
        pageNum:1,
        pageSize:10
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
  methods: {
    openDialog(){
      this.$nextTick(function () {
        this.bindQRCode();
      })
    },
    closeDialog(){
      var obj = document.getElementById('qrCode')
      obj.innerHTML =''
      console.log(obj)
      clearInterval(this.timer);
      this.timer = null;
    },
    loginqrcode(){
       let _this=this;
      //  _this.tab_mue()
      commonAPI('loginUri','').then(res => {
        if(res.data.code == 200){
               _this.QRcodeurl=res.data.data
               _this.$bindQRCode(_this.QRcodeurl)   //由后台提供的长连接生成的二维码
               _this.loginUriState()
           }
           else {
               _this.$Message.error(res.data.message)
           }
       }).catch( error =>{
           console.log(error)
       })
   },
   //扫码后发送通讯，轮番去查是否有用户扫码后未登入
    loginUriState(){
      let _this=this;
      commonAPI('queryLoginStatus',{token:_this.token}).then( res =>{
        if(res.data.status == 200){
          this.loginForm.userName = res.data.data.data[0].userName;
          console.log('this.loginForm.userName',this.loginForm.userName)
          commonAPI('queryUserList',this.loginForm).then(res=>{
            this.loginForm.password = res.data.data.rows[0].password
            commonAPI("queryUser", this.loginForm)
              .then(res => {
                let data = res.data;
                console.log(data)
                if (data.info.code === "0" && data.data.status === "OK") {
                  this.$store.dispatch("changeUserName",this.loginForm.userName)
                  this.$store.dispatch("saveToken",data.data.token)
                  setTimeout(() => {
                    this.$router.push({
                      name: "layoutYHGL"
                    });
                  },1000)
                  this.option.isShow = false
                }
              })
          })
        }
        else{   //在不扫码的时候没三秒轮循一次接口
            _this.timer=setTimeout( () =>{
                _this.loginUriState()
            },3000)
        }
      })
    },
    bindQRCode() {
       commonAPI('loginByQR').then(res=>{
         console.log(res)
        if(res.data.data.status === 'OK'){
          console.log('res')
          this.token = res.data.data.token
          this.QRcodeurl = res.data.data.QR
          let _this=this
          new QRCode(this.$refs.qrCodeDiv, {
            text: _this.QRcodeurl +'='+this.token,
            width: 200,
            height: 200,
            colorDark: "#333333", //二维码颜色
            colorLight: "#ffffff", //二维码背景色
            correctLevel: QRCode.CorrectLevel.L//容错率，L/M/H
          })
          this.loginUriState()
        }
      })
    }
  }
}
</script>

<style>
.text{
  text-align: center;
}
</style>
