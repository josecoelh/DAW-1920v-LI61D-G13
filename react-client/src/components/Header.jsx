import React from 'react';
import logo from "../img/logo.png";
import logout from "../img/logout.png";
function GPHeader({ username }) {
    return (
        <div className="header" >
            <img src={logo} alt='left' />
            <div class="dropdown">
               <input type = "image"  class="dropbtn" src = {logout} alt = "logout" ></input>
                <div class="dropdown-content">
                    <a onClick={(e) => {
                        fetch(`http://localhost:8080/githubPremium/logout`, {
                            method: 'PUT',
                        }).then(response => response.json())
                    }}> Logout</a>
                </div>
            </div>
        </div>
    );
}

export default GPHeader;