import React, {useEffect, useRef} from 'react';
import './App.scss';
import  { Login, Register } from "./components/login/index"

function App(){
  let isLogginActive = null;
  useEffect(()=>{
    isLogginActive = true;
  }, [])
  const sideRef = useRef(null);
  const current = isLogginActive? "Register" : "Login";
  const currentActive = isLogginActive?  "Login" :"Register" ;

  const Side = props => {
    return (
      <div
        className="side"
        ref={sideRef}
        onClick={props.onClick}
      >
        <div className="inner-container">
          <div className="text"> {props.current} </div>
        </div>
      </div>
    );
  };
  
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
            onClick={sideClick.bind(this)}
          />
        </div>
      </div>
    );

}




export default App;
