<template>
  <!-- 名片设置 -->
  <div class="business">
    <div class="headportrait">
      <div class="head">头像</div>
      <div class="portrait">
        <img :src="form.headImgUrl" alt />
      </div>
      <div style="font-size:25px;color:#ccc;float:right;margin-top:0.2rem">
        <i class="iconfont icon-yduiqianjin"></i>
      </div>
    </div>
    <div class="wechat" @click="openFile">
      <div>微信二维码</div>
      <div>
        <i class="iconfont icon-ico" style="font-size:25px;"></i>
        <i class="iconfont icon-yduiqianjin" style="font-size:18px;color:#ccc;margin:0 0.1rem"></i>
      </div>
    </div>
    <div class="inputlist">
      <yd-cell-group>
        <yd-cell-item>
          <span slot="left">姓名</span>
          <yd-input
            slot="right"
            v-model="form.name"
            :debug="true"
            placeholder="请输入姓名">
            {{ form.name }}
          </yd-input>
        </yd-cell-item>

        <yd-cell-item>
          <span slot="left" style="margin-right: 1.4rem;">手机号码</span>
          <yd-input
            slot="right"
            ref="contactsTel"
            v-model="form.phone"
            regex="mobile"
            placeholder="请输入手机号码">
            {{ form.phone }}
          </yd-input>
        </yd-cell-item>

        <yd-cell-item>
          <span slot="left">职位</span>
          <yd-input slot="right" v-model="form.position" placeholder="请输入职位">{{ form.position }}</yd-input>
        </yd-cell-item>
        <yd-cell-item>
          <span slot="left">公司</span>
          <yd-input slot="right" v-model="form.company" placeholder="请输入公司">{{ form.company }}</yd-input>
        </yd-cell-item>
        <yd-cell-item>
          <span slot="left">行业</span>
          <yd-input slot="right" v-model="form.industry" placeholder="请输入行业">{{ form.industry }}</yd-input>
        </yd-cell-item>
        <yd-cell-item>
          <span slot="left">城市</span>
          <yd-input slot="right" v-model="form.city" placeholder="请输入城市">{{ form.city }}</yd-input>
        </yd-cell-item>
        <yd-cell-item>
          <span slot="left">个人简介</span>
        </yd-cell-item>
        <yd-cell-group>
          <yd-cell-item>
            <yd-textarea v-model="form.personal_profile" slot="right" maxlength="100">{{ form.personal_profile }}</yd-textarea>
          </yd-cell-item>
        </yd-cell-group>
      </yd-cell-group>
    </div>
    <div class="footbutton">
      <yd-button
        size="large"
        type="danger"
        shape="circle"
        :disabled="saveDisabled"
        @click.native="submit">
        保存
      </yd-button>
    </div>
     <!-- 二维码弹出框 -->
    <input
      ref="inputFile"
      type="file"
      name="files"
      class="file_sty"
      @change="addImg">
  </div>
</template>
<script>
import { getUserInfo } from '@/api/personage.js'
import { uploadFile } from '@/utils/reqUpload'

