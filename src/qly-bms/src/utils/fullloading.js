// loading框设置局部刷新，且所有请求完成后关闭loading框
let loading
let needLoadingRequestCount = 0 // 声明一个对象用于存储请求个数
function startLoading() {
  // eslint-disable-next-line
  loading = Vue.prototype.$loading({
    lock: true,
    text: '努力加载中...',
    background: 'rgba(0,0,0,0)',
    target: document.querySelector('.loading-area') // 设置加载动画区域
  })
}
function endLoading() {
  setTimeout(() => {
    loading.close()
  }, 100)
}
export function showFullLoading() {
  if (needLoadingRequestCount === 0) {
    startLoading()
  }
  needLoadingRequestCount++
}
export function hideFullLoading() {
  if (needLoadingRequestCount <= 0) return
  needLoadingRequestCount--
  if (needLoadingRequestCount === 0) {
    endLoading()
  }
}
