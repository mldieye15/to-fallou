import { defineStore } from 'pinia'
import axios from '@/plugins/axios.js'

const  loginURL = '/auth/v1/connexion';
const  lougoutURL = '/auth/v1/deconnexion';
const  refreshtokenURL = '/auth/v1/refresh-token';
const verifyTokenURL = '/auth/v1/verif-token';
const  resetPasswordURL = '/auth/v1/reset-password';
const newPasswordURL= '/auth/v1/new-password';
export const useUserStore = defineStore('user', {

  state: () => ({
    isLoggedIn: false,
    successMessage: null,
    errorMessage: null,
    /*userDetails: {
      email: '',
      prenoms: '',
      nom: '',
      username: ''
    },*/
    user: {
      username: '',
      fullname: '',
      photo: '',
      initiale: '',
      role:'',
    },
    refreshToken:'',
    username:'',
    users: [],
    user: null,
    loading: false,
    error: null
  }),

  getters: {
    getLoggedIn: (state) => state.isLoggedIn,
    getUerDetails: (state) => state.userDetails,
    getUser: (state) => state.user,
    getRefreshToken: (state) => state.refreshToken,
    getUsername: (state) => state.username,
    getRole: (state) => state.role,
    getError: (state) => state.error
  },

  actions: {
    //  connexion
    async login(payload){
      this.user = null;
      this.loading = true;
      this.error = null
      console.log("Payload connexion", payload);
      console.log("loginURL", loginURL);
      try {
        //`${loginURL}`
        await axios.post(loginURL, payload)
        .then((response) => {
          if(response.status === 200 && response.data.authenticationToken){
            localStorage.setItem('token', response.data.authenticationToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);
            localStorage.setItem('username', response.data.username);
            localStorage.setItem('role', response.data.role);
            this.user = {
              username: response.data.username,
              fullname: response.data.fullname,
              photo: response.data.photo,
              initiale: response.data.initiale,
              role: response.data.role,
            };
            console.log(this.user);
            this.changeLoggedIn(true);
            this.refreshToken = response.data.refreshToken;
            this.username = response.data.username;
            axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.authenticationToken}`;
            resolve(response);
          } else{
            reject(response);
        }

        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },

    async logout(){
      //this.user = null;
      this.loading = true;
      let refreshTokenLocal = localStorage.getItem('refreshToken');//(this.refreshToken == " ") ? localStorage.getItem('refreshToken') : this.refreshToken;
      let usernameLocal = localStorage.getItem('username'); // this.username || localStorage.getItem('username');
      const payload = {
        refreshToken: refreshTokenLocal, //this.refreshToken,
        username: usernameLocal //this.username
      };
      //console.log(payload);
      try {
        await axios.post(lougoutURL, payload).then((response) => {
          if(response.status === 200){
            /*this.changeLoggedIn(false);
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('username');
            localStorage.removeItem('token');
            this.user = null;*/
            this.resetCredentials();
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },

    async refreshToken(){
      //this.user = null;
      this.loading = true;
      const payload = {
        refreshToken: this.refreshToken,
        username: this.username
      };
      console.log(payload);
      try {
        await axios.post(refreshtokenURL, payload).then((response) => {
          if(response.status === 200 && response.data.authenticationToken){
            localStorage.setItem('token', response.data.authenticationToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);
            localStorage.setItem('username', response.data.username);
            this.user = {
              username: response.data.username,
              fullname: response.data.fullname,
              photo: response.data.photo,
              initiale: response.data.initiale
            };
            this.changeLoggedIn(true);
            this.refreshToken = response.data.refreshToken;
            this.username = response.data.username;
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //
    changeLoggedIn() {
      console.log("changeLoggedIn in user store");
      if(localStorage.getItem('token')){
        this.isLoggedIn = true;
      } else{
        this.isLoggedIn = false;
      }
    },

    //  reset credentials
    resetCredentials() {
      this.changeLoggedIn(false);
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('username');
      localStorage.removeItem('token');
      this.user = null;
      this.refreshToken = "";
      this.username = "";
    },
    async resetPassword(email) {
      try {
        await axios.post(`${resetPasswordURL}?email=${email}`);
        this.successMessage = 'Demande de réinitialisation de mot de passe réussie. Veuillez vérifier votre e-mail.';
        console.log('Réinitialisation de mot de passe réussie');
      } catch (error) {
        console.error('Erreur lors de la réinitialisation de mot de passe', error);
      }
    },
    async newPassword(token, newPassword) {
      try {
        // Appel direct à resetPassword avec le token
        await axios.post(`${newPasswordURL}?token=${token}&newPassword=${newPassword}`);
        console.log('Mot de passe réinitialisé avec succès');
        return 'Mot de passe réinitialisé avec succès';
      } catch (error) {
        console.error('Erreur lors de la réinitialisation de mot de passe', error);
        throw error;
      }
    },
    async verifyToken(token) {
      try {
        await axios.get(`${verifyTokenURL}/${token}`);
        console.log('Activation du compte effectuée avec succès.');
        return 'Activation du compte effectuée avec succès.';
      } catch (error) {
        console.error('Erreur lors de la vérification du token :', error);
        throw error;
      }
    },
  }
})

/*

import { defineStore } from 'pinia'

export const usePostStore = defineStore({
  id: 'post',
  state: () => ({
    posts: [],
    post: null,
    loading: false,
    error: null
  }),
  getters: {
    getPostsPerAuthor: (state) => {
      return (authorId) => state.posts.filter((post) => post.userId === authorId)
    }
  },
  actions: {
    async fetchPosts() {
      this.posts = []
      this.loading = true
      try {
        this.posts = await fetch('https://jsonplaceholder.typicode.com/posts')
        .then((response) => response.json())
      } catch (error) {
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async fetchPost(id) {
      this.post = null
      this.loading = true
      try {
        this.post = await fetch(`https://jsonplaceholder.typicode.com/posts/${id}`)
        .then((response) => response.json())
      } catch (error) {
        this.error = error
      } finally {
        this.loading = false
      }
    }
  }
})

*/
