const { createApp } = Vue

createApp({
  data() {
    return {
      loans: [],
      selectedLoan: null,
      selectedLoanAmount:null,
      selectedPayment: null,
      amount:"",
      id:"",
      payments:"",
      payment:null,
      accounts: [],
      accountNumber: "",
    }
  },

  created () {
    axios.get('/api/loans')
      .then(response => {
        
      this.loans = response.data
      }),

      axios.get('/api/clients/current/accounts')
      .then(response => {
        
        this.accounts = response.data
        
      })

      
  },

  
  watch: {
    selectedLoan(newValue) {
      // Actualiza el campo "Max Amount" cuando cambie el préstamo seleccionado
      const LoanSelected = this.getLoanById(newValue);
      if (LoanSelected) {
        this.selectedLoanAmount = LoanSelected.maxAmount.toLocaleString("en-US", {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
        }); ;
      } else {
        console.warn("Selected loan not found.");
        this.selectedLoanAmount = ""; // Otra opción si no se encuentra el préstamo
      }
    },
  },
  methods: {
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
    isBlank(str) {
      return (!str || /^\s*$/.test(str));
    },

    getLoanById(id) {
      return this.loans.find(loan => loan.id === id);
    },
    
    sendLoan(){
      if (this.isBlank(this.accountNumber)) {
        alert('Please, complete the account.');
        return;
      }
      if (this.isBlank(this.selectedLoan)) {
        alert('Please, complete the loan.');
        return;
      }
      if (this.isBlank(this.payment)) {
        alert('Please,  complete the payment.');
        return;
      }
      if(confirm('¿are you sure to send the loan? remember the amount have a plus'))
      console.log(this.selectedLoan);
        axios.post('/api/loan', { loanId:this.selectedLoan, amount:this.amount, accountNumber: this.accountNumber, payment: this.payment})
          .then(response => {
            console.log(response);
            alert("Loan Created");
            window.location.href = '/web/pages/accounts.html'
          })
          .catch(error => {
            if (error.response) {
                // La solicitud se hizo y el servidor respondió con un código de estado
                // que cae fuera del rango de 2xx
                console.log(error.response.data);
                alert(error.response.status + " - " + error.response.data);
                console.log(error.response.headers);
            } else if (error.request) {
                // La solicitud se hizo pero no se recibió ninguna respuesta
                alert(error.request);
            } else {
                // Algo sucedió en la configuración de la solicitud que provocó un error
                console.log('Error', error.message);
            }
            console.log(error.config);
        });
    },
    showIncrement(){
      if (this.payment >= 32){
        return this.amount * 1.8
      }
      else if (this.payment < 32 && this.payment > 12){
        return this.amount * 1.6
      }
      else {
        return this.amount * 1.2
      }
    },
  }    
})
.mount('#app')

