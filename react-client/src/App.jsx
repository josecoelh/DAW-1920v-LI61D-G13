import React, { useRef } from 'react';
import { Authentication } from "./components/login/Authentication";
import {List} from './components/list/List';
import './App.scss';
import './components/Header.scss'
import type from './type';



const projects = [{name : 'project1', description : 'cool Project'},{name : 'project2', description : 'cool Project'},{name : 'project3', description : 'cool Project'},{name : 'project1', description : 'cool Project'}]
function App() {
    const sideRef = useRef();
    return (
      
        <List elements = {projects} elemType = {type.issue}></List>
        /*<Authentication
            sideRef={sideRef}>
        </Authentication>*/
    );
}

export default App;














