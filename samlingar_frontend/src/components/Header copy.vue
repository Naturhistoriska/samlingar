<template>
  <div class="grid nested-grid divbg nesteddiv">
    <div class="col-5 pt-5 pb-5 logodiv">
      <Image src="/nrm-logo-liggande-svensk-negativ_copy.png" width="50%" />
    </div>
    <div class="col-7 pt-6">
      <div class="col-5 text-right" style="float: right">
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
  padding-left: 3em;
  padding-top: 2em;
}
.divbg {
  background-color: #144836;
}
@media (max-width: 768px) {
  .p-image-preview {
    max-width: 100vw;
    max-height: 100vh;
  }
}
</style>
