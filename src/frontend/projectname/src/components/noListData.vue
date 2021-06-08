<template>
  <div class="no-more">
    <i class="icon_qly_noData span1_sty"></i>
    <div class="span2_sty">抱歉，暂无数据</div>
  </div>
</template>

<script>
export default {
  name: 'NoListData',
  props: {
    // current: {
    //   type: Number,
    //   default: 0
    // }
  },
  data () {
    return {
      // active: this.current
    }
  },
  mounted () {
    // this.$refs.dotted.style.left = this.$refs.circular[0].offsetLeft + 'px'
  }
}
</script>

<style lang="less">
// 暂无数据
.no-more{
  text-align: center;
  margin-top: 1rem;

  .span1_sty{
    font-size: 1.2rem;
    color: #cdcdcd;
  }
  .span2_sty{
    margin-top: .133333rem;
    font-size: .34rem;
    color: #cdcdcd;
  }
}
</style>
