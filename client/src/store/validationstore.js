// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/users';
const emailAvailability = modulesURL +'/email-availability';
const usernameAvailability = modulesURL +'/username-availability';
const matriculeAvailability = modulesURL +'/matricule-availability';

export const useValidationStore = defineStore('utilisateur', {
  state: () => ({
    isAvailable: false,

  }),
 
  actions: {
    async emailAvailability(email) {
      try {
        console.log("Avant la requête à l'API emailAvailability");
        const response = await axios.get(`${emailAvailability}?email=${email}`);
        console.log("Réponse de emailAvailability :", response);
    
        if (response && response.data && typeof response.data.isAvailable !== 'undefined') {
          return response.data.isAvailable;
        } else {
          console.error('La réponse ne contient pas la propriété isAvailable attendue.');
          return false;
        }
      } catch (error) {
        console.error('Erreur lors de la vérification de l\'email :', error);
        return false;
      }
    },
    async matriculeAvailability(matricule) {
      try {
        console.log("Avant la requête à l'API matriculeAvailability");
        const response = await axios.get(`${matriculeAvailability}?matricule=${matricule}`);
        console.log("Réponse de matriculeAvailability :", response);
    
        if (response && response.data && typeof response.data.isAvailable !== 'undefined') {
          return response.data.isAvailable;
        } else {
          console.error('La réponse ne contient pas la propriété isAvailable attendue.');
          return false;
        }
      } catch (error) {
        console.error('Erreur lors de la vérification de l\'matricule :', error);
        return false;
      }
    },
    
    
    
    

    
// async usernameAvailability(username) {
//   try {
//     const response = await axios.get(`${usernameAvailability}?username=${username}`);
//     console.log("Réponse de usernameAvailability :", response);

//     if (response.status === 200 && response.data && response.data.isAvailable !== undefined) {
//       return response.data.isAvailable;
//     } else {
//       console.error('La réponse de usernameAvailability n\'est pas conforme.');
//       return false;
//     }
//   } catch (error) {
//     console.error('Erreur lors de la vérification de l\'username :', error);
//     return false;
//   }
// },

  //   async matriculeAvailability(matricule) {
  //     try {
  //       const response = await axios.get(`${matriculeAvailability}?matricule=${matricule}`);
  //       console.log("Réponse de matriculeAvailability :", response);

  //   if (response.status === 200 && response.data && response.data.isAvailable !== undefined) {
  //     return response.data.isAvailable;
  //   } else {
  //     console.error('La réponse de matriculeAvailability n\'est pas conforme.');
  //     return false;
  //   }
  // } catch (error) {
  //   console.error('Erreur lors de la vérification de l\'matricule :', error);
  //   return false;
  //     }
  //   },
  },
  
})
