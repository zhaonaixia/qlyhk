import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import router from '../router'
import { getToken } from '@/utils/auth'

// 创建一个AXIOS实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  // withCredentials: true, // 跨域请求时发送cookies
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 请求前
    if (store.getters.token) {
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // 处理请求错误
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 是否响应为200
    if (res.code === '1') {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    if (String(error).indexOf('code 400') !== -1) {
      Message.error('参数有误')
      // 函数进入then后面的第二个err函数，如果没有就进入catch函数, 表单提交就可以根据这个重置参数以及重置按钮状态，防止按钮卡滞
      return Promise.reject(error)
    }
    if (error.response.status === 401 || String(error).indexOf('code 401') !== -1) {
      MessageBox.confirm('因长时间未使用，系统已安全登出您的账户。', '系统安全提示', {
        confirmButtonText: '重新登录',
        type: 'warning'
      }).then(() => {
        store.dispatch('FedLogOut').then(() => {
          router.push('/login')
          location.reload() // 为了重新实例化vue-router对象 避免bug
          // 终止Promise调用链
          return new Promise(() => {})
        })
      })
    }
    if (String(error).indexOf('code 500') !== -1) {
      Message.error('服务器出现问题，请刷新重试')
      return Promise.reject(error)
    }

    return Promise.reject(error)
  }
)

export default service
