# Samlingar - Vue 3 Frontend for Museum Collections Search

A Vue 3 web application for exploring the museum’s online collections.

This UI connects to the Museum Search REST API (backed by Solr) to provide interactive search, filtering, and item browsing.

## Purpose

This project provides a user-friendly interface that allows visitors to search and browse the museum’s digital collection.

It is built using Vue 3, Vite, and Axios, and communicates with a RESTful backend.

## Tech Stack

- Vue 3 (Composition API)
- Vite (development server + build)
- Vue Router 4
- Axios for API requests
- Vuex
- Node.js 22.3.0
- yarn 1.22.22
- primevue 4.x
- leaflet
- openseadragon
- RESTful API backend (Java 8 + SolrJ + Thorntail)

## Configuration

Environment variables for API endpoints are stored in:

.env

```
VITE_SAMLINGAR_API_LOCAL=http://localhost:8180
```

## Getting Started

1. Install Node.js

Make sure you have Node.js (LTS recommended):

https://nodejs.org/

2. Install dependencies

```
npm install

# or

yarn install
```

3. Run the development server

```
yarn dev
```

4. Open the UI:

```
http://localhost:5173/
```
