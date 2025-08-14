import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

export function useNavigationHistory() {
  const route = useRoute()

  const previousRoute = ref({ fullPath: '(initial)', path: '(initial)', query: {} })
  const currentRoute = ref(route)

  // On first load, check external referrer
  if (document.referrer) {
    previousRoute.value = {
      fullPath: document.referrer,
      path: document.referrer,
      query: {}
    }
  }

  // Watch route changes to update previousRoute
  watch(
    () => route.fullPath,
    (newFullPath, oldFullPath) => {
      previousRoute.value = { ...currentRoute.value } // store current as previous
      currentRoute.value = { ...route } // update current
    },
    { immediate: true }
  )

  return { previousRoute, currentRoute }
}
