import req from '../common/js/req'

// 3.1.获取套餐列表
export function getPackageList (id) {
  return req({
    url: 'openapi/order/getPackageList.do',
    method: 'GET'
  })
}

// 3.2.新建订单
export function newOrder (par) {
  return req({
    url: 'openapi/order/newOrder.do',
    method: 'GET',
    params: par
  })
}

// 3.2.开通会员
export function payfor (par) {
  return req({
    url: 'openapi/order/payfor.do',
    method: 'GET',
    params: par
  })
}
