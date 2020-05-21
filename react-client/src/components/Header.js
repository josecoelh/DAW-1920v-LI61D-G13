import React, {useRef} from 'react';
import logo from "../img/logo.png";
import logout from "../img/logout.png";
import links from "../links"
import Cookies from 'js-cookie';

function GPHeader({ username }) {

  


    return ( 
        <div className="header" >
            <img src={logo} alt='left' />
            <button className="nav" onClick = {()=>{
                window.location = "/githubPremium/projects";
                }}>Projects</button>
            <h1 className = "username">{sessionStorage.getItem('username')}</h1>
            <div className="dropdown">
                <input type="image" className="dropbtn" src={logout} alt="logout" ></input>
                <div className="dropdown-content">
                    <h1 className = "logout" onClick={(e) => {
                         e.preventDefault()
                        fetch(`${links.logout}`, {
                            method: 'PUT',
                        }).then(()=>{sessionStorage.removeItem('username')}).then(()=> window.location = '/');
                    }}> Logout</h1>
                </div>
            </div>
        </div>
    );
}

export default GPHeader;