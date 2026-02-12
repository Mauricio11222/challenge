# Build Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build  # Esto genera dist/challenge-springboot-angular/browser

# Nginx
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Movemos browser al root de Nginx
COPY --from=build /app/dist/challenge-springboot-angular/browser /usr/share/nginx/html
EXPOSE 80
