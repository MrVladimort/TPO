import React from 'react';
import {Route} from 'react-router-dom';
import 'semantic-ui-css/semantic.min.css';

import HomePage from "./components/pages/HomePage";

import './assets/css/default.min.css';

const App = () => (
    <div className="reactBody">
        <div className="content">
            <Route path="/" exact component={HomePage}/>
        </div>
    </div>
);


export default App;