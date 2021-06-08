<!-- 测试 -->
<template>
  <div>
    <span>我是来测试页面显示的：</span>
   <div v-html="url"></div>
  </div>
</template>

<script>
import { getReptileArticleInfo } from '@/api/testPage.js'
import {
  getQuery
  // getQueryVariable // 获取url参数
} from '@/utils/getUrlParam.js'
export default {
  name: 'TestPag',

  components: {},
  data () {
    return {
      url: ''
    }
  },

  created () {
    let id = getQuery('articleId')
    alert('文章ID：' + id)
    this.fundGetReptileArticleInfo(id)
  },
  methods: {
    fundGetReptileArticleInfo (id) {
      getReptileArticleInfo(id).then(res => {
        this.url = res.data.data.article_content
      })
    }
  }
}

</script>
