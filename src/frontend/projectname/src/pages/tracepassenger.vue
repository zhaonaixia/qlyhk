<template>
  <div class="trace">
    <!-- 名片信息 -->
    <div class="news">
      <div class="peole">
        <div class="pic">
          <img :src="dataList.headImgUrl" alt />
        </div>
        <div class="namenews" style="margin-left:0.3rem">
          <div style="font-size:16px;">
            <span>{{ dataList.user_name | myFilter(5) }}</span>
            <span class="span_pos" v-if="dataList.position!=='' && dataList.position!==null">{{ dataList.position | myFilter(5) }}</span>
          </div>
          <div style="font-size:13px;margin-top:0.2rem;">{{ dataList.company | myFilter(10) }}</div>
          <div style="margin-top:0.3rem">
            <yd-button
              class="but_sty"
              size="mini"
              type="danger"
              @click.native="call(dataList.telphone)"
              shape="circle"
              color="#fff">
              打电话
            </yd-button>
            <yd-button
              class="but_sty"
              @click.native="openWx"
              size="mini"
              type="danger"
              shape="circle"
              color="#fff">
              加微信
            </yd-button>
          </div>
        </div>
      </div>
      <div class="textnew">
        {{ dataList.lately_read_date }}
        <span class="span_sty">|</span> 时长：
        {{dataList.total_readtimes}}分
      </div>
      <!-- 阅读情况 -->
      <div class="read">
        <div style="width:33%;text-align: center;">
          <div style="font-size:0.39rem;font-weight:600;">{{dataList.total_num}}</div>
          <div style="font-size:0.28rem;color:#848484;margin-top:0.2rem;">阅读文章</div>
        </div>
        <div style="width:33%;text-align: center;">
          <div style="font-size:0.39rem;font-weight:600;">{{dataList.total_readtimes}}</div>
          <div style="font-size:0.28rem;color:#848484;margin-top:0.2rem;">总阅读时长</div>
        </div>
        <div style="width:33%;text-align: center;">
          <div style="font-size:0.39rem;font-weight:600;">{{dataList.total_sharenum}}</div>
          <div style="font-size:0.28rem;color:#848484;margin-top:0.2rem;">分享文章</div>
        </div>
      </div>
    </div>
    <!-- 文章列表 -->
    <div v-if="tableData.length>0">
      <div class="readlist" v-for="(item,index) in tableData" :key="index">
        <div class="tit_sty" >{{item.article_title}}</div>
        <div style="display: flex; padding: 0.4rem 0.2rem 0.2rem 0.2rem;">
          <div style="width:33%;text-align: center;">
            <div class="cs_sty1" >{{item.read_num}}</div>
            <div class="cs_sty2" >阅读次数</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div class="cs_sty1" >{{item.read_time}}</div>
            <div class="cs_sty2" >阅读时长</div>
          </div>
          <div style="width:33%;text-align: center;">
            <div class="cs_sty1" >{{item.share_num}}</div>
            <div class="cs_sty2" >分享次数</div>
          </div>
        </div>
        <!-- 查看详情 -->
        <div style="">
          <yd-accordion>
            <yd-accordion-item title="展开详情">
              <div style="padding: 0.2rem;">
                <yd-timeline>
                  <yd-timeline-item v-for="(item2,index2) in item.readDetails" :key="index2">
                    <span>{{item2.read_date}}</span>
                    <p style="margin-top: 0.2rem;">{{item2.read_time}}分</p>
                  </yd-timeline-item>
                </yd-timeline>
              </div>
            </yd-accordion-item>
          </yd-accordion>
        </div>
      </div>
    </div>
    <!-- 无数据显示 -->
    <noListData v-if="tableData.length===0"/>
    <!-- 二维码弹窗 -->
    <yd-popup v-model="show1" position="center" width="80%">
      <yd-lightbox>
        <yd-lightbox-img :src="src"></yd-lightbox-img>
      </yd-lightbox>
    </yd-popup>
  </div>
</template>
<script>
import { queryRecordConditionDetails } from '@/api/personage.js'
import noListData from '@/components/noListData.vue'
import {
  callUp // 拨打电话
} from '@/utils/getUrlParam.js'
export default {
  name: 'Tracepassenger',
  components: {
    noListData
  },
  data () {
    return {
      userId: '', // 查看的用户ID
      sharer: '', // 当前用户的ID
      dataList: {}, // 页面数据
      tableData: [], // 列表数据
      show1: false, // 二维码弹窗
      src: '' // 二维码地址
    }
  },
  created () {
    this.userId = this.$route.params.userId
    this.sharer = this.$route.params.sharer
    this.fundQueryRecordConditionDetails(this.userId, this.sharer)
  },
  methods: {
    // 打电话
    call (tel) {
      callUp(tel)
    },
    // 获取时间轴
    // obtainTimes (item, index) {
    //   let openId = sessionStorage.userId
    //   let shareId = item.share_uuid
    //   queryRCArticleDetails(openId, shareId).then(res => {
    //     if (res.data.code === '0') {
    //       let ref = 'accordion' + index
    //       this.$refs[ref][0].openItem()
    //     }
    //   })
    // },
    // 打开微信
    openWx () {
      if (this.dataList.ewm_url !== null) {
        this.src = this.dataList.ewm_url
        this.show1 = true
      } else {
        this.$dialog.toast({
          mes: '此用户没有上传微信二维码！',
          timeout: 1500
        })
      }
    },
    // 查询页面数据
    fundQueryRecordConditionDetails (id, sharer) {
      this.$dialog.loading.open('很快加载好了')
      queryRecordConditionDetails(id, sharer).then(res => {
        if (res.data.code === '0') {
          this.dataList = res.data.data.recordCondDetails
          this.tableData = this.dataList.articleDetails
          this.$dialog.loading.close()
        }
      }).catch(e => {
        this.$dialog.loading.close()
      })
    }
  }
}
</script>
<style lang="less" scoped>
.tit_sty{
  font-size:0.32rem;
  font-weight: bold;
  padding:0.3rem 0 0 0.3rem;
}
.cs_sty1{
  font-size:0.35rem;
  font-weight:600;
}
.cs_sty2{
  font-size:0.25rem;
  color:#848484;
  margin-top:0.2rem;
  color:#D3D3D3;
}
.but_sty{
  margin-right:0.1rem;
  font-size:0.2rem;
  width:1.3rem;
  background-image: linear-gradient(to right, #FF8F01 , #FF5204);
}
.span_sty{
  color: #E8E8E8;
}
.span_pos{
  background: #FEF6EC;
  border-radius: .106667rem;
  padding: .05rem;
  color: #FF8E01;
  font-size: .24rem;
}
.trace {
  .news {
    height: 4.8rem;
    background-color: #fff;
    .peole {
      height: 2rem;
      display: flex;
      justify-content: flex-start;
      padding: 0.4rem 0.4rem 0.4rem 0.4rem;
      .pic {
        height: 1.5rem;
        width: 1.5rem;
        img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }
      }
    }
    .textnew {
      font-size: 0.26rem;
      color: #9E9E9E;
      height: 1rem;
      line-height: 1rem;
      text-align: center;
      background-color: #fcfcfc;
      margin-top: 0.5rem;
    }
    .read {
      display: flex;
      padding: 0.2rem 0.2rem 0 0.2rem;
    }
  }
  .readlist {
    // height: 2rem;
    background-color: #fff;
    margin: 0.15rem 0.2rem;
  }
}
</style>
