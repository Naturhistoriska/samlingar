<template>
  <div class="card flex justify-center selectGroup">
    <label class="searchLabel">
      {{ $t('search.datasetName') }}
    </label>
    <Select
      id="groupDataset"
      v-model="selectedGroupDataset"
      :options="groupDataset"
      optionLabel="label"
      optionGroupLabel="label"
      optionGroupChildren="items"
      :placeholder="$t('search.selectDatasetName')"
      checkmark
      :highlightOnSelect="true"
      class="w-full md:w-56"
      showClear
      @change="onChange"
      style="margin-left: 7px"
    />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
const store = useStore()
let selectedGroupDataset = ref()

const groupDataset = ref([
  {
    label: 'Zoological Collection',
    items: [
      { label: 'Amphibians and reptiles', value: 'herps' },
      { label: 'Bird', value: 'va' },
      { label: 'Fish', value: 'fish' },
      { label: 'Invertebrate main collection', value: 'ev' },
      { label: 'Invertebrate type collection', value: 'et' },
      { label: 'Mammals', value: 'ma' },
      { label: 'NRM Entomology Collection Objects', value: '163840' },
      { label: 'Swedish Malaise Trap Project (SMTP) Collection Obj', value: '262144' },
      { label: 'Swedish Malaise Trap Project (SMTP) Species Lists', value: '655361' }
    ]
  },
  {
    label: 'Botanical and mycological collections',
    items: [
      { label: 'Algae', value: 'algae' },
      { label: 'Fungi/Lichens', value: 'fungi' },
      { label: 'Mosses', value: 'mosses' },
      { label: 'Vascular Plants', value: 'vp' }
    ]
  },
  {
    label: 'Palaeontological collection',
    items: [
      { label: 'Paleozoology', value: 'pz' },
      { label: 'Paleobotany', value: 'pb' }
    ]
  },
  {
    label: 'Geological collection',
    items: [
      { label: 'NRM Isotope Geology', value: '753664' },
      { label: 'NRM Mineralogy', value: '557057' },
      { label: 'NRM Nodules', value: '786432' }
    ]
  }
])

function onChange() {
  console.log('handleChange')
  if (selectedGroupDataset.value) {
    console.log('selectedGroupDataset', selectedGroupDataset.value.value)
    const collectionCode = `%2BcollectionId:${selectedGroupDataset.value.value}`
    store.commit('setSelectedDataset', collectionCode)
  } else {
    store.commit('setSelectedDataset', null)
  }
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
