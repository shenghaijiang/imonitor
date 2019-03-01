<template>
  <section>
    <div>
      <el-menu class="el-menu-vertical-demo" :collapse="isCollapse" v-for="(item,index) in menuList" :key="index"
               v-if="item.displayFlag" :router="true" background-color="rgb(238, 241, 246)">
        <el-submenu :index="item.path" v-if="item.children.length>=1">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title" v-if="!isCollapse">{{item.name}}</span>
          </template>
          <!-- 子菜单 -->
          <div v-for="(child,childIndex) in item.children" :key="childIndex" v-if="child.displayFlag">
            <el-submenu :index="child.path" v-if="child.children.length>=1" style="background-color:white">
              <template slot="title">
                <i :class="item.icon"></i>
                <span slot="title">{{item.name}}</span>
              </template>
            </el-submenu>
            <el-menu-item :index="child.path" v-else>
              <i :class="child.icon"></i>
              <span slot="title">{{child.name}}</span>
            </el-menu-item>
          </div>
        </el-submenu>

      </el-menu>
    </div>
  </section>
</template>

<script>
import menus from "../menus";
import {mapGetters} from "vuex";

export default {
  name: "SideBar",
  data () {
    return {
      menuList: [],
      sysName: "供应链管理系统"
    };
  },
  computed: {
    ...mapGetters(["isCollapse"])
  },
  methods: {
    collapse: function () {
      this.$store.dispatch("changeIsCollapse");
    }
  },
  created () {
    this.menuList = menus;
  }
};
</script>

<style lang="scss" scoped>
  .bar {
    // position: fixed;
    // z-index: 1000px;
    background-color: #1885DD;
    height: 2.8rem;
    line-height: 2.8rem;
    color: #ffffff;
    font-size: 18px;
    border-color: rgba(238, 241, 146, 0.3);
    border-right-width: 1px;
    border-right-style: solid;
    label {
      margin-left: 0.5rem;
    }
  }

  .sidebar {
    border-color: #eef1f6;
  }

  .el-menu {
    border-right: solid 1px #eef1f6;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
  }

  .el-menu--collapse {
    width: 60px;
  }
</style>
