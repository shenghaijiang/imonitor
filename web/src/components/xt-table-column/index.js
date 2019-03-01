import XtTableColumn from "../xt-table/table-column.vue";

/* istanbul ignore next */
XtTableColumn.install = function (Vue) {
  Vue.component(XtTableColumn.name, XtTableColumn);
};

export default XtTableColumn;
