import VueTreeSelect from "./vue-tree-select-validate.vue";

VueTreeSelect.install = function(Vue) {
  Vue.component(VueTreeSelect.name, VueTreeSelect);
};

export default VueTreeSelect;
