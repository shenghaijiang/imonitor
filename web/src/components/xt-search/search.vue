<template>
  <div class="search-box">
    <slot></slot>
    <div class="search-box__button-wrap">
      <el-button size="small" @click="search" class="search-box__button">查询</el-button>
      <slot name="actions"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "XtSearch",
  methods: {
    search() {
      this.$emit("search");
    }
  }
};
</script>

<style lang="sass" scoped>
.search-box
  display: flex
  background-color: #eee
  margin-bottom: 8px
  .search-box__input
    width: 230px
    margin: 2px 2px 2px
  .search-box__button-wrap
    display: flex
    align-items: center
    margin-left: 8px
</style>

