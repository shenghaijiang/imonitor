<template>
  <json-view :parsedData="parsedData" v-model="parsedData"></json-view>
</template>

<script>
export default {
  name: "JsonEditor",
  props: { "objData": { required: true } },
  data: function () {
    return {
      "parsedData": []
    };
  },
  created: function () {
    this.parsedData = this.jsonParse(this.objData);
  },
  watch: {
    "parsedData": {
      handler (newValue, oldValue) {
        this.$emit("input", this.makeJson(this.parsedData));
      },
      deep: true
    }
  },
  methods: {
    "jsonParse": function (jsonStr) {
      const parseJson = (json) => {
          if (!json) {
            return [];
          }
          const result = [],
            keys = Object.keys(json);
          keys.forEach((k, index) => {
            const val = json[k],
              opt = {
                "name": k,
                "type": val ? this.getType(val) : "string"
              };
            let parsedVal = val;
            if (this.getType(val) === "object") {
              // console.debug('-- o --')
              parsedVal = parseJson(val);
              // result.push(fr)
            } else if (this.getType(val) === "array") {
              // console.debug('-- a --')
              // console.debug(val)
              parsedVal = parseArray(val);
              // result.push(fr)
            }

            if (opt.type === "array" || opt.type === "object") {
              opt.childParams = parsedVal;
              opt.remark = null;
            } else {
              opt.childParams = null;

              opt.remark = parsedVal;
            }
            result.push(opt);
          });
          return result;
        },

        //
        parseArray = (arrayObj) => {
          const result = [];
          for (let i = 0; i < arrayObj.length; ++i) {
            const val = arrayObj[i],
              opt = {
                "name": null,
                "type": this.getType(val)
              };
            let parsedVal = val;
            if (this.getType(val) === "object") {
              parsedVal = parseJson(val);
            } else if (this.getType(val) === "array") {
              parsedVal = parseArray(val);
            }

            if (opt.type === "array" || opt.type === "object") {
              opt.childParams = parsedVal;
              opt.remark = null;
            } else {
              opt.childParams = null;
              opt.remark = parsedVal;
            }

            result.push(opt);
          }
          return result;
        },

        // --
        parseBody = (json) => {
          const r = parseJson(json);
          return r;
        };

      return parseBody(jsonStr);
    },

    "getType": function (obj) {
      switch (Object.prototype.toString.call(obj)) {
        case "[object Array]":
          return "array";
        case "[object Object]":
          return "object";
        default:
          return typeof (obj);
      }
    },

    "makeJson": function (dataArr) {
      const revertWithObj = function (data) {
          const r = {};
          for (let i = 0; i < data.length; ++i) {
            const el = data[i], key = el.name;
            let val;
            if (el.type === "array") {
              val = revertWithArray(el.childParams);
            } else if (el.type === "object") {
              val = revertWithObj(el.childParams);
            } else {
              val = el.remark;
            }

            r[key] = val;
          }
          return r;
        },

        revertWithArray = function (data) {
          const arr = [];
          for (let i = 0; i < data.length; ++i) {
            const el = data[i];
            let r;
            if (el.type === "array") {
              r = revertWithArray(el.childParams);
            } else if (el.type === "object") {
              r = revertWithObj(el.childParams);
            } else {
              r = el.remark;
            }

            arr.push(r);
          }
          return arr;
        },

        revertMain = function (data) {
          const r = revertWithObj(data);
          return r;
        };

      return revertMain(dataArr);
    }
  }
};

</script>

<style lang="less">
  @import url('./assets/styles/common.less');

</style>
