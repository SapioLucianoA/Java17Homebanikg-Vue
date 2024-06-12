const { createApp } = Vue

createApp({
  data() {
    return {
      clients:[],
      isboolean: false,
      name:"",
      lastName:"",
      email:"",
      password:"",
      isAdmin: false,

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
  },
 
  methods: {
    deleteClient(email){
      confirm("are you sure to delete this client?")
      axios.post("/api/delete/client/admin", `email=${email}`)
      .then((response)=> {
        console.log(response.data),
        window.location.href ="/web/pages/admin.html"
      })
      .catch(error => {
        if (error.response) {
          // Mostrar mensaje de error al usuario
          alert(error.response.status + " - " + error.response.data);
        } else if (error.request) {
          // Mostrar mensaje de error al usuario
          alert("Error en la solicitud. Inténtalo de nuevo más tarde.");
        } else {
          // Mostrar mensaje de error al usuario
          alert("Error desconocido. Inténtalo de nuevo más tarde.");
        }
        console.log(error.config);
      })
    },
    isnewClient() {
      this.isboolean = this.isboolean ? false : true;
      console.log(this.isboolean)
    },
    createdClient(){
        confirm("are you sure to created a user?")
      axios.post("/api/client/admin", {
        name: this.name,
        lastName: this.lastName,
        email: this.email,
        password: this.password,
        isAdmin: this.isAdmin
      })
      .then(response => 
        console.log(response.data),
        window.location.href ="/web/pages/admin.html"
      )
      .catch(error => {
        if (error.response) {
            console.log(error.response.data);
            alert(error.response.status + " " + error.response.data);
            console.log(error.response.headers);
        } else if (error.request) {
            alert(error.request);
        } else {
            console.log('Error', error.message);
        }
        console.log(error.config);
    });;
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
}).mount('#app')