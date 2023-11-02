import { defineStore } from 'pinia'

export const useLangStore = defineStore('lang', {
  /*
    Info: state est l'équivalent d'attributs en java, ici lang est un attribut en java ou une variable en général. Il peut être considéré comme un annuaire ou on y met les infos relatives à un objet.
    lang: la langue de l'utilisateur ou fr par défaut
  */
  state: () => ({
    lang: localStorage.getItem('lang') || import.meta.env.VITE_I18N_LOCALE || 'fr',
    locales: [{
        id: 'fr',
        locale: 'Français' 
      },
      {
        id: 'en',
        locale: 'Anglais' 
      },
      {
        id: 'ar',
        locale: 'Arabe' 
      }
    ]
  }),

  /*
    Info: getters est l'équivalent des getters en java sauf qu'ici nous sommes pas obligé de suivre la nomenclature en java getX..
    NB: On ne peut pas accéder le contenu du state lang sans passer par le getter, ici getLang
    getLang: retourne le contenu du state lang stocké dans l'anuaire
  */
  getters: {
    getLang: (state) => state.lang,
    getLocales: (state) => state.locales
  },

  /*
    Info: actions regroupe les fonctions métiers pour le traitement intermédiaire; on peut y faire des appels api et tout les besoins métiers
    NB: Pour accéder à une information stockée dan l'annuaire (state), on passe par getX et setX pour la modification du contenu
    changeLang: enregistre la langue choisie par l'utilisateur dans la stockage locale avant de changer le contenu de l'attribut/state lang en passant par les muttations
    setLangState: vérifie s'il y'a une langue enregistré dans le stockage e met a jour l'annuaire sinon il prend la langue par défaut qu'est défini dans le fichier .env
  */
  actions: {
    changeLang(payload) {
      localStorage.setItem('lang', payload);
      this.lang = payload;
    },

    setLangState(payload) {
      if (localStorage.getItem('lang')){
        this.changeLang(payload);
      } else{
        this.changeLang(import.meta.env.VITE_I18N_LOCALE );
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