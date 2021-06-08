// 引入
import Vue from 'vue'
// import store from '../store'
import axios from 'axios'

// 设置基准路径
const service = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 10000
})

// request拦截器
service.interceptors.request.use(
  config => {
    Vue.prototype.$dialog.loading.open('数据加载中')
    return config
  },
  error => {
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// 添加响应拦截器
service.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  Vue.prototype.$dialog.loading.close()
  if (response.data.code === '1') {
    Vue.prototype.$dialog.toast({
      mes: response.data.message,
      timeout: 1500,
      icon: 'error',
      callback: () => {
      }
    })
  }
  return response
}, function (error) {
  // 对响应错误做点什么
  Vue.prototype.$dialog.loading.close()
  if (error.response.status === 400) {
    /* eslint-disable-next-line */
   Vue.prototype.$dialog.toast({
      mes: '数据请求出错',
      timeout: 1500,
      icon: 'error',
      callback: () => {
      }
    })
  }
  // return Promise.reject(error)
})

export default service
