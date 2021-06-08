<template>
<!-- 阅读情况 -->
  <div class="condition">
    <div class="readnav">
      <div style="width:33%;text-align: center;">
        <div style="font-size:0.35rem;font-weight:600;">{{ readerToday }}</div>
        <div style="font-size:0.3rem;color:#848484;margin-top:0.2rem;">今日阅读数</div>
      </div>
      <div style="width:33%;text-align: center;">
        <div style="font-size:0.35rem;font-weight:600;">{{ readerSevenDay }}</div>
        <div style="font-size:0.3rem;color:#848484;margin-top:0.2rem;">近7日阅读</div>
      </div>
      <div style="width:33%;text-align: center;">
        <div style="font-size:0.35rem;font-weight:600;">{{ totalreader }}</div>
        <div style="font-size:0.3rem;color:#848484;margin-top:0.2rem;">累计阅读</div>
      </div>
    </div>
    <yd-search
      class="search_sty"
      :on-submit="search"
      :on-cancel="cancel"
      placeholder="请输入文章名称"
      cancel-text="重置"
      v-model="value1">
    </yd-search>
    <!-- 有数据显示列表 -->
    <div v-if="personaData.length>0">
      <div class="people" v-for="(item,index) in personaData" :key="index">
        <div class="headnav">{{ item.article_title }}</div>
        <div class="read">
          <div style="width:25%;text-align: center;">
            <div style="font-size:0.4rem;font-weight:600;">{{ item.total_num }}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">总阅读次数</div>
          </div>
          <div style="width:25%;text-align: center;">
            <div style="font-size:0.4rem;font-weight:600;">{{ item.total_readers }}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">总阅读人数</div>
          </div>
          <div style="width:25%;text-align: center;">
            <div style="font-size:0.4rem;font-weight:600;">{{ item.share_num }}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">分享次数</div>
          </div>
          <div style="width:25%;text-align: center;">
            <div style="font-size:0.4rem;font-weight:600;">{{ item.share_readers }}</div>
            <div style="font-size:0.25rem;color:#848484;margin-top:0.2rem;">分享人数</div>
          </div>
        </div>
        <div class="link-line" @click="lookdetail(item)">
          查看详情
        </div>
      </div>
    </div>
    <!-- 无数据显示 -->
    <noListData v-if="personaData.length===0"/>
  </div>
</template>
<script>
import { getArticleRead } from '@/api/personage.js'
import noListData from '@/components/noListData.vue'
export default {
  name: 'Readcondition',
  components: {
    noListData
  },
  data () {
    return {
      value1: '',
      personaData: [],
      readerToday: '', // 今日阅读
      readerSevenDay: '', // 7日阅读
      totalreader: '', // 共阅读
      userId: '' // 用户ID
    }
  },
  created () {
    // 获取用户ID
    this.userId = sessionStorage.userId
    this.loaadData(this.userId)
  },

  methods: {
    // 模糊查询
    search () {
      // 去空格
      let input = this.value1.replace(/\s*/g, '')
      if (input !== '') {
        let arry = []
        this.personaData.forEach(item => {
          if (item.article_title.indexOf(input) > 0) {
            arry.push(item)
          }
        })
        this.personaData = arry
      } else {
        this.loaadData(this.userId)
      }
    },
    // 取消模糊查询
    cancel () {
      this.value1 = ''
      this.loaadData(this.userId)
    },
    // 文章阅读详情
    loaadData (userId) {
      getArticleRead(userId).then(res => {
        if (res.data.code === '0') {
          this.personaData = res.data.data.shareArticleList
          this.readerToday = res.data.data.totalReaderToday
          this.readerSevenDay = res.data.data.totalReaderSevenDay
          this.totalreader = res.data.data.totalReader
        }
      })
    },
    // 查看详情
    lookdetail (item) {
      sessionStorage.share_uuid = item.share_uuid
      window.location.href = process.env.BASE_URL +
      '/#/readdetails?share_uuid=' + item.share_uuid +
      '&article_title=' + item.article_title +
      '&userId=' + this.userId
      // this.$router.push({
      //   name: 'Readdetails',
      //   params: {
      //     share_uuid: item.share_uuid,
      //     article_title: item.article_title
      //   }
      // })
    }
  }
}
</script>
<style lang="less" scoped>
.condition {
  .readnav {
    background-color: #fff;
    display: flex;
    padding: 0.4rem 0.4rem;
  }
  .people{
  //  height: 3.8rem;
   background-color: #fff;
   margin: 0.2rem;
   .headnav{
     font-size: 0.35rem;
     padding: 0.3rem;
     font-weight: 600;
     line-height: .4rem;
   }
   .read{
      display: flex;
      padding: 0.4rem 0.2rem;
      border-bottom: 1px solid #E8E8E8;

   }
   .datetime{
    font-size: 0.3rem;
    color:#848484;
   }
 }
}
</style>
<style lang='less'>
.search_sty{
  .yd-search-input {
    background: #F5F5F5;
    border-bottom: none;
  }
  .yd-search-input>.search-input{
    background: #fff;
  }
  .search-input{
    border-radius: 45px;
    height:.7rem;
    font-size: 14px;
  }
}
</style>
