const { createApp } = Vue

createApp({
  data() {
    return {
      clients:[],

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
    

  }
}).mount('#app')