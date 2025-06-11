<template>
  <div class="card">
    <DataView
      :value="records"
      :layout="layout"
      paginator
      :rows="30"
      :paginator="true"
      :lazy="true"
      @page="onPage($event)"
      :totalRecords="totalCount"
    >
      <template #grid="slotProps">
        <div class="grid grid-cols-12">
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-6 p-2"
          >
            <div
              class="flex flex-row justify-between items-start gap-3"
              style="max-width: 350px; min-width: 350px"
            >
              <card style="height: 150px; width: 350px" v-if="item != undefined">
                <template #title>
                  <div class="grid">
                    <div class="col-10" no-gutters>
                      <small>{{ item.scientificName }}</small>
                    </div>
                    <div class="col-2" no-gutters>
                      <Button variant="link" @click="view(item)">
                        <small>{{ $t('records.view') }}</small>
                      </Button>
                    </div>
                  </div>
                </template>
                <template #subtitle>
                  <small>{{ item.collectionName }}</small>
                </template>
                <template #content>
                  <div style="font-size: 0.7em">
                    {{ item.catalogNumber }}
                  </div>
                  <div style="font-size: 0.7em">
                    {{ item.locality }} {{ item.country }} {{ item.continent }}
                  </div>
                  <div v-if="item.decimalLatitude != null" style="font-size: 0.7em">
                    Lat: {{ item.decimalLatitude }} Lon: {{ item.decimalLongitude }}
                  </div>
                </template>
              </card>
            </div>
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

const store = useStore()
const router = useRouter()

const service = new Service()

let records = ref(Array.from({ length: 50 }))

const layout = ref('grid')

const searchText = computed(() => {
  let text = store.getters['searchText']
  return text ? text : '*'
})

const scientificName = computed(() => {
  return store.getters['scientificName']
})

const isFuzzySearch = computed(() => {
  return store.getters['isFuzzySearch']
})

const hasImages = computed(() => {
  return store.getters['filterImage']
})

const hasCoordinates = computed(() => {
  return store.getters['filterCoordinates']
})

const isInSweden = computed(() => {
  return store.getters['filterInSweden']
})

const isType = computed(() => {
  return store.getters['filterType']
})

onMounted(async () => {
  console.log('labels onMounted')

  await new Promise((res) => setTimeout(res, 500))
  fetchData(0, 30)
})

function fetchData(start, end) {
  service
    .apiSearch(
      searchText.value,
      scientificName.value,
      isFuzzySearch.value,
      hasImages.value,
      hasCoordinates.value,
      isType.value,
      isInSweden.value,
      start,
      end
    )
    .then((response) => {
      const total = response.response.numFound
      const results = response.response.docs

      store.commit('setResults', results)
      // store.commit('setTotalRecords', total)

      records.value = results
    })
    .catch()
    .finally(() => {})
}

const onPage = async (event) => {
  console.log('event')
  const { first, rows } = event
  await new Promise((res) => setTimeout(res, 500))

  fetchData(first, rows)
  // service
  //   .apiSearch(searchText.value, null, true, false, false, false, false, first, rows)
  //   .then((response) => {
  //     const total = response.response.numFound
  //     const results = response.response.docs

  //     store.commit('setResults', results)
  //     store.commit('setTotalRecords', total)

  //     records.value = results
  //   })
  //   .catch()
  //   .finally(() => {})
}

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

function view(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}
</script>
<style scoped></style>
