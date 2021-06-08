<template>
<!-- 个人中心 -->
  <div class="personal">
    <div class="bgcol"></div>
    <div class="header">
      <div class="topbox">
        <div class="imgpitic">
          <img :src="personData.headImgUrl" alt />
          <div class="setvisi" @click="setCard">
            <yd-button
            class="yd-button butto_2"
            size="mini"
            color="#fff"
            type="danger"
            style="font-size:0.25rem">
            <i class="icon_qly_shezhi2"></i> 设置名片
            </yd-button>
          </div>
        </div>
        <div class="name">{{ personData.user_name | myFilter(12) }}</div>
        <div v-if="isMember === '0'" style="text-align: center;color:#ccc;font-size:13px;">您还不是vip会员~</div>
        <div v-if="isMember === '1'" style="text-align: center;color:#ccc;font-size:13px;">您已经是vip会员~</div>
        <div class="read">
          <div style="width:33%;text-align: center;">
            <div style="font-size:20px;font-weight:600;">{{ personData.total_share }}</div>
            <div style="font-size:16px;color:#848484;margin-top:0.2rem;">分享文章</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div style="font-size:20px;font-weight:600;">{{ personData.today_reader }}</div>
            <div style="font-size:16px;color:#848484;margin-top:0.2rem;">今日阅读</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div style="font-size:20px;font-weight:600;">{{ personData.total_reader }}</div>
            <div style="font-size:16px;color:#848484;margin-top:0.2rem;">累计阅读</div>
          </div>
        </div>
      </div>
      <div v-if="isMember === '0'" class="vip_sty">
          <span style="margin:0 0 0 0.4rem" class="icon_qly_huiyuan"></span>
          <span style="font-size:.25rem;">立即开通会员</span>
          <yd-button
            class="button_sty2"
            bgcolor="#E8D9AE"
            type="danger"
            shape="circle"
            @click.native="buyVip"
          >立即开通</yd-button>
      </div>
      <div v-if="isMember === '1'" class="member">
          <div class="member-div1" @click="openReadcondition">
            <i class="icon_qly_yudu icon_sty_gr" style="margin-right:0.25rem;font-size:43px;"></i>
            <span>阅读情况</span>
            <div class="div_1_sty">
              <div class="div_2_sty">
                <i class="icon_qly_yan i_1_sty"></i>
              </div>
            </div>
          </div>
          <div class="member-div2" @click="openVisitorcondition">
            <i class="icon_qly_fangke icon_sty_gr" style="margin-right:0.25rem"></i>
            <span>访客追踪</span>
            <div class="div_1_sty div_3_sty">
              <div class="div_2_sty">
                <i class="icon_qly_juchi i_1_sty"></i>
              </div>
            </div>
          </div>
      </div>
    </div>
    <yd-cell-group>
      <yd-cell-item arrow @click.native="openScription">
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #FFE8CC;">
            <i class="icon_qly_dingyue icon_sty_lb" style="color:#FE7B01;"></i>
          </div>
          <div class="div_sty text_sty">
            订阅文章
          </div>
        </div>
      </yd-cell-item>
      <yd-cell-item arrow @click.native="buyVip">
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #FFE0DE;">
            <i class="icon_qly_huobi icon_sty_lb" style="color:#FF6660;"></i>
          </div>
          <div class="div_sty text_sty" style="padding-left:4px;">
            <span v-if="isMember === '1'">会员续期</span>
            <span v-if="isMember === '0'">开通会员</span>
          </div>
        </div>
      </yd-cell-item>
      <yd-cell-item arrow @click.native="openSetNews">
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #CAF8FD;">
            <i class="icon_qly_shezhi icon_sty_lb" style="color:#02DFFE;"></i>
          </div>
          <div class="div_sty text_sty">
            消息设置
          </div>
        </div>
      </yd-cell-item>
      <yd-cell-item arrow @click.native="useGuide">
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #D5F4F7;">
            <i class="icon_qly_wenhao icon_sty_lb" style="color:#1DC7D6;"></i>
          </div>
          <div class="div_sty text_sty">
            使用指南
          </div>
        </div>
      </yd-cell-item>
    </yd-cell-group>
  </div>
