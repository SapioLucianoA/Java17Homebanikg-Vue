const { createApp } = Vue

createApp({
  data() {
    return {
      count: 0,
      cards: [],
      creditsCards: [],
      debitsCards: [],
      message: '',
      messageCredit: '',
      messageDebit: '',
      isFullCard: false,
      today: "",
    }
  },
  created() {
    axios.get('/api/clients/current/cards')
      .then(response => {
        this.cards = response.data;
        this.creditsCards = this.cards.filter(card => card.type == 'CREDIT');
        console.log(this.creditsCards);
        this.debitsCards = this.cards.filter(card => card.type == 'DEBIT');
        console.log(this.debitsCards);
        this.today = new Date();
        this.checkCards;
        this.checkCardsCredit;
        this.checkCardsDebit;
        this.checkTotalCard;
      })

  },
  methods: {
    formatDate(dateString) {
      let date = new Date(dateString);
      let month = date.getMonth() + 1; // Los meses en JavaScript van de 0 a 11, por lo que se agrega 1
      let year = date.getFullYear();

      // Formatear el mes y el año con dos dígitos
      let formattedMonth = (month < 10 ? '0' : '') + month;
      let formattedYear = year.toString();
      return formattedMonth + "/" + formattedYear.slice(2);
    },
    isExpired(date1, date2) {
      let cardDate = new Date(date2);
      return cardDate < date1;
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
    removeCard(number, isActive) {
      if (confirm("¿Estás seguro de continuar? Esta acción eliminará tu tarjeta.")) {
        axios.patch('/api/client/remove/card', `number=${number}&isActive=${isActive}`)
          .then(response => {
            console.log(response);
            window.location.href = '/web/pages/cards.html';
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
          });
      }       else alert("request = cancelled")
      


  }
},
  computed: {
  checkCards() {
    if (this.cards.length === 0) {
      this.message = 'You dont have any capyCards to see';
    }
  },
  checkCardsCredit() {
    if (this.creditsCards.length === 0) {
      this.messageCredit = 'You dont have any to see here :c';
    }
  },
  checkCardsDebit() {
    if (this.debitsCards.length === 0) {
      this.messageDebit = 'You dont have any to see here :c';
    }
  },
  checkTotalCard() {
    if (this.creditsCards.length >= 3 && this.debitsCards.length >= 3) {
      this.isFullCard = true;
      console.log("is Full Cards")
      console.log(this.isFullCard)
    }
  }
},

}).mount('#app')
