import Vue from 'vue'
import Router from 'vue-router'
import Getarticle from '@/pages/getarticle.vue'
// import Contents from '@/pages/contents.vue'
import Business from '@/pages/business.vue'
import Personal from '@/pages/personal.vue'
import Operating from '@/pages/operating.vue'
import Visitorcondition from '@/pages/visitorcondition.vue'
import Tracepassenger from '@/pages/tracepassenger.vue'
import Buymember from '@/pages/buymember.vue'
import Setnews from '@/pages/setnews/setnews.vue'
import Readcondition from '@/pages/readarticle/readcondition.vue'
import Readdetails from '@/pages/readarticle/readdetails.vue'
import Subscription from '@/pages/readarticle/subscription'
import TestPage from '@/pages/testPage/index.vue'

// 人脉雷达
import Connections from '@/pages/connections/index.vue'
Vue.use(Router)

export default new Router({
  // base: '/qlyhk-rh/',
  // mode: 'history',
  routes: [
    {
      path: '/',
      redirect: { name: 'getarticle' }
    },
    {
      name: 'getarticle',
      path: '/getarticle',
      component: Getarticle,
      meta: { title: '文章列表' }

    },
    {
      name: 'Connections',
      path: '/connections',
      component: Connections,
      meta: { title: '人脉雷达' }

    },
    {
      name: 'contents',
      path: '/contents',
      component: resolve => require(['@/pages/contents.vue'], resolve), // 路由懒加载,
      meta: { title: '文章详情', isBack: false, keepAlive: true }

    },
    {
      name: 'business',
      path: '/business',
      component: Business,
      meta: { title: '设置名片' }
    },
    {
      name: 'personal',
      path: '/personal',
      component: Personal,
      meta: { title: '个人中心' }
    },
    {
      name: 'Operating',
      path: '/operating',
      component: Operating,
      meta: { title: '使用指南' }
    },
    {
      name: 'Visitorcondition',
      path: '/visitorcondition',
      component: Visitorcondition,
      meta: { title: '访客情况' }
    },
    {
      name: 'Tracepassenger',
      path: '/tracepassenger',
      component: Tracepassenger,
      meta: { title: '追踪客源' }
    },
    {
      name: 'Buymember',
      path: '/buymember',
      component: Buymember,
      meta: { title: '购买会员' }
    },
    {
      name: 'Setnews',
      path: '/setnews',
      component: Setnews,
      meta: { title: '消息提醒设置' }
    },
    {
      name: 'Readcondition',
      path: '/readcondition',
      component: Readcondition,
      meta: { title: '文章阅读情况' }
    },
    {
      name: 'Readdetails',
      path: '/readdetails',
      component: Readdetails,
      meta: { title: '文章阅读详情' }
    },
    {
      name: 'Subscription',
      path: '/subscription',
      component: Subscription,
      meta: { title: '订阅文章' }
    },
    {
      name: 'TestPage',
      path: '/testPage',
      component: TestPage,
      meta: { title: '测试文章显示' }
    }
  ]
})
