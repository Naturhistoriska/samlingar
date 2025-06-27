<template>
  <div class="grid" id="scientificNameDiv">
    <div class="col-12" no-gutters>
      <InputGroup>
        <InputText
          id="searchScientificName"
          v-model="scientificName"
          @input="onInputScientificName"
          :placeholder="$t('search.searchScientificName')"
          size="small"
          class="w-full"
        />
        <Button icon="pi pi-times" v-if="showClearScentificName" @click="clearScientificName" />
      </InputGroup>
    </div>

    <div class="col-12">
      <div class="flex flex flex-wrap gap-3" style="margin-top: -9px">
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOption1"
            name="option1"
            value="exact"
            size="small"
            class="mt-1"
            :disabled="checkboxDisabled"
            @value-change="change"
          />
          <label for="searchOption1" class="ml-2">
            <small>{{ $t('search.exact') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOptions2"
            name="option2"
            value="contains"
            class="mt-1"
            size="small"
            :disabled="checkboxDisabled"
            @value-change="change"
          />
          <label for="searchOptions2" class="ml-2">
            <small>{{ $t('search.contains') }}</small>
          </label>
        </div>
        <!-- <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOptions3"
            name="option3"
            value="startsWith"
            size="small"
            class="mt-1"
            @value-change="change"
          />
          <label for="searchOptions3" class="ml-2">
            <small>{{ $t('search.startsWith') }}</small>
          </label>
        </div> -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
const store = useStore()

let scientificName = ref()
let searchOptions = ref()
let showClearScentificName = ref(false)

// const emits = defineEmits(['search'])

watch(
  () => store.getters['scientificName'],
  (newValue, oldValue) => {
    scientificName.value = newValue
    setSearchOption()
  }
)

const checkboxDisabled = computed(() => {
  return !scientificName.value
})

onMounted(() => {
  scientificName.value = store.getters['scientificName']
  setSearchOption()
})

function setSearchOption() {
  if (scientificName.value) {
    searchOptions.value = store.getters['isFuzzySearch'] ? 'contains' : 'exact'
    showClearScentificName = true
  }
}

function change() {
  console.log('change', searchOptions.value)
  const option = searchOptions.value

  if (option === 'exact') {
    store.commit('setIsFuzzySearch', false)
  } else if (option === 'contains') {
    store.commit('setIsFuzzySearch', true)
  }

  // emits('search')
}

function onInputScientificName() {
  showClearScentificName = scientificName.value

  let option = false
  if (scientificName !== undefined && scientificName.value) {
    searchOptions.value = 'contains'
    option = true
  }
  search(scientificName.value, option)
}

function clearScientificName() {
  scientificName.value = ''
  showClearScentificName = false
  searchOptions.value = null

  search(null, false)
}

function search(scientificName, fuzzy) {
  store.commit('setScientificName', scientificName)
  store.commit('setIsFuzzySearch', fuzzy)
  emits('search')
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1rem;
}
/* .searchLabel {
  padding-right: 2rem;
  padding-top: 0.3rem;
  text-align: right;
  min-width: 150px;
} */
</style>
