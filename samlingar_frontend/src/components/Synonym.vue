<template>
  <div class="grid" id="synonymDiv">
    <div class="col-8">
      <div class="flex flex-col gap-2 selectGroup">
        <label class="searchLabel" for="synonym">
          {{ $t('search.synonym') }}
        </label>
        <InputGroup>
          <InputText
            id="synonym"
            v-model="synonym"
            @input="onInputSynonym"
            :placeholder="$t('search.searchSynonym')"
            class="w-11 md:w-56"
          />
          <Button icon="pi pi-times" v-if="showClearSynonym" @click="clearSynonym" />
        </InputGroup>
      </div>
    </div>
    <div class="col-4">
      <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
        <div class="flex items-center vertical-align-middle">
          <RadioButton
            v-model="synonymOptions"
            inputId="synOption1"
            name="option1"
            value="exact"
            class="mt-1"
          />
          <label for="synOption1" class="ml-2">{{ $t('search.exact') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="synonymOptions"
            inputId="synOption2"
            name="option2"
            value="contains"
            class="mt-1"
          />
          <label for="synOption2" class="ml-2">{{ $t('search.contains') }}</label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="synonymOptions"
            inputId="synOption3"
            name="option3"
            value="startsWith"
            class="mt-1"
          />
          <label for="synOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
const store = useStore()

let synonym = ref()
let showClearSynonym = ref(false)
let synonymOptions = ref()

function onInputSynonym() {
  // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  showClearSynonym = synonym.value
  if (synonym.value) {
    const option = synonymOptions.value
    let searchSynonym
    if (option === 'exact') {
      searchSynonym = `%2Bsynonym:"${synonym.value}"`
    } else if (option === 'startsWith') {
      searchSynonym = `%2B(synonym:${synonym.value}* synonym:${synonym.value})`
    } else {
      searchSynonym = `%2B(synonym:*${synonym.value}* synonym:"${synonym.value}")`
    }
    if (searchSynonym) {
      store.commit('setSynonym', searchSynonym)
    }
  } else {
    store.commit('setSynonym', null)
  }
}

function clearSynonym() {
  synonym.value = ''
  showClearSynonym = false
  synonymOptions.value = null
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
