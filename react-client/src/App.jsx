import React from 'react';
import './App.scss';
import  { Login, Register } from "./components/login/index"

class App extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      isLogginActive : true
    }
  }

  changeState(){
    const {isLogginActive} = this.state;
    if (isLogginActive) {
      this.side.classList.remove("right");
      this.side.classList.add("left");
    } else {
      this.side.classList.remove("left");
      this.side.classList.add("right");
    }
    this.setState(prevState => ({ isLogginActive: !prevState.isLogginActive })); 
  }

  render(){
    const {isLogginActive} = this.state;
    const current = isLogginActive? "Register" : "Login";
    const currentActive = isLogginActive?  "Login" :"Register" ;

    return (
      <div className="App">
        <div className="login">
          <div className="container" ref={ref => (this.container = ref)}>
            {isLogginActive && <Login  />}
            {!isLogginActive && <Register />}
          </div>
          <Side
            current={current}
            currentActive={currentActive}
            containerRef={ref => (this.side = ref)}
            onClick={this.changeState.bind(this)}
          />
        </div>
      </div>
    );
  }
}

const Side = props => {
  return (
    <div
      className="side"
      ref={props.containerRef}
      onClick={props.onClick}
    >
      <div className="inner-container">
        <div className="text"> {props.current} </div>
      </div>
    </div>
  );
};

export default App;
