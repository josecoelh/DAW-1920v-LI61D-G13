import React from "react";
import links from "../../links";

function ProjectForm({formRef}) {
    let name;
    let desc;
    return (
        <div className="baseForm hidden" ref = {formRef}>
            <div className="base-container" >
                <div className="type">Add Project/*</div>
                <div className="content">
                    <div className="form">
                        <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input type="text" name="name" onChange={(e) => name = e.target.value} placeholder="Name" />
                        </div>
                        <div className="form-group">
                            <label htmlFor="description">Description</label>
                            <input type="text" name="description" onChange={(e) => desc = e.target.value} placeholder="Description" />
                        </div>
                    </div>
                </div>
                <div className="footer">
                    <button type="button"
                            onClick={(e) => {
                                e.preventDefault();
                                fetch(links.projects, {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json',
                                    },
                                    body : JSON.stringify({
                                        name : name
                                    })
                                }).then(response => response.json())
                            }}
                        className="submit">Submit</button>
                </div>
            </div>
        </div>);
}

export default ProjectForm;