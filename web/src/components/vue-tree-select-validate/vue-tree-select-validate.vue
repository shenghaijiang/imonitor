<style scoped>
  .vue-treeselect__control {
    border: 0px solid #fff;
  }
  .vue-select-box{
    border-color: white
  }
  .vue-treeselect__placeholder,.vue-treeselect__single-value {
    line-height: 28px;
  }
  .vue-line-height__0{
    line-height: 0px
  }
  .vue-select--small{
    height:28px;
  }
</style>

<template>
  <section>
    <div :class="[
            inputSize ? 'el-input--' + inputSize : '',
           inputSize ? 'vue-select--' + inputSize : '',
            {
            'is-disabled': disabled
            },
            'vue-line-height__0'
        ]">
      <tree-select
        v-model="currentValue"
        :multiple="multiple"
        :options="options"
        :placeholder="placeholder"
        :noChildrenText="noChildrenText"
        :noOptionsText="noOptionsText"
        :noResultsText="noResultsText"
        @input="handleInput"
        class="vue-select-box el-input__inner"
        :disabled="disabled"
        style="padding: 0px 0px 0px 0px;margin-top: 3px;"
      />
    </div>

  </section>
</template>

<script>
import TreeSelect from "@riophae/vue-treeselect";
import Emitter from "../../mixins/emitter";

export default {
  name: "vue-tree-select-validate",
  mixins: [Emitter],
  inject: {
    elForm: {
      default: ""
    },

    elFormItem: {
      default: ""
    }
  },
  components: {
    TreeSelect
  },
  props: {
    value: [String, Number],
    validateEvent: {
      type: Boolean,
      default: true
    },
    placeholder: String,
    options: Array,
    multiple: Boolean,
    noChildrenText: {
      type: String,
      default: () => "暂无数据"
    },
    noOptionsText: {
      type: String,
      default: () => "暂无数据"
    },
    noResultsText: {
      type: String,
      default: () => "暂无数据"
    },
    size: String,
    disabled: {
      type: Boolean,
      default: () => false
    },
    updateFlag: {
      type: Boolean,
      default: () => false
    }
  },
  data() {
    return {
      currentValue: this.value
    };
  },
  watch: {
    "value"(val, oldValue) {
      if ((!val && typeof val !== "undefined" && val !== 0) !== "null" && oldValue) {
        // undefined
      } else {
        this.validate(val);
      }
      this.setCurrentValue(val);
    }
  },
  computed: {
    inputSize() {
      return this.size || this._elFormItemSize;
    }
  },
  methods: {
    _elFormItemSize() {
      return (this.elFormItem || {}).elFormItemSize;
    },
    handleInput(value, id) {
      this.$emit("input", value, id);
      this.setCurrentValue(value);
    },
    setCurrentValue(value) {
      if (value === this.currentValue) {
        return;
      }
      this.currentValue = value;
      // if (this.validateEvent) {
      //   this.dispatch('ElFormItem', 'el.form.blur', [this.currentValue]);
      // }

    },
    validate(value) {
      if (typeof value !== "undefined" && !this.updateFlag) {
        this.elFormItem && this.elFormItem.validate("change");
      }
    }
  },
  mounted() {
    // mounted
  },
  beforeUpdate() {
    // beforeUpdate
  },
  created() {
    // created
  }
};
</script>


