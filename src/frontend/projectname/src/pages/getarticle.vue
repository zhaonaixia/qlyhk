<template>
<!-- 文章列表 -->
  <div class="getarticle">
    <!-- <div class="looklist" @click="looklist">
      <i class="iconfont icon-icon-test" style="font-size:0.7rem;"></i>
    </div> -->
    <!-- <yd-popup v-model="show1" position="center" width="100%">
      <div style="background-color:#fff;height:9.5rem;">111</div>
    </yd-popup> -->
    <yd-search
      v-model.trim="inputName"
      class="search_sty"
      :on-submit="searchName"
      :on-cancel="cancel"
      cancel-text="重置"
      placeholder="请输入文章名称">
    </yd-search>
    <yd-infinitescroll :callback="loadData" ref="infinitescroll">
      <yd-tab
        v-model="tab"
        active-color="#FE6D31"
        font-size="16px"
        horizontal-scroll
        :prevent-default="false"
        :item-click="itemClick"
        height="1rem"
        slot="list">
        <yd-tab-panel
          v-for="(item,index) in label"
          :key="index"
          :tabkey="item.category_id"
          :label="item.category_name">
          <div class="roll">
            <div class="listitem" v-for="(item,index) in tableData" :key="index" @click="readwen(item)">
              <div class="list-item1">
                <div class="list-div1">{{ item.article_title }}</div>
                <span class="span1">{{item.total_reader}}人阅读</span>
                <span class="span2">|</span>
                <span class="span3">{{item.total_share}}人分享</span>
              </div>
              <div class="list-item2"><img :src="item.pic_url" alt=""></div>
            </div>

          </div>
        </yd-tab-panel>
      </yd-tab>
      <span slot="doneTip">啦啦啦，啦啦啦，没有数据啦~~</span>
    </yd-infinitescroll>
    <!-- <div class="footer">W
      <div class="footbutton">
        <span class="footspan">还没找到满意的文章？</span>
        <yd-button type="danger" size="small" class="butto" shape="circle">告诉我们</yd-button>
      </div>
    </div> -->
    <div class="change">
      <yd-button type="danger" size="small" class="butto butto_2" @click.native="changeRead">换一批</yd-button>
    </div>
  </div>
</template>
<script>
import {
  getArticleList,
  getOpenId
} from '@/api/article.js'
import {
  // getSubscribeList, // 获取已订阅类别
  getCategoryList // 获取所有文章类别
} from '@/api/commonality.js'
import {
  generateUUID,
  getQuery
} from '@/utils/getUrlParam.js'
export default {
  data () {
    return {
      tab: 0,
      show1: false,
      //   当前页码
      pagenum: 1,
      //   每页显示的记录数
      pagesize: 6,
      code: '',
      category: 0, // 类别ID
      inputName: '',
      total: 0, // 总条数
      tableData: [], // 列表数据
      tableData2: [],
      selected: '',
      result: [],
      userId: '', // 授权用户id
      label: [] // 已经订阅的类别
    }
  },
  created () {
    this.getUseOpenId()
  },
  mounted () {
  },
  methods: {
    // 获取用户id
    getUseOpenId () {
      this.code = getQuery('code')
      const par = this.code
      if (this.code !== '') {
        getOpenId(par).then(res => {
          this.userId = res.data.data.openId
          this.getCateList()
        })
      }
      // this.userId = 'oSVdLv-Oat4WYLkj8orizgEbIQXQ'
      // this.getCateList()
    },
    // 取消模糊查询
    cancel () {
      this.inputName = ''
      this.loadData()
    },
    // 搜索
    searchName (value) {
      this.inputName = value
      this.pagenum = 1
      if (this.inputName === '') {
        this.loadData()
      } else {
        this.tableData = this.tableData2.filter(e => {
          return e.article_title.indexOf(value) !== -1
        })
      }
    },

    // 切换tab
    itemClick (key) {
      this.tableData = []
      this.tableData2 = []
      this.tab = key
      this.inputName = ''
      this.category = this.label[key].category_id
      this.loadData()
    },

    // 换一批
    changeRead () {
      this.tableData = []
      this.tableData2 = []
      if (this.pagenum > this.total) {
        this.pagenum = 1
      }

      this.loadData()
    },

    // 跳转文章详情页
    readwen (item) {
      let type = 'listGetInto' // 标识列表进入
      window.location.href = process.env.BASE_URL +
      '/#/contents?rh_articleId=' + item.uuid + '&articleId=' +
      generateUUID() + '&userId=' + this.userId + '&getInto=' + type
    },

    loadData () {
      const par = {
        category_id: this.category,
        pageQueryVO: {
          currentPage: this.pagenum,
          pageSize: this.pagesize
        }
      }
      // 获取文章了表
      getArticleList(par).then(res => {
        if (res.data.code === '0') {
          let data = res.data.data
          this.total = data.page.totalPage
          const _tableData = data.articlesList
          this.tableData = [...this.tableData, ..._tableData]
          this.tableData2 = [...this.tableData, ..._tableData]
          if (_tableData.length < this.pagesize || this.pagenum === this.total) {
            this.$refs.infinitescroll.$emit('ydui.infinitescroll.loadedDone')
            return
          }
          /* 单次请求数据完毕 */
          this.$refs.infinitescroll.$emit('ydui.infinitescroll.finishLoad')
          this.pagenum++
        } else {
          this.$dialog.toast({ mes: res.data.message, timeout: 3000, icon: 'error' })
        }
      })
    },
    // 获取所有类别
    getCateList () {
      getCategoryList().then(res => {
        this.label = res.data.data
        let par = {
          category_id: 0,
          category_name: '推荐'
        }
        this.label.unshift(par)
        this.category = this.label[0].category_id
        this.loadData()
      })
    }
    // 获取已订阅类别
    // getCateList (id) {
    //   getSubscribeList(id).then(res => {
    //     if (res.data.data !== null) {
    //       this.label = res.data.data.subscribeList
    //       let par = {
    //         category_id: 0,
    //         category_name: '推荐'
    //       }
    //       this.label.unshift(par)
    //       this.category = this.label[0].category_id
    //       this.loadData()
    //     } else {
    //       let par = {
    //         category_id: 0,
    //         category_name: '推荐'
    //       }
    //       this.label.unshift(par)
    //       this.category = this.label[0].category_id
    //       this.loadData()
    //     }
    //   })
    // }
  }
}
</script>
<style lang="less" scoped>

