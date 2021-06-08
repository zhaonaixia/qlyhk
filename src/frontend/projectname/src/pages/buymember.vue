<template>
<!-- 开通会员 -->
  <div class="buymember">
    <div class="helplist">
      <div class="listline">
        <span class="icon_qly_yuangou span_sty"></span>
        <span>实时通知谁看了你的文章/早晚安播报</span>
      </div>
      <div class="listline">
        <span class="icon_qly_yuangou span_sty"></span>
        <span>精准定位意向客户，省心省力</span>
      </div>
      <div class="listline">
        <span class="icon_qly_yuangou span_sty"></span>
        <span>不限次数的创建</span>
      </div>
      <div class="listline">
        <span class="icon_qly_yuangou span_sty"></span>
        <span>自由删减</span>
      </div>
      <div class="listline">
        <span class="icon_qly_yuangou span_sty"></span>
        <span>赠送税务</span>
      </div>
    </div>
    <div class="buyvip">
      <div style="font-size:0.3rem;font-weight:600;">请选择会员级别</div>
      <span v-for="(item,index) in dataList" :key="index">
        <div :class="item.default_opt==='true'?'div_sty_bq':'div_sty_bq1'" @click="switchSty(item,index)">
          <i class="icon_qly_gou i_sty"></i>
          <div class="div_name_sty">{{ item.name }}</div>
          <div class="div_price_sty">￥<span class="price_sty">{{ item.price }}</span></div>

        </div>
      </span>
      <div style="margin-top:0.3rem" v-if="wxPay!==0">
        <yd-button
          class="but_sty"
          size="large"
          type="primary"
          @click.native="handleClick"
          shape="circle">
          微信支付 {{ wxPay }}元
        </yd-button>
      </div>
    </div>
  </div>
</template>
<script>
import {
  getPackageList, // 获取套餐
  newOrder, // 新建订单
  payfor // 开通会员
} from '@/api/Member.js'
import {
  getQuery
} from '@/utils/getUrlParam.js'
export default {
  name: 'Buymember',
  data () {
    return {
      butShow: true,
      radio1: '68',
      wxPay: 0,
      dataList: [], // 套餐列表
      userId: ''
    }
  },
  watch: {
    radio1 (val) {
      this.wxPay = val
    }
  },
  created () {
    this.fundGetPackageList()
  },
  methods: {
    // 支付套餐
    handleClick () {
      this.userId = sessionStorage.userId ? sessionStorage.userId : getQuery('userId')
      // 获取已选套餐ID
      let arr = {}
      this.dataList.forEach(element => {
        if (element.default_opt === 'true') {
          arr = element
        }
      })
      // 组装参数
      let par = {
        packageId: arr.packageId, // 套餐ID
        packageCode: arr.packageCode, // 套餐编码
        openId: this.userId // 用户ID
      }
      newOrder(par).then(res => {
        if (res.data.code === '0') {
          let par2 = {
            orderId: res.data.data.orderId, // 订单ID
            openId: this.userId // 用户ID
          }
          payfor(par2).then(res => {
            if (res.data.code === '0') {
              this.onBridgeReady(res.data.data)
            }
          })
        }
      })
    },

    // 微信内置浏览器类，weChatParameter对象中的参数是3.步骤代码中从后端获取的数据
    onBridgeReady (par) {
      /* global WeixinJSBridge */
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
          debug: true,
          'appId': par.appId, // 公众号名称，由商户传入
          'timeStamp': par.timeStamp, // 时间戳，自1970年以来的秒数
          'nonceStr': par.nonceStr, // 随机串
          'package': par.package,
          'signType': par.signType, // 微信签名方式：
          'paySign': par.paySign, // 微信签名
          success (res) {
          },
          fail (e) {
            alert(e)
          }
        })
    },

    // 点击切换样式
    switchSty (item, index) {
      this.dataList.forEach((e, i) => {
        if (index !== i) {
          this.dataList[i].default_opt = 'false'
        }
      })
      if (this.dataList[index].default_opt === 'true') {
        this.dataList[index].default_opt = 'false'
        this.wxPay = 0
      } else {
        this.dataList[index].default_opt = 'true'
        this.wxPay = this.dataList[index].price
      }
    },
    // 获取套餐列表
    fundGetPackageList () {
      getPackageList().then(res => {
        if (res.data.code === '0') {
          this.dataList = res.data.data
          this.dataList.forEach((element, index) => {
            if (element.default_opt === 'true') {
              this.wxPay = element.price
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.div_name_sty{
  font-size: .3rem;
  margin-top: .4rem;
  margin-bottom: .2rem;
}
.div_price_sty{
  font-size: .31rem;
}
.price_sty{
  font-size: .6rem;
  font-weight: bold;
}
.div_sty_bq{
  position: relative;
  border: 1px solid #FF5500;
  display: inline-block;
  width:2rem;
  height: 2rem;
  margin: .2rem .2rem 0 0;
  text-align: center;
  border-radius: .1rem;
  // background: linear-gradient(225deg, transparent 8px,#ff5200 0);
  background:linear-gradient(-135deg, #FF5500 15px, #FEF3EE 0);
  .i_sty{
    position:absolute;
    font-size: .25rem;
    margin-bottom: .146667rem;
    left: 1.7rem;
    top: -.02rem;
    color: #fff;
  }
}
.div_sty_bq1{
  position: relative;
  border: 1px solid #E6E6E6;
  display: inline-block;
  width:2rem;
  height: 2rem;
  margin: .2rem .2rem 0 0;
  text-align: center;
  border-radius: .1rem;
  background: #fff;
  .i_sty{
    position:absolute;
    font-size: .25rem;
    margin-bottom: .146667rem;
    left: 1.7rem;
    top: -.02rem;
    color: #fff;
  }
}
.but_sty{
  background-image: linear-gradient(to right, #FF8F02 , #FF5000);
}
.span_sty{
  color: #FF8C51;
}
.buymember{
  height: 100%;
  background-color: #fff;
  .helplist {
    background-color: #fff;
    .listline{
      height: 1rem;
      line-height: 1rem;
      border-bottom: 2px solid #f4f4f4;
      span{
        margin-left: 0.3rem;
        font-size: 0.3rem;
      }
    }
  }
  .buyvip{
    padding: 0.3rem;
    border-top:3px solid #f4f4f4;
  }
}
</style>
