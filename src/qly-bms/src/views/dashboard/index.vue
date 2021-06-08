<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>
    <div class="container">
      <div href="http://hovertree.com/" class="btn btn-1">
        <svg>
          <rect x="0" y="0" fill="none" width="100%" height="100%" />
        </svg>
        Hover
      </div>
      <div class="btn btn-2">何问起去</div>
      <div class="btn btn-3">其味无穷</div>
      <div class="btn btn-4">阿萨德是</div>
      <div class="btn btn-5">染色的无</div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
