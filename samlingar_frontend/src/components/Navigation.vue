<template>
  <div class="card navbg header-tabs">
    <!-- ===== Top Bar ===== -->
    <div class="tabs-header">
      <!-- Mobile Hamburger Menu -->
      <button class="menu-btn" @click="menuOpen = !menuOpen">
        <i class="pi" :class="menuOpen ? 'pi-times' : 'pi-bars'"></i>
      </button>
      <div class="tabs-section">
        <Tabs v-model:value="value">
          <TabList>
            <Tab value="0" to="/" as="router-link" @keydown.enter="onTabEnter(0)" class="navbg">
              <i class="pi pi-home navbg"></i> {{ $t('nav.home') }}
            </Tab>
            <Tab
              value="1"
              to="/search"
              as="router-link"
              @keydown.enter="onTabEnter(1)"
              class="navbg"
            >
              {{ $t('nav.search') }}
            </Tab>
            <Tab
              value="2"
              to="/collections"
              as="router-link"
              @keydown.enter="onTabEnter(2)"
              class="navbg"
            >
              {{ $t('nav.collections') }}
            </Tab>
            <!-- <Tab
              value="3"
              to="/about"
              as="router-link"
              @keydown.enter="onTabEnter(3)"
              class="navbg"
            >
              {{ $t('nav.about') }}
            </Tab> -->
            <Tab
              value="3"
              to="/contact"
              as="router-link"
              @keydown.enter="onTabEnter(4)"
              class="navbg"
            >
              {{ $t('common.contactus') }}
            </Tab>
          </TabList>
        </Tabs>
      </div>

      <!-- Locale Switcher -->
      <div class="locale-section">
        <LocaleSwitcher />
      </div>
    </div>

    <!-- ===== Collapsible Mobile Nav ===== -->
    <transition name="slide-fade">
      <div v-if="menuOpen" class="mobile-nav">
        <RouterLink to="/" class="mobile-nav-item" @click="menuOpen = false">{{
          $t('nav.home')
        }}</RouterLink>
        <RouterLink to="/search" class="mobile-nav-item" @click="menuOpen = false">{{
          $t('nav.search')
        }}</RouterLink>
        <RouterLink to="/collections" class="mobile-nav-item" @click="menuOpen = false">{{
          $t('nav.collections')
        }}</RouterLink>
        <RouterLink to="/contact" class="mobile-nav-item" @click="menuOpen = false">{{
          $t('common.contactus')
        }}</RouterLink>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'
import LocaleSwitcher from '@/components/LocaleSwitcher.vue'

const store = useStore()
const router = useRouter()
const route = useRoute()

const value = ref('0')
const menuOpen = ref(false)

// Map routes to tab values
const routeTabMap = {
  '/': '0',
  '/search': '1',
  '/record/:id': '1',
  '/collections': '2',
  '/contact': '3'
}

value.value = routeTabMap[route.path] || '0'

watch(
  () => route.path,
  (newPath) => {
    value.value = routeTabMap[newPath] || '0'
    if (value.value === '0') {
      onClick()
    }
  }
)

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
      case 'Record':
        value.value = '1'
        break
      case 'Collections':
        value.value = '2'
        break
      // case 'About':
      // value.value = '3'
      // break
      case 'Contact':
        value.value = '3'
        break
      default:
        value.value = '0'
    }
  }
)

// const onClick = () => (menuOpen.value = false)

function onClick() {
  store.commit('setSearchText', null)
  store.commit('setFullTextSearchMode', 'contains')
  store.commit('setScientificName', null)
  store.commit('setSearchMode', 'contains')

  store.commit('setCollectionGroup', null)
  store.commit('setSelectedCollection', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)

  store.commit('setStartYear', null)
  store.commit('setEndYear', null)
  store.commit('setDateFilter', 'date')

  store.commit('setSearchParams', null)

  store.commit('setFields', [])
  store.commit('setDataResource', null)

  store.commit('setPageNum', 0)
  store.commit('setRowsPerPage', 20)
}

const onTabEnter = (index) => {
  console.log('Tab pressed enter:', index)
  menuOpen.value = false
}
</script>

<style scoped>
/* === Layout === */
.header-tabs {
  background-color: #052b1e;
  border-bottom: 1px solid #052b1e;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.tabs-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  position: relative;
}

.tabs-section {
  flex: 1;
}

.nav-tabs {
  background: transparent;
  border: none;
}

/* === Tab Items === */
.nav-item {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  color: #333;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border: none;
  background: none;
}

.nav-item:hover {
  color: #007ad9;
}

/* === Locale Switcher === */
.locale-section {
  margin-left: 1rem;
}

/* === Hamburger Button === */
.menu-btn {
  display: none;
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #eee;
  cursor: pointer;
}

/* === Mobile Nav (Collapsible) === */
.mobile-nav {
  display: flex;
  flex-direction: column;
  background: #052b1e;
  border-top: 1px solid #eee;
}

.mobile-nav-item {
  display: block;
  text-align: center;
  padding: 1rem;
  text-decoration: none;
  color: #333;
  font-weight: 500;
}

.mobile-nav-item:hover {
  background: #052b1e;
  color: #007ad9;
}

/* === Animation === */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.p-tablist-tab-list {
  border-style: none !important;
  border-radius: 0;
  background: #0b583d !important;
}

.p-tabs {
  flex-direction: row !important;
  border-color: transparent !important;
}

.p-tab {
  border-style: none !important;
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
  background: #0b583d !important;
}

.pi {
  background-color: transparent !important;
}

.pi:hover {
  background: #0b583d !important;
}

.p-tablist-tab-list {
  background: #0b583d !important;
}
/* === Responsive === */
@media (max-width: 768px) {
  .tabs-section {
    display: none; /* hide desktop tabs */
  }

  .menu-btn {
    display: block; /* show hamburger */
  }

  .locale-section {
    margin-left: 0;
  }
}

a {
  cursor: pointer;
  text-decoration: none;
  color: #fff !important;
}

a:hover {
  color: rgb(166, 158, 158) !important;
  background: #0b583d !important;
}

:deep(.p-tablist) {
  display: flex;
}

:deep(.p-tablist .p-tab) {
  flex: 1 1 0;
  text-align: center;
  max-width: 7em;
  min-width: 7em;
}

/* .p-tabview-nav {
  display: flex !important;
}

.p-tabview-nav li {
  flex: 1 1 0;
  max-width: 200px;
  min-width: 200px;
} */

.navbg {
  background-color: #052b1e;
  /* background-color: #1d634a; */
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
