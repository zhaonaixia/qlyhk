import Vue from 'vue'
import Vuex from 'vuex'
import home from './home'
import createLogger from 'vuex/dist/logger'

Vue.use(Vuex)
const debug = true

export default new Vuex.Store({
  modules: {
    home
  },
  plugins: debug ? [createLogger()] : [] // 是否开启vuex的debug模式
})
