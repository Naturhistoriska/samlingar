<template>
  <div id="recordList">
    <DataTable
      :value="virtualRecords"
      scrollable
      scrollHeight="400px"
      tableStyle="min-width: 50rem"
      :virtualScrollerOptions="{
        lazy: true,
        onLazyLoad: loadCarsLazy,
        itemSize: 44,
        delay: 200,
        showLoader: true,
        loading: lazyLoading,
        numToleratedItems: 10
      }"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold">
            {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalRecords) }}]
          </span>
          <Button type="button" label="Columns" @click="addColumns" />
        </div>
      </template>

      <Column
        v-for="col of columns"
        :key="col.field"
        :field="col.field"
        :header="col.header"
        stripedRows
      >
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column>
    </DataTable>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()

const emits = defineEmits(['freeTextSearch'])

const records = ref()
const virtualRecords = ref(Array.from({ length: 100 }))
const totalRecords = ref()
const lazyLoading = ref(false)
const loadLazyTimeout = ref()

let columns = ref([
  { field: 'collectionName', header: 'Collection Name' },
  { field: 'scientificName', header: 'Scientific Name' },
  { field: 'locality', header: 'Locality' },
  { field: 'catalogNumber', header: 'CatalogNumber' },
  { field: null, header: 'Columns' }
])

onMounted(() => {
  console.log('onMounted')
  // const searchText = store.getters['searchText']
  // let value = searchText == null ? '*' : searchText

  loadData(0, 10)

  // service.freeTextSearch(value, 0, 10).then((response) => {
  //   const total = response.response.numFound
  //   const results = response.response.docs

  //   store.commit('setResults', results)
  //   store.commit('setTotalRecords', total)

  //   console.log('total:', total)
  //   console.log('results:', results)

  //   records.value = results
  //   totalRecords.value = total
  // })
})

const loadCarsLazy = async (event) => {
  // !lazyLoading.value && (lazyLoading.value = true)

  lazyLoading.value = true

  if (loadLazyTimeout.value) {
    clearTimeout(loadLazyTimeout.value)
  }
  //
  //simulate remote connection with a timeout
  loadLazyTimeout.value = setTimeout(
    () => {
      let _virtualRecords = [...virtualRecords.value]
      let { first, last } = event

      console.log('first, last', first, last)
      loadData(last, 10)

      //load data of required page
      const loadedRecords = records.value

      //populate page of virtual cars
      Array.prototype.splice.apply(_virtualRecords, [...[first, last - first], ...loadedRecords])

      virtualRecords.value = _virtualRecords
      lazyLoading.value = false
    },
    Math.random() * 1000 + 250
  )
}

function loadData(start, numPerPage) {
  console.log('loadData', start)

  const searchText = store.getters['searchText']
  let value = searchText == null ? '*' : searchText

  service.freeTextSearch(value, start, numPerPage).then((response) => {
    const total = response.response.numFound
    const results = response.response.docs

    store.commit('setResults', results)
    store.commit('setTotalRecords', total)

    console.log('total:', total)
    console.log('results:', results)

    records.value = results
    totalRecords.value = total
  })
}

function addColumns() {
  console.log('add columns')
}

//simulate remote connection with a timeout
// loadLazyTimeout.value = setTimeout(() => {
//     let _virtualCars = [...virtualRecords.value];
//     let { first, last } = event;

//     //load data of required page
//     const loadedCars = cars.value.slice(first, last);

//     //populate page of virtual cars
//     Array.prototype.splice.apply(_virtualCars, [...[first, last - first], ...loadedCars]);

//     virtualCars.value = _virtualCars;
//     lazyLoading.value = false;
// }, Math.random() * 1000 + 250);

// const loadCarsLazy = (event) => {
//   !lazyLoading.value && (lazyLoading.value = true)

//   if (loadLazyTimeout.value) {
//     clearTimeout(loadLazyTimeout.value)
//   }

//   loadLazyTimeout.value = setTimeout(() => {
//     let { first, last } = event

//     console.log('what...', first, last)
//     const searchText = store.getters['searchText']
//     let value = searchText == null ? '*' : searchText
//     emits('freeTextSearch', value, last, 10)

//     // records.value = store.getters['results']

//     // console.log('what. again..', first, last)
//     // //load data of required page

//     // //populate page of virtual cars
//     let _virtualCars = [...virtualRecords.value]

//     records.value = _virtualCars
//     lazyLoading.value = false
//   })

// !lazyLoading.value && (lazyLoading.value = true)
// console.log('lazyLoading', lazyLoading.value)

// if (loadLazyTimeout.value) {
//   clearTimeout(loadLazyTimeout.value)
// }

//simulate remote connection with a timeout
// loadLazyTimeout.value = setTimeout(
//   () => {
//     let { first, last } = event
//     console.log('what ...', first, last)
//     const searchText = store.getters['searchText']
//     // emits('freeTextSearch', searchText, last, 10)
//     // let _results = records

//     //load data of required page
//     // const loadResults = records.value.slice(first, last)

//     //populate page of virtual cars
//     // Array.prototype.splice.apply(_results, [...[first, last - first], ...loadResults])

//     // records.value = _results
//     lazyLoading.value = false
//   }
//   // Math.random() * 1000 + 250
// )
// }

// const columns = [
//   { field: 'collectionName', header: 'Collection Name' },
//   { field: 'scientificName', header: 'Scientific Name' },
//   { field: 'locality', header: 'Locality' },
//   { field: 'catalogNumber', header: 'CatalogNumber' }
// ]
</script>
