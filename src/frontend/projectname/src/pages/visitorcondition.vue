<template>
<!-- 追踪客源 -->
  <div class="visitor">
    <div class="headserch">
      <yd-search
        class="search_sty"
        :on-submit="search"
        :on-cancel="cancel"
        placeholder="请输入阅读人昵称"
        cancel-text="重置"
        v-model="value1">
      </yd-search>
    </div>
    <div v-if="dataList.length>0">
      <div
        class="people" v-for="(item,index) in dataList" :key="index" @click="opneDile(item)">
        <div class="clearfix">
        <!-- 头像 -->
        <div class="fl">
          <img :src="item.headImgUrl" alt="">
          <div class="nameSty">{{ item.user_name | myFilter(5) }}</div>
        </div>
        <div class="fr">
          <yd-button
            style="padding:0 0.3rem"
            @click.native.stop="call(item.telphone)"
            shape="circle"
            type="hollow">
            打电话
          </yd-button>
          <yd-button
            style="padding:0 0.3rem;margin-left:0.3rem"
            type="hollow"
            @click.native.stop="openWx(item)"
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
        <div class="datetime">{{item.lately_read_date}}</div>
      </div>

    </div>

    <!-- 无数据显示 -->
    <noListData v-if="dataList.length===0"/>
    <!-- 二维码弹窗 -->
    <yd-popup v-model="show1" position="center" width="80%">
      <yd-lightbox>
        <yd-lightbox-img :src="src"></yd-lightbox-img>
      </yd-lightbox>
    </yd-popup>
 </div>
</template>

<script>
import { queryRecordCondition } from '@/api/personage.js'
import noListData from '@/components/noListData.vue'
import { getOpenId } from '@/api/article.js' // 获取用户ID
import {
  getQuery, // 获取url参数
  callUp // 打电话
} from '@/utils/getUrlParam.js' // 截取url code
import { getCategoryList } from '@/api/connections.js' // 获取是否开通会员
export default {
  name: 'Visitorcondition',
  components: {
    noListData
  },
  data () {
    return {
      value1: '', // 搜索框模糊查询
      dataList: [], // 列表数据
      dataList2: [], // 模糊查询数据
      userId: '', // 用户ID
      show1: false, // 二维码弹窗
      src: '' // 二维码地址
    }
  },
  created () {
    if (sessionStorage.userId !== undefined) {
      this.userId = sessionStorage.userId
      this.fundQueryRecordCondition(this.userId)
    } else {
      this.getUseOpenId()
    }
  },
  methods: {
    // 获取用户id
    getUseOpenId () {
      this.code = getQuery('code') // 截取路径中的code，如果没有就去微信授权，如果已经获取到了就直接传code给后台获取openId
      const par = this.code
      // alert('获取到的code:' + this.code)
      if (this.code !== '') {
        getOpenId(par).then(res => {
          this.userId = res.data.data.openId
          // alert('userID:' + this.userId)
          sessionStorage.userId = this.userId
          this.fundGetCategoryList(this.userId)
        })
      }
    },
    // 获取当前用户是否已开通会员
    fundGetCategoryList (id) {
      getCategoryList(id).then(res => {
        this.dataList = res.data.data
        // alert('是否是会员：' + this.dataList.isMember)
        if (this.dataList.isMember !== '1') {
          // 进入人脉雷达页面
          this.$router.push({
            name: 'Connections'
          })
        } else {
          this.fundQueryRecordCondition(this.userId)
        }
      })
    },
    // 打电话
    call (tel) {
      callUp(tel)
    },
    // 打开访客详情
    opneDile (item) {
      this.$router.push({
        name: 'Tracepassenger',
        params: {
          userId: item.openId,
          sharer: this.userId
        }
      })
    },
    // 模糊查询
    search () {
      // 去空格
      let input = this.value1.replace(/\s*/g, '')
      if (input !== '') {
        let arry = []
        this.dataList2.forEach(item => {
          if (item.user_name.indexOf(input) > -1) {
            arry.push(item)
          }
        })
        this.dataList = arry
      } else {
        this.fundQueryRecordCondition(this.userId)
      }
    },
    // 取消模糊查询
    cancel () {
      this.value1 = ''
      this.fundQueryRecordCondition(this.userId)
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
    // 查询列表数据
    fundQueryRecordCondition (id) {
      queryRecordCondition(id).then(res => {
        this.dataList = res.data.data.recordConditionList
        this.dataList2 = res.data.data.recordConditionList
      })
    }
  }
}
</script>
<style lang="less" scoped>
.visitor{
 .headserch{
  background-color: #fff;
 }
 .people{
   height: 3.8rem;
   background-color: #fff;
   margin: 0 0.2rem 0.2rem 0.2rem;
   .fl{
     height: 1rem;
     line-height: 1rem;
     margin: 0.3rem 0 0 0.3rem;
     img{
      width:1rem;
      height: 1rem;
      border-radius: 50%;
      margin-right: .133333rem;
      float: left;
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
    font-size: 0.3rem;
    color:#848484;
    margin:0.3rem;
   }
 }

}
</style>
