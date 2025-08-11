import { createWebHistory, createMemoryHistory, createRouter } from 'vue-router'
import About from '../views/About.vue'
import Collections from '../views/Collections.vue'
import Contact from '../views/Contact.vue'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import Results from '../views/Results.vue'
import SearchView from '../views/SearchView.vue'
import RecordView from '../views/Record.vue'


let previousRoute = null

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'Home | Samlingar'
    }
  },
  {
    path: '/search',
    name: 'Search',
    component: SearchView
  },
  {
    path: '/record/:id',
    name: "Record",
    component: RecordView
  },

  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/collections',
    name: 'Collections',
    component: Collections
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
  routes
})

// Track previous route
router.beforeEach((to, from, next) => {
  previousRoute = from
  next()
})


export { router, previousRoute }

export default router