</template>
<script>
import { getMain } from '@/api/personage.js'
import { getOpenId } from '@/api/article.js'
import { getQuery } from '@/utils/getUrlParam.js'
export default {
  data () {
    return {
      isMember: '0', // 是否是会员
      code: '',
      personData: {},
      userId: '' // 用户id
    }
  },
  created () {
    this.userId = sessionStorage.userId
    if (this.userId !== undefined) {
      this.loadData(this.userId)
    } else {
      this.getUseOpenId()
    }
  },
  methods: {
    // 获取用户id
    getUseOpenId () {
      this.code = getQuery('code')
      if (this.code) {
        getOpenId(this.code).then(res => {
          this.userId = res.data.data.openId
          sessionStorage.userId = this.userId
          this.loadData(this.userId)
        })
      }
      // this.userId = 'oSVdLv05qzm-bo26hSxuFBZV-EoU'
      // this.userId = 'oSVdLv_upIwkk10EGMwavHMQfqaE'
      // sessionStorage.userId = this.userId
      // this.loadData(this.userId)
    },
    // 个人首页
    loadData (id) {
      getMain(id).then(res => {
        if (res.data.code === '0') {
          this.personData = res.data.data
          this.isMember = this.personData.isMember
        }
      })
    },
    // 打开访客情况
    openVisitorcondition () {
      this.$router.push({
        name: 'Visitorcondition'
      })
    },
    // 购买会员,续费会员
    buyVip () {
      this.$router.push({name: 'Buymember'})
    },
    // 跳转到阅读情况
    openReadcondition () {
      this.$router.push({
        name: 'Readcondition'
      })
    },
    // 打开消息设置
    openSetNews () {
      this.$router.push({
        name: 'Setnews',
        params: {
          userId: this.userId
        }
      })
    },
    // 打开订阅
    openScription () {
      this.$router.push({
        name: 'Subscription',
        params: {
          userId: this.userId
        }
      })
    },
    // 打开使用指南
    useGuide () {
      this.$router.push({name: 'Operating'})
    },

    // 打开设置名片
    setCard () {
      this.$router.push({
        name: 'business',
        params: {
          opId: this.userId
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.name {
  font-size: 0.3rem;
  font-weight: 600;
  text-align: center;
  width: 100%;
  padding: 0.2rem;
  margin-top: -.7rem;
}
.text_sty{
  font-weight: bold;
  font-size: .3rem;
}
.button_sty2{
  border: 1px solid #CBBB96;
  position: relative;
  left: 40%;
}
.div_1_sty{
  position: relative;
  top: -.4rem;
  left:.42rem;
  width: .57rem;
  height: .57rem;
  background: #FFD9AB;
  border-radius: 50%;
  text-align: center;
  line-height: .57rem;
  .div_2_sty{
    background: linear-gradient(to top right, #FEDBA6,#FE5900);
    width: .4rem;
    height: .4rem;
    border-radius: 50%;
    text-align: center;
    line-height: .4rem;
    position: absolute;
    left:15%;
    top: 15%;
  }
  .i_1_sty{
    color: #fff;
  }
}
.div_3_sty{
  top: -.4rem;
  left:.6rem;
}
.div_sty{
  width: .7rem;
  height: .7rem;
  border-radius: 50%;
  text-align: center;
  line-height: .7rem;
  margin-top: 0.15rem;
  margin-left: 0.2rem;
  margin-right: .2rem;
  float: left;
  .icon_sty_lb{
    font-size: .38rem;
  }
}
.icon_sty_gr{
  font-size: 35px;
  vertical-align:middle;
  background: linear-gradient(to top right, #FEDBA6,#FE5900);
  -webkit-background-clip: text;
  color: transparent;
}
.butto_2{
  border-radius: 25px 0 0 25px;
  background: #FF7F00;
  font-size: 14px;
}
.personal {
  height: 100vh;
  background-color: #fff;
}
.bgcol {
  height: 2.5rem;
  position: relative;
  z-index: 55;
  background-image: linear-gradient(to right, #FF8500 , #FE5900);
  border-radius: 0 0 25% 25%;
}
.header {
  height: 4.7rem;
  position: relative;
  margin: 0rem 0.3rem 0 0.3rem;
  z-index: 66;
  top: -1.3rem;
  background-color: #fff;

  .topbox {
    height: 4.2rem;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    .imgpitic {
      margin: 0 auto;
      width: 1.5rem;
      position: relative;
      top: -0.7rem;
      height: 1.5rem;
      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
      .setvisi{
         position: absolute;
         height: 0.4rem;
         text-align: center;
         line-height: 0.4rem;
         font-size: 0.25rem;
         bottom: 0.2rem;
         right:-2.7rem;

      }
    }
    .read {
      display: flex;
      padding: 0.4rem 0.4rem 0 0.4rem;
    }
  }
  .vip_sty {
    margin-top: 0.3rem;
    margin-bottom: .6rem;
    background-color: #EBD9B0;
    height: 1rem;;
    line-height: 1rem;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    border-radius: .066667rem;
  }
  .member{
    margin-top: 0.3rem;
    margin-bottom: .5rem;
    height: 1.2rem;
    background-color: #fff;
    display: flex;
    padding: 0.3rem 0.5rem;
    justify-content: space-between;
    .member-div1{
      font-size: 0.3rem;
      line-height: 0.6rem;
      width: 50%;
      border-right: 1px solid #ccc
    }
    .member-div2{
      font-size: 0.3rem;
      line-height: 0.6rem;
    }
  }
}
.footlist {
  height: 5.3rem;
  margin-top: 0.5rem;
  border-top: 0.3rem solid #f4f4f4;
  .listline{
    height: 1rem;
    line-height: 1rem;
    span{
     margin-left: 0.3rem;
     font-size: 18px;
    }
  }
}
</style>
