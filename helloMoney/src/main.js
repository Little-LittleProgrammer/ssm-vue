// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias. 可在webpack.base.conf设置别名
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import VueResource from 'vue-resource';
import ElementUI from 'element-ui'
import Print from 'vue-print-nb'
import 'element-ui/lib/theme-chalk/index.css';
import '@/icons'

Vue.use(VueResource);
Vue.use(ElementUI, { size: 'small' }) //设置了size，就会默认size为small
Vue.use(Print); //这个为 打印所必须的引入

const whiteList = ['/login'] // no redirect whitelist login为重定向白名单

// 路由转跳前的操作
router.beforeEach(async(to, from, next) => {
  // determine whether the user has logged in确定用户是否已登录
  if (store.getters.getToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page如果已登录，则重定向到主页
      next()
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      // 确定用户是否通过getInfo获得了他的权限角色
      const hasRoles = store.state.roles && store.state.roles.length > 0
      if (hasRoles) {
        // 当有用户权限的时候，说明所有可访问路由已生成 如访问没权限的全面会自动进入404页面
        next()
      } else {
        try {
          // get user info
          // 注意:角色必须是一个对象数组!例如:['admin']或['developer'，'editor']
          const roles = []
          await store.dispatch("setRoles",store.getters.getToken)
          roles.push(store.getters.getRoles)
          // 根据角色生成可访问路由映射
          const accessRoutes = await store.dispatch('generateRoutes', roles)
          // 动态添加可访问路由
          router.addRoutes(accessRoutes)

          // hack方法 确保addRoutes已完成
          // 设置replace: true，这样导航就不会留下历史记录
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
    }
  }
})


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
