<template>
  <div class="grid nested-grid divbg nesteddiv">
    <div class="col-1 pt-5 pb-5 logodiv">
      <img src="/nrm-logo-liggande-svensk-negativ_copy.png" width="280" />
      <!-- <img src="/nrm-logo.svg" width="300" /> -->
    </div>
    <div class="col-11 pt-6">
      <div class="col-5 text-right" style="max-width: 800px; float: right">
        <Search />
      </div>
      <!-- <div class="grid">
        <div class="col-12 pt-5" style="width: 100%; min-height: 20px"><br /></div>
      </div> -->
      <!-- <div class="grid">
        <div class="flex flex-column col-7"></div>

      </div> -->
      <!-- <div class="grid">
        <div class="col-12 text-left">
          <span class="title-text">{{ $t('title') }}</span>
        </div>
      </div> -->
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
.title-text {
  color: #fff !important;
  width: 200px;
  font-size: 20px;
}
.nesteddiv {
  min-height: 130px;
}
.logodiv {
  max-width: 100px;
  padding-left: 5rem;
  padding-top: 2rem;
}
.divbg {
  /* background-color: #144836; */
  background-color: #144836;
  max-height: 8rem;
}
</style>
