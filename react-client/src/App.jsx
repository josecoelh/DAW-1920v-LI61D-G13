import React, {useRef} from 'react';
import { Authentication } from "./components/login/authentication";
import './App.scss';




 function App(){
     const sideRef = useRef(null);
    return (
           <Authentication sideRef = {sideRef}></Authentication>
        );
    }

export default App;

  

  
  


 






