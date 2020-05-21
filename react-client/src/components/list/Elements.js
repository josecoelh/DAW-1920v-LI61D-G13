import links from "../../links";
import type from "../../type";


export  {getElements , addElements}

    function getElements(elemType) {
        const linkBase = elemType === type.projects ? links.projects : links.issues;
        return fetch(linkBase, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('codedUser')
            }
        }).then(res => res.json())
    }

    function addElements(elemType, elem){

        const linkBase = elemType === type.projects ? links.projects : links.issues;
        return fetch(linkBase, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('codedUser')
            }
        }).then(res => res.json())
    }

