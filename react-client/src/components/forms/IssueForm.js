import React from "react";
import links from "../../links";

function issueForm({formRef, onChange}) {
    let name;
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
                    </div>
                </div>
                <div className="footer">
                    <button type="button"
                        onClick = {(e ) => {onChange(e,{name : name, state : 'OPEN'})}}
                        className="submit">Submit</button>
                </div>
            </div>
        </div>);
}

export default issueForm;