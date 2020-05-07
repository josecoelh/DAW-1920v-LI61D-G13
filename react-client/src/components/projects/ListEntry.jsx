import React from 'react'

function ListEntry({onClick, name},ref){
     
    return (
        <a className="list-group-item list-group-item-action"
        onClick={onClick}
        data-toggle="list"
        href="#home"
        ref = {ref}
        role="tab">
        {name}</a>
        );
    }

export default React.forwardRef(ListEntry);