<!-- 人脉雷达 -->
<template>
  <div>
   <yd-flexbox class="heard_sty">
    <yd-flexbox-item>
      <p class="p1">{{dataList.today_reader}}/{{dataList.total_reader}}</p>
      <p class="p2">客户浏览文章</p>
      <p class="p3">（今日/累计）</p>
    </yd-flexbox-item>
    <yd-flexbox-item class="div_sty1">
      <p class="p4">未开通会员，无法追踪</p>
      <p class="p2">准客户</p>
    </yd-flexbox-item>
    <yd-flexbox-item>
      <p class="p5">提醒设置</p>
      <p class="p2">立即提醒</p>
    </yd-flexbox-item>
   </yd-flexbox>
   <div class="tis_div_sty2">

   </div>
   <div class="tis_div_sty">
     <div>
       <i class="icon_qly_gangtanhao i_sty_font" ></i>
     </div>
     <div class="div_sty2">
       开通会员后才能追踪用户，您已错过1次客户跟进机会，还有多少次机会可以继续错过？
     </div>
     <div>
       <yd-button
          type="danger"
          class="butto_sty"
          color="#fff"
          @click.native="wxisShow">开通会员</yd-button>
     </div>
     <div class="div_sty3">
       客户追踪示例
     </div>
     <div>
       <img src="./imges/pic.png" alt="">
     </div>
   </div>
  </div>
</template>

<script>
// import { getOpenId } from '@/api/article.js' // 获取用户ID
import { getCategoryList } from '@/api/connections.js' // 获取是否开通会员
import { getQuery } from '@/utils/getUrlParam.js' // 获取url code
import { pushHistory, remoHistory } from '@/utils/public.js'
export default {
  name: 'Connections',
  components: {},
  data () {
    return {
      userId: '', // 用户ID
      dataList: {}
    }
  },
  created () {
    let userId = getQuery('userId')
    if (!userId) {
      this.userId = sessionStorage.userId
    } else {
      this.userId = userId
    }

    this.fundGetCategoryList(this.userId)
  },
  // 加载时  追加监听
  mounted () {
    this.onpopstate()
  },
  // 离开时 销毁监听
  destroyed () {
    this.repopstate()
  },
  methods: {
    // 监听历史记录点, 添加返回事件监听
    onpopstate () {
      pushHistory()
    },
    // 删除事件监听
    repopstate () {
      remoHistory()
    },
    // 开通会员
    wxisShow () {
      this.$router.push({name: 'Buymember'})
    },
    // 获取用户id
    // getUseOpenId () {
    //   this.code = getQuery('code') // 截取路径中的code，如果没有就去微信授权，如果已经获取到了就直接传code给后台获取openId
    //   const par = this.code
    //   // const par = '1q1q1q1q11q'
    //   if (this.code !== '') {
    //     getOpenId(par).then(res => {
    //       this.userId = res.data.data.openId
    //       sessionStorage.userId = this.userId
    //       this.fundGetCategoryList(this.userId)
    //     })
    //   }
    // },
    fundGetCategoryList (id) {
      getCategoryList(id).then(res => {
        this.dataList = res.data.data
        if (this.dataList.isMember === '1') {
          // 进入访客追踪页面
          this.$router.push({
            name: 'Visitorcondition',
            params: {
              userId: this.userId
            }
          })
        }
      })
    }
  }
}

</script>
<style lang='less' scoped>
.div_sty3{
  margin-top: .5rem;
  margin-bottom: .2rem;
  letter-spacing: .03rem;
  font-size: .266667rem;
  color: #333333;
  font-weight: 600;
}
.butto_sty{
  width: 2.3rem;
  height: .8rem;
  border-radius: .4rem;
  font-size: .27rem;
  font-weight: bold;
  letter-spacing: .036667rem;
  background: linear-gradient(left, #FF8E01, #FE5200);
  margin-top: 0;
}
.div_sty2{
  color: #E72B2A;
  font-size: .253333rem;
  margin-top: .16rem;
  line-height: .38rem;
  letter-spacing: .036667rem;
  margin-bottom: .3rem;
}
.tis_div_sty2{
  background: url(./imges/test2.png);
  height:80vh;
  text-align: center;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  filter: blur(17px);
}
.tis_div_sty{
  padding: .366667rem .266667rem;
  text-align: center;
  position:absolute;
  top: 1.833333rem;
  left: 10px;
  right: 10px;
}
.i_sty_font{
  color: #E72B2A;
  font-size: .5rem;
}
.div_sty1:before {
  content: '';
  position: absolute;
  left: 30%;
  top: 4%;
  bottom: auto;
  right: auto;
  height: .7rem;
  width: 1px;
  background-color: #F0F0F0;
}
.div_sty1:after {
  content: '';
  position: absolute;
  left: auto;
  top: 4%;
  bottom: 5px;
  right: 30%;
  height: .7rem;
  width: 1px;
  background-color: #F0F0F0;
}

 .heard_sty{
   background: #fff;
 }
.yd-flexbox-item{
  text-align: center;
  padding: .2rem 0;
}
.p1{
  font-size: .5rem;
  font-weight: bold;
  margin-bottom: .133333rem;
}
.p2{
  font-size: .3rem;
  color: #8E8E8E;
  margin-bottom: .133333rem;
}
.p3{
  color: #8E8E8E;
}
.p4{
  color: #E72B2A;
  margin-bottom: .133333rem;
}
.p5{
  color: #9FBDEC;
  margin-bottom: .133333rem;
}
 </style>
