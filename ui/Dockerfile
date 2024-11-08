FROM node:18 AS build-stage

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy all files and build the app
COPY . .

# Build the Vue app
RUN npm run build

# Install nginx to serve the built files
FROM nginx:alpine

# Copy built files to nginx's default directory
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Copy custom Nginx config
COPY ./nginx.conf /etc/nginx/nginx.conf

# Expose the port Nginx is running on
EXPOSE 80

# Start nginx server
CMD ["nginx", "-g", "daemon off;"]
