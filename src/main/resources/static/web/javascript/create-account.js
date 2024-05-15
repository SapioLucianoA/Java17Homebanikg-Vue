const { createApp } = Vue

createApp({
  data() {
    return {
      isSaved: false,
      isCommon: false,
      typeAccount:"",
    }
  },
  methods: {
    changeSaved(){
      this.isSaved = true;
      this.isCommon = false;
    },
    changeCommon(){
      this.isSaved = false;
      this.isCommon = true;
    }
  }
}).mount('#app')
