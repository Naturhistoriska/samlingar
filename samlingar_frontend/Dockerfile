# Step 1: Build the Vue app
FROM node:18-alpine as build-stage

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build


# Step 2: Serve it with Nginx
FROM nginx:stable-alpine as production-stage

# Copy custom Nginx config
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy built app from previous stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expose port (optional, for local dev)
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
