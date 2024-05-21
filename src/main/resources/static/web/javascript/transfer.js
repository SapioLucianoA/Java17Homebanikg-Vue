const { createApp } = Vue

createApp({
  data() {
    return {
      accounts:[],
      amount:"",
      accountNumberOrigin:"",
      originAmount:"",
      accountNumberSend:"",
      description:"",
      picked: null,
    }
  },

  created () {
    axios.get('/api/clients/current/accounts')
      .then(response => {
        console.log(response)
        this.accounts = response.data
      })
  },
  watch: {
    accountNumberOrigin(newValue) {
      if (newValue) {
        // Find the selected account object based on the account number
        const selectedAccount = this.accounts.find(account => account.number === newValue);
        if (selectedAccount) {
          this.originAmount = selectedAccount.balance; // Update the amount with the balance
        } else {
          // Handle case where the account number doesn't match any existing account (optional)
          console.warn('Selected account not found.');
        }
      } else {
        // Clear the amount if no account is selected
        this.originAmount = '';
      }
    }
  },
  methods: {
    isBlank(str) {
      return (!str || /^\s*$/.test(str));
    },

    SendTransaction(){
      if (this.isBlank(this.accountNumberOrigin)) {
        alert('Please, complete the origin account.');
        return;
      }
      if (this.isBlank(this.accountNumberSend)) {
        alert('Please, complete the account to send the transaction.');
        return;
      }
      if (this.isBlank(this.description)) {
        alert('Please,  complete the description.');
        return;
      }
      if(confirm('¿are you sure to send this transaction? Please take a another look before to send'))
        console.log(this.accountNumberSend)
        axios.post('/api/transactions', `amount=${(this.amount)}&accountNumberOrigin=${this.accountNumberOrigin}&accountNumberSend=${this.accountNumberSend}&description=${this.description}`)
          .then(response => {
            console.log(response)
            alert("Transaction Send!")
            window.location.href = '/web/pages/accounts.html'
          })
          .catch(error => {
            if (error.response) {
                // La solicitud se hizo y el servidor respondió con un código de estado
                // que cae fuera del rango de 2xx
                console.log(error.response.data);
                alert(error.response.status + " " + error.response.data);
                console.log(error.response.headers);
            } else if (error.request) {
                // La solicitud se hizo pero no se recibió ninguna respuesta
                console.log(error.request);
            } else {
                // Algo sucedió en la configuración de la solicitud que provocó un error
                console.log('Error', error.message);
            }
            console.log(error.config);
        });
    },

    logOut() {
      // Send a POST request to the logout endpoint
      axios.post('/api/logout')
          .then(response => {
              // Handle successful logout
              console.log('Logged out successfully');
              window.location.href ="/web/pages/login.html"

          })
          .catch(error => {
              // Handle logout error
              console.error('Error logging out:', error);
          });
  },
}
})
.mount('#app')