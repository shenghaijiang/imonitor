import ArrayView from "./ArrayView.vue";
import JsonEditor from "./JsonEditor.vue";
import JsonView from "./JsonView.vue";
import PackageFile from "./package.json";

const VERSION = PackageFile.version,
  install = (Vue) => {
    if (install.installed) {return;}

    Vue.component("JsonEditor", JsonEditor);
    Vue.component("json-view", JsonView);
    Vue.component("array-view", ArrayView);

    Array.prototype.rmIndex = function (index) {
      this.splice(index, 1);
      return this;
    };
  };

export default {
  install,
  JsonEditor,
  VERSION
};
