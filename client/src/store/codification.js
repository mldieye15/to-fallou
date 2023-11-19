import { defineStore } from "pinia";
import axios from '@/plugins/axios.js';
const  modulesURL = '/v1/codifications'
const getCodeURL = modulesURL +'/get-code';
const sendCodeURL = modulesURL +'/send-code';
const verifyCodeURL = modulesURL + '/verify-code';
 export const useCodeStore=defineStore("code",{
    state: () => ({
        code: null,
        error: null,
        successMessage: null,
        isCodeValid: null,
      }),
    
      actions: {
      async getCode (email){
            try {
                const response = await axios.get(`${getCodeURL}?email=${email}`);
                this.code = response.data.code;
                this.error = null;
                this.successMessage = 'Code récupéré avec succès.';
            } catch (error) {
                this.code = null;
                if (error.response && error.response.data && error.response.data.message && error.response.data.message.startsWith("Aucun code de vérification trouvé pour l'e-mail : ")) {
                this.error = "Aucun code de vérification trouvé pour cet e-mail.";
                } else {
                this.error = "Aucun code de vérification trouvé pour l'e-mail verifier votre email ou contacter l'office";
                }
                this.successMessage = null;
            }
            },
            async sendCode(email){
            try {
                const response = await axios.post(`${sendCodeURL}?email=${email}`);
                this.code = response.data.code;
                this.successMessage = 'Code envoyé avec succès.';
            } catch (error) {
                this.code = null;
                if (error.response && error.response.data && error.response.data.message && error.response.data.message.startsWith("Aucun code de vérification trouvé pour l'e-mail : ")) {
                this.error = "Aucun code d'inscription trouvé pour l'e-mail  verifier votre email ou contacter l'office.";
                } else {
                this.error = " Code non envoyé:Aucun code d'inscription trouvé pour l'e-mail  verifier votre email ou contacter l'office.";
                }
            }
            },
            async verifyCode(code, email) {
                try {
                  const response = await axios.get(`${verifyCodeURL}?code=${code}&email=${email}`);
                  this.isCodeValid=response.data;
                  return this.isCodeValid;
                } catch (error) {
                    this.isCodeValid=false;
                  console.error('Erreur lors de la vérification du code :', error);
                  return false;
                }
              },
      }
    });