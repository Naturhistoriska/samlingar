<template>
  <div class="grid" id="catalogNumberDiv">
    <div class="col-8">
      <div class="flex flex-col gap-2 selectGroup">
        <label class="searchLabel" for="catalogNumber">
          {{ $t('search.catalogNumber') }}
        </label>
        <InputGroup>
          <InputText
            id="catalogNumber"
            v-model="catalogNumber"
            @input="onInputCatalogNumber"
            :placeholder="$t('search.searchCatalogNumber')"
            class="w-11 md:w-56"
          />
          <Button icon="pi pi-times" v-if="showClearCatalogNumber" @click="clearCatalogNumber" />
        </InputGroup>
      </div>
    </div>
    <div class="col-4">
      <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
        <div class="flex items-center vertical-align-middle">
          <RadioButton
            v-model="catlogNumOptions"
            inputId="catOption1"
            name="option1"
            value="exact"
            class="mt-1"
          />
          <label for="catOption1" class="ml-2">{{ $t('search.exact') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="catlogNumOptions"
            inputId="catOption2"
            name="option2"
            value="contains"
            class="mt-1"
          />
          <label for="catOption2" class="ml-2">{{ $t('search.contains') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="catlogNumOptions"
            inputId="catOption3"
            name="option3"
            value="startsWith"
            class="mt-1"
          />
          <label for="catOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'
const store = useStore()

let catalogNumber = ref()
let showClearCatalogNumber = ref(false)
let catlogNumOptions = ref()

watch(
  () => store.getters['catalogNumber'],
  (newValue, oldValue) => {
    console.log('new value:', oldValue, newValue)
    catalogNumber.value = newValue
  }
)

function onInputCatalogNumber() {
  // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  showClearCatalogNumber = catalogNumber.value
  if (catalogNumber.value) {
    const option = catlogNumOptions.value
    let searchCatalogNumber
    if (option === 'exact') {
      searchCatalogNumber = `%2Bcn:"${catalogNumber.value}"`
    } else if (option === 'startsWith') {
      searchCatalogNumber = `%2B(cn:${catalogNumber.value}* cn:${catalogNumber.value})`
    } else {
      searchCatalogNumber = `%2B(cn:*${catalogNumber.value}* cn:${catalogNumber.value})`
    }
    if (searchCatalogNumber) {
      store.commit('setCatalogNumber', searchCatalogNumber)
    }
  } else {
    store.commit('setCatalogNumber', null)
  }
}

function clearCatalogNumber() {
  catalogNumber.value = ''
  showClearCatalogNumber = false
  catlogNumOptions.value = null
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1rem;
}
.searchLabel {
  padding-right: 2rem;
  padding-top: 0.3rem;
  text-align: right;
  min-width: 150px;
}
</style>
