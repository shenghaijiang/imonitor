<template>
  <section>
    <div class="bar">
      <label>
          <span style="cursor: pointer;margin-left:8px" @click.prevent="collapse">
              <i class="fa fa-align-justify" ></i>
          </span>
        {{systemName}}
      </label>
      <el-dropdown trigger="hover" style="float:right;margin-right:5px" class="userinfo">
          <span class="el-dropdown-link userinfo-inner">
              <img src="../../../assets/images/mouse.jpg" /> {{sysUserName}}
          </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleUserInfo">
            <i class="fa fa-drivers-license-o"></i> 我的资料
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <i class="fa fa-sign-out"></i> 退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "NavBar",
  data () {
    return {
      sysUserName: "",
      systemName: window.SYSTEM_NAME || "管理系统"
    };
  },
  computed: {
    ...mapGetters(["isCollapse"])
  },
  methods: {
    handleUserInfo () {
      location.href = "#/userinfo";
    },
    // 退出登录
    logout: function () {
      this.$confirm("确认退出吗?", "提示", {
      }).then(() => {
        localStorage.clear();
        sessionStorage.clear();
        // localStorage.setItem("Logout", new Date());
        // localStorage.setItem('isautologin', "0");
        // localStorage.setItem('userId','');
        // localStorage.setItem('userName','');
        this.$router.push("/login");
      });
    },
    collapse: function () {
      this.$store.dispatch("changeIsCollapse");
    }
  },
  mounted () {
    const user = localStorage.getItem("user");
    if (user) {
      this.sysUserName = user;
    }
  }
};
</script>

<style lang="scss" scoped>
  .bar{
    background-color: #1885DD;
    height: 2.8rem;
    line-height: 2.8rem;
    color:#ffffff;
    .userinfo{
      .userinfo-inner {
        cursor: pointer;
        color: #fff;
        img {
          width: 35px;
          height: 35px;
          border-radius: 20px;
          margin: 4px 0px 0px 10px;
          float: right;
        }
      }
    }

  }
</style>
