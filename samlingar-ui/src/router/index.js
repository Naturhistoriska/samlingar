import { createWebHistory, createRouter } from 'vue-router'
import Home from '@/views/Home.vue'
import Contact from '@/views/Contact.vue'
import About from '@/views/About.vue'
import NotFound from '@/views/NotFound.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
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
  history: createWebHistory(import.meta.env.VITE_BASE_URL),
  // history: createWebHistory(),
  routes
})

export default router
