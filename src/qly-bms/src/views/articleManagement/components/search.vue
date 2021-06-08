<!-- 搜索框 -->
<template>
  <div class="div_sty">
    <el-input
      v-model="input"
      :placeholder="placeholder"
      style="width: 250px;margin-right:10px;"
      size="mini"
      @keyup.enter.native="input_s"
    >
      <el-button slot="prefix" style="color:#A8A8A8" type="text" @click="input_s">
        <i class="el-input__icon el-icon-search" />
      </el-button>
      <el-button slot="append" icon="icon_qly_loudou" class="but_sty_input" @click="openVisible" />
    </el-input>
    <el-select
      v-model="value"
      placeholder="选择分类"
      size="mini"
      style="width: 150px;"
      @change="valueChange"
    >
      <el-option
        v-for="item in classificationOptions"
        :key="item.id"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
    <el-drawer
      ref="drawer"
      title="快速筛选"
      :visible.sync="dialog"
      :with-header="false"
      direction="ltr"
      size="22%"
      custom-class="demo-drawer"
      :wrapper-closable="false"
    >
      <div class="demo-drawer__content">
        <div class="titile_sty_sx">
          快速筛选
          <div class="el-icon-close close_sty" @click="closeDrawer" />
        </div>
        <el-form :model="form" style="padding:10px;" label-width="100px" class="bady_sty_drawer">
          <el-form-item label="文章标题：">
            <el-input v-model="form.articleTitle" placeholder="请输入文章标题" style="width:90%;" size="mini" />
          </el-form-item>
          <el-form-item label="来源：">
            <el-input v-model="form.sourceValue" placeholder="请输入文章来源" style="width:90%;" size="mini" />
          </el-form-item>
          <el-form-item label="归属类别：">
            <el-select
              v-model="form.ascription"
              filterable
              placeholder="请选择文章归属类别"
              size="mini"
              style="width:90%;"
            >
              <el-option
                v-for="item in ascriptionOptions"
                :key="item.category_id"
                :label="item.category_name"
                :value="item.category_id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="审核人：">
            <el-input v-model="form.auditor" placeholder="请输入审核人" style="width:90%;" size="mini" />
          </el-form-item>
          <el-form-item label="是否置顶：">
            <el-radio v-model="form.roofPlacement" label="1">是</el-radio>
            <el-radio v-model="form.roofPlacement" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="采集日期段：">
            <el-date-picker
              v-model="form.dates"
              style="width:90%;"
              type="daterange"
              range-separator="至"
              start-placeholder="采集日期起"
              end-placeholder="采集日期止"
              size="mini"
            />
          </el-form-item>
        </el-form>
        <div class="demo-drawer__footer footer_sty">
          <el-button
            size="mini"
            type="text"
            icon="icon_qly_save"
            style="margin-left:10px;"
            @click="saveClassification"
          > 保存分类
          </el-button>
          <span style="float:right;margin-right:10px;">
            <el-button size="mini" @click="reset">重 置</el-button>
            <el-button type="primary" size="mini" @click="advancedSearch">查 询</el-button>
          </span>

        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {
  getCategoryList, // 获取归属类别
  queryUserCustomQuery // 查询用户自定义查询分类列表接口
} from '@/api/public.js'
export default {
  name: '',
  components: {},
  props: {
    // 模糊查询 提示语
    placeholder: {
      type: String,
      default: '请输入查询'
    },
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      input: '', // 输入框
      value: '', // 分类选择项
      classificationOptions: [], // 分类下拉数据
      dialog: false, // 控制高级查询
      form: {
        articleTitle: '', // 文章标题
        dates: '', // 采集日期段
        sourceValue: '', // 文章来源
        ascription: '', // 归属类别
        roofPlacement: '1', // 是否置顶
        auditor: '' // 审核人
      },
      ascriptionOptions: [] // 归属类别下拉数据
    }
  },

  created() {
    this.fundQueryUserCustomQuery() // 查询用户自定义分类
    this.fundGetCategoryList() // 获取归属类别列表
  },

  methods: {
    // 获取归属类别列表
    fundGetCategoryList() {
      getCategoryList().then(res => {
        this.ascriptionOptions = res.data
      })
    },
    // 查询用户自定义分类
    fundQueryUserCustomQuery() {
      queryUserCustomQuery(this.code).then(res => {
        this.classificationOptions = res.data.data.customList
      })
    },
    // 选择自定义分类
    valueChange(val) {
      this.$emit('on-valueChange', val)
    },
    // 高级搜索
    advancedSearch() {
      this.$refs.drawer.closeDrawer()
    },
    // 搜索 模糊查询
    input_s() {
      this.$emit('on-search', this.input)
    },
    // 打开高级查询
    openVisible() {
      this.dialog = true
      this.form.articleTitle = this.input
    },
    // 关闭高级查询
    closeDrawer() {
      this.dialog = false
    },
    // 重置
    reset() {
      this.form.articleTitle = '' // 文章标题
      this.form.dates = '' // 采集日期段
      this.form.sourceValue = '' // 文章来源
      this.form.ascription = '' // 归属类别
      this.form.roofPlacement = '1' // 是否置顶
      this.form.auditor = '' // 审核人
    },
    // 保存分类
    saveClassification() {

    }
  }
}

</script>
<style lang='scss' scoped>
.bady_sty_drawer{
 height: calc(100vh - 120px)
}
.titile_sty_sx{
 font-weight: bold;
 letter-spacing:1px;
 position: relative;
 padding: 15px 10px;
 color: #666;
 background: #fff;
 border-bottom: 1px solid #f4f4f4;
}
.div_sty{
 height: 45px;
}
</style>
<style lang="scss" scoped>
.but_sty_input{
 padding: 3px 8px;
}
.footer_sty{
 border-top: 1px solid #f4f4f4;
 margin-top: 20px;
 height:40px;
 line-height: 40px;
}
.close_sty{
 width: 20px;
 height: 20px;
 border-radius: 10px;
 line-height: 20px;
 font-size: 12px;
 text-align: center;
 position: absolute;
 top: 12px;
 right: 5px;
 cursor: pointer;
 &:hover{
  background: #3487E2;
  color: #fff;
 }
}
</style>
