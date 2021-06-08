
// 引入
import Vue from 'vue'
export function GetUrlParam (name) { // 获取url指定参数
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)') //
  const url = window.location.href
  var r = url.substr(1).match(reg) // 匹配目标参数
  if (r != null) return unescape(r[2])
  return null // 返回参数
}

// 随机生成id
export function generateUUID () {
  var d = new Date().getTime()
  if (window.performance && typeof window.performance.now === 'function') {
    /* global performance */
    d += performance.now() // use high-precision timer if available
  }
  var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
    function (c) {
      var r = (d + Math.random() * 16) % 16 | 0
      d = Math.floor(d / 16)
      return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16)
    })
  return uuid
}

export function getQuery (variable) {
  let url = window.location.href + ''
  let index3 = url.indexOf('?')
  url = url.substring(index3 + 1, url.length)
  let vars = url.split('&')
  for (var i = 0; i < vars.length; i++) {
    let pair = vars[i].split('=')
    if (pair[0] === variable) {
      return pair[1]
    }
  }
  return (false)
}

export function getQuery2 (variable) {
  let url = window.location.href + ''
  url = url.replace('/contents?', '&')
  let index3 = url.indexOf('?')
  url = url.substring(index3 + 1, url.length)
  let vars = url.split('&')
  for (var i = 0; i < vars.length; i++) {
    let pair = vars[i].split('=')
    if (pair[0] === variable) {
      return pair[1]
    }
  }
  return (false)
}

// 获取url参数
export function getQueryVariable (variable) {
  let url = window.location.href + ''
  url = decodeURIComponent(url)
  let num1 = url.indexOf('type=')
  let num2 = url.indexOf('code=')
  let num3 = url.indexOf('contents')
  let str = url.substring(num1 + 5, num2) // 获取code前面是否有weixin&
  let str2 = url.substring(num2, num3) // 获取到code= 至 contents
  let str3 = url.substring(num3 + 9, url.length) // 获取到 contents？ 之后的
  if (str === 'weixin&') {
    url = str3 + '&' + str2 // 把code拼接到路由最后面
    // alert('type在前：' + url)
  } else {
    let str4 = url.substring(num3 + 9, num1 - 1) // 获取到contents 至 ？type=
    url = str4 + '&' + str2
    // alert('type在后：' + url)
  }
  // 根据‘&’分割  key value形式获取参数的值
  let vars = url.split('&')
  for (var i = 0; i < vars.length; i++) {
    let pair = vars[i].split('=')
    if (pair[0] === variable) { return pair[1] }
  }
  return (false)
}

// 实现数组、对象的深拷贝
export function deepCopy (obj) {
  const objClone = Array.isArray(obj) ? [] : {}
  if (obj && typeof obj === 'object') {
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        if (obj[key] && typeof obj[key] === 'object') {
          objClone[key] = deepCopy(obj[key])
        } else {
          objClone[key] = obj[key]
        }
      }
    }
  }
  return objClone
}

// 拨打电话
export function callUp (tel) {
  if (tel === null || tel === '') {
    Vue.prototype.$dialog.toast({
      mes: '此用户没有设置电话号码！',
      timeout: 1500
    })
  } else {
    const a = document.createElement('a') // 生成一个a元素
    /* global MouseEvent */
    const event = new MouseEvent('click') // 创建一个单击事件
    a.href = 'tel:' + tel // 将生成的URL设置为a.href属性
    return a.dispatchEvent(event) // 触发a的单击事件
  }
}
