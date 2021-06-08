// 公共接口
import req from '@/utils/request'

// 1.1.	获取类别列表
export function getCategoryList(code) {
  return req({
    url: 'openapi/common/getCategoryList.do',
    method: 'GET'
  })
}

// 1.5.	查询用户自定义查询分类列表接口
export function queryUserCustomQuery(code) {
  return req({
    url: 'admin/user/queryUserCustomQuery.do?module_code=' + code,
    method: 'GET'
  })
}

// 1.6.	根据用户自定义查询分类查询数据接口
export function queryDataForUserCustomQuery(par) {
  return req({
    url: 'admin/user/queryDataForUserCustomQuery.do',
    method: 'POST',
    data: Object.assign({}, par)
  })
}
