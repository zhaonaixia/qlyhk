<template>
  <div class="tags-view-container">
    <scroll-pane ref="scrollPane" class="tags-view-wrapper">
      <router-link
        v-for="tag in Array.from(visitedViews)"
        ref="tag"
        :key="tag.path"
        :class="isActive(tag)?'active':''"
        :to="tag"
        class="tags-view-item"
        @contextmenu.prevent.native="openMenu(tag,$event)"
      >
        {{ tag.title }}
        <span v-if="!tag.meta.affix" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)" />
      </router-link>
    </scroll-pane>
    <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">刷新标签页</li>
      <li v-if="!(selectedTag.meta&&selectedTag.meta.affix)" @click="closeSelectedTag(selectedTag)">关闭标签页</li>
      <li @click="closeOthersTags">关闭其它标签页</li>
      <li @click="closeRightTags">关闭右侧标签页</li>
      <li @click="closeAllTags">关闭所有标签页</li>
    </ul>
  </div>
</template>

<script>
import ScrollPane from '@/components/scrollPane'
export default {
  name: 'TagsView',
  components: { ScrollPane },
  data() {
    return {
      visible: false,
      top: 0,
      left: 0,
      selectedTag: {},
      closeRight: ''
    }
  },
  computed: {
    visitedViews() {
      return this.$store.state.tagsView.visitedViews
    }
  },
  watch: {
    $route() {
      this.addViewTags()
      this.moveToCurrentTag()
    },
    visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  created() {
    const that = this
    window.addEventListener('beforeunload', () => {
      const value = that.$store.state.tagsView.visitedViews
      const routArry = []
      for (let i = 0; i < value.length; i++) {
        const obj = { name: value[i].name, meta: value[i].meta, path: value[i].path }
        routArry.push(obj)
      }
      window.sessionStorage.asd = JSON.stringify(routArry)
    })
    if (sessionStorage.asd !== undefined) {
      const routers = JSON.parse(sessionStorage.asd)
      for (let j = 0; j < routers.length; j++) {
        if (routers[j].meta.affix) {
          that.$store.dispatch('addView', routers[j])
        }
      }
    }
  },
  mounted() {
    this.addViewTags()
  },
  methods: {
    generateRoute() {
      if (this.$route.name) {
        return this.$route
      }
      return false
    },
    isActive(route) {
      return route.path === this.$route.path
    },
    addViewTags() {
      const route = this.generateRoute()
      if (!route) {
        return false
      }
      this.$store.dispatch('addView', route)
    },
    moveToCurrentTag() {
      const tags = this.$refs.tag
      this.$nextTick(() => {
        if (tags !== undefined) {
          for (const tag of tags) {
            if (tag.to.path === this.$route.path) {
              this.$refs.scrollPane.moveToTarget(tag.$el)
              break
            }
          }
        }
      })
    },
    refreshSelectedTag(view) {
      const row = this.$route.query
      this.$store.dispatch('delCachedView', view).then(() => {
        const { fullPath } = view
        this.$router.replace({
          path: '/redirect' + fullPath,
          // query: {
          //   cust_id: row.cust_id,
          //   code: row.code
          // }
          query: row
        })
      })
    },
    closeSelectedTag(view) {
      this.$store.dispatch('delView', view).then(({ visitedViews }) => {
        if (this.isActive(view)) {
          const latestView = visitedViews.slice(-1)[0]
          if (latestView) {
            this.$router.push(latestView)
          } else {
            this.$router.push('/')
          }
        }
      })
    },
    // 关闭右侧标签页
    closeRightTags() {
      const visitedViews = this.$store.getters.visitedViews
      const arry = []
      for (let i = 0; i < visitedViews.length; i++) {
        if (visitedViews[i].title !== this.closeRight) {
          arry.push(visitedViews[i])
          if (visitedViews[i].title === this.closeRight) {
            arry.push(visitedViews[i])
            break
          }
        } else {
          arry.push(visitedViews[i])
          break
        }
      }
      this.$store.state.tagsView.visitedViews = arry
      // this.$store.dispatch('visitedViews', arry)
      // this.$router.push(arry[arry.length - 1].path)
    },
    // 关闭其他标签页
    closeOthersTags() {
      this.$router.push(this.selectedTag)
      this.$store.dispatch('delOthersViews', this.selectedTag).then(() => {
        this.moveToCurrentTag()
      })
    },
    closeAllTags() {
      this.$store.dispatch('delAllViews')
      this.$router.push('/')
    },
    openMenu(tag, e) {
      this.closeRight = tag.title
      this.visible = true
      this.selectedTag = tag
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      this.left = e.clientX - offsetLeft - 45 // 15: margin right
      this.top = e.clientY + 17
    },
    closeMenu() {
      this.visible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 36px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      box-shadow: 2px 3px 5px 0 #606060;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 8px;
      margin-top: 4px;
      &:hover{
        background-color: #409EFF;
        color: #fff;
        border-color: #409EFF;
        font-weight: bold;
      }
      &:first-of-type {
        margin-left: 15px;
      }
      &:last-of-type {
        margin-right: 15px;
      }
      &.active {
        background-color: #3487E2;
        color: #fff;
        border-color: #3487E2;
        font-weight: bold;
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }
  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);
    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;
      &:hover {
        background: #eee;
      }
    }
  }
}
</style>

<style lang="scss">
//reset element css of el-icon-close
.tags-view-wrapper {
  .tags-view-item {
    .el-icon-close {
      width: 16px;
      height: 16px;
      vertical-align: 2px;
      border-radius: 50%;
      text-align: center;
      transition: all .3s cubic-bezier(.645, .045, .355, 1);
      transform-origin: 100% 50%;
      &:before {
        transform: scale(.6);
        display: inline-block;
        vertical-align: -3px;
      }
      &:hover {
        background-color: #fff;
        color: #000;
      }
    }
  }
}
</style>
