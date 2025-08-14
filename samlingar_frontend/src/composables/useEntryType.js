import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export function useEntryType() {
  const router = useRouter()
  const route = useRoute()

  const entryType = ref('unknown') // "first-visit", "reload", "route-push"
  const previousRoute = ref(null)
  const currentRoute = ref(route)

  let isInitialLoad = true

  onMounted(() => {
    // Detect navigation type using Performance API
    const [nav] = performance.getEntriesByType('navigation')
    if (nav) {
      switch (nav.type) {
        case 'reload':
          entryType.value = 'reload'
          break
        case 'navigate':
          entryType.value = 'first-visit'
          break
        case 'back_forward':
          entryType.value = 'back_forward'
          break
        default:
          entryType.value = 'unknown'
      }
    }

    // Listen for internal SPA route pushes
    router.beforeEach((to, from, next) => {
      if (!isInitialLoad) {
        previousRoute.value = from
        currentRoute.value = to
        entryType.value = 'route-push'
      }
      isInitialLoad = false
      next()
    })
  })

  return { entryType, previousRoute, currentRoute }
}

