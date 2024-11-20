<template>
  <div class="grid" id="scientificNameDiv">
    <div class="col-8">
      <div class="flex flex-col gap-2 selectGroup">
        <label class="searchLabel" for="searchScientificName">
          {{ $t('search.scientificName') }}
        </label>
        <InputGroup>
          <InputText
            id="searchScientificName"
            v-model="scientificName"
            @input="onInputScientificName"
            @blur="onBlur"
            @mouseout="onMouseout"
            :placeholder="$t('search.searchScientificName')"
            class="w-11 md:w-56"
          />
          <Button icon="pi pi-times" v-if="showClearScentificName" @click="clearScientificName" />
        </InputGroup>
      </div>
    </div>
    <div class="col-4">
      <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
        <div class="flex items-center vertical-align-middle">
          <RadioButton
            v-model="taxonOptions"
            inputId="taxonOption1"
            name="option1"
            value="exact"
            class="mt-1"
          />
          <label for="taxonOption1" class="ml-2">{{ $t('search.exact') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="taxonOptions"
            inputId="taxonOption2"
            name="option2"
            value="contains"
            class="mt-1"
          />
          <label for="taxonOption2" class="ml-2">{{ $t('search.contains') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="taxonOptions"
            inputId="taxonOption3"
            name="option3"
            value="startsWith"
            class="mt-1"
          />
          <label for="taxonOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'
const store = useStore()

let scientificName = ref()
let taxonOptions = ref()
let showClearScentificName = ref(false)

watch(
  () => store.getters['scientificName'],
  (newValue, oldValue) => {
    console.log('new value:', oldValue, newValue)
    scientificName.value = newValue
  }
)

function onInputScientificName() {
  showClearScentificName = scientificName.value

  if (scientificName !== undefined && scientificName.value) {
    store.commit('setScientificName', scientificName.value)
  }

  // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  // if (scientificName.value) {
  //   const option = taxonOptions.value
  //   let searchTaxon
  //   if (option === 'exact') {
  //     searchTaxon = `%2Btx:"${scientificName.value}"`
  //   } else if (option === 'startsWith') {
  //     searchTaxon = `%2B(tx:${scientificName.value}* tx:${scientificName.value})`
  //   } else {
  //     searchTaxon = `%2B(tx:*${scientificName.value}* tx:"${scientificName.value}")`
  //   }
  //   if (searchTaxon) {
  //     store.commit('setScientificName', searchTaxon)
  //   }
  // } else {
  //   store.commit('setScientificName', null)
  // }
}

function clearScientificName() {
  scientificName.value = ''
  showClearScentificName = false
  taxonOptions.value = null
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
