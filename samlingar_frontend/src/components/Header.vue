<template>
  <div class="grid nested-grid divbg nesteddiv">
    <div class="col-1 pt-5 pb-5 logodiv">
      <img src="/nrm-logo-liggande-svensk-negativ_copy.png" width="280" />
    </div>
    <div class="col-11 pt-6">
      <div class="col-5 text-right" style="max-width: 800px; float: right">
        <Search />
      </div>
    </div>
  </div>
</template>
<script>
import Search from './QuickSearch.vue'
export default {
  name: 'Header',
  components: {
    Search
  }
}
</script>
<style scoped>
.nesteddiv {
  min-height: 9em;
}
.logodiv {
  padding-left: 3rem;
  padding-top: 2rem;
}
.divbg {
  background-color: #144836;
  max-height: 8rem;
}
</style>
