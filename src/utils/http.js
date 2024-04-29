import axios from 'axios'

class Http {
  constructor() {
    this.instance = axios.create({
      baseURL: 'https://api-deepfake-detection-1.onrender.com/',
      timeout: 10000,      
      headers: {
        Accept: "application/json",
        "Content-Type": "multipart/form-data",

      },
    })
  }
}

const http = new Http().instance

export default http
