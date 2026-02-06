<template>
  <div class="layout">
    <!-- Sidebar / drawer -->
    <aside :class="['sidebar', { open: isOpen }]">
      <a
        href="#"
        class="main-link"
        :class="{ active: current === 'about' }"
        @click.prevent="goToTab('about')"
      >
        {{ $t('about.aboutSamlingar') }}
      </a>

      <div class="separator"></div>

      <a href="#" :class="{ active: current === 'contact' }" @click.prevent="goToTab('contact')">
        {{ $t('common.contactus') }}
      </a>
      <a
        href="#"
        :class="{ active: current === 'accessibility' }"
        @click.prevent="goToTab('accessibility')"
      >
        {{ $t('about.accessibility') }}
      </a>
    </aside>

    <!-- Main panel -->
    <main class="content">
      <header class="topbar">
        <button class="menu-btn" @click="isOpen = !isOpen">☰</button>
      </header>
      <div v-if="current === 'about'">
        <About />
      </div>

      <div v-else-if="current === 'contact'">
        <Contact />
      </div>

      <div v-else-if="current === 'accessibility'">
        <Accessibility />
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import About from '../components/About.vue'
import Accessibility from '../components/Accessibility.vue'
import Contact from '../components/Contact.vue'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const isOpen = ref(true)
const current = ref('about')

onMounted(() => {
  // If query parameter exists, set the current tab accordingly
  if (route.query.tab) {
    current.value = route.query.tab
  }
})

watch(
  () => route.query.tab,
  (newTab) => {
    if (newTab) current.value = newTab
  }
)

const goToTab = (tabName) => {
  current.value = tabName
  router.push({ query: { tab: tabName } }) // <-- update URL query
}
</script>

<style scoped>
/* Layout */
.layout {
  display: flex;
  height: 100vh;
  font-family: sans-serif;
}

/* Sidebar / drawer */
.sidebar {
  width: 200px;
  padding: 24px;
  background: transparent;
  transition: width 0.3s ease;
  overflow: hidden;
  flex-shrink: 0;
  border-right: 1px solid #ddd; /* <-- add border between sidebar and main panel */
}

.sidebar:not(.open) {
  width: 0;
  padding: 24px 0;
}

/* Sidebar links */
.sidebar a {
  display: block;
  margin-bottom: 12px;
  text-decoration: none;
  color: #144836;
}

.sidebar a:hover {
  text-decoration: underline;
  color: #144836;
}

.sidebar a.active:not(.main-link) {
  font-weight: bold;
  color: #144836;
}

.main-link {
  color: #000;
  font-size: 1.2em;
  font-weight: normal;
}

/* Separator line below main link */
.separator {
  height: 1px;
  background: #ddd;
  margin-bottom: 16px;
}

/* hr {
  margin: 16px 0;
  border: none;
  border-top: 0;
} */

/* Main panel */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* Top bar */
.topbar {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  background: #fff;
  box-shadow: none; /* <-- remove topbar border / shadow */
}

.menu-btn {
  font-size: 20px;
  background: none;
  border: none;
  cursor: pointer;
}

/* Main content */
.content {
  padding: 24px;
}
</style>
