<template>
  <div class="grid">
    <div class="col-6" no-gutters>
      <div class="grid startPageStyle">
        <Search @freeTextSearch="search" />
      </div>
      <div class="grid">
        <start-page-text />
      </div>

      <div class="grid">
        <Filter style="padding-top: 2rem" @filterSearch="search" />
      </div>
    </div>

    <div class="col-6" no-gutters>
      <Collections @searchCollections="search" />
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'

import StartPageText from './StartPageText.vue'
import Search from './SearchAllFields.vue'
import Collections from './CollectionsFilter.vue'
import Filter from './Filter.vue'

const router = useRouter()

function search() {
  router.push('/search')
}
</script>
