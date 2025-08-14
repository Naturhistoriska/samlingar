import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const navType = ref('initial')
const entryType = ref('unknown')
const fromRoute = ref({ fullPath: '(initial)' })
const toRoute = ref(null)
const formMethod = ref(null)
let isInitialLoad = true
let formListener = null

export function useNavigationInfo() {
  const router = useRouter()
  const route = useRoute()

  // Always track current route
  toRoute.value = route

  // Detect initial navigation type and external referrer
  onMounted(() => {
    const [entry] = performance.getEntriesByType('navigation')
    if (entry) {
      entryType.value = entry.entryType || 'unknown'
      navType.value = entry.type || 'initial'
    }

    // External referrer for first load
    if (isInitialLoad && document.referrer) {
      fromRoute.value = { fullPath: document.referrer }
    }

    // Detect SPA form submissions dynamically
    formListener = (event) => {
      const form = event.target
      if (form && form.tagName === 'FORM') {
        const method = form.method?.toUpperCase()
        formMethod.value = method === 'POST' ? 'POST' : 'GET'
      }
    }
    document.addEventListener('submit', formListener, true)
  })

  onBeforeUnmount(() => {
    if (formListener) {
      document.removeEventListener('submit', formListener, true)
    }
  })

  // Detect back/forward buttons
  window.addEventListener('popstate', () => {
    navType.value = 'back_forward'
  })

  // Router navigation guard
  if (!router._hasNavInfoGuard) {
    router._hasNavInfoGuard = true

    router.beforeEach((to, from, next) => {
      // Update fromRoute only if not first load
      if (!isInitialLoad) {
        fromRoute.value = from
        navType.value = 'router-link'
      }

      // Always update toRoute
      toRoute.value = to
      isInitialLoad = false
      next()
    })
  }

  return { navType, entryType, fromRoute, toRoute, formMethod }
}
