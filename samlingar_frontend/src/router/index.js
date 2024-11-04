import { createWebHistory, createRouter } from 'vue-router'
import About from '../views/About.vue'
import Contact from '../views/Contact.vue'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import Results from '../views/Results.vue'
import ResultDetail from '../components/ResultDetail.vue'
import SearchFilter from '../components/SearchFilter.vue'

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
    path: '/results',
    name: 'Results',
    component: Results,
    meta: {
      title: 'Results | Samlingar'
    }
  },
  {
    path: '/results',
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
    path: '/result/:id',
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
  routes
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
