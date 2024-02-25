// Utilities
import { defineStore } from 'pinia';

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: []
  }),

  getters: {
    getNotifications: (state) => state.notifications
  },

  actions: {
    // addNotification(payload) {
    //   this.notifications.push(payload);;
    // },

    removeNotification(payload) {
      this.notifications[payload].show = false;
      this.notifications = this.notifications.splice(payload, 1);
    }
  }
})
