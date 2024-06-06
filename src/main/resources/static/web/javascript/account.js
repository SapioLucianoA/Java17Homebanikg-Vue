const { createApp } = Vue

createApp({
  data() {
    return {
      message: '',
      clients: [],
      accounts: [],
      account: [],
      transactions:[],
    }
  },

  
  created () {
    let params = new URLSearchParams(window.location.search).get(`accountId`);
    console.log(params)
    axios
      .get('/api/clients/current')
      .then(response => {
        this.accounts = response.data.accountSet;  
        this.account = this.accounts.find(account => String(account.id) === String(params));
        console.log(this.account);

        this.transactions = this.account.transactions
        console.log(this.transactions)

        this.transactions.sort((a, b) => b.id - a.id)

        this.checktransactions;
      })
    
  },
  methods: {
    formatDate(dateString){
      const date = new Date(dateString);
      return date.toLocaleString()
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
  computed: {
    checktransactions() {
      if (this.transactions.length === 0) {
        this.message = 'You dont have any capytransactions to see';
      }
    }
  },
})
.mount('#app')