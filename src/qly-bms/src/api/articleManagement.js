import req from '@/utils/request'

// 获取文章列表
export function getArticlesList(par) {
  return req({
    url: 'admin/articles/getArticlesList.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}
