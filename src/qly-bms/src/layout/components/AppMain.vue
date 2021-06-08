<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
$radius:2em;
.app-main {
  /*50 = navbar  */
  height: calc(100vh - 86px);
  width: 100%;
  position: relative;
  padding: 20px;
  overflow:auto;
  &::-webkit-scrollbar {
    width: 4px;
  }
  &::-webkit-scrollbar-track {
    background-color:#FFFFFF;
    -webkit-border-radius: $radius;
    -moz-border-radius: $radius;
    border-radius:$radius;
  }
  &::-webkit-scrollbar-thumb {
    background-color:#C8C8C8;
    -webkit-border-radius: $radius;
    -moz-border-radius: $radius;
    border-radius:$radius;
  }
}
.fixed-header+.app-main {
  padding-top: 50px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
