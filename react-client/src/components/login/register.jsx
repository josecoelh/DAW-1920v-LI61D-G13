import React from "react";
import logo from "../../img/logo.png";

export class Register extends React.Component{
    
    constructor(props){
        super(props);
    }

    render(){
        let username = null;
        let password = null;
        return(
        <div className="base-container" >
            <div className="header">Register</div>
            <div className="content">
                <div className="image">
                    <img src={logo}/>
                </div>
                <div className="form">
                    <div className="form-group"> 
                        <label htmlFor="username">Username</label>
                        <input type="text"   name = "username" onChange =  {(e)=>username = e.target.value} placeholder="username"/>
                    </div> 
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" name = "password" onChange =  {(e)=>password = e.target.value} placeholder="password"/>
                    </div> 
                </div>
            </div>
            <div className="footer">
                <button type = "button" 
                onClick = {(e)=>{
                    e.preventDefault();
                    alert(`${username} \n${password}`)
                }} 
                className="btn">Register</button>
            </div>
        </div>);
    }
}