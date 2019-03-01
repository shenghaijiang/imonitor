<template>
    <div @keyup.13="handleLogin" class="mainBox">
    <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleForm login-container">
      <div class="form-title-wrap">
        <!--<h4 class="form-subtitle">{{systemName}}</h4>-->
        <h2 >{{systemName}}</h2>
      </div>
      <div class="user">
        <i class="fa fa-user-circle-o user__avatar"></i>
      </div>
      <el-form-item prop="account">
        <el-input type="text" v-model="ruleForm2.account" icon="" auto-complete="off" placeholder="账号">
          <template slot="prepend">
            <i class="fa fa-user-o"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="checkPass">
        <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码">
          <template slot="prepend">
            <i class="fa fa-key"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-checkbox v-model="IsRemember" class="remember">记住密码</el-checkbox>
      <el-form-item style="width:100%;margin-top: -20px">
        <el-button type="primary" style="width:100%;" @click.native.prevent="handleLogin" :loading="logining">登录</el-button>
        <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { XTP } from "../../modules/api";
const UserAPI = XTP.user;

export default {
  data () {
    return {
      systemName: window.SYSTEM_NAME || "xtp通用管理系统",
      logining: false,
      ruleForm2: {
        account: "",
        checkPass: ""
      },
      rules2: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" }
        ],
        checkPass: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ]
      },
      IsRemember: false
    };
  },
  methods: {
    handleLogin (ev) {
      this.$refs.ruleForm2.validate((valid) => {
        if (valid) {
          this.logining = true;
          const loginParams = { account: this.ruleForm2.account, password: this.ruleForm2.checkPass };
          UserAPI.loginUser(loginParams).then(({data}) => {
            sessionStorage.clear();
            this.logining = false;
            const { code } = data;
            if (code !== 1) {
              this.$message({
                message: "登录失败,请确认用户名密码！",
                type: "error"
              });
            } else {
              this.saveItem(data.data);
              this.redirect();
              this.$store.dispatch("delMenuList", []);
              this.$store.dispatch("delMenuOperation", []);
              this.$store.dispatch("delAllViews", []);
              // this.$store.dispatch('getMenuList',{},true)
            }
          })
            .catch(() => {
              this.logining = false;
              this.$message({
                message: "与服务器连接错误,请检查服务器并刷新页面！",
                type: "error"
              });
            });
        } else {
          return false;
        }
      });
    },
    redirect () {
      let redirect = this.$route.query.redirect;
      redirect = redirect || "/";
      this.$router.push({ path: redirect });
    },
    saveItem (data) {
      if (this.isRemember) {
        localStorage.setItem(window.TOKEN_KEY, data);
        // localStorage.setItem(ACCOUNT, this.ruleForm2.account);
      } else {
        sessionStorage.setItem(window.TOKEN_KEY, data);
        // localStorage.setItem(ACCOUNT, this.ruleForm2.account);
      }
    }
  },
  created () {
    try {
      this.systemName = window.SYSTEM_NAME;
    } catch (e) {
      console.warn(e);
    }
  }
};

</script>

<style lang="scss" scoped>
@import './css/login.scss';
</style>
