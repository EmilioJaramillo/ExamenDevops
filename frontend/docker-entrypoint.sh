#!/bin/sh
set -e

API_URL="${API_URL:-http://localhost:8080/api}"

envsubst '${API_URL}' < /usr/share/nginx/html/env-config.template.js > /usr/share/nginx/html/env-config.js

exec "$@"