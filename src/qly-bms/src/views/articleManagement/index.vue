<!-- 文章管理 -->
<template>
  <div>
    <!-- 头部搜索 -->
    <!--
      on-valueChange 选择自定义分类
     -->
    <search
      @on-valueChange="valueChange"
    />
    <!-- 主体表格 -->
    <el-table :data="tableData" style="width: 100%" stripe :height="tableHeight">
      <el-table-column
        v-for="(fruit,index) in formThead"
        :key="index"
        :label="fruit.head_name"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="fruit.head_key==='pic_url'">
            <img :src="scope.row[fruit.head_key]" alt="" class="img_sty">
          </span>
          <span v-else>{{ scope.row[fruit.head_key] }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" align="right" width="50">
        <template slot="header">
          <!--
            data 初始列表数据
            on-saveLayout 保存自定义布局
          -->
          <CustomColumns
            :data="formThead"
            @on-saveLayout="saveLayout"
          />
        </template>
      </el-table-column>
    </el-table>
    <!-- 底部分页 -->
    <el-pagination
      style="margin-top: 10px;float: right;"
      :current-page="currentPage"
      :page-sizes="[15, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
// import { headList, bodyList } from './mock_data.js'
import CustomColumns from '@/components/CustomColumns' // 自定义列
import search from './components/search' // 搜索框
import {
  queryDataForUserCustomQuery // 根据用户自定义查询分类查询数据接口
} from '@/api/public.js'
import {
  getArticlesList // 获取文章列表
} from '@/api/articleManagement.js'
export default {
  name: 'ArticleManagement',

  components: {
    CustomColumns, // 自定义列
    search // 搜索框
  },
  data() {
    return {
      pageSize: 15,
      currentPage: 1,
      total: 50,
      from: {}, // 高级查询数据
      tableData: [], // 表格数据
      formThead: [], // 表头数据
      tableHeight: document.documentElement.clientHeight - 215
    }
  },

  created() {
    this.fundGetArticlesList()// 获取文章列表
  },

  methods: {
    // 保存自定义布局
    saveLayout(val) {
      console.log(val)
    },
    // 获取文章列表
    fundGetArticlesList() {
      const par = {

        pageQueryVO: {
          currentPage: this.currentPage,
          pageSize: this.pageSize
        }
      }
      getArticlesList(par).then(res => {
        this.formThead = res.data.headList
        this.tableData = res.data.bodyList
      })
    },
    // 选择自定义分类
    valueChange(val) {
      const par = {
        id: val,
        pageQueryVO: {
          currentPage: this.currentPage,
          pageSize: this.pageSize
        }
      }
      // 根据用户自定义查询分类查询数据接口
      queryDataForUserCustomQuery(par).then(res => {
        this.formThead = res.data.data.headList
        this.tableData = res.data.data.bodyList
      })
    },
    // 切换每页显示
    handleSizeChange(val) {

    },
    // 切换页码
    handleCurrentChange(val) {

    }
  }
}

</script>
<style lang='scss' scoped>
.img_sty{
  width: 30px;
  height: 30px;
  border-radius: 4px;
}
</style>
<style lang="scss">
</style>
