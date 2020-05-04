import React, {useRef,useEffect} from "react";
import logo from "../../img/logo.png";

function LoginInterface({state}) {
        let username = null;
        let password = null;
        const usernameRef = useRef(null);
        const passwordRef = useRef(null);
        const buttonRef = useRef(null);
        function usernameDown(e) {
            if(e.key === "Enter") {
                passwordRef.current.focus();
            }
        }
        function passwordDown(e) {
            if(e.key === "Enter") {
                buttonRef.current.focus();
            }
        }
        useEffect(()=> {usernameRef.current.focus()},[]);
        return(
        <div className="base-container" >
            <div className="header">{state}</div>
            <div className="content">
                <div className="image">
                    <img src={logo}/>
                </div>
                <div className="form">
                    <div className="form-group"> 
                        <label htmlFor="username">Username</label>
                        <input onKeyDown = {usernameDown} ref = {usernameRef} type="text"   name = "username" onChange =  {(e)=>username = e.target.value} placeholder="username"/>
                    </div> 
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input onKeyDown = {passwordDown} ref = {passwordRef} type="password" name = "password" onChange =  {(e)=>password = e.target.value} placeholder="password"/>
                    </div> 
                </div>
            </div>
            <div className="footer">
                <button ref = {buttonRef} type = "button" 
                onClick = {(e)=>{
                    e.preventDefault();
                    alert(`${username} \n${password}`)
                }} 
                className="btn">{state}</button>
            </div>
        </div>);
}

export default LoginInterface;
