import React, { useRef, useEffect, createContext } from "react";
import logo from "../../img/logo.png";
import { Base64 } from 'js-base64';
import links from "../../links";
import { Redirect } from "react-router-dom";
import { MyContext } from "../../Context.js";

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
    function validationAndRedirect(validated, state, username,context) {
        if (validated) {
            context.setUser(username);
            window.location = '/projects';
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
                    <img src={logo} alt="logo" />
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
                <MyContext.Consumer>
                    {(context) => {
                        return(
                        <button ref={buttonRef} type="button"
                            onClick={(e) => {
                                fetch(`${links.base}${state.toLowerCase()}`, {
                                    method: 'PUT',
                                    credentials: 'include',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Authorization': `Basic ${Base64.encode(`${username}:${password}`)}`
                                    },
                                }).then(response => response.json()).then(validated => validationAndRedirect(validated, state.toLowerCase(), { username },context))
                            }}
                            className="btn">{state}</button>)
                    }}
                </MyContext.Consumer>
            </div>
        </div>);
}

export default LoginInterface;