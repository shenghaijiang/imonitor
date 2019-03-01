import { GetQueryString } from "../utils";

export default{
  data() {
    return {
      operation: {
        insertFlag: this.getOperation("insert"), //新增
        updateFlag: this.getOperation("update"), //修改
        deleteFlag: this.getOperation("delete"), //删除
        auditFlag: this.getOperation("audit"), //审核
        inspectionFlag: this.getOperation("inspection"), //检验
        previewFlag: this.getOperation("preview") //预览
      }
    };
  },
  methods: {

    /*多个按钮的时候控制按钮的操作列的宽度
    * @params params获取有几个按钮的宽度，比如：1个（'update',15）,就是如果存在update 一个权限，15px
    * */
    operationWidth(_operations = "", ...params) { //'update|delete|audit|inspection'
      const operations = _operations.split("|") || [];
      const exitStatus = operations.map((item) => this.getOperation(item));
      let width = 76;
      switch (exitStatus.filter((item) => item).length) {
        case 1:width = params[0] || 76; break;
        case 2:width = params[1] || 150; break;
        case 3:width = params[2] || 220; break;
        case 4:width = params[3] || 290; break;
        default:break;
      }
      return width;
    },

    /* 判断获取是否含有这个操作权限*/
    /* value:可以传入相应的权限，例如'delete'、'update',或者 'delete|update' 支持两种形式*/
    getOperation(value) {
      // debugger
      let isExist = false;
      const operation = GetQueryString("operation") || localStorage.getItem("operation") || sessionStorage.getItem("operation") || "",

        /* 是否包含操作按钮名称*/
        exitOperation = operation && operation.split("|").includes(value),

        /* 是否包含操作列*/
        newOperation = value.split("|").find((val) => operation.split("|").includes(val)),
        newExitOperation = operation && operation.split("|").includes(newOperation);

      /* 是否包含按钮名称*/
      if (exitOperation || newExitOperation) {
        isExist = true;
      } else {
        isExist = false;
      }
      return isExist;
    }
  }
};
