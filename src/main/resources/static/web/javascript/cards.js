const { createApp } = Vue

createApp({
  data() {
    return {
      count: 0,
      cards:[],
      creditsCards:[],
      debitsCards:[],
      message: '',
      messageCredit: '',
      messageDebit: '',
    }
  },
    created () {
      axios.get('/api/clients/current/cards')
        .then(response => {
          this.cards = response.data;
          this.creditsCards = this.cards.filter(card => card.type == 'CREDIT');
          console.log(this.creditsCards);
          this.debitsCards = this.cards.filter(card => card.type == 'DEBIT');
          console.log(this.debitsCards);
          this.checkCards;
          this.checkCardsCredit;
          this.checkCardsDebit;
        })

    },
    methods: {
      formatDate(dateString){
        let date = new Date(dateString);
        let month = date.getMonth() + 1; // Los meses en JavaScript van de 0 a 11, por lo que se agrega 1
        let year = date.getFullYear();
      
        // Formatear el mes y el año con dos dígitos
        let formattedMonth = (month < 10 ? '0' : '') + month;
        let formattedYear = year.toString();
        return formattedMonth+ "/" + formattedYear.slice(2);
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
    checkCards() {
      if (this.cards.length === 0) {
        this.message = 'You dont have any capyCards to see';
      }
    },
    checkCardsCredit(){
        if (this.creditsCards.length === 0) {
            this.messageCredit = 'You dont have any to see here :c';
          }
    },
    checkCardsDebit(){
        if (this.debitsCards.length === 0) {
            this.messageDebit = 'You dont have any to see here :c';
          }
    },
  },

}).mount('#app')
