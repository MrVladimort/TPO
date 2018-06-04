import axios from 'axios';
import mainConfig from "../configs/main";

export default {
    getAllBooks: () => axios.get(mainConfig.apiHost + '/pozycje').then(res => res.data),
    getFilteredBooks: filter => axios.post(mainConfig.apiHost + '/pozycje/filtered', filter).then(res => res.data)
}
