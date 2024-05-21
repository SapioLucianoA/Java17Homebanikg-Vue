const { createApp } = Vue;

createApp({
  data() {
    return {
      accounts: [],
      data: {},
      userName: "",
      loans:[]
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

    //  
    // console.log(this.userName)
  })
  .catch(error => {
    console.error('Error al obtener los datos:', error);
  });
},

}).mount("#app");

