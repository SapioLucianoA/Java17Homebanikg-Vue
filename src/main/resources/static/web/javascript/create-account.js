const { createApp } = Vue

createApp({
  data() {
    return {
      isSaved: false,
      isCommon: false,
      accountType: "",
    }
  },
  methods: {
    changeSaved() {
      this.isSaved = true;
      this.isCommon = false;
    },
    changeCommon() {
      this.isSaved = false;
      this.isCommon = true;
    },
    logOut() {
      // Send a POST request to the logout endpoint
      axios.post('/api/logout')
        .then(response => {
          // Handle successful logout
          console.log('Logged out successfully');
          window.location.href = "/web/pages/login.html"

        })
        .catch(error => {
          // Handle logout error
          console.error('Error logging out:', error);
        });
    },
    newAccount() {
      console.log("is work!!!"+ " " + this.accountType)
      axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
      .then(
        response => {
          console.log(response);
          window.location.href = `/web/pages/accounts.html`
        }
      )
      .catch(error => {
          // Handle logout error
          alert(error.response.data + ", " + "status: "+ error.response.status)
        });
    },
    isBlank(str) {
      return (!str || /^\s*$/.test(str));
    },
  }
}).mount('#app')