.getarticle {
  // height: 100%;
  background-color: #fff;
}
.looklist{
  position: fixed;
  top: 1.1rem;
  right:0;
  z-index: 1000;
}
.footer {
  position: fixed;
  width: 100%;
  bottom: 0px;
  left: 0px;
}
.listitem{
  justify-content: center;
  display: flex;
  margin: 20px 10px;
  height: 2rem;
  padding-bottom:0.2rem;
  justify-content: space-between;
  border-bottom: 1px solid #F0F0F0;
}
.list-item1{
  width: 70%;
  font-weight: 600;
  font-size: 0.35rem;
  margin-right: 1rem;
}
.list-item2{
  width: 30%;
  margin-bottom:10px;
  img {
    width:100%;
    height: 100%
  }
}
.span1{
  font-size: 0.25rem;
  font-weight: 500;
  color: #ccc;
  margin-left:0.1rem;
}
.span2{
  font-size: 0.25rem;
  font-weight: 500;
  color: #ccc;
  margin: 0 5px;
}
.span3{
  font-size: 0.25rem;
  font-weight: 500;
  color: #ccc;
}
.list-div1{
  margin-bottom: 0.3rem;
}
.footbutton{
  background: linear-gradient(left, #EAEAEA, #FEFEFE);
  height: 60px;
  line-height: 60px;
  text-align: center;
}
.footspan{
  font-size: 0.3rem;
  color:#909090;
  margin-right: 5px;
}
.roll{
  padding-bottom:1rem;
  // height: calc(100vh - 223px;);
  // height: calc(100vh - 172px;);
}

.butto{
  height:0.7rem;
  // background-color: #FB8600;
  background: linear-gradient(left, #FF8E01, #FE5200);
  font-size: 0.3rem;
  padding: 0 0.2rem;
}
.butto_2{
  border-radius: 0 25px 25px 0;
}
.change{
  position: fixed;
  z-index: 1000;
  width: 2rem;
  bottom: 20%;
}

</style>
<style lang='less'>
.yd-tab-nav-item {
  line-height: .8rem !important;

}
.yd-tab-nav-item{
  padding: 0px 10px !important;
}
.search_sty{
  .yd-search-input {
    background: #fff;
    border-bottom: none;
  }
  .yd-search-input>.search-input{
    background: #F5F5F5;
  }
  .search-input{
    border-radius: 45px;
    height:45px;
    font-size: 14px;
  }
}
</style>
