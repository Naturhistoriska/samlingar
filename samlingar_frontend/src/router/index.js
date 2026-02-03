import { createWebHistory, createRouter } from 'vue-router'
import { ref } from 'vue'
// import About from '../views/About.vue'
import Collections from '../views/Collections.vue'
import Contact from '../views/Contact.vue'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import SearchView from '../views/SearchView.vue'
import RecordView from '../views/Record.vue'

export const entryType = ref(null)
export const previousRoute = ref(null)
export const currentRoute = ref(null)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'Home | Samlingar'
    }
  },
  {
    path: '/search',
    name: 'Search',
    component: SearchView
  },
  {
    path: '/record/:id',
    name: 'Record',
    component: RecordView
  },

  // {
  // path: '/about',
  // name: 'About',
  // component: About
  // },
  {
    path: '/collections',
    name: 'Collections',
    component: Collections
  },
  {
    path: '/contact',
    name: 'Contact',
    component: Contact
  },
  {
    path: '/:catchAll(.*)',
    component: NotFound
  }
]

const router = createRouter({
  // history: createWebHistory(import.meta.env.VITE_BASE_URL),
  history: createWebHistory(),
  routes
})

// Track previous route
// router.beforeEach((to, from, next) => {
//   previousRoute = from
//   next()
// })

// Step 1: Detect initial navigation
const nav = performance.getEntriesByType('navigation')[0]
if (nav) {
  if (nav.type === 'navigate') entryType.value = 'first-visit'
  else if (nav.type === 'reload') entryType.value = 'reload'
  else if (nav.type === 'back_forward') entryType.value = 'back_forward'
} else {
  entryType.value = 'first-visit'
}

// Step 2: Track internal/back_forward
let lastRoute = null
let backForwardTriggered = false
let initialNavHandled = false

// Detect browser back/forward
window.addEventListener('popstate', () => {
  backForwardTriggered = true
})

router.afterEach((to) => {
  if (initialNavHandled) {
    if (backForwardTriggered) {
      entryType.value = 'back_forward'
      backForwardTriggered = false
    } else {
      entryType.value = 'internal'
    }
  } else {
    // First afterEach run (initial load)
    initialNavHandled = true
  }

  if (lastRoute && lastRoute.fullPath !== to.fullPath) {
    previousRoute.value = { ...lastRoute }
  }

  currentRoute.value = { fullPath: to.fullPath, path: to.path, query: { ...to.query } }
  lastRoute = { fullPath: to.fullPath, path: to.path, query: { ...to.query } }

  if (window._paq) {
    window._paq.push(['setCustomUrl', to.fullPath])
    window._paq.push(['setDocumentTitle', document.title])
    window._paq.push(['trackPageView'])
  }
})

// router.afterEach((to, from) => {
//   if (lastRoute && lastRoute.fullPath !== to.fullPath) {
//     entryType.value = 'internal'
//     previousRoute.value = { ...lastRoute }
//   }
//   currentRoute.value = { fullPath: to.fullPath, path: to.path, query: { ...to.query } }
//   lastRoute = { fullPath: to.fullPath, path: to.path, query: { ...to.query } }
// })

// export { router, previousRoute }

export default router
