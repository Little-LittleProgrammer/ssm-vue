import Vue from 'vue'
import Vuex from 'vuex'
import { asyncRoutes, menu } from '@/router';
import { resetRouter } from '@/router'

Vue.use(Vuex)

// 通过递归过滤asyncRoutes
export function filterAsyncRoutes(routes, roles) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })
  console.log('res',res)
  return res
}

function hasPermission(roles, route) {
  if (route.meta && route.meta.role) {
    // some() 方法用于检测数组中的元素是否满足指定条件（函数提供）
    return roles.some(role => route.meta.role.indexOf(role) >= 0)
  } else {
    return true
  }
}

const store = new Vuex.Store({
  state:{
    userName:'',
    token:'',
    routers: menu,
    addRouters: [],
    roles: [],
    uId:Number,
  },
  getters:{
    getUserName:function(state){
      return state.userName;
    },
    getToken:function(state){
      return state.token;
    },
    getRoutes:function(state){
      return state.routers
    },
    getAddRouters:function(state){
      return state.addRouters
    },
    getRoles: state => state.roles,
    getUId: state => state.uId
    // getUserName: state => {
    //   return state.userName
    // }
  },
  mutations:{
    changeUserName(state,userName){
      state.userName=userName
    },
    saveToken(state,token){
      state.token =token
    },
    SET_ROUTES: (state, routers) => {
      state.addRouters = routers;
      state.routers = menu.concat(routers);
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_UID: (state,uId) =>{
      state.uId = uId
    }
  },
  actions:{
    changeUserName(context,userName){
      context.commit("changeUserName",userName)
    },
    saveToken(context,token){
      context.commit("saveToken",token)
    },
    setRoles(context,roles){
      context.commit("SET_ROLES",roles)
    },
    generateRoutes({ commit }, roles) {
      return new Promise(resolve => {
        let accessedRoutes
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
        // 返回一个解析过带着给定值的Promise对象，如果返回值是一个promise对象，则直接返回这个Promise对象。
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      })
    },
    setUId(context,uId){
      context.commit("SET_UID",uId)
    },
    // remove token
    resetToken({ commit }) {
      return new Promise(resolve => {
        commit('saveToken', '')
        commit('SET_ROLES', [])
        resetRouter()
        resolve()
      })
    },
  },
})

export default store
