FROM nginx:1.13.3-alpine

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*

COPY dist/shopbiz-UI-App /usr/share/nginx/html
