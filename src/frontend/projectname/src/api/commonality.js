import req from '../common/js/req'

// 获取文章类别列表
export function getCategoryList () {
  return req({
    url: 'openapi/common/getCategoryList.do'
  })
}

// 获取访问记录
export function getPageAccess (par) {
  return req({
    url: 'openapi/wxshare/pageAccess.do',
    method: 'POST',
    data: par
  })
}

// 获取访客信息
export function getWxUserinfo (par) {
  return req({
    url: 'openapi/wxshare/getWxUserinfo.do?code=' + par
  })
}

// 阅读更新时长
export function getPageExit (par) {
  return req({
    url: 'openapi/wxshare/pageExit.do?uuid=' + par
  })
}

// 获取已订阅类别接口
export function getSubscribeList (id) {
  return req({
    url: 'openapi/common/getSubscribeList.do?userId=' + id,
    method: 'GET'
  })
}
