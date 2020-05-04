import React, { useRef,useEffect } from 'react';
import { Authentication } from "./components/login/Authentication";
import './App.scss';




function App() {
    const sideRef = useRef(null);
    
    return (
        <Authentication
            sideRef={sideRef}>
        </Authentication>
    );
}

export default App;














