import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export function useNavigationTracker() {
  const router = useRouter()
  const route = useRoute()

  const entryType = ref(null)
  const previousRoute = ref(null)
  const currentRoute = ref({
    fullPath: route.fullPath,
    path: route.path,
    query: { ...route.query }
  })

  // Detect first visit / reload
  const nav = performance.getEntriesByType('navigation')[0]
  if (nav) {
    if (nav.type === 'navigate') entryType.value = 'first-visit'
    else if (nav.type === 'reload') entryType.value = 'reload'
    else if (nav.type === 'back_forward') entryType.value = 'back_forward'
  } else {
    entryType.value = 'first-visit'
  }

  // Keep track of the last route manually
  let lastRoute = { ...currentRoute.value }

  router.afterEach((to) => {
    // Detect internal navigation
    if (entryType.value === 'first-visit' || entryType.value === 'reload') {
      // keep first load/reload as entryType
    } else {
      entryType.value = 'internal'
    }

    // Update previous route only if it changed
    if (lastRoute.fullPath !== to.fullPath) {
      previousRoute.value = { ...lastRoute }
    }

    // Update current route and lastRoute
    currentRoute.value = { fullPath: to.fullPath, path: to.path, query: { ...to.query } }
    lastRoute = { ...currentRoute.value }
  })

  return { entryType, previousRoute, currentRoute }
}
