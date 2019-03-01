import Vue from "vue";
import Vuex from "vuex";
import app from "./modules/app";
import tagsView from "./modules/tagsView";
import sideBar from "./modules/sideBar";
import getters from "./getters";

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    app,
    tagsView,
    sideBar
  },
  getters
});

export default store;
