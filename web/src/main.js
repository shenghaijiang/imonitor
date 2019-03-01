import "font-awesome/css/font-awesome.min.css";
import "./assets/styles/scss/index.scss";
import App from "./app";
import Components from "./components";
import ElementUI from "element-ui";
import Router from "vue-router";
import routers from "./router";
import store from "./store";
import Vue from "vue";
import { GetQueryString } from "./utils";
import Operation from "./mixins/operation";

Vue.config.productionTip = false;

Vue.use(Router);
Vue.use(ElementUI);
Vue.use(Components);

Vue.mixin(Operation);/* 挂载全局混用判断是否有操作权限事件*/

const router = new Router({
    routes: routers
});

router.beforeEach((to, from, next) => {
    const xtitOauth = to.query.xtit_oauth || GetQueryString("xtit_oauth") || sessionStorage.getItem(window.TOKEN_KEY) || localStorage.getItem(window.TOKEN_KEY) || "";
    sessionStorage.setItem(window.TOKEN_KEY, xtitOauth);
    localStorage.setItem(window.TOKEN_KEY, xtitOauth);
    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (!xtitOauth) {
            next({
                path: "/login"
                // query: { redirect: to.fullPath }
            });
        }
    }
    next();
});

/* eslint-disable no-new */
window._Vue = new Vue({
    el: "#app",
    store,
    router,
    components: { App },
    template: "<App/>",
    data: {
        Bus: new Vue()
    }
});
