<template>
<!-- 阅读详情 -->
  <div class="readdetails">
    <div class="readnav">
      {{ article_title }}
    </div>
    <div class="navserch">
      <yd-search
        class="search_sty"
        :on-submit="search"
        :on-cancel="cancel"
        placeholder="请输入阅读人昵称"
        cancel-text="重置"
        v-model="value1">
      </yd-search>
    </div>
    <div v-if="detailData.length>0">
      <div class="people" v-for="(item,index) in detailData" :key="index">
        <div class="clearfix">
          <div class="fl">
            <img :src="item.headImgUrl" alt="">
            <div class="nameSty">{{ item.user_name | myFilter(5) }}</div>
          </div>
          <div class="fr">
            <yd-button
              style="padding:0 0.3rem"
              type="hollow"
              shape="circle"
              @click.native="call(item.telphone)">
              打电话
            </yd-button>
            <yd-button
              style="padding:0 0.3rem;margin-left:0.3rem"
              type="hollow"
              @click.native="openWx(item)"
              shape="circle">
              加微信
            </yd-button>
          </div>
        </div>
        <div class="read">
          <div style="width:33%;text-align: center;">
            <div style="font-size:0.3rem;font-weight:600;">{{item.total_num}}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">阅读文章数</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div style="font-size:0.3rem;font-weight:600;">{{item.total_readtimes}}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">阅读总时长</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div style="font-size:0.3rem;font-weight:600;">{{item.total_sharenum}}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">分享文章数</div>
          </div>
        </div>
        <div class="datetime">
          时长：{{item.read_time}}分&nbsp;&nbsp;
          <span class="span_sty_shu">|</span>
          次数：{{item.read_num}}次&nbsp;&nbsp;
          <span class="span_sty_shu">|</span>
          分享：{{item.share_num}}次
        </div>
      </div>
    </div>
    <!-- 无数据显示 -->
    <noListData v-if="detailData.length===0"/>
    <!-- 二维码弹窗 -->
    <yd-popup v-model="show1" position="center" width="80%">
      <yd-lightbox>
        <yd-lightbox-img :src="src"></yd-lightbox-img>
      </yd-lightbox>
    </yd-popup>
  </div>
</template>
<script>
import { getQueryArticle } from '@/api/personage.js'
import noListData from '@/components/noListData.vue'
import {
  callUp, // 拨打电话
  getQuery
} from '@/utils/getUrlParam.js'

export default {
  name: 'Readdetails',
  components: {
    noListData
  },
  data () {
    return {
      value1: '',
      detailData: [],
      detailData2: [],
      share_uuid: '', // 文章ID
      article_title: '', // 文章标题
      show1: false, // 二维码弹窗
      src: '', // 二维码地址
      userId: ''
    }
  },
  created () {
    this.share_uuid = getQuery('share_uuid') // 文章ID
    let tile = getQuery('article_title')
    this.article_title = decodeURIComponent(tile) // 文章标题
    this.userId = getQuery('userId') // openId
    this.loadData(this.share_uuid, this.userId)
  },
  methods: {
    // 打电话
    call (tel) {
      callUp(tel)
    },
    // 模糊查询
    search () {
      // 去空格
      let input = this.value1.replace(/\s*/g, '')
      if (input !== '') {
        let arry = []
        this.detailData2.forEach(item => {
          if (item.user_name.indexOf(input) > -1) {
            arry.push(item)
          }
        })
        this.detailData = arry
      } else {
        this.loadData(this.share_uuid, this.userId)
      }
    },
    // 取消模糊查询
    cancel () {
      this.value1 = ''
      this.loadData(this.share_uuid, this.userId)
    },
    // 打开微信
    openWx (item) {
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
    // 获取阅读详情
    loadData (id, userId) {
      getQueryArticle(id, userId).then(res => {
        if (res.data.code === '0') {
          this.detailData = res.data.data.readCondDetails
          this.detailData2 = res.data.data.readCondDetails
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.span_sty_shu{
  color: #e0e0e0;
  margin-right: .16667rem;
}
.readdetails {
  .readnav {
    background-color: #fff;
    font-weight: bold;
    padding: .15rem 0.3rem;
    line-height: .45rem;
    font-size: 0.35rem;
  }
  .people{
   height: 3.8rem;
   background-color: #fff;
   margin: 0 0.2rem .2rem .2rem;
   .fl{
     margin: 0.3rem 0 0 0.3rem;
     line-height: 1rem;
     img{
      width:1rem;
      height: 1rem;
      float: left;
      border-radius: 50%;
      margin-right: .106667rem;
     }
   }
   .fr{
      height: 1rem;
      line-height: 1rem;
      margin: 0.3rem 0.3rem 0 0;
   }
   .read{
      display: flex;
      padding: 0.4rem 0.2rem;
      border-bottom: 1px solid #E8E8E8;

   }
   .datetime{
    font-size: 0.25rem;
    color:#848484;
    margin:0.3rem;
    text-align: center
   }
 }
}
</style>
