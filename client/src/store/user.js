import { defineStore } from 'pinia'
import axios from '@/plugins/axios.js'

const  loginURL = '/auth/v1/connexion';

export const useUserStore = defineStore('user', {

  state: () => ({
    isLoggedIn: false,
    userDetails: {
      email: '',
      prenoms: '',
      nom: '',
      username: ''
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
    getRefreshToken: (state) => state.refreshToken,
    getUsername: (state) => state.username,
  },

  actions: {
    //  connexion
    async login(payload){
      this.user = null;
      this.loading = true;
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
            this.user = response.data;
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

    //
    changeLoggedIn() {
      console.log("changeLoggedIn in user store");
      if(localStorage.getItem('token')){
        this.isLoggedIn = true;
      } else{
        this.isLoggedIn = false;
      }
    }
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
