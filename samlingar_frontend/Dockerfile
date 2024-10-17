FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]




# for local
# FROM node:20-alpine3.19
# RUN npm install -g @vue/cli
# RUN mkdir /app
# WORKDIR /app
# COPY . /app
# RUN yarn install
# EXPOSE 8080
# CMD ['yarn', 'dev']
