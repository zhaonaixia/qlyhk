'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  ES_NAME: '开发',
  BASE_API: '"http://47.107.46.219:8089/qlyhk/"',
  BASE_URL: '"http://www.finway.com.cn/qlyhk-rh"',
  REDIRECT_URL: '"http://www.finway.com.cn/share"'
})
