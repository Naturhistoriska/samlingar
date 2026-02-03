<template>
  <div class="grid">
    <div>
      {{ $t('search.freeTextSearch') }}
      <small style="font-size: 11px">[{{ $t('search.searchAll') }}]</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <input-with-icon @doAction="handleFreeTextSearch" />
      </div>
    </div>
  </div>
</template>
<script setup>
// import { ref } from 'vue'
// import { useRouter } from 'vue-router'
// import { useStore } from 'vuex'
import Button from 'primevue/button'

import InputWithIcon from './baseComponents/InputWithIcon.vue'

const emits = defineEmits(['freeTextSearch'])

function handleFreeTextSearch() {
  console.log('handleFreeTextSearch')
  emits('freeTextSearch')
}
</script>
<style scoped>
.searchInput {
  min-width: 90%;
  text-align: left;
  float: left;
}
.advanceLink {
  float: right;
}
</style>
