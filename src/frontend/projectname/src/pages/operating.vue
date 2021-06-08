<template>
<!-- 帮助指南 -->
  <div class="operating">
    <div class="headhelp">
      <div class="imgpic" style="margin-right:0.1rem">
        <img src="../assets/pic1.png"  style="vertical-align:middle;" alt="">
        <span>获客小帮手</span>
      </div>
    </div>
    <yd-cell-group>
      <a href="https://mp.weixin.qq.com/s/rKM1QHb0rFPb4EG7o7T0oQ">
        <yd-cell-item arrow>
          <div slot="left" class="div_two_sty" >
            <div class="div_sty" style="background: #D5F4F7;">
              <i class="icon_qly_mingpian icon_sty_lb" style="color:#1CC7D6;"></i>
            </div>
            <div class="div_sty text_sty">
              如何设置名片
            </div>
          </div>
        </yd-cell-item>
      </a>
      <!-- <yd-cell-item arrow >
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #FFE8CC;">
            <i class="icon_qly_dingyue icon_sty_lb" style="color:#FF7900;"></i>
          </div>
          <div class="div_sty text_sty">
            如何订阅文章
          </div>
        </div>
      </yd-cell-item> -->
      <!-- <yd-cell-item arrow>
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #FEDFDD;">
            <i class="icon_qly_wenzhang icon_sty_lb" style="color:#FF5E56;"></i>
          </div>
          <div class="div_sty text_sty">
            如何生成文章
          </div>
        </div>
      </yd-cell-item> -->
      <a href="https://mp.weixin.qq.com/s/Bdwrtkt5Ie9-XbuDXWPBxg">
        <yd-cell-item arrow>
          <div slot="left" class="div_two_sty" >
            <div class="div_sty" style="background: #CAF8FD;">
              <i class="icon_qly_renyuan icon_sty_lb" style="color:#1CC7D6;"></i>
            </div>
            <div class="div_sty text_sty">
              如何开通会员
            </div>
          </div>
        </yd-cell-item>
      </a>
      <a href="https://mp.weixin.qq.com/s/Gum7yp_bm0OBb4fiD5HVhQ">
        <yd-cell-item arrow>
          <div slot="left" class="div_two_sty" >
            <div class="div_sty" style="background: #FFE8CC;">
              <i class="icon_qly_yan icon_sty_lb" style="color:#FF7900;"></i>
            </div>
            <div class="div_sty text_sty">
              如何查看阅读情况
            </div>
          </div>
        </yd-cell-item>
      </a>
      <a href="https://mp.weixin.qq.com/s/0eEH2-_r9DC3sWo1mttXqg">
        <yd-cell-item arrow>
          <div slot="left" class="div_two_sty" >
            <div class="div_sty" style="background: #CAF8FD;">
              <i class="icon_qly_shezhi icon_sty_lb" style="color:#02DFFE;"></i>
            </div>
            <div class="div_sty text_sty">
              如何消息设置
            </div>
          </div>
        </yd-cell-item>
      </a>
      <!-- <yd-cell-item arrow>
        <div slot="left" class="div_two_sty" >
          <div class="div_sty" style="background: #D5F4F7;">
            <i class="icon_qly_tel icon_sty_lb" style="color:#1DC7D6;"></i>
          </div>
          <div class="div_sty text_sty">
            如何联系客服
          </div>
        </div>
      </yd-cell-item> -->
    </yd-cell-group>
  </div>
</template>

<script>
export default {
  name: 'Operating',
  data () {
    return {

    }
  }

}
</script>

<style lang="less" scoped>
.text_sty{
  font-weight: bold;
  font-size: .3rem;
}
.div_sty{
  width: .7rem;
  height: .7rem;
  border-radius: 50%;
  text-align: center;
  line-height: .7rem;
  margin-top: 0.15rem;
  margin-left: 0.2rem;
  float: left;
  .icon_sty_lb{
    font-size: .38rem;
  }
}
.operating{
  height: 100%;
  background-color: #F5F5F5;
  .headhelp{
    height: 1.4rem;
    line-height: 1.4rem;
    margin-left: 0.44rem;
    display: flex;
    justify-content: start;
    .imgpic{
      img{
        width:.7rem;
        height:.7rem;
        margin-right: .1rem;
      }
      span{
        font-size: .35rem;
      }
    }
   }
   .helplist {
    background-color: #fff;
  .listline{
    height: 1rem;
    line-height: 1rem;
    border-bottom: 2px solid #f4f4f4;
    span{
     margin-left: 0.3rem;
     font-size: 18px;
    }
  }
}
 }
</style>
