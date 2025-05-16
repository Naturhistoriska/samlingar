import { createWebHistory, createMemoryHistory, createRouter } from 'vue-router'
import About from '../views/About.vue'
import Collections from '../views/Collections.vue'
import Contact from '../views/Contact.vue'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import Results from '../views/Results.vue'
import SearchView from '../views/SearchView.vue'
import ResultDetail from '../components/ResultDetail.vue'
import SearchFilter from '../components/SearchFilter.vue'
import MapView from '../components/Map.vue'

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
    path: '/advanceSearch',
    name: 'AdvanceSearch',
    component: Home,
    meta: {
      title: 'Home | Samlingar'
    }
  },
  {
    path: '/records',
    name: 'Results',
    component: Results,
    meta: {
      title: 'Results | Samlingar'
    }
  },
  {
    path: '/records',
    component: Results,
    children: [
      {
        path: 'filter/:filter',
        name: 'filter',
        component: SearchFilter
      },
      {
        path: 'type/:filter',
        name: 'type',
        component: SearchFilter
      }
    ]
  },
  {
    path: '/map',
    component: Results,
    children: [
      {
        path: '',
        name: 'Map',
        component: MapView
      }
    ]
  },
  {
    path: '/record/:id',
    component: Results,
    children: [
      {
        path: '',
        name: 'Result',
        component: ResultDetail
      }
    ]
  },

  // {
  //   path: '/result/:id',
  //   name: 'Result',
  //   component: Results
  // },
  {
    path: '/search',
    name: 'Search',
    component: SearchView
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
  // history: createMemoryHistory(),
  // routes
  history: createWebHistory(import.meta.env.VITE_BASE_URL),
  routes
  //  history: createWebHistory(import.meta.env.VITE_BASE_URL),
  // history: createWebHistory(),
  // mode: 'history',
  // base: import.meta.env.BASE_URL,
  // routes,
  // scrollBehavior: function (to) {
  //   if (to.hash) {
  //     return {
  //       selector: to.hash
  //     }
  //   }
  // }
})


export default router
