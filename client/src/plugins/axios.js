import axios from 'axios';

let lang = localStorage.getItem('lang') || import.meta.env.VITE_I18N_LOCALE || 'fr';

const defaultOptions = {
baseURL: import.meta.env.VITE_BASE_URL+"/pjobac/api",
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


export default axiosInstance;
/*

import axios from 'axios'

export default {
    install: (app, options) => {
        app.config.globalProperties.axios = axios.create({
            baseURL: options.baseUrl,
            withCredentials: true,
            headers: {
                Authorization: options.token ? `Bearer ${options.token}` : '',
            }
        })
    }
}

*/
