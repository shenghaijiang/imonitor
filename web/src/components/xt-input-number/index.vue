<script>
import emitter from './emitter';

export default {
  mixins: [emitter],
  props: {
    value: Number,
    max: { type: Number, default: 99999 },
    min: { type: Number, default: 0 },
    int: { type: Boolean, default: false },
    precision: { type: Number, default: 3 },
    long: { type: Boolean, default: false },
  },
  methods: {
    handleInput(event) {
      let strValue = event.target.value;
      let numValue = this.convertValue(strValue);
      this.showValue(strValue, numValue);
      this.$emit('input', numValue);
      this.dispatch('ElFormItem', 'el.form.blur', [numValue]);
    },
    handleBlur(event) {
      let strValue = event.target.value, precision = this.int ? 0 : this.precision;
      strValue = Number(strValue).toFixed(precision);
      this.$refs.input.value = strValue;
      let numValue = Number(strValue);
      this.$emit('blur', numValue);
      this.dispatch('ElFormItem', 'el.form.change', [numValue]);
    },
    convertValue(strValue) {
      let value = Number(strValue);
      if (this.int) {
        if (!Number.isInteger(value)) {
          value = Math.floor(value);
        }
      } else {
        let precision = strValue.length - strValue.indexOf('.') - 1;
        if (precision > this.precision) {
          value = Number(value.toFixed(this.precision));
        }
      }
      while (value > this.max) {
        value = Math.floor(value / 10);
      }
      return value;
    },
    showValue(strValue, numValue) {
      if (strValue.indexOf('.') === -1) {
        this.$refs.input.value = numValue;
        return;
      }
      let precision = this.int ? 0 : this.precision;
      let isInRange = Number(strValue).toFixed(precision).indexOf(strValue) === 0;
      this.$refs.input.value = isInRange ? strValue : numValue;
    }
  },
  render (h) {
    let { value, max, min, handleInput, handleBlur, handleChange, long } = this;
    return (
      <input value={ value } ref="input" max={ max } min={ min } onInput={ handleInput } onBlur={ handleBlur }
        type="number" class={"m-input-number" + (long ? " m-xt-xt-input-number--long" : "")}/>
    );
  }
};
</script>

<style scoped>
.m-input-number {
  height: 2em;
  width: 8em;
  box-sizing: border-box;
  padding-left: 6px;
  outline: none;
  transition: border-color .2s cubic-bezier(.645,.045,.355,1);
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #bfcbd9;
  color: #1f2d3d;
}
.m-input-number.m-input-number--long {
  height: 2.58em;
}
.m-input-number[disabled] {
  background: #eee;
}
.m-input-number::placeholder {
   color: #97a8be;
}
</style>