export default {
  data () {
    return {
      saveDisabled: false, // 控制保存按钮
      file: null,
      form: {
        ewm_url: '', // 二维码
        headImgUrl: '', // 头像
        name: '', // 姓名
        phone: '', // 手机
        position: '', // 职位
        company: '', // 公司
        industry: '', // 行业
        city: '', // 城市
        personal_profile: ''// 个人简介
      },
      uploadForm: new FormData(), // 保存传值
      userId: '', // 用户唯一ID
      businData: {},
      showHead: false,
      myItems1: [
        {
          label: '拍照',
          callback: () => {
            this.$dialog.toast({mes: '咔擦，此人太帅！'})
            /* 注意： callback: function() {} 和 callback() {}  这样是无法正常使用当前this的 */
          }
        },
        {
          label: '从相册中选择',
          callback: () => {
            this.$dialog.toast({mes: '看到了不该看到的东西！'})
          }
        }
      ]
    }
  },
  created () {
    this.userId = this.$route.params.opId
    this.loaddata()
  },
  methods: {
    // 打开图片选取
    openFile () {
      this.$refs.inputFile.click()
    },
    // 选取图片触发
    addImg (event) {
      if (event.target.files.length !== 0) {
        if (event.target.files[0].size > 5000000) {
          this.$dialog.toast({
            mes: '上传的图片不能大于5M！',
            timeout: 1500
          })
          this.saveDisabled = true
        } else {
          this.file = event.target.files[0]
          this.saveDisabled = false
        }
      } else {
        this.saveDisabled = false
      }
    },
    loaddata () {
      const id = this.userId
      getUserInfo(id).then(res => {
        if (res.data.code === '0') {
          this.businData = res.data.data
          this.form.headImgUrl = this.businData.headImgUrl === '' ? '' : this.businData.headImgUrl
          this.form.ewm_url = this.businData.ewm_url === '' ? '' : this.businData.ewm_url
          this.form.name = this.businData.user_name === '' ? '' : this.businData.user_name
          this.form.phone = this.businData.telphone === '' ? '' : this.businData.telphone
          this.form.position = this.businData.position === '' ? '' : this.businData.position
          this.form.company = this.businData.company === '' ? '' : this.businData.company
          this.form.position = this.businData.position === '' ? '' : this.businData.position
          this.form.industry = this.businData.industry === '' ? '' : this.businData.industry

          this.form.city = this.businData.city === '' ? '' : this.businData.city
          this.form.personal_profile = this.businData.personal_profile === '' ? '' : this.businData.personal_profile
        }
      })
    },

    // 保存
    submit () {
      // 判断姓名是否为空
      if (this.form.name === '') {
        this.$dialog.notify({
          mes: '姓名不能为空！！',
          timeout: 1500
        })
      } else {
        // 判断联系人手机号是否填写正确
        const contactsTelRul = this.$refs.contactsTel
        if (contactsTelRul.errorMsg !== '') {
          this.$dialog.notify({
            mes: '手机号码' + contactsTelRul.errorMsg,
            timeout: 1500
          })
        } else {
          this.form.name = this.form.name ? this.form.name : ''
          this.form.phone = this.form.phone ? this.form.phone : ''
          this.form.position = this.form.position ? this.form.position : ''
          this.form.company = this.form.company ? this.form.company : ''
          this.form.industry = this.form.industry ? this.form.industry : ''
          this.form.city = this.form.city ? this.form.city : ''
          this.form.personal_profile = this.form.personal_profile ? this.form.personal_profile : ''
          this.uploadForm = new FormData()
          this.uploadForm.append('openId', this.userId)
          if (this.file !== null) {
            this.uploadForm.append('ewmImg', this.file)
          }
          this.uploadForm.append('user_name', this.form.name)
          this.uploadForm.append('telphone', this.form.phone)
          this.uploadForm.append('industry', this.form.industry) // 行业
          this.uploadForm.append('position', this.form.position) // 职位
          this.uploadForm.append('company', this.form.company) // 公司
          this.uploadForm.append('city', this.form.city) // 城市
          this.uploadForm.append('personal_profile', this.form.personal_profile) // 个人简介
          uploadFile('openapi/user/saveEditUserInfo.do', this.uploadForm).then(res => {
            if (res.data.code === '0') {
              this.$dialog.toast({
                mes: '保存成功',
                timeout: 1500,
                icon: 'success'
              })
              this.$router.push({
                path: 'personal',
                params: {
                  success: new Date()
                }
              })
            }
          }).catch(e => {
            alert(e)
          })
        }
      }
    }
  }
}
</script>
<style lang="less" scoped>
.file_sty{
  display: none;
}
.business {
  height: 100vh;
  background-color: #fff;
}
.headportrait {
  padding: 0.1rem 0 0 0.4rem;
  display: flex;
  height: 1.4rem;
  justify-content: space-between;
  border-bottom: 1px solid #ccc;
  .head {
    height: 1rem;
    line-height: 1rem;
    font-size: 16px;
    font-weight: 600;
  }
  .portrait {
    position: relative;
    right: -2.4rem;
    height: 1rem;
    width: 1rem;
    img {
      width: 100%;
      height: 100%;
    }
  }
}
.inputlist {
  padding: 0.2rem 0 0 0.2rem;
  span {
    color: #000;
    font-size: 16px;
    font-weight: 600;
    margin-right: 2rem;
  }
}
.wechat {
  display: flex;
  padding: 0 0 0 0.4rem;
  height: 1.2rem;
  line-height: 1.2rem;
  font-size: 16px;
  font-weight: 600;
  justify-content: space-between;
  border-bottom: 2px solid #ccc;
}
.footbutton {
  // background-color: #ccc;
  position: relative;
    &::before{
      position: absolute;
      width:100%;
      content:"";
      bottom:0;
      left:0;
    }
}
</style>
