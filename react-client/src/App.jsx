import React, { useRef,useEffect } from 'react';
import { Authentication } from "./components/login/Authentication";
import Projects  from "./components/projects/Projects";
import './App.scss';



const projectsw = [{name : 'project1'},{name : 'project2'}]
function App() {
    const sideRef = useRef(null);
    const projectsA = projectsw.map(element=>element.ref = useRef(null))
    return (
        <Projects projects = {projectsA}></Projects>
        /*<Authentication
            sideRef={sideRef}>
        </Authentication>*/
    );
}

export default App;














