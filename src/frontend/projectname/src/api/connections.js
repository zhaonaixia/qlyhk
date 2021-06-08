import req from '../common/js/req'

// 4.14.人脉雷达
export function getCategoryList (id) {
  return req({
    url: 'openapi/common/networkingRadar.do?userId=' + id
  })
}
