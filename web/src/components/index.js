/**
 * Created by Xu on 2018/5/10.
 */
import XtInputNumber from "./xt-input-number/index.js";
import XtTable from "./xt-table";
import XtTableColumn from "./xt-table-column";
import VueTreeSelectValidate from "./vue-tree-select-validate/index";
import XtSearch from "./xt-search/search";

const components = [
    XtInputNumber,
    XtTable,
    XtTableColumn,
    XtSearch,
    VueTreeSelectValidate
  ],
  install = function (Vue) {
    /* istanbul ignore if */
    if (install.installed) {return;}
    components.map((component) => {
      Vue.component(component.name, component);
    });
  };

/* istanbul ignore if */
if (typeof window !== "undefined" && window.Vue) {
  install(window.Vue);
}

export default {
  version: "1.0.0",
  install,
  XtInputNumber,
  XtTable,
  XtTableColumn,
  XtSearch,
  VueTreeSelectValidate
};
