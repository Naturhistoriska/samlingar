<template>
  <div class="grid">
    <div>
      {{ $t('search.freeTextSearch') }}
      <small style="font-size: 11px">[{{ $t('search.searchAll') }}]</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <input-with-icon
          @doAction="handleFreeTextSearch"
          v-bind:placehold="label"
          v-bind:size="size"
          v-bind:icon="icon"
          v-bind:loading="loading"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import InputWithIcon from './baseComponents/InputWithIcon.vue'

const emits = defineEmits(['freeTextSearch'])

const store = useStore()

const props = defineProps(['loading'])

const icon = ref('pi pi-search')
const size = ref('small')

const label = ref('search.searchAll')

function handleFreeTextSearch(value) {
  console.log('handleFreeTextSearch', value)

  const searchText = value ? value : '*'

  console.log('searchText', searchText)
  store.commit('setSearchText', searchText)
  emits('freeTextSearch', searchText)
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
