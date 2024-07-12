import { createStore } from 'vuex'

export default createStore({
  state: {
    results: []
  },

  getters: {
    results (state) {
      return state.results
    }
  },

  mutations: {
    setResults: (state, payload) => (state.results = payload)
  }

  // mutations: {
    // addCartItem(state, item) {
      // item.quantity = 1
      // state.cart.push(item)
    // },
    // getResults(state) {
      // return state.results
    // },
    // removeCartItem(state, item) {
      // state.cart = state.cart.filter((cartItem) => {
        // return cartItem.id != item.id
      // })
    // }
  // }
})
