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
            client: [],
        }
    },
    created() {
        axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data
            })
            .catch((error) => {
                console.log(error);
            });
    },
    methods: {
        existClient(miObject){
            return Object.entries(miObject).length !== 0;
        },
        changeActive() {
            this.isActive = !this.isActive;
        },
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
        submitForm() {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    console.log(response)

                    window.location.href = `/web/pages/accounts.html`;
                })
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
        register() {
            axios.post(`/api/client`, this.clientRecord)

                .then(response => {
                    console.log(response)
                    alert("register succes! thanks to be capybara")
                    this.changeActive();
                })
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


    }
}).mount('#app')