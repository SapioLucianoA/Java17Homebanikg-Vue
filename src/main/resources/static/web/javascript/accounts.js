const { createApp } = Vue;

createApp({
  data() {
    return {
      accounts: [],
      data: {},
      userName: "",
      loans:[],
      message: ''
    };
  },
  created() {

    axios.get('/api/clients/current')
  .then(response => {
    console.log(response.data);
    this.data = response.data;
    console.log(this.data)
    this.userName = this.data.lastName +" "+this.data.name;
    this.accounts = this.data.accountSet;

    console.log(this.accounts)
    this.loans = this.data.clientLoanSet;
    console.log(this.loans)
    this.checkLoans;
    //  
    // console.log(this.userName)
  })
  .catch(error => {
    console.error('Error al obtener los datos:', error);
  });

},
methods:{
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
removeAccount(number, isActive){
  alert("Are you sure you want to perform the action? Remember that this action is permanent, and the account cannot be deleted if it has money or owes money to the bank.")
  console.log("cuenta eliminada:" + number + isActive)

  axios.patch(`/api/clients/remove/account`,`number=${number}&isActive=${isActive}`)
  .then(response =>{
    console.log(response)
    alert("Account remove!")
    window.location.href = `/web/pages/accounts.html`
    
  })
  .catch(error => {
    if (error.response) {
        // La solicitud se hizo y el servidor respondió con un código de estado
        // que cae fuera del rango de 2xx
        console.log(error.response.data);
        alert(error.response.status + "- " + error.response.data);
        console.log(error.response.headers);
    } else if (error.request) {
        // La solicitud se hizo pero no se recibió ninguna respuesta
        console.log(error.request);
    } else {
        // Algo sucedió en la configuración de la solicitud que provocó un error
        alert('Error', error.message);
    }
    console.log(error.config);
});

}
},

computed: {
  checkLoans() {
    if (this.loans.length === 0) {
      this.message = 'you dont have any loan to see here <a href="./loan-aplication.html">click here</a> to create a loan.';
    }
  },
  
}


}).mount("#app");

