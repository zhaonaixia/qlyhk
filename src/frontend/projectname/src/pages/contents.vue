<template>
<!-- 文章分享 -->
  <div style="height:100vh;background: #fff;" class="contents" id="js-scroll" ref="infinitescroll">
    <yd-layout>
      <div class="navpic">
        <div style="font-size:20px;font-weight: bold;">{{ tableData.article_title }}</div>
        <div class="conter">
          <div class="imgspan">
            <img :src="personData.headImgUrl" alt="">
          </div>
          <div class="name">{{ personData.user_name | myFilter(5) }}</div>
          <div class="butto">
            <yd-button
              type="danger"
              shape="circle"
              class="butto_sty"
              color="#fff"
              @click.native="toBottom">
              联系TA
            </yd-button>
          </div>
        </div>
      </div>
      <!-- <div><a :href="tableData.article_url">{{ tableData.article_url }}</a></div> -->
      <div :width="width_div" v-if="iframeShow">
        <iframe
        :src="url"
        id="testIfra"
        ref="iframeRef"
        sandbox="allow-scripts"
        height="100%"
        frameborder ="0"
        scrolling="no" ></iframe>
      </div>
      <!-- <img src="http://img01.store.sogou.com/net/a/04/link?appid=100520029&url=https://mmbiz.qpic.cn/mmbiz_jpg/X1fWcGkwZFfP7icSPxHhBQ9ibu6HYSFMHCnDdpZZOI2XB6694SoCicmhe3JSnbW1iasDQ7HQwSMol4vrsUsCqoXib0A/640?wx_fmt=jpeg" alt=""> -->

      <div ref="divId" id="divId" v-html="url" v-if="!iframeShow" class="div_sty_lodding">
      </div>

      <!-- 名片信息 -->
      <div class="news">
          <div class="peole">
            <div class="pic"><img :src="personData.headImgUrl" alt=""></div>
            <div class="namenews" style="margin-left:0.3rem">
                <div><span style="font-size:0.3rem;margin-right:0.1rem">{{ personData.user_name | myFilter(5) }}</span>
                    <span class="span_font_sty" v-if="personData.position">{{ personData.position | myFilter(5) }}</span>
              </div>
                <div v-if="personData.company" style="font-size:0.25rem;margin-top:0.2rem;">{{ personData.company | myFilter(10) }}</div>
                <div style="margin-top:0.3rem">
                  <yd-button
                    style="margin-right:0.1rem;"
                    type="danger"
                    class="butto_sty butto_sty1"
                    shape="circle"
                    color="#fff"
                    @click.native="call(personData.telphone)">
                    打电话
                  </yd-button>
                  <yd-button
                    type="danger"
                    class="butto_sty butto_sty1"
                    shape="circle"
                    color="#fff"
                    @click.native="addWx(personData)">
                    加微信
                  </yd-button>
                </div>
            </div>
          </div>
          <div class="textnew">{{ personData.personal_profile }}</div>
      </div>
      <!-- 锚点标记处 -->
      <a id="goto"></a>
      <yd-tabbar slot="tabbar" style="padding:0 0 1px 0" v-if="showButt">
        <yd-button
          size="large"
          type="danger"
          class="butto_sty"
          bgcolor="#FF8700"
          color="#fff"
          @click.native="wxisShow">免费换成我的名片</yd-button>
      </yd-tabbar>
    </yd-layout>
    <div>
      <yd-popup class="yd-popup" v-model="wxShow" position="center" width="80%">
            <div class="wxer" style="background-color:#fff;height:8rem">
                <div style="text-align: center;margin-top:0.3rem;">
                    <i class="icon_qly_yuangou2" style="font-size:0.8rem;color:#01A300;"></i>
                </div>
                <div style="text-align:center;color:#00A300;font-size:0.3rem;margin: 0.3rem 0">更换成功</div>
                <div style="text-align:center;color:#333;font-size:0.25rem;">扫码领取你的名片文章</div>
                <div style="text-align:center;margin: 0 0 0 0">
                    <img :src="this.imgCode" alt="" style="heigt:100%;width:100%;">
                </div>
                <div style="text-align: center;font-size:0.3rem;">
                    精准锁定客户，月签十单
                </div>
            </div>
      </yd-popup>
    </div>
    <!-- 二维码弹窗 -->
    <yd-popup v-model="show1" position="center" width="80%">
      <yd-lightbox>
        <yd-lightbox-img :src="src"></yd-lightbox-img>
      </yd-lightbox>
    </yd-popup>
  </div>
