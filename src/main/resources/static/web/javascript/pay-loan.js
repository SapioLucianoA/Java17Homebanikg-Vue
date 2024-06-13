const { createApp } = Vue

createApp({
  data() {
    return {
      count: 0,
      amount:"",
      originAmount:"",
      loanName:"",
      accountNumberOrigin:"",
      accounts:[],
      loan:[],
      number:"",
      loanCost:"",
    }
  },
  created () {
    let params = new URLSearchParams(window.location.search).get(`clientLoanId`);
    console.log(params)
    let url = `/api/loans/${params}`;
    axios.get(url).then(
      response =>{
        this.loan = response.data;
        console.log(this.loan)

        this.loanName = this.loan.loanName;
        this.loanCost = (this.loan.amount / this.loan.payments).toLocaleString("en-US", {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
        });
        this.amount = parseFloat(this.loanCost);
      }
    )
    axios.get("/api/clients/current/accounts").then(
      response => {
        
        this.accounts = response.data;
        console.log(this.accounts)
      }
    )
  },
  watch: {
    accountNumberOrigin(newValue) {
      if (newValue) {
        // Find the selected account object based on the account number
        const selectedAccount = this.accounts.find(account => account.number === newValue);
        if (selectedAccount) {
          this.number = selectedAccount.number;
          this.originAmount = selectedAccount.balance.toLocaleString("en-US", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          }); // Update the amount with the balance
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
    payLoan(){
      axios.post("/api/client/pay",  `amount=${(this.amount)}&loanName=${this.loanName}&number=${this.number}`).then(response =>{
        console.log(response.data)
        window.location.href ="/web/pages/accounts.html"
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
},
}).mount('#app')