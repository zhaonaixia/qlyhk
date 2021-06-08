import req from '../common/js/req'

// 解决跨域
export function agentUrl (url) {
  return req({
    url: url,
    method: 'GET'
  })
}
