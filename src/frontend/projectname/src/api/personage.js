import req from '../common/js/req'

// 个人首页信息
export function getMain (id) {
  return req({
    url: 'openapi/user/main.do?userId=' + id
  })
}

// 查询二维码
export function getTempCodeUrl (id) {
  return req({
    url: 'openapi/wxshare/getTempCodeUrl.do?article_id=' + id
  })
}

// 查询个人资料
export function getUserInfo (id) {
  return req({
    url: 'openapi/user/queryUserInfo.do?userId=' + id
  })
}
// 保存设置名片
export function getEditUserInfo (par) {
  return req({
    url: 'openapi/user/saveEditUserInfo.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}

// 消息提醒设置
export function getMessageRemindSet (id) {
  return req({
    url: 'openapi/common/getMessageRemindSet.do?userId=' + id
  })
}

// 保存消息设置
export function saveMessage (par) {
  return req({
    url: 'openapi/common/saveMessageRemindSet.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}

// 文章阅读情况
export function getArticleRead (id) {
  return req({
    url: 'openapi/articles/queryArticleReadCond.do?openId=' + id
  })
}

// 文章阅读详情
export function getQueryArticle (id, userId) {
  return req({
    url: 'openapi/articles/queryArticleReadCondDetails.do?shareId=' + id + '&openId=' + userId
  })
}

// 访客情况
export function queryRecordCondition (id) {
  return req({
    url: 'openapi/articles/queryRecordCondition.do?openId=' + id,
    method: 'GET'
  })
}

// 追踪客源
export function queryRecordConditionDetails (id, sharer) {
  return req({
    url: 'openapi/articles/queryRecordConditionDetails.do?openId=' + sharer + '&visitorId=' + id,
    method: 'GET'
  })
}

// 访客追踪文章详情
export function queryRCArticleDetails (id, id2) {
  return req({
    url: 'openapi/articles/queryRCArticleDetails.do?openId=' + id + '&shareId=' + id2,
    method: 'GET'
  })
}

// 4.6.保存订阅文章
export function saveSubscribeArticles (par) {
  return req({
    url: 'openapi/common/saveSubscribeArticles.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}
