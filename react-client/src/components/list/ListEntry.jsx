import React from 'react'

function ListEntry({onClick, name},ref){
     
    return (
        
        <a className="list-group-item list-group-item-action"
        onClick={onClick}
        href = {name}
        data-toggle="list"
        ref = {ref}
        role="tab">
        {name}</a>
        );
    }

export default React.forwardRef(ListEntry);