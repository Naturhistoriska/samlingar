<template>
  <div class="grid">
    <div>
      {{ $t('search.freeTextSearch') }}
      <small style="font-size: 11px">[{{ $t('search.searchAll') }}]</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <input-with-icon @doAction="handleFreeTextSearch" />
        <!-- <InputGroup>
          <InputText
            id="simpleSearchInput1"
            v-model="value"
            @keydown.enter="onSearch"
            :placeholder="$t('search.searchAll')"
            aria-describedby="simpleSearchInput-help"
            class="w-full"
          />
          <Button
            icon="pi pi-search"
            style="max-width: 30px; min-height: 30px"
            :loading="loading"
            @click="onSearch"
          />
        </InputGroup> -->

        <!-- <div class="advanceLink"> -->
        <!-- <Button variant="link" @click="onAdvanceSearchLinkClick"> -->
        <!-- <small id="simpleSearchInput-help">{{ $t('search.advanceSearch') }} </small> -->
        <!-- </Button> -->
        <!-- </div> -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

import InputWithIcon from './baseComponents/InputWithIcon.vue'

const emits = defineEmits(['freeTextSearch'])

function handleFreeTextSearch() {
  console.log('handleFreeTextSearch')
  emits('freeTextSearch')
}

// function onAdvanceSearchLinkClick() {
//   store.commit('setIsAdvanceSearch', true)
//   router.push('/advanceSearch')
// }

// function onSearch() {
//   loading.value = true

//   let searchText = '*:*'
//   if (value.value) {
//     searchText = value.value
//     // searchText = '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
//   }

//   store.commit('setSearchText', searchText)
//   emits('simpleSearch')

//   setTimeout(() => {
//     loading.value = false
//   }, 2000)
// }
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
