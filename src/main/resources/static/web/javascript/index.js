const { createApp } = Vue

createApp({
    data() {
        return {
            message: 'Hello Vue!',
            clientRecord: {
                name: "",
                lastName: "",
                email: "",
                password: "",
            },
            email: '',
            password: '',

            isActive: false,
        }
    },
    methods: {
        changeActive() {
            this.isActive = !this.isActive;
        },
        submitForm() {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    console.log(response)

                    window.location.href = `/web/pages/accounts.html`;
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
                    console.log(error.config);
                });;
        },
        register() {
            axios.post(`/api/client`, this.clientRecord)

                .then(response => {
                    console.log(response)
                    alert("register succes! thanks to be capybara")
                    // window.location.href = `/web/index.html`
                    this.changeActive();
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
                    console.log(error.config);
                });;
        },


    }
}).mount('#app')