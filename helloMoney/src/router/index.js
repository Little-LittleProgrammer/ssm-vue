import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const menu=[
  {
    path:'/',
    redirect:'login',
    meta:{
      menu:false
    }
  },
  {
    path:'/login',
    name:'login',
    component:()=>import('@/views/login/login.vue'),
    meta:{
      menu:false
    }
  },
  {
    path:'/user',
    name:'layoutYHGL',
    component:()=>import('@/views/layout/Layout'),
    meta:{
      title:'用户管理',
      icon:'el-icon-user',
      menu:true,
      funcNode:'1'
    },
    children:[
      {
        path:'/user/userList',
        name:'UserList',
        component:()=>import('@/views/user/UserList'),
        meta:{
          title:'用户列表',
          icon:'el-icon-notebook-2',
          menu:true,
          funcNode:'1-1'
        }
      },
      {
        path:'/user/addUser',
        name:'UserAdd',
        component:()=>import('@/views/user/UserAdd'),
        meta:{
          title:'用户添加',
          icon:'el-icon-circle-plus-outline',
          menu:true,
          funcNode:'1-2'
        }
      }
    ]
  },
  {
    path:'/role',
    redirect:'user/userList',
    meta:{
      title:'角色管理',
      icon:'el-icon-help',
      menu:true
    }
  },
]


export const asyncRoutes = [
  {
    path:'/sys',
    name:'layoutXTGL',
    component:()=>import('@/views/layout/Layout'),
    meta:{
      title:'系统管理',
      icon:'el-icon-setting',
      menu:true,
      funcNode:'2',
      role: ['admin']
    },
    children:[
      {
        path:'/sys/sysLogList',
        name:'SysLogList',
        component:()=>import('@/views/sys/SysLogList'),
        meta:{
          title:'系统访问日志',
          icon:'el-icon-notebook-1',
          menu:true,
          funcNode:'2-1',
          role: ['admin']
        }
      }
    ]
  },
  {
    path:'/book',
    name:'book',
    component:()=>import('@/views/layout/Layout'),
    meta:{
      title:'图书管理',
      icon :'el-icon-reading',
      menu:true,
      funcNode:'3',
      role:['admin','editor']
    },
    children:[
      {
        path:'/book/subBook',
        name:'subBook',
        component:()=>import('@/views/book/subBook'),
        meta:{
          title:'图书借阅',
          icon :'el-icon-notebook-1',
          menu:true,
          funcNode:'3-1',
          role:['editor']
        }
      },
      {
        path:'/book/bookManage',
        name:'bookManage',
        component:()=>import('@/views/book/bookManage'),
        meta:{
          title:'图书管理',
          icon :'el-icon-notebook-2',
          menu:true,
          funcNode:'3-2',
          role:['admin']
        }
      },
      {
        path:'/book/returnBook',
        name:'returnBook',
        component:()=>import('@/views/book/returnBook'),
        meta:{
          title:'图书归还',
          icon :'el-icon-collection',
          menu:true,
          funcNode:'3-3',
          role:['editor']
        }
      },
      {
        path:'/book/subUserManage',
        name:'returnBook',
        component:()=>import('@/views/book/subUserManage'),
        meta:{
          title:'借阅用户',
          icon :'el-icon-document-copy',
          menu:true,
          funcNode:'3-4',
          role:['admin']
        }
      },
      {
        path:'/book/badGay',
        name:'returnBook',
        component:()=>import('@/views/book/badGay'),
        meta:{
          title:'失信用户',
          icon :'el-icon-user',
          menu:true,
          funcNode:'3-5',
          role:['admin']
        }
      },
    ]
  },
  {
    path: '*',
    redirect: '/404',
    meta:{
      title:'错误',
      icon:'el-icon-setting',
      menu:false,
      funcNode:'3',
    },
  }
];

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: menu
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}
export default router
