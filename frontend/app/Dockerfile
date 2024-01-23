# Stage 1: Build Angular app
FROM node:18 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve Angular app with Nginx
FROM nginx:latest
COPY --from=build /app/dist/app /usr/share/nginx/html
COPY ./nginx-config/nginx.conf /etc/nginx/conf.d/default.conf 
COPY ./nginx-config/cert.crt /etc/nginx/ssl/cert.crt
COPY ./nginx-config/cert.key /etc/nginx/ssl/cert.key
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]
