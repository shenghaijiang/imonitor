<!--suppress ALL -->
<template>
	<section>
      <div class="main">
          <div class="header">
            <nav-bar></nav-bar>
          </div>
          <div class="body">
              <div class="body_header" :style="sidebar?'width:64px':'width:200px'">
                  <side-bar class="sidebar" :style="sidebar?'position: absolute':''">{{sidebar}}</side-bar>
              </div>
              <div class="body_body">
                <div class="body_tags">
                    <tags-view></tags-view>
                </div>
                <div class="body_bodys">
                    <app-main ></app-main>
                </div>
                <div class="body_foot">
                    杭州贤二智能科技有限公司Copyright © 2018Version 1.0
                </div>

              </div>
          </div>
      </div>

	</section>
</template>

<script>
import NavBar from "./src/nav-bar.vue";
import SideBar from "./src/side-bar.vue";
import TagsView from "./src/tags-view.vue";
import AppMain from "./src/app-main.vue";

export default {
  name: "Home",
  components: {
    NavBar,
    SideBar,
    TagsView,
    AppMain
  },
  computed: {
    sidebar () {
      return this.$store.state.sideBar.isCollapse;
    }
  }
};
</script>

<style lang="scss" scoped>
.main{
    .header{
        position: absolute;
        height:2.8rem;
        width:100%;
    }
    .body{
        position: absolute;
        display: flex;
        margin:0; /*公共*/
        width:100%;
        position:fixed;/*FF IE7 SF*/
        top:2.8rem;
        bottom:0;
        overflow: auto !important;
        _position: relative;/**IE6***/
        _top:0px;
        _OVERFLOW-Y: hidden;
        _OVERFLOW-X: hidden;
        SCROLLBAR-FACE-COLOR: #EEEEEE;/*设置IE下滚动条颜色*/
        SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
        SCROLLBAR-SHADOW-COLOR: #919192;
        SCROLLBAR-3DLIGHT-COLOR:#ffffff;
        SCROLLBAR-ARROW-COLOR: #919192;
        SCROLLBAR-TRACK-COLOR: #ffffff;
        SCROLLBAR-DARKSHADOW-COLOR: #ffffff;
        _height:100%;
        &:after {
            clear: both;
            content: "";
        }
        .body_header{
          z-index: 999;
            float: left;
            background-color: #eef1f6;
            height: 100%;
            width: 200px;
            overflow-y: auto;
            overflow-x: hidden;
        }
        .body_body{
            width: 100%;
            height: 100%;
            float: left;
            overflow: hidden;
            &:after {
                clear: both;
                content: "";
            }
            .body_tags{
                width:100%;
                height:2.5rem;
            }
            .body_bodys{
                height: 90%;
                padding:1rem 1rem 0 1rem;
                overflow-y:auto;
            }
            .body_foot{
              text-align: center;
              margin-top: 4px;
              background-color: #eef1f6;
              height: 2rem;
              line-height: 2rem;
            }

        }
    }
}

</style>
