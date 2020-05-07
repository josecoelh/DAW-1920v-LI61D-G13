import React, { useRef, useEffect } from "react";
import logo from "../../img/logo.png";
import { Base64 } from 'js-base64'

function LoginInterface({ state }) {
    let username = null;
    let password = null;
    const usernameRef = useRef();
    const passwordRef = useRef();
    const buttonRef = useRef();
    function usernameDown(e) {
        if (e.key === "Enter") {
            passwordRef.current.focus();
        }
    }
    function passwordDown(e) {
        if (e.key === "Enter") {
            buttonRef.current.focus();
        }
    }
    function validationAndRedirect(validated,state) {
        if(validated) {
            //todo mega fetch
        } else {
            alert(`${state} failed`)
        }
    }
    useEffect(() => { usernameRef.current.focus() }, []);
    return (
        <div className="base-container" >
            <div className="type">{state}</div>
            <div className="content">
                <div className="image">
                    <img src={logo} />
                </div>
                <div className="form">
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input onKeyDown={usernameDown} ref={usernameRef} type="text" name="username" onChange={(e) => username = e.target.value} placeholder="username" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input onKeyDown={passwordDown} ref={passwordRef} type="password" name="password" onChange={(e) => password = e.target.value} placeholder="password" />
                    </div>
                </div>
            </div>
            <div className="footer">
                <button ref={buttonRef} type="button"
                    onClick={(e) => {
                        fetch(`http://localhost:8080/githubPremium/${state.toLowerCase()}`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Basic ${Base64.encode(`${username}:${password}`)}`
                            },
                        }).then(response => response.json()).then(validated => validationAndRedirect(validated,state.toLowerCase()))
                    }}
                    className="btn">{state}</button>
            </div>
        </div>);
}

export default LoginInterface;
