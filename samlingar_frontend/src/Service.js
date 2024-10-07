import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API
// const speciesSearchUrl = import.meta.env.VITE_SBDI_SPECIES_SEARCH
const institutionId = import.meta.env.VITE_SUPPORTED_INSTITUTIONS
const resultsPerPage = 10

export default class Service {
  async autoComplete(searchText, start, rows) {
    const url = `${baseUrl}/search?q=${searchText}* AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,year`

    const response = await axios.get(url)

    return response.data
  }

  async quickSearch(searchText, start, rows) {
    const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,year`

    const response = await axios.get(url)

    return response.data
  }

  async fetchSeachResult(searchText, start) {
    const url = `${baseUrl}/search?q=taxa:${searchText} AND data_hub_uid:${institutionId}&start=${start}&pageSize=${resultsPerPage}&sort=eventDate&dir=desc&facets=collectionName`

    const response = await axios.get(url)

    return response.data
  }

  async advanceSearch(searchText, species_group, dataset, catalogNumber, startDate, endDate, start, rows) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url = url + ` AND taxa:${searchText}`
    }

    if (species_group) {
      url = url + ` AND species_group:${species_group}`
    }

    if (dataset) {
      url = url + ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url = url + ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url = url + ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }


    url = url + `&start=${start}&pageSize=${rows}&facets=collectionName,year`
    const response = await axios.get(url)

    return response.data
  }

  async uuidSearch(uuid) {
    const url = `${baseUrl}/${uuid}`
    const response = await axios.get(url)
    return response.data
  }

  async searchFiltByCollection(searchText, start, collectionName, year, rows) {
    // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&fq=collection_name:"${collectionName}"&facets=collectionName,year`
    const url = this.buildUrl(searchText, start, collectionName, year, rows)
    const response = await axios.get(url)
    return response.data
  }

  async searchFiltByYear(searchText, start, collectionName, year, rows) {
    const url = this.buildUrl(searchText, start, collectionName, year, rows)
    const response = await axios.get(url)
    return response.data
  }

  async conditionalSearch(searchText, start, collectionName, year, rows) {
    const url = this.buildUrl(searchText, start, collectionName, year, rows)
    const response = await axios.get(url)
    return response.data
  }

  buildUrl(searchText, start, collectionName, year, rows) {
    let url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}`

    if (year) {
      if (year === 'Not supplied') {
        url = url + '&fq=-year:*'
      } else {
        url = url + `&fq=year:${year}`
      }
    }

    if (collectionName) {
      url = url + `&fq=collection_name:"${collectionName}"`
    }

    return url + '&facets=collectionName,year'
  }
}
