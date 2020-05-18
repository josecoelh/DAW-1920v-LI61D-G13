import React, { useRef } from 'react';
import { Authentication } from "./components/login/Authentication";
import { List } from './components/list/List';
import './App.scss';
import './components/Header.scss'
import type from './type';
import { ProjectView } from './components/element/ProjectView'
import { IssueView } from './components/element/IssueView'



const projects = [
    {
        "id": "3779a41b-7920-4f9c-b3aa-75d9302a1abe",
        "name": "Issue ",
        "description" : "The quick brown fox  jumped over the lazy dog ",
        "state": "OPEN"
    }]
function App() {
    const sideRef = useRef();
    return (
       //<IssueView element = {projects}></IssueView>
        //<ProjectView element = {projects}/>
        <List elements={projects} elemType={type.project}></List>
        /*<Authentication
            sideRef={sideRef}>
        </Authentication>*/
    );
}

export default App;














