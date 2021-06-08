<template>
  <div style="height:100vh;background: #fff;">
    <yd-layout>
      <div>
        <yd-search
          v-model.trim="inputName"
          class="search_sty"
          fullpage
          :on-submit="searchName"
          placeholder="请输入标签名称">
        </yd-search>
      </div>
      <div class="category">
        <div class="headcate">
          <div style="font-size:0.35rem;font-weight:600;">行业</div>
        </div>
        <span
          v-for="(item,index) in industryDataList"
          :key="index">
          <div
            :class="item.sty ? 'div_sty_bq':'div_sty_bq1'"
            :ref="'industry'+index"
            @click="switchSty(index,1)">
            <i class="icon_qly_gou i_sty"></i>
            {{ item.category_name }}
          </div>
        </span>
      </div>
      <div class="category">
        <div class="headcate">
          <div style="font-size:0.35rem;font-weight:600;">公司品牌</div>
        </div>
        <span v-for="(item,index) in brandDataList" :key="index">
          <div
            :class="item.sty ? 'div_sty_bq':'div_sty_bq1'"
            :ref="'brand'+index"
            @click="switchSty(index,2)">
            <i class="icon_qly_gou i_sty"></i>
            {{ item.category_name }}
          </div>
        </span>
      </div>
      <yd-tabbar slot="tabbar" style="padding:0 0 1px 0">
        <yd-button
          class="footbutton"
          shape="circle"
          size="large"
          @click.native="addCate"
          type="hollow">
          保存
        </yd-button>
      </yd-tabbar>
    </yd-layout>
  </div>
</template>

<script>
import { deepCopy } from '@/utils/getUrlParam.js'
import { saveSubscribeArticles } from '@/api/personage.js' // 保存订阅接口
import { getSubscribeList, getCategoryList } from '@/api/commonality.js'
export default {
  name: 'Subscription',
  data () {
    return {
      inputName: '',
      subscribeData: [], // 订阅类别
      industryDataList: [], // 所有行业
      brandDataList: [], // 所有公司品牌
      industryDataList1: [], // 所有行业
      brandDataList1: [], // 所有公司品牌
      industryData: [], // 已订阅行业
      brandData: [], // 已订阅公司品牌
      userId: '' // 用户ID
    }
  },
  created () {
    // 获取用户ID
    this.userId = sessionStorage.userId
    this.fundGetCategoryList()
  },
  computed: {

  },
  methods: {
    // 切换样式
    switchSty (index, num) {
      if (num === 1) {
        this.industryDataList[index].sty = !this.industryDataList[index].sty
        this.replaceClass('industry', index)
      } else if (num === 2) {
        this.brandDataList[index].sty = !this.brandDataList[index].sty
        this.replaceClass('brand', index)
      }
    },
    // 替换样式
    replaceClass (str, index) {
      let ref = str + index
      let calssName = this.$refs[ref][0].className
      if (calssName === 'div_sty_bq1') {
        this.$refs[ref][0].className = 'div_sty_bq'
      } else {
        this.$refs[ref][0].className = 'div_sty_bq1'
      }
    },
    // 获取所有类别
    fundGetCategoryList () {
      getCategoryList().then(res => {
        let response = res.data.data
        // 行业
        let industryDataList = response.filter(e => { return e.type === '01' })
        // 公司
        let brandDataList = response.filter(e => { return e.type === '02' })
        industryDataList.forEach(element => {
          element.sty = false
        })
        brandDataList.forEach(element => {
          element.sty = false
        })
        this.industryDataList1 = deepCopy(industryDataList)
        this.brandDataList1 = deepCopy(brandDataList)
        // 获取当前用户已关注的
        this.loaadData(this.userId)
      })
    },
    // 获取类别
    loaadData (userId) {
      getSubscribeList(userId).then(res => {
        if (res.data.data !== null) {
          const response = res.data.data
          if (response !== undefined) {
            let industryData = response.subscribeList.filter(e => {
              return e.type === '01'
            })
            let brandData = response.subscribeList.filter(e => {
              return e.type === '02'
            })
            // 比较行业中 拿到已订阅的
            this.industryDataList1.forEach(el1 => {
              industryData.forEach(el2 => {
                if (el1.category_code === el2.category_code) {
                  el1.sty = true
                }
              })
            })
            this.brandDataList1.forEach(el1 => {
              brandData.forEach(el2 => {
                if (el1.category_code === el2.category_code) {
                  el1.sty = true
                }
              })
            })
            this.industryDataList = deepCopy(this.industryDataList1)
            this.brandDataList = deepCopy(this.brandDataList1)
          }
        } else {
          this.industryDataList = deepCopy(this.industryDataList1)
          this.brandDataList = deepCopy(this.brandDataList1)
        }
      })
    },
    // 搜索
    searchName (value) {
      this.inputName = value
      this.pagenum = 1
      if (this.inputName === '') {
        this.loadData()
      } else {
        this.tableData = this.tableData.filter(e => {
          return e.article_title.indexOf(value) !== -1
        })
      }
    },

    // 添加订阅文章
    addCate () {
      let ary = []
      let arry1 = this.industryDataList.filter(e => {
        if (e.sty) {
          return e.category_id
        }
      })
      let arry2 = this.brandDataList.filter(e => {
        if (e.sty) {
          return e.category_id
        }
      })
      let newArr = arry1.concat(arry2)
      newArr.forEach(element => {
        let obj = {
          category_id: element.category_id
        }
        ary.push(obj)
      })
      let par = {
        openId: sessionStorage.userId,
        categoryList: ary
      }
      saveSubscribeArticles(par).then(res => {
        if (res.data.code === '0') {
          this.$dialog.toast({
            mes: res.data.message,
            timeout: 1500,
            icon: 'success'
          })
          this.$router.back(-1)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.div_sty_bq{
  position: relative;
  border: 1px solid #ff5200;
  display: inline-block;
  width:1.4rem;
  height: .633333rem;
  margin-right: .133333rem;
  text-align: center;
  line-height: .633333rem;
  border-radius: .04rem;
  // background: linear-gradient(225deg, transparent 8px,#ff5200 0);
  background:linear-gradient(-135deg, #ff5200 13px, #fff 0);
  .i_sty{
    position:absolute;
    font-size: .19rem;
    margin-bottom: .146667rem;
    left: 1.19rem;
    top: -.18rem;
    color: #fff;
  }
}
.div_sty_bq1{
  position: relative;
  border: 1px solid #EDEDED;
  display: inline-block;
  width:1.4rem;
  height: .633333rem;
  margin-right: .133333rem;
  text-align: center;
  line-height: .633333rem;
  border-radius: .04rem;
  .i_sty{
    position:absolute;
    font-size: .19rem;
    margin-bottom: .146667rem;
    left: 1.19rem;
    top: -.18rem;
    color: #fff;
  }
}
.category{
  padding: 0.3rem;
  height: 4rem;
  background-color: #fff;
  .headcate{
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.1rem;
  }
  .indust{
    display: inline-block;
    margin: 0.1rem
  }
}
.footbutton{
  border:1px solid #ff5200 !important;
  color: #ff5200;
  margin: 0;
}
</style>
<style lang="less">
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
