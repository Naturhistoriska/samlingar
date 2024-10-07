<template>
  <div class="grid nested-grid divbg nesteddiv">
    <div class="col-1 pt-5 pb-5 logodiv">
      <img src="/nrm-logo.png" />
    </div>
    <div class="col-11">
      <div class="grid">
        <div class="col-12 pt-3" style="width: 100%; min-height: 20px"><br /></div>
      </div>
      <div class="grid">
        <div class="flex flex-column col-7"></div>
        <div class="col-5 text-right" style="max-width: 600px; float: right">
          <Search />
        </div>
      </div>
      <div class="grid">
        <div class="col-12 text-left">
          <span class="title-text">{{ $t('title') }}</span>
        </div>
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
  // setup() {
  // const { t, locale } = useI18n()
  //
  // const supportedLocales = Tr.supportedLocales
  // const switchLanguage = async (event) => {
  // const newLocale = event.target.value
  // await Tr.switchLanguage(newLocale)
  // }
  //
  // return { t, locale, supportedLocales, switchLanguage }
  // },
  // watch: {
  // selectedLocale: function () {
  // Tr.switchLanguage(this.selectedLocale)
  // }
  // }
}
</script>
<style scoped>
.title-text {
  width: 200px;
  font-size: 20px;
}
.nesteddiv {
  min-height: 130px;
}
.logodiv {
  max-width: 100px;
}
.divbg {
  background-color: #144836;
  max-height: 8rem;
}
</style>
