import req from '../common/js/req'

// 获取文章列表
export function getArticleList (par) {
  return req({
    url: 'openapi/articles/getArticlesList.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}

// 1.2.获取用户的OpenId
export function getOpenId (par) {
  return req({
    url: 'openapi/wxshare/getOpenId.do?code=' + par
  })
}

// 查询文章url
export function getArticleInfo (id) {
  return req({
    url: 'openapi/articles/getArticleInfo.do?articleId=' + id
  })
}

// 查询文章内容
export function getReptileArticleInfo (id) {
  return req({
    url: 'openapi/articles/getReptileArticleInfo.do?articleId=' + id
  })
}

// 分享
export function getReqParam (par) {
  return req({
    url: 'openapi/wxshare/getReqParam.do?localUrl=' + par,
    method: 'GET'
  })
}
