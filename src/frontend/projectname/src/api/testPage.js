import req from '../common/js/req'

// 获取页面内容
export function getReptileArticleInfo (id) {
  return req({
    url: 'openapi/articles/getReptileArticleInfo.do?articleId=' + id
  })
}
