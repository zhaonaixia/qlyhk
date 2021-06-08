import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'homepage', affix: true }
    }]
  },
  // 文章管理
  {
    path: '/articleManagement',
    component: Layout,
    redirect: '/articleManagement/index',
    children: [
      {
        path: 'index',
        name: 'ArticleManagement',
        component: () => import('@/views/articleManagement/index.vue'),
        meta: { title: '文章管理', icon: 'article' }
      }
    ]
  },
  // 分类管理
  {
    path: '/classifiedManagement',
    component: Layout,
    redirect: '/classifiedManagement/index',
    children: [
      {
        path: 'index',
        name: 'ClassifiedManagement',
        component: () => import('@/views/classifiedManagement/index.vue'),
        meta: { title: '分类管理', icon: 'classification' }
      }
    ]
  },
  // 财税早报
  {
    path: '/financeMorningPaper',
    component: Layout,
    redirect: '/financeMorningPaper/index',
    children: [
      {
        path: 'index',
        name: 'FinanceMorningPaper',
        component: () => import('@/views/financeMorningPaper/index.vue'),
        meta: { title: '财税早报', icon: 'wealth' }
      }
    ]
  },
  // 阅读情况
  {
    path: '/readingSituation',
    component: Layout,
    redirect: '/readingSituation/index',
    children: [
      {
        path: 'index',
        name: 'ReadingSituation',
        component: () => import('@/views/readingSituation/index.vue'),
        meta: { title: '阅读情况', icon: 'read' }
      }
    ]
  },
  // 用户管理
  {
    path: '/userManagement',
    component: Layout,
    redirect: '/userManagement/index',
    children: [
      {
        path: 'index',
        name: 'UserManagement',
        component: () => import('@/views/userManagement/index.vue'),
        meta: { title: '用户管理', icon: 'users' }
      }
    ]
  },
  // 统计数据
  {
    path: '/statisticalData',
    component: Layout,
    redirect: '/statisticalData/monthlyTop',
    name: 'StatisticalData',
    meta: {
      title: '统计数据',
      icon: 'statistics'
    },
    children: [
      // 月度top10
      {
        path: 'monthlyTop',
        component: () => import('@/views/statisticalData/monthlyTop/index.vue'),
        name: 'MonthlyTop',
        meta: { title: '月度TOP 10' }
      },
      // 行为时段分析
      {
        path: 'behaviorPeriodAnalysis',
        component: () => import('@/views/statisticalData/behaviorPeriodAnalysis/index.vue'),
        name: 'BehaviorPeriodAnalysis',
        meta: { title: '行为时段分析' },
        children: [
          {
            path: 'sharingSession',
            component: () => import('@/views/statisticalData/behaviorPeriodAnalysis/sharingSession'),
            name: 'SharingSession',
            meta: { title: '分享时段分析' }
          },
          {
            path: 'readingPeriod',
            component: () => import('@/views/statisticalData/behaviorPeriodAnalysis/readingPeriod'),
            name: 'ReadingPeriod',
            meta: { title: '阅读时段分析' }
          }
        ]
      },
      // 用户区域分布
      {
        path: 'userDistribution',
        component: () => import('@/views/statisticalData/userDistribution/index.vue'),
        name: 'UserDistribution',
        meta: { title: '用户区域分布' }
      }
    ]
  },
  // 外部路由
  // {
  //   path: 'external-link',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
  //       meta: { title: 'External Link', icon: 'link' }
  //     }
  //   ]
  // },

  // 404页必须放在末尾！!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // 重置路由器
}

export default router
