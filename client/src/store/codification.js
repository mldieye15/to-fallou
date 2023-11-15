import { defineStore } from "pinia";
import axios from '@/plugins/axios.js';

const getCodeURL = '/v1/codifications/get-code';
const sendCodeURL = '/v1/codifications/send-code';
 export const useCodeStore=defineStore("code",{
    state: () => ({
        code: null,
        error: null,
        successMessage: null,
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
                this.error = 'Erreur lors de la récupération du code.';
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
                this.error = "Aucun code de vérification trouvé pour cet e-mail.";
                } else {
                this.error = 'Erreur lors de l\'envoi du code.';
                }
            }
            },
      }
    });