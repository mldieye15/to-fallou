import axios from 'axios';

let lang = localStorage.getItem('lang') || import.meta.env.VITE_I18N_LOCALE || 'fr';

const defaultOptions = {
baseURL: import.meta.env.VITE_BASE_URL,
crossdomain: true,
//withCredentials: true,
headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'Accept-Language': lang,
    'X-localization': lang,
    }
};

let axiosInstance = axios.create(defaultOptions);

axiosInstance.interceptors.request.use(function (config) {
  const token = localStorage.getItem('token');
  config.headers.Authorization =  token ? `Bearer ${token}` : '';
  return config;
});

axiosInstance.interceptors.response.use(
  function (response) {
    // Vérifier si le token a expiré dans la réponse
    if (response.status === 401) {
      clearLocalStorage(); // Nettoyer le stockage local
      // Rediriger vers la page de connexion ou afficher un message d'erreur
    }
    return response;
  },
  function (error) {
    // Gérer les erreurs de réponse ici
    if (error.response && error.response.status === 401) {
      clearLocalStorage(); // Nettoyer le stockage local
      // Rediriger vers la page de connexion ou afficher un message d'erreur
    }
    return Promise.reject(error);
  }
);

function clearLocalStorage() {
  localStorage.removeItem('token');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('email');
  localStorage.removeItem('role');
  localStorage.removeItem('fullname');
  localStorage.removeItem('dateExpiration');
  // Nettoyer d'autres données sensibles si nécessaire
}


export default axiosInstance;


