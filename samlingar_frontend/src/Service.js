import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API
// const speciesSearchUrl = import.meta.env.VITE_SBDI_SPECIES_SEARCH
const institutionId = import.meta.env.VITE_SUPPORTED_INSTITUTIONS
const resultsPerPage = 10

export default class Service {
  async autoComplete(searchText, start, rows) {
    const url = `${baseUrl}/search?q=data_hub_uid:${institutionId} AND text:${searchText}*&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc`
    // const url = `${baseUrl}/search?q=${searchText}*  AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc`

    const response = await axios.get(url)

    return response.data
  }

  async quickSearch(searchText, start, rows) {
    // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,year,typeStatus&flimit=2000`
    const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,point-1,typeStatus&flimit=2000`
    const response = await axios.get(url)

    return response.data
  }

  async conditionalSearch(searchText, start, collectionName, typeStatus, rows) {
    const url = this.buildUrl(searchText, start, collectionName, typeStatus, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  async advanceConditionalSearch(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    selectedCollection,
    selectedTypeStatus,
    start,
    rows
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
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

    if (isType) {
      url = url + '&fq=typeStatus:*'
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (selectedTypeStatus) {
      url += `&fq=typeStatus:"${selectedTypeStatus}"`
    }

    // if (selectedYear) {
    // if (selectedYear === 'Not supplied') {
    // url += '&fq=-year:*'
    // } else {
    // url += `&fq=year:${selectedYear}`
    // }
    // }

    url =
      url + `&start=${start}&pageSize=${rows}&facets=collectionName,lat_long,typeStatus&flimit=2000`
    const response = await axios.get(url)

    return response.data
  }

  async searchMapData(searchText, selectedCollection, selectedYear, total) {
    const url = this.buildUrl(searchText, 1, selectedCollection, selectedYear, total, true)
    const response = await axios.get(url)
    return response.data
  }

  async advanceSearch(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    start,
    rows
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
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

    if (isType) {
      url = url + '&fq=typeStatus:*'
    }
    url = url + `&start=${start}&pageSize=${rows}&facets=collectionName,lat_long,typeStatus`
    // url = url + `&start=${start}&pageSize=${rows}&facets=collectionName,year,lat_long,typeStatus`
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
    const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  async searchFiltByYear(searchText, start, collectionName, year, rows) {
    const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  buildUrl(searchText, start, collectionName, typeStatus, rows, isMap) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"&start=${start}&pageSize=${rows}`
    }

    if (collectionName) {
      url += `&fq=collection_name:"${collectionName}"`
    }

    if (typeStatus) {
      url += `&fq=typeStatus:"${typeStatus}"`
    }

    // if (year) {
    //   if (year === 'Not supplied') {
    //     url += '&fq=-year:*'
    //   } else {
    //     url += `&fq=year:${year}`
    //   }
    // }



    // if (isMap) {
    //   url += `&fq=lat_long:*`
    // }
    return url + '&facets=collectionName,lat_long,typeStatus&flimit=2000'
  }
}
