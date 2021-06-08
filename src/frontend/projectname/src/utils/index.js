import wx from 'weixin-js-sdk'

export function shard (tit, url) {
  // var localUrl = window.location.href.split('#')[0]
  // var encodeLocal = encodeURIComponent(localUrl)
  // var redirectURL = 'http://www.finway.com.cn/share/redirect2.html?redirectURL=' + encodeLocal
  wx.config({
    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: this.appId, // 必填，公众号的唯一标识
    timestamp: this.timestamp, // 必填，生成签名的时间戳
    nonceStr: this.nonceStr, // 必填，生成签名的随机串
    signature: this.signature, // 必填，签名，见附录1
    jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
  })
  wx.ready(function () {
    wx.onMenuShareTimeline({
      title: tit, // 分享标题
      link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
      imgUrl: 'http://www.finway.com.cn/share/images/logo.png', // 分享图标
      success () {
      // 用户确认分享后执行的回调函数
      },
      cancel () {
      // 用户取消分享后执行的回调函数
      }
    })

    wx.onMenuShareAppMessage({
      title: tit, // 分享标题
      // desc: this.details.videoTitle // 分享描述
      link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
      imgUrl: 'http://www.finway.com.cn/share/images/logo.png', // 分享图标
      // type: 'video', // 分享类型,music、video或link，不填默认为link
      // dataUrl: this.details.videoUrl, // 如果type是music或video，则要提供数据链接，默认为空
      success: function () {
      // 用户确认分享后执行的回调函数
      },
      cancel: function () {
      // 用户取消分享后执行的回调函数
      }
    })
  })
}
