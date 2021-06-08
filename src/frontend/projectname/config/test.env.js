'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"tests"',
  ES_NAME: '测试',
  EVN_CONFIG: '"test"',
  BASE_API: '"http://47.107.46.219:8089/qlyhk/"',
  BASE_URL: '"http://www.finway.com.cn/qlyhk-rh"',
  REDIRECT_URL: '"http://www.finway.com.cn/share"'
})
