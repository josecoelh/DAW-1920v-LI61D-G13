import React from 'react'

function ListEntry({ onClick, name, entryRef ,onDelete}) {

    return (
        
        <a className="list-group-item list-group-item-action"
            onClick={onClick}
            href={name}
            data-toggle="list"
            ref= {entryRef}
            role="tab">
            {name} <button type="button" className="delButton" onClick= {onDelete}></button>  
               </a>
            
    );
}

export default ListEntry;