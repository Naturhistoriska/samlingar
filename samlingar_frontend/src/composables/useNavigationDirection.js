// composables/useNavigationDirection.js
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export function useNavigationDirection() {
  const direction = ref('programmatic') // 'back', 'forward', 'programmatic'
  const router = useRouter()

  let lastPosition = window.history.state?.position ?? 0

  router.beforeEach((to, from, next) => {
    const newPosition = window.history.state?.position ?? 0

    if (newPosition < lastPosition) {
      direction.value = 'back'
    } else if (newPosition > lastPosition) {
      // Could be forward or programmatic push
      // If the difference is > 1, it's forward
      direction.value = newPosition - lastPosition > 1 ? 'forward' : 'programmatic'
    } else {
      // Same position: likely replace()
      direction.value = 'programmatic'
    }

    lastPosition = newPosition
    next()
  })

  return { direction }
}
