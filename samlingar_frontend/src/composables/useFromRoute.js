import { ref } from "vue"
import { useRouter, useRoute } from "vue-router"

export function useFromRoute() {
  const router = useRouter()
  const route = useRoute()

  const fromRoute = ref({ fullPath: "(initial...)" }) // default
  const toRoute = ref(route)

  let isInitialLoad = true

  if (!router._hasFromRouteGuard) {
    router._hasFromRouteGuard = true

    router.beforeEach((to, from, next) => {
      if (isInitialLoad) {
        // On first load, use external referrer if available
        if (document.referrer) {
          fromRoute.value.fullPath = document.referrer
        }
      } else {
        // SPA navigation: previous internal route
        fromRoute.value = from
      }

      toRoute.value = to
      isInitialLoad = false
      next()
    })
  }

  return { fromRoute, toRoute }
}
