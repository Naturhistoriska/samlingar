import { ref } from 'vue'
import { useRouter } from 'vue-router'

const previousRoute = ref(null)
const previousPath = ref(null)

export function usePreviousRoute() {
  const router = useRouter()

  // Register global guard once (only the first time composable is used)
  if (!router.__hasPreviousRouteGuard) {
    router.__hasPreviousRouteGuard = true

    router.beforeEach((to, from, next) => {
      // Clone values so the reactive Proxy does not get overwritten
      previousRoute.value = from.fullPath ? { ...from } : null
      previousPath.value = from.fullPath || null
      next()
    })
  }

  return {
    previousRoute,
    previousPath
  }
}
