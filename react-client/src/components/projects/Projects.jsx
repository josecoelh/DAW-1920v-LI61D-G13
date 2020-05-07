import React, {useRef} from "react";
import ReactDOM from 'react-dom';
import ListEntry from "./ListEntry";

function Projects({projects}) {

    const tabClick = (e) => {
        e.preventDefault()
        this.ref.current.classList.add('active')
    }
        return (
            <div className="row">
                <div className="list-group" id="myList" role="tablist">
                    
                    {projects.map(element,index => 
                        <ListEntry ref = {element.ref} onClick = {this.tabClick} name = {element.project.name}></ListEntry>
                    )}
                </div>
                <div className="tab-content">
                    <div className="tab-pane" id="home" role="tabpanel">...</div>
                    <div className="tab-pane" id="profile" role="tabpanel">...</div>
                    <div className="tab-pane" id="messages" role="tabpanel">...</div>
                    <div className="tab-pane" id="settings" role="tabpanel">...</div>
                </div>

            </div>
        );
    }
    export default  Projects;