import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API

export default class Service {
  async fetchSeachResult(searchText) {
    const url = `${baseUrl}q=taxa:"${searchText}" AND institution_uid:in0`

    const response = await axios.get(url)

    return response.data
  }


}
