const { createApp } = Vue

createApp({
  data() {
    return {
      message: 'Hello Vue!',
      email: '',
      password: '',
      name: '',
      lastName: '',
      clientRole: '',
      paymentsArray:'',
      newLoanDTO: {
        name: '',
        maxAmount: '',
        payment: [],
      },
      loans:[],

    }
  },

  created() {
    axios.get('/api/clients')
      .then((response) => {
        console.log(response.data);
        this.clients = response.data;
        console.log(this.clients);
      })
      .catch((error) => {
        console.log(error);
      });
      axios.get('/api/loans')
      .then((response) =>{
        this.loans = response.data
        console.log(this.loans)
      }
    )
  },

  methods: {
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
    newLoan() {
      console.log(this.paymentsArray);

      const numbersArray = this.paymentsArray.split(" ");
      

      const numberList = numbersArray.map(number => parseInt(number));
      console.log("numberList:", numberList);
      
      if(numberList != null)
        this.newLoanDTO.payment = numberList; 
        console.log(this.newLoanDTO.payment)
      

      
      alert( "are you sure to continue?, please read all form before to continue" )
      axios.post('/api/loan/new', {
        name: this.newLoanDTO.name,
        maxAmount: this.newLoanDTO.maxAmount,
        payment: this.newLoanDTO.payment,
      })
        .then(response => {
          console.log(numberList)
          console.log(response.data);
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
            alert(error.request);
          } else {
            // Algo sucedió en la configuración de la solicitud que provocó un error
            console.log('Error', error.message);
          }
          console.log(error.config.data);
        });
    }
  }
}).mount('#app')