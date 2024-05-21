const { createApp } = Vue

createApp({
  data() {
    return {
      count: 0,
      type:"",
      color:"",
    }
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
  newCard(){
    if (this.isBlank(this.type)) {
      alert('Please, select your card type.');
      return;
    }
    if (this.isBlank(this.color)) {
      alert('Please, select your card color.');
      return;
    }

    axios.post("/api/clients/current/cards", `type=${this.type}&color=${this.color}`)
    .then(response =>{
      console.log(response);
      
      window.location.href = `/web/pages/cards.html`;
    })
    .catch(error => {
      // Handle logout error
    alert('error:', error);
  });
  },
  isBlank(str) {
    return (!str || /^\s*$/.test(str));
},
}


}).mount('#app')
