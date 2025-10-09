<template>
  <div class="card" id="recordsLabelsDiv">
    <DataView
      :value="records"
      layout="grid"
      paginator
      :rows="rows"
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
                      <small>{{ getTaxon(item) }}</small>
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
                    {{ item.locality }}
                    <br />
                    {{ item.continent }} {{ item.country }} {{ item.stateProvince }}
                    {{ item.county }}
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
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

const service = new Service()

const store = useStore()
const router = useRouter()

// const emits = defineEmits(['search'])

let records = ref(Array.from({ length: 50 }))

let windowWidth = ref(window.innerWidth)

const loading = ref(false)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

const rows = computed(() => {
  const width = windowWidth.value
  console.log('width 1', width)

  if (width < 1000) {
    return 20
  } else if (width < 1300) {
    return 30
  } else if (width < 1800) {
    return 40
  } else {
    return 50
  }
})

watch(
  () => store.getters['results'],
  (newValue, oldValue) => {
    records.value = store.getters['results']
  }
)

onMounted(async () => {
  console.log('RecordLabel')
  window.addEventListener('resize', onWidthChange)
  records.value = store.getters['results']
})

const onWidthChange = () => {
  windowWidth.value = window.innerWidth
  // emits('search', 0, rows.value, false)
}

const onPage = async (event) => {
  const { first, rows } = event
  const params = store.getters['searchParams']
  loadRecordsLazy(params, first, rows)
}

function getTaxon(data) {
  const { collectionCode, genus, scientificName, species, taxonRank } = data

  if (collectionCode === 'pz' || collectionCode === 'pb') {
    return taxonRank === 'species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'PI' || collectionCode === 'HE') {
    return taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'vp') {
    if (species) {
      return genus ? genus + ' ' + species : species
    }
  } else {
    return scientificName
  }
}

async function loadRecordsLazy(params, first, rows) {
  loading.value = true

  console.log('params', params.toString())
  await service
    .apiSearch(params, first, rows)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)

      store.commit('setTotalRecords', total)

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
      store.commit('setSearchParams', params)
    })
}

function view(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}
</script>
<style scoped>
::v-deep(.p-card-title) {
  font-size: 15px;
  font-weight: bold;
  font-style: italic;
  margin-top: 0 !important;
  margin-bottom: 0 !important;
}

::v-deep(.p-card-subtitle) {
  margin-top: 0 !important;
}
</style>
