<template>
  <el-container style="height: 100%;" class="abc" :class="{active:isActive}">
    <!-- 头部 -->
    <el-header style="">
      <span class="el-dropdown-link" style="color: #eeeeee;font-size:36px">
        图书管理
      </span>
      <el-dropdown trigger="click" @command="handleCommand" style="float: right;">
        <span class="el-dropdown-link" style="color: #eeeeee;font-size: 16px">
          你好，{{uName}}<i class="el-icon-setting el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">修改密码</el-dropdown-item>
          <el-dropdown-item command="b">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-header>
    <el-container>
      <!-- 左侧栏 -->
      <div class="el-aside">
        <!-- or v-show -->
        <el-button @click="changeHiddlea" id="a" style="float:right;border:none;height:0px;outline:none;display:none;background-color: rgb(238, 241, 246);" >>></el-button>
        <el-button @click="changeHiddleb" id="b" style="float:right;border:none;height:0px;outline:none;display:block;background-color: rgb(238, 241, 246);" >{{left}}</el-button><br>
        <el-menu :collapse="isCollapse" class="el-menu-vertical-demo" unique-opened = true>
          <!--如果菜单(menu)是true 循环侧栏路由列表  -->
          <template
            v-for="item in menuData"
            v-if="hiddlen(item)"
          >
            <!-- 这里必须设置index,相当唯一标识这个菜单标签，否则菜单列表点击后随意展开 -->
            <el-submenu
              :index="item.meta.funcNode"
              :key="item.path"
            >
              <template slot="title"><i :class="item.meta.icon"></i><span slot="title">{{item.meta.title}}</span></template>
              <!-- 如果菜单有孩子菜单，则循环孩子菜单 -->
              <template
                v-for="itemC in item.children"
                v-if="item.children"
              >
                <el-menu-item
                  :index="itemC.meta.funcNode"
                  :key="itemC.path"
                  @click="clickMenu(itemC)"
                ><i :class="itemC.meta.icon"></i><span slot="title">{{itemC.meta.title}}</span></el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-menu>
      </div>
      <!-- 内容渲染 -->
      <el-main style="background-color: white;">
        <router-view />
      </el-main>
    </el-container>

    <!-- dialog -->
    <loginUser
      :option="dialogLogin"
    />
  </el-container>
</template>

<script>
import loginUser from "./dialog/updatePassword";
import {commonAPI} from '@/api/commonAPI';
export default {
  components: { loginUser },
  data() {
    return {
      /*
         获取挂载的routes
        （$router相当于一个全局的路由器对象，里面含有很多属性和子对象 ）
        （options是一个对象，有成员：1、data函数成员  2、methods对象成员  3、模板template    4、挂载元素el
        5、生命周期钩子   6、props属性声明   7、computed计算成员    8、watch监视成员）
        */
      menuData: this.$store.getters.getRoutes,
      userName: "",
      activeIndex2: "1",
      formInline: {
        userName: '',
        sex: '',
        pageNum:1,
        pageSize:10
      },
      dialogLogin: {
        isShow: false,
      },
      tableData:[],
      uName:"",
      isActive:true,
      bookName:[],
      messageList:[],
      isCollapse: false,
      left:'<<'
    };
  },
  created(){
    this.getData();
  },
  mounted(){
    window.addEventListener('mousedown',this.onMouseDown,false)
    this.onMouseDown()
  },
  methods: {
    changeHiddlea(){
      console.log(this.isCollapse)
      this.isCollapse = false
      document.getElementById('a').style.display = 'none';
      document.getElementById('b').style.display = 'block';
    },
    onMouseDown(event){
      if (event === undefined) {
        return
      } else {
        if (event.button == 0) {
          clearTimeout(this.watchClick)
          clearTimeout(this.watchClicks)
          const _this = this
          this.watchClicks = setTimeout(() => {
            sessionStorage.clear()
            _this.exitLogin()
          }, 1000 * 60 * 30);
          console.log('鼠标左键!')
        } else if (event.button == 2) {
          clearTimeout(this.watchClick)
          clearTimeout(this.watchClicks)
          const _this = this
          this.watchClicks = setTimeout(() => {
            sessionStorage.clear()
            _this.exitLogin()
          }, 1000 * 60 * 30);
          console.log('鼠标右键!');
        } else if (event.button == 1) {
          clearTimeout(this.watchClick)
          clearTimeout(this.watchClicks)
          const _this = this
          this.watchClicks = setTimeout(() => {
            sessionStorage.clear()
            _this.exitLogin()
          }, 1000 * 60 * 30);
          console.log('鼠标滚轮!');
        }
      }
    },
    changeHiddleb(){
      console.log(this.isCollapse)
      this.isCollapse =true
      document.getElementById('b').style.display = 'none';
      document.getElementById('a').style.display = 'block';
    },
    handleCommand(command){
      if(command == 'a'){
        this.updataPassword()
      }else if(command == 'b'){
        this.exitLogin()
      }
    },
    clickMenu(item) {
      this.$router.push({ path: item.path }); //跳转的路由对象
      //this.$router.push({name:item.name})    通过name跳转
    },
    exitLogin() {
      this.$router.push({ path: "/login" });
      this.$store.dispatch("resetToken")
    },
    updataPassword() {
      this.dialogLogin.isShow = true;
    },
    getData() {
      this.loading = true,
      this.formInline.userName = this.$store.getters.getUserName
      commonAPI('queryUserList',this.formInline)
      .then(res => {
        console.log(res)
        this.tableData = res.data.data.rows;
        this.uName=this.tableData[0].uName
        this.$store.dispatch("setUId",this.tableData[0].id)
        commonAPI('querySub',{uId:this.$store.getters.getUId,bookType:1,returntype:2})
        .then(res1 =>{
          this.messageList = res1.data.data.rows
          var j =0
          for(var i=0;i<this.messageList.length;i++){
            commonAPI('queryBookList',{bId:this.messageList[i].bId,pageNum:1,pageSize:10}).then(res2 =>{
              this.bookName.push(res2.data.data.rows[0].bookName)
              if(j==this.messageList.length-1){
                console.log(this.bookName)
                if(this.messageList !=null || this.messageList !=''){
                  this.loading = false;
                  for(var i=0;i<this.bookName.length;i++){
                    this.$notify({
                      title:'催还消息',
                      message:'您借阅的《'+this.bookName[i]+'》即将到需归还日期，请及时归还',
                      type:'warning',
                      duration: 0,
                      offset: 100*i
                    })
                  }
                }
              }
              j++
            })
          }
        })
      })
      this.bookName = []
    },
    hiddlen(item){
      if(item.meta.menu){
        return true
      }else{
        return false
      }
    }
  }
};
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
.el-header {
    background-color: #9ba5b3;
    color: #333;
    line-height: 60px;
  }
.user-avatar {
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 10px;
}
.el-aside,
.el-menu,
.el-submenu,
.el-menu-item {
  background-color: rgb(238, 241, 246);
}


body {
  margin: 0px;
}
</style>
