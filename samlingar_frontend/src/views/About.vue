<template>
  <div class="layout">
    <!-- Sidebar / drawer -->
    <!-- <aside :class="['sidebar', { open: isOpen }]">
      <a
        href="#"
        class="main-link"
        :class="{ active: current === 'about' }"
        @click.prevent="(goToTab('about'), closeMenu())"
      >
        {{ $t('about.aboutSamlingar') }}
      </a>

      <div class="separator"></div>

      <a
        href="#"
        :class="{ active: current === 'contact' }"
        @click.prevent="(goToTab('contact'), closeMenu())"
      >
        {{ $t('common.contactus') }}
      </a>
      <a
        href="#"
        :class="{ active: current === 'accessibility' }"
        @click.prevent="(goToTab('accessibility'), closeMenu())"
      >
        {{ $t('about.accessibility') }}
      </a>
    </aside> -->

    <aside :class="['sidebar', { open: isOpen }]">
      <a
        href="#"
        class="main-link"
        :class="{ active: current === 'about' }"
        @click.prevent="(goToTab('about'), closeMenu())"
      >
        {{ $t('about.aboutSamlingar') }}
      </a>

      <div class="separator"></div>

      <a href="#" @click.prevent="(goToTab('contact'), closeMenu())">
        {{ $t('common.contactus') }}
      </a>

      <a href="#" @click.prevent="(goToTab('accessibility'), closeMenu())">
        {{ $t('about.accessibility') }}
      </a>
    </aside>

    <div v-if="isOpen" class="backdrop" @click="closeMenu()"></div>

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
const isMobile = ref(false)

onMounted(() => {
  // If query parameter exists, set the current tab accordingly
  if (route.query.tab) {
    current.value = route.query.tab
  }

  isMobile.value = window.innerWidth <= 768

  if (isMobile.value) {
    isOpen.value = false
  }

  // Optional: update isMobile on resize
  window.addEventListener('resize', () => {
    isMobile.value = window.innerWidth <= 768
  })
})

watch(
  () => route.query.tab,
  (newTab) => {
    if (newTab) current.value = newTab
  }
)

watch(isOpen, (open) => {
  document.body.style.overflow = open ? 'hidden' : ''
})

watch(
  () => route.fullPath,
  () => {
    if (window.innerWidth <= 768) {
      isOpen.value = false
    }
  }
)

const closeMenu = () => {
  if (window.innerWidth <= 768) {
    isOpen.value = false
  }
  // isOpen.value = false
}

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

@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    top: 0;
    left: 0;
    height: 100%;
    width: min(80vw, 260px);
    background: #fff;
    z-index: 1001;

    transform: translateX(-100%);
    transition: transform 0.25s ease;
    padding: 20px;
    padding-top: 56px;
  }

  .sidebar.open {
    transform: translateX(0);
  }

  /* Backdrop */
  .backdrop {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.35);
    z-index: 1000;
  }

  .content {
    width: 100%;
    padding: 16px;
  }

  .topbar {
    /* position: sticky; */
    top: 0;
    background: #fff;
    z-index: 1102;
  }
}
</style>
