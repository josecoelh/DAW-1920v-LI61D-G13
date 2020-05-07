import React, { useRef } from 'react';
import { Authentication } from "./components/login/Authentication";
import List  from "./components/list/List";
import './App.scss';
import './components/Header.scss'



const projects = [{name : 'project1', description : 'cool Project'},{name : 'project1', description : 'cool Project'},{name : 'project1', description : 'cool Project'},{name : 'project1', description : 'cool Project'}]
function App() {
    const sideRef = useRef();
   
   
    return (
        
        <List elements = {projects}></List>
        /*<Authentication
            sideRef={sideRef}>
        </Authentication>*/
    );
}

export default App;














