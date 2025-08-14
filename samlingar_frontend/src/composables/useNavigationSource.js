import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

export function useNavigationSource() {
  const isBrowserNavigation = ref(false)
  const router = useRouter()

  const onPopState = () => {
    isBrowserNavigation.value = true
  }

  onMounted(() => {
    window.addEventListener('popstate', onPopState)
  })

  onUnmounted(() => {
    window.removeEventListener('popstate', onPopState)
  })

  router.beforeEach((to, from, next) => {
    // Continue with navigation
    next()
  })

  router.afterEach(() => {
    // Reset after route finishes
    isBrowserNavigation.value = false
  })

  return { isBrowserNavigation }
}
