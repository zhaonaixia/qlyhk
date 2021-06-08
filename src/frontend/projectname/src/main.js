// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import YDUI from 'vue-ydui' /* 相当于import YDUI from 'vue-ydui/ydui.rem.js' */
import 'vue-ydui/dist/ydui.rem.css'
import '@/style/index.less'
import App from './App.vue'
import store from './store'
import router from './router/index.js'
import wx from 'weixin-js-sdk'

Vue.use(YDUI)
Vue.use(wx)

Vue.config.productionTip = false

// 注册全局过滤器 （处理文字过长）
Vue.filter('myFilter', function (value, num) {
  if (value !== '' && value !== null && value !== undefined) {
    // 返回处理后的值
    if (value.length > num) {
      return value.substring(0, num) + '...'
    } else {
      return value
    }
  }
})

// 添加路由导航守卫
router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title || '首页'
  }
  next()
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
