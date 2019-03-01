import BasicRoutes from "./router.config";
import Home from "../components/xt-home/home.vue";
import Login from "../components/xt-login/login.vue";

const baseRoutes = [...BasicRoutes.basicRoutes.map((route) => {
    route.meta = {
        title: route.meta.title,
        requiresAuth: true,
      ...route.meta
    };
    return route;
})];
let routes = [];
    routes = [{
        sort: 1,
        icon: "fa fa-th-large",
        displayFlag: false,
        path: "/login",
        name: "Login",
        title: "登录",
        component: Login,
        meta: {title: "登录"},
        children: []
    }, {
        sort: 2,
        path: "/",
        icon: "fa fa-bookmark",
        component: Home,
        displayFlag: false,
        title: "基础信息",
        name: "Home",
        meta: {title: "基础信息", requiresAuth: true},
        children: [...baseRoutes]
    }];


export default routes;
