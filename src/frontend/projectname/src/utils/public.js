var handler = function (e) {
  /* global WeixinJSBridge */
  WeixinJSBridge.call('closeWindow')
}

// 监听返回 直接到公众号
export const pushHistory = () => {
  window.history.pushState(null, null, '#')
  window.addEventListener('popstate', handler, false
  )
}

// 注销事件
export const remoHistory = () => {
  window.removeEventListener('popstate', handler, false)
}
