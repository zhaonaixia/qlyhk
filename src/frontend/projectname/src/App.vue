<template>
 <div id="app">
    <meta name="referrer" content="no-referrer">
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive" />
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive" />
</div>
</template>

<script>
export default{
  name: 'app'
}
</script>

<style lang="less">
 @import "./style/iconfont/ico.css";
</style>
