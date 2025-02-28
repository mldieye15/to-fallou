# Étape 1 : Construction de l'application Vue.js
# Étape 1 : Serveur Nginx pour héberger l'application
FROM nginx:latest AS production-stage

# Copier le dossier dist (généré localement) dans le répertoire Nginx
COPY ./dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80 pour que Nginx soit accessible
EXPOSE 80

# Commande par défaut pour démarrer Nginx en mode non détaché
CMD ["nginx", "-g", "daemon off;"]

