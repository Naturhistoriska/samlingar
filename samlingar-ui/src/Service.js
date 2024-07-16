import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API
const institutionId = import.meta.env.VITE_SUPPORTED_INSTITUTIONS


export default class Service {
  async fetchSeachResult(searchText) {
    const url = `${baseUrl}/search?q=taxa:"${searchText}" AND institution_uid:${institutionId}`

    const response = await axios.get(url)

    return response.data
  }

  async advanceSearch(searchText, species_group, catalogNumber) {
    const url = `${baseUrl}/search?q=species_group:${species_group} AND taxa:"${searchText}" AND catalogue_number:${catalogNumber} AND institution_uid:${institutionId}`

    const response = await axios.get(url)

    return response.data
  }

  async uuidSearch(uuid) {
    const url = `${baseUrl}/${uuid}`

    const response = await axios.get(url)

    return response.data
  }

  // buildUrl(speciesGroup, taxa, catalogNumber) {
  //   if (speciesGroup === null && taxa === null && catalogNumber === null) {
  //     return null;
  //   }
  //   if (speciesGroup !== null && taxa === null && catalogNumber === null) {
  //     re
  //   }
  //     if (a !== null && b !== null && c !== null) {
  //       return `${baseUrl}/search?q=species_group:${a} AND taxa:"${b}" AND catalogue_number:${c} AND institution_uid:${institutionId}`
  //     }
  //   if (a === null) {
  //     if (b !== null && c !== null) {
  //       return `${baseUrl}/search?q=taxa:"${b}" AND catalogue_number:${c} AND institution_uid:${institutionId}`
  //     }
  //   }
  // }
}