</template>
<script>
import { getReptileArticleInfo, getReqParam } from '@/api/article.js'
import { getUserInfo, getTempCodeUrl } from '@/api/personage.js'
import {
  // GetUrlParam,
  generateUUID,
  getQuery,
  getQuery2,
  callUp // 拨打电话
  // getQueryVariable // 获取url参数
} from '@/utils/getUrlParam.js'
import {
  getPageAccess,
  getWxUserinfo,
  getPageExit
} from '@/api/commonality.js'
// import { shard } from '@/utils/index.js'
import wx from 'weixin-js-sdk'
import { agentUrl } from '@/api/crossDomain.js'
export default{
  data () {
    return {
      showButt: false, // 控制免费跟换名片按钮
      show1: false, // 二维码弹窗
      src: '', // 二维码地址
      iframeShow: false, // 控制显示
      openId: '', // 访客ID
      nickname: '', // 访客昵称
      headImgUrl: '', // 访客头像
      wxShow: false,
      isKeepLive: false,
      tableData: {},
      imgCode: '', // 二维码
      mathId: '', // 文章分享随机生成的uuid
      readId: '', // 阅读和访问随机生成的uuid
      consId: '', // 访客id

      // article_url: '', // 分享文章的url
      personData: {},
      visitData: [],
      userInfoId: '',
      article_title: '',
      url: '', // 嵌套文章地址
      appId: '',
      signature: '',
      timestamp: '',
      nonceStr: '',
      rh_articleId: '',
      userId: '',
      articleId: '',
      width_div: document.body.clientWidth,
      oneUrl: '', // 初始路由
      containerId: 'testIfra',
      articleUrl: ''

    }
  },
  watch: {
    $route (to, from) {
      this.getReadTime()
    }
  },
  created () {
    if (getQuery('getInto')) {
      this.showButt = false
    } else {
      this.showButt = true
    }
    this.oneUrl = window.location.href + '&'
    if (!getQuery('code')) {
      this.userId = getQuery('userId') // 用户ID
      this.rh_articleId = getQuery('rh_articleId')// 获取文章ID
      this.articleId = getQuery('articleId') // 随机生成的UUID
    } else {
      this.userId = getQuery2('userId') // 用户ID
      this.rh_articleId = getQuery2('rh_articleId')// 获取文章ID
      this.articleId = getQuery2('articleId') // 随机生成的UUID
    }
    // this.userId = this.$route.query.userId // 用户ID
    // this.rh_articleId = this.$route.query.rh_articleId// 获取文章ID
    // this.articleId = this.$route.query.articleId // 随机生成的UUID

    this.mathId = generateUUID()
    this.readId = generateUUID()
    this.loadData() // 这里许要初始化dada()中的数据
    this.inquireData()
    this.timer()
  },
  destroyed () {
    this.getReadTime()
  },
  mounted () {
    this.$nextTick(() => {
      this.iframeWidth()
    })
  },
  methods: {
    getURL (url) {
      let http = (window.location.protocol === 'http:' ? 'http:' : 'https:')
      let realurl = http + '//cors-anywhere.herokuapp.com/' + url
      agentUrl(realurl).then(res => {
        if (res) {
          let data = res.data.replace(/data-src="/g, 'src="http://img01.store.sogou.com/net/a/04/link?appid=100520029&url=')
          // http://read.html5.qq.com/image?src=forum&q=5&r=0&imgflag=7&imageUrl=防盗链图片地址
          // let htmlSrc = 'data:text/html;charset=utf-8,' + data // 解析码解决乱码
          // this.$refs.iframeRef.src = htmlSrc
          this.url = data
          // 删除文章原标题和名称,阅读原文
          this.$nextTick(() => {
            let titleNmae = document.getElementById('activity-name')
            let jsName = document.getElementById('js_name')
            let jsViewSource = document.getElementById('js_view_source')
            if (titleNmae !== null && titleNmae !== undefined) { titleNmae.parentNode.removeChild(titleNmae) }
            if (jsName !== null && titleNmae !== undefined) { jsName.parentNode.removeChild(jsName) }
            if (jsViewSource !== null && titleNmae !== undefined) { jsViewSource.parentNode.removeChild(jsViewSource) }
          })
        }
      }).catch(e => {
        agentUrl(realurl).then(res => {
          if (res) {
            let data = res.data.replace(/data-src="/g, 'src="http://img01.store.sogou.com/net/a/04/link?appid=100520029&url=')
            // http://read.html5.qq.com/image?src=forum&q=5&r=0&imgflag=7&imageUrl=防盗链图片地址
            // let htmlSrc = 'data:text/html;charset=utf-8,' + data // 解析码解决乱码
            // this.$refs.iframeRef.src = htmlSrc
            this.url = data
            // 删除文章原标题和名称,阅读原文
            this.$nextTick(() => {
              let titleNmae = document.getElementById('activity-name')
              let jsName = document.getElementById('js_name')
              let jsViewSource = document.getElementById('js_view_source')
              if (titleNmae !== null && titleNmae !== undefined) { titleNmae.parentNode.removeChild(titleNmae) }
              if (jsName !== null && titleNmae !== undefined) { jsName.parentNode.removeChild(jsName) }
              if (jsViewSource !== null && titleNmae !== undefined) { jsViewSource.parentNode.removeChild(jsViewSource) }
            })
          }
        }).catch(e => {
          agentUrl(realurl).then(res => {
            // alert('第三次数据请求正确')
            if (res) {
              let data = res.data.replace(/data-src="/g, 'src="http://img01.store.sogou.com/net/a/04/link?appid=100520029&url=')
              // http://read.html5.qq.com/image?src=forum&q=5&r=0&imgflag=7&imageUrl=防盗链图片地址
              // let htmlSrc = 'data:text/html;charset=utf-8,' + data // 解析码解决乱码
              // this.$refs.iframeRef.src = htmlSrc
              this.url = data
              // 删除文章原标题和名称,阅读原文
              this.$nextTick(() => {
                let titleNmae = document.getElementById('activity-name')
                let jsName = document.getElementById('js_name')
                let jsViewSource = document.getElementById('js_view_source')
                if (titleNmae !== null && titleNmae !== undefined) { titleNmae.parentNode.removeChild(titleNmae) }
                if (jsName !== null && titleNmae !== undefined) { jsName.parentNode.removeChild(jsName) }
                if (jsViewSource !== null && titleNmae !== undefined) { jsViewSource.parentNode.removeChild(jsViewSource) }
              })
            }
          }).catch(e => {
            this.$dialog.toast({
              mes: '网络错误，请稍后再试！',
              timeout: 1500,
              icon: 'error'
            })
            console.log(e)
          })
        })
      })
    },

    // 判断系统
    iframeWidth () {
      let that = this
      let ua = navigator.userAgent.toLowerCase()
      let screenwidth = window.screen.width
      let screenheight = window.screen.height
      if (!/iphone|ipad|ipod/.test(ua)) {
        that.$nextTick(() => {
          that.$refs.iframeRef.attr('scrolling', 'auto')
        })
      }
      that.$refs.iframeRef.width = screenwidth
      that.$refs.iframeRef.height = screenheight
    },
    timer () {
      setInterval(() => {
        this.getReadTime()
      }, 180000)
    },
    // 查询文章
    loadData () {
      const id = this.rh_articleId
      getReptileArticleInfo(id).then(res => {
        this.tableData = res.data.data
        this.article_title = res.data.data.article_title
        this.url = res.data.data.article_content
        this.shareData()
        this.getUserCode() // 这里发起数据请求
      })
    },
    // loadData () {
    //   const id = this.rh_articleId
    //   getArticleInfo(id).then(res => {
    //     if (res.data.code === '0') {
    //       this.tableData = res.data.data
    //       this.article_title = res.data.data.article_title
    //       let path = res.data.data.article_url
    //       this.articleUrl = path
    //       let head = path.slice(0, 24)
    //       if (head === 'https://mp.weixin.qq.com') { // 是微信公众号文章
    //         this.iframeShow = false
    //         this.getURL(path)
    //       } else {
    //         this.iframeShow = true
    //         this.url = path
    //       }
    //       this.shareData()
    //       this.getUserCode() // 这里发起数据请求
    //     }
    //   })
    // },
    // 获取访客信息
    getUserCode () {
      this.code = getQuery('code') // 截取路径中的code
      if (this.code) {
        getWxUserinfo(this.code).then(res => {
          if (res.data.data !== null) {
            this.headImgUrl = res.data.data.headImgUrl // 访客头像
            this.openId = res.data.data.openId // 访客ID
            this.nickname = res.data.data.nick_name // 访客名称
            this.visitRecord(this.tableData.article_title, this.tableData.article_url)
          }
        })
      }
      //  else {
      //   this.visitRecord(this.tableData.article_title, this.tableData.article_url)
      // }
    },
    // 访问记录
    visitRecord (articleTitle, articleUrl) {
      const par = {
        uuid: this.readId, // 访问记录的UUID
        visitor_id: this.openId, // 访客ID
        user_name: this.nickname, // 访客昵称
        headImgUrl: this.headImgUrl, // 访客头像
        sharer: this.userId, //
        article_id: this.articleId, // 分享文章的ID
        article_title: articleTitle,
        article_url: '',
        rh_articleId: this.rh_articleId
      }
      getPageAccess(par).then(res => {
        if (res.data.code === '0') {

        }
      })
    },

    // 阅读更新时长
    getReadTime () {
      const par = this.readId
      getPageExit(par).then(res => {
        if (res.data.code === '0') {}
      })
    },
    // 文章分享
    shareData () {
      const par = encodeURIComponent(window.location.href.split('#')[0])
      getReqParam(par).then(res => {
        this.appId = res.data.data.appid
        this.signature = res.data.data.signature
        this.timestamp = res.data.data.timestamp
        this.nonceStr = res.data.data.nonceStr
        const title = this.tableData.article_title
        const piurl = this.tableData.pic_url
        let url = process.env.BASE_URL + '/#/contents?rh_articleId=' + this.rh_articleId + '&articleId=' + this.articleId + '&userId=' + this.userId + '&'
        // alert('分享时的路由地址：' + url)
        let encodeLocal = encodeURIComponent(url)
        let redirectURL = process.env.REDIRECT_URL + '/redirect.html?redirectURL=' + encodeLocal

        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: this.appId, // 必填，公众号的唯一标识
          timestamp: this.timestamp, // 必填，生成签名的时间戳
          nonceStr: this.nonceStr, // 必填，生成签名的随机串
          signature: this.signature, // 必填，签名，见附录1
          jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'updateAppMessageShareData',
            'updateTimelineShareData'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        })
        wx.ready(function () {
          wx.onMenuShareTimeline({
            title: title, // 分享标题
            link: redirectURL, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: piurl, // 分享图标
            success () {
              // 用户确认分享后执行的回调函数
            },
            cancel () {
              // 用户取消分享后执行的回调函数
            }
          })
          // let url2 = 'https://www.baidu.com/'
          wx.onMenuShareAppMessage({
            title: title, // 分享标题
            // desc: this.article_title, // 分享描述
            link: redirectURL, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: piurl, // 分享图标
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
      })
    },
    // 查询个人资料
    inquireData () {
      const id = this.userId
      getUserInfo(id).then(res => {
        if (res.data.code === '0') {
          this.personData = res.data.data
        }
      })
    },
    // 获取二维码
    wxisShow () {
      this.wxShow = true
      const id = this.articleId
      getTempCodeUrl(id).then(res => {
        if (res.data.code === '0') {
          this.imgCode = res.data.data.ewmUrl
        }
      })
    },
    // 点击跳转到底部
    toBottom () {
      window.location.href = '#goto' // 通过锚点跳转到底部
    },

    // 打电话
    call (tel) {
      callUp(tel)
    },
    // 加微信
    addWx (item) {
      if (item.ewm_url !== null) {
        this.src = item.ewm_url
        this.show1 = true
      } else {
        this.$dialog.toast({
          mes: '此用户没有上传微信二维码！',
          timeout: 1500
        })
      }
    },
    // 关闭微信页面
    qq () {
      window.addEventListener('beforeunload', this.getReadTime())
    }
  }
}
</script>
<style lang="less" scoped>
.div_sty_lodding{
  min-height: calc(100vh - .2rem);
}
.span_font_sty{
  background: #FFF5EC;
  padding: .04rem;
  color: #FF8E01;
  border: #FF8E01 1px solid;
  border-radius: .103333rem;
}
.butto_sty{
  background: linear-gradient(left, #FF8E01, #FE5200);
  margin-top: 0;
  padding: .0rem .3rem;
}
.butto_sty1{
  padding: .0rem .4rem;
}
.contents{
  background-color:#fff;
  height: 100%;
  width: 100%;
  .navpic{
    padding: 0.2rem 0.2rem 0 0.2rem;
    margin-bottom: .3rem;
    .conter{
      display: flex;
      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
      border-radius: 5px;
      justify-content: space-between;
      margin-top: 0.5rem;
      height: 1.5rem;
      .name{
        line-height: 1.3rem;
        font-size: 20px;
        margin-left: -1.2rem;
        width: 3rem;
      }
      .imgspan{
        width:1.5rem;
        height: 1.5rem;
        padding: 0.2rem;
        line-height: 1rem;
        img{
          width: 100%;
          height: 100%;
          margin-right:0.2rem;
          border-radius: 50%;
        }
      }
      .butto{
        line-height: 1.5rem;
        margin-right: 0.2rem;
      }
    }
  }
  .swaip{
    height: 100%;
    width: 100%;
    padding:0.1rem;
    font-size: 0.3rem;
    margin-top: 0.7rem;
  }
  .news{
    height: 4rem;
    margin: 0.2rem;
    padding:0 0.4rem;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    .peole{
      height: 2rem;
      display: flex;
      justify-content: flex-start;
      padding: 0.4rem 0.4rem 0.4rem 0.4rem;
      .pic{
        height: 1.5rem;
        width: 1.5rem;
      img{
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
      }
    }
    .textnew{
      font-size: 14px;
      border-top: 1px dashed #ccc;
      margin-top:0.2rem;
      padding:0.2rem 0rem;
    }
  }
  .yd-popup{
     .wxer{
       padding:0.3rem 0.7rem 0.4rem 0.7rem;
       border-radius: .2rem;
     }
  }
}
</style>
