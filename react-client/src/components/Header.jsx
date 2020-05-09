import React, {useRef} from 'react';
import logo from "../img/logo.png";
import logout from "../img/logout.png";
import links from "../links"
function GPHeader({ username }) {

  
    const navClick = (e, isProj) => {
        e.preventDefault()
        const link = isProj? links.projects : links.issues
        fetch(link, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        
    }

    return ( 
        <div className="header" >
            <img src={logo} alt='left' />
            <button className="nav" onClick = {()=>{console.log('fetching projects')}/*(e) => navClick(e, isProj = true)*/}>Projects</button>
            <button className="nav" onClick = {()=>console.log('fetching issues')/*(e) => navClick(e, isProj = false)*/}>Issues</button>
            <h1 className = "username">Repd</h1>
            <div className="dropdown">
                <input type="image" className="dropbtn" src={logout} alt="logout" ></input>
                <div className="dropdown-content">
                    <h1 className = "logout" onClick={(e) => {
                         e.preventDefault()
                        fetch(`${links.logout}`, {
                            method: 'PUT',
                        }).then(response => response.json())
                    }}> Logout</h1>
                </div>
            </div>
        </div>
    );
}

export default GPHeader;