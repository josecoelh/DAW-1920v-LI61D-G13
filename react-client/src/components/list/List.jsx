import React, { createRef } from "react";
import ListEntry from "./ListEntry";
import ListTab from "./ListTab";
import GPHeader from "../Header"

function List({ elements }) {

    const arrLength = elements.length
    const entryRefs = React.useRef([]);
    if (entryRefs.current.length !== arrLength) {
        entryRefs.current = Array(arrLength).fill().map((_, i) => entryRefs.current[i] || createRef());
    }
    const tabRefs = React.useRef([]);
    if (tabRefs.current.length !== arrLength) {
        tabRefs.current = Array(arrLength).fill().map((_, i) => tabRefs.current[i] || createRef());
    }


    const entryClick = (e, index) => {
        e.preventDefault()
        entryRefs.current.forEach((element, i) => {
            if (i === index) {
                tabRefs.current[i].current.classList.add('active')
                element.current.classList.add('active')
            }
            else {
                tabRefs.current[i].current.classList.remove('active')
                element.current.classList.remove('active');
            }
        });
    }

    function elementStringify(element) {
        const keys = Object.keys(element);
        let toRet = "";
        keys.forEach(it => toRet += `${it}: ${element[it]} \n`)
        return toRet;
    }
    return (
        <div className="cont">
            <GPHeader></GPHeader>
            <div className="row">
                <div class="col-4">
                    <div class="list-group" id="list-tab" role="tablist">
                        {elements.map((it, i) => { return <ListEntry ref={entryRefs.current[i]} onClick={(e) => entryClick(e, i)} name={it.name}></ListEntry> })}
                    </div>
                </div>
                <div class="col-8">
                    <div class="tab-content" id="nav-tabContent">
                        {elements.map((it, i) => { return <ListTab ref={tabRefs.current[i]} element={elementStringify(it)}></ListTab> })
                        }
                    </div>
                </div>
            </div>
        </div>

    );
}
export default List;