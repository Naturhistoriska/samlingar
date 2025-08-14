import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

export function usePreviousRoute() {
  const route = useRoute()
  const previousRoute = ref(null)
  const currentRoute = ref(route)

  // Watch route changes to update previousRoute
  watch(
    () => route.fullPath,
    () => {
      previousRoute.value = { ...currentRoute.value } // store the current as previous
      currentRoute.value = { ...route } // update current
    },
    { immediate: true }
  )

  return { previousRoute, currentRoute }
}
