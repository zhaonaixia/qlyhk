<!-- 自定义列 -->
<template>
  <span class="span_sty">
    <el-tooltip class="item" effect="dark" content="自定义列设置" placement="left">
      <i class="el-icon-setting" @click="open" />
    </el-tooltip>
    <el-dialog
      title="自定义列设置"
      append-to-body
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="22%"
      top="60px"
      class="dialog_stys"
    >
      <el-table
        ref="dragTable"
        :data="tableData"
        :height="tableHeight"
        style="width: 100%"
        class="table_stty"
      >
        <el-table-column
          prop="head_name"
          label="字段"
        />
        <el-table-column
          prop="head_name"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-checkbox v-model="!scope.row.head_hide" />
          </template>
        </el-table-column>
        <el-table-column
          prop="head_name"
          label="排 序"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <i class="el-icon-top font_sty" @click="moveUpward(scope.$index)" />
            <i class="el-icon-bottom font_sty" @click="moveDown(scope.$index)" />
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="save">保存布局</el-button>
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
import Sortable from 'sortablejs' // 引入拖拽库
export default {
  name: '',

  components: {},
  props: {
    data: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false, // 控制自定义列设置 弹窗
      tableHeight: document.documentElement.clientHeight - 230,
      tableData: [],
      oldList: [], // 初始值
      newList: [] // 自定义排序后的值
    }
  },

  created() {
  },
  mounted() {
  },
  methods: {
    // 上移
    moveUpward(index) {
      if (index === 0) {
        this.$message({ message: '已经处于顶端，不能再上移了！', type: 'warning' })
      } else {
        const obj = this.newList[index]
        this.newList.splice(index, 1)
        this.newList.splice(index - 1, 0, obj)
        this.tableData = this.newList
      }
    },
    // 下移
    moveDown(index) {
      if (index + 1 === this.newList.length) {
        this.$message({ message: '已经处于末尾，不能再下移了！', type: 'warning' })
      } else {
        1
      }
      const obj = this.newList[index]
      this.newList.splice(index, 1)
      this.newList.splice(index + 1, 0, obj)
      this.tableData = this.newList
    },
    // 保存布局
    save() {
      this.dialogVisible = false
      this.$emit('on-saveLayout', this.newList)
    },
    // 打开
    open() {
      console.log(this.data)
      this.dialogVisible = true
      this.tableData = this.data.slice()
      this.oldList = this.data.slice()
      this.newList = this.oldList.slice()
      this.$nextTick(() => {
        this.setSort()
      })
    },
    // 拖拽
    setSort() {
      const example1 = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      new Sortable(example1, {
        animation: 150,
        ghostClass: 'blue-background-class',
        onEnd: evt => {
          const targetRow = this.oldList.splice(evt.oldIndex, 1)[0]
          this.oldList.splice(evt.newIndex, 0, targetRow)

          // for show the changes, you can delete in you code
          const tempIndex = this.newList.splice(evt.oldIndex, 1)[0]
          this.newList.splice(evt.newIndex, 0, tempIndex)
        }
      })

      // new Sortable(example1, {
      //   multiDrag: true, // Enable multi-drag
      //   selectedClass: 'selected', // The class applied to the selected items
      //   animation: 150
      // })
    }
  }
}

</script>
<style lang='scss' scoped>
.font_sty{
  font-weight: bold;
  cursor:pointer;
  margin-left: 5px;
  &:hover{
    color: #3487E2;
  }
}
.title_sty{
 font-weight: bold;
}
.span_sty{
  cursor:pointer;
  &:hover{
   color: #3487E2;
  }
}
</style>
<style lang="scss">
.table_stty{
 .el-table__header th{
   background: #fff!important;
   border-bottom: 1px solid #fff;
 }
}
.dialog_stys{
 .el-dialog__body{
  padding-top: 0px;
 }
 // .el-dialog__header{
 //  box-shadow: 1px 1px 1px 1px rgba(0,0,0,.1);
 // }
 .table_stty{
  max-height:  calc(100vh - 290px);
 }
 .el-dialog__title{
  font-size: 16px;
  font-weight: bold;
  letter-spacing:1px;
 }
}
</style>
