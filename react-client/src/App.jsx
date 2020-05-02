import React, {useEffect, useRef} from 'react';
import './App.scss';
import  { Login, Register } from "./components/login/index"
import   Side  from "./components/login/side"

function App(){
  let isLogginActive = null;
  useEffect(()=>{
    isLogginActive = true;
  }, [])
  const sideRef = useRef(null);
  const current = isLogginActive? "Register" : "Login";

  
  
  const sideClick = function changeState(){
    if (isLogginActive) {
      sideRef.current.classList.remove("right");
      sideRef.current.classList.add("left");
    } else {
      sideRef.current.classList.remove("left");
      sideRef.current.classList.add("right");
    }
    isLogginActive = !isLogginActive 
  }

  return(
      <div className="App">
        <div className="login">
          <div className="container" >
            {isLogginActive && <Login  />}
            {!isLogginActive && <Register />}
          </div>
          <Side
            current={current}
            ref = {sideRef}
            onClick={sideClick.bind(this)}
          />
        </div>
      </div>
    );

}




export default App;
