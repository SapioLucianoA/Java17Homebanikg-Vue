const { createApp } = Vue

createApp({
  data() {
    return {
      count: 0,
      type: "",
      color: "",
    }
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
    newCard() {
      if (this.isBlank(this.type)) {
        alert('Please, select your card type.');
        return;
      }
      if (this.isBlank(this.color)) {
        alert('Please, select your card color.');
        return;
      }

      axios.post("/api/clients/current/cards", `type=${this.type}&color=${this.color}`)
        .then(response => {
          console.log(response);

          window.location.href = `/web/pages/cards.html`;
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
    isBlank(str) {
      return (!str || /^\s*$/.test(str));
    },
  }


}).mount('#app')
