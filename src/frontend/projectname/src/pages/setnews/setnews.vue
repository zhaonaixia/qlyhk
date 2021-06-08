<template>
  <div class="setnews">
     <div style="margin:0.3rem 0 0.2rem 0.3rem;font-size:0.25rem;color:#A0A0A0;">谁看我消息提醒</div>
     <div class="remind">
       <yd-radio-group v-model="wholook"  color="#FE5501">
        <div style="padding:0.3rem 0;margin-left:0.2rem;border-bottom: 1px solid #ccc">
          <yd-radio val="1">立即提醒我</yd-radio>
        </div>
        <div style="padding:0.3rem 0;margin-left:0.2rem;">
          <yd-radio val="2">勿扰模式&nbsp;(5分钟汇总提醒,23:00-7:00不提醒)</yd-radio>
        </div>
      </yd-radio-group>
     </div>

     <div style="margin:0.3rem 0 0.2rem 0.3rem;font-size:0.25rem;color:#A0A0A0;">热门文章推送</div>
     <div class="openswith">
        <div class="opennews" style="margin-left:0.2rem;border-bottom: 1px solid #ccc">
          <div style="font-size:0.3rem;">推荐热门资讯</div>
          <div style="padding:0.2rem 0.2rem">
            <yd-switch v-model="switch1" color="#FE5501"></yd-switch>
          </div>
        </div>
        <div class="opennews" style="margin-left:0.2rem;">
            <div style="font-size:0.3rem;">你订阅的文章</div>
          <div style="padding:0.2rem 0.2rem">
            <yd-switch v-model="switch2" color="#FE5501"></yd-switch>
          </div>
        </div>
     </div>
  </div>
</template>
<script>
import { getMessageRemindSet, saveMessage } from '@/api/personage.js'
export default {
  name: 'Setnews',
  data () {
    return {
      wholook: '1',
      switch1: true,
      switch2: true,
      newsData: {},
      userId: '' // 用户ID
    }
  },
  created () {
    // 获取用户ID
    this.userId = sessionStorage.userId
    this.lodaData(this.userId)
  },
  // 离开当前页面时  保存设置
  destroyed () {
    this.saveNews()
  },
  methods: {
    // 获取消息设置
    lodaData (userId) {
      getMessageRemindSet(userId).then(res => {
        if (res.data.code === '0') {
          if (res.data.data !== null) {
            this.newsData = res.data.data
            this.wholook = this.newsData.remind_type
            if (this.newsData.push_settings[0].rec_hot_news === '0') {
              this.switch1 = false
            } else {
              this.switch1 = true
            }
            if (this.newsData.push_settings[1].rec_sub_article === '0') {
              this.switch2 = false
            } else {
              this.switch2 = true
            }
          }
        }
      })
    },
    // 保存消息设置
    saveNews () {
      let ary = [
        {
          rec_hot_news: this.switch1 ? '1' : '0'
        },
        {
          rec_sub_article: this.switch2 ? '1' : '0'
        }
      ]
      const par = {
        userId: this.userId, // 用户id
        remind_type: this.wholook, // 谁看我提醒设置（1表示立即提醒  2表示勿扰模式）
        push_settings: ary
      }
      saveMessage(par).then(res => {

      })
    }

  }

}
</script>

<style lang="less" scoped>
 .setnews{
   .remind{
     background-color: #fff;
   }
   .openswith{
     background-color: #fff;
    .opennews{
       display: flex;
       height: 1rem;
       line-height: 1rem;
       justify-content: space-between;
    }
   }
 }
</style>
