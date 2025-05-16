<template>
  <div class="card">
    <div class="grid navbg">
      <div class="col-8" style="margin-left: 1em">
        <Tabs v-model:value="value">
          <TabList>
            <Tab value="0" to="/" as="router-link" @click="onClick" class="navbg">
              <i class="pi pi-home navbg"></i> {{ $t('nav.home') }}
            </Tab>
            <Tab value="1" to="/search" as="router-link" class="navbg">
              {{ $t('nav.search') }}
            </Tab>
            <Tab value="2" to="/collections" as="router-link" class="navbg">
              {{ $t('nav.collections') }}
            </Tab>
            <Tab value="3" to="/about" as="router-link" class="navbg">
              {{ $t('nav.about') }}
            </Tab>
            <Tab value="4" to="/contact" as="router-link" class="navbg">
              {{ $t('nav.contactUs') }}
            </Tab>
          </TabList>
        </Tabs>
      </div>
      <div class="col text-right pr-4">
        <LocaleSwitcher />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import LocaleSwitcher from './LocaleSwitcher.vue'

const store = useStore()

let value = ref('0')
const router = useRouter()

watch(
  () => router.currentRoute.value.name,
  () => {
    const currentRouteName = router.currentRoute.value.name

    switch (currentRouteName) {
      case 'Home':
        value.value = '0'
        break
      case 'Search':
        value.value = '1'
        break
      case 'Collections':
        value.value = '2'
        break
      case 'About':
        value.value = '3'
        break
      case 'Contact':
        value.value = '4'
        break
      default:
        value.value = '0'
    }
  }
)

function onClick() {
  store.commit('setShowResults', false)
  store.commit('setIsAdvanceSearch', false)
  store.commit('setShowDetail', false)

  clearStore()
}

function clearStore() {
  store.commit('setFamily', [])
  store.commit('setGenus', [])

  store.commit('setCollections', [])
  store.commit('setTypeStatus', [])
  store.commit('setImageCount', 0)
  store.commit('setIsTypeCount', 0)
  store.commit('setHasCoordinatesCount', 0)
  store.commit('setInSwedenCount', 0)
  store.commit('setTotalRecords', 0)
  store.commit('setResults', [])

  store.commit('setScientificName', null)
  store.commit('setCatalogNumber', null)
  store.commit('setSynonym', null)
  store.commit('setSelectedDataset', null)
  store.commit('setDateRange', null)
  store.commit('setSelectedTypes', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterType', false)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', false)
}
</script>

<style scoped>
a {
  cursor: pointer;
  text-decoration: none;
  color: #fff;
}

a:hover {
  color: rgb(166, 158, 158) !important;
}

.navbg {
  background-color: #052b1e;
}

.p-tablist-tab-list {
  border-style: none !important;
  border-radius: 0;
}

.p-tabs {
  flex-direction: row !important;
  border-color: transparent !important;
}

.p-tab {
  border-style: none !important;
}

.p-tab:hover {
  background: #0b583d !important;
}

.p-tabmenu .p-tabmenu-tablist {
  border-style: none !important;
  border: 0px;
}

.p-tabmenu-tablist {
  border-style: none !important;
}

.p-tabmenu > .p-tabmenu-tablist {
  border-style: none !important;
}

.pi {
  background-color: transparent !important;
}

.pi:hover {
  background: #0b583d !important;
}
</style>
