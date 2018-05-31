import axios from 'axios';
import mainConfig from "../configs/main";

export default {
    getAllBooks: () => axios.get(mainConfig.apiHost + '/pozycje').then(res => res.data),
}
