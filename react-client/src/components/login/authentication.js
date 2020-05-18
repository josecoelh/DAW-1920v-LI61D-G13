import React from 'react';
import  LoginInterface from "./LoginInterface";
import   Side  from "./Side";
import "./style.scss";

export class Authentication extends React.Component{
  constructor(props){
        super(props);
        this.state = {
           isLogginActive : false
        }
    }
    render(){
    const sideRef = this.props.sideRef;
    const sideText = this.state.isLogginActive? "Register" : "Login";

      const sideClick = function changeState(){
        if (this.state.isLogginActive) {
          sideRef.current.classList.remove("right");
          sideRef.current.classList.add("left");
        } else {
          sideRef.current.classList.remove("left");
          sideRef.current.classList.add("right");
        }
        this.setState( {
          isLogginActive : !this.state.isLogginActive
        })
      }

    
    //const current = this.state.isLogginActive? "Login" : "Register";
    
       return(
      <div className="App">
        <div className="login">
          <div className="container" >
            {this.state.isLogginActive && <LoginInterface state = "Login"
              />}
            {!this.state.isLogginActive && <LoginInterface state = "Register"
              />}
          </div>
          <Side
            current={sideText}
            ref = {sideRef}
            onClick={sideClick.bind(this)}
          />
        </div>
      </div>
    );
    }
  }