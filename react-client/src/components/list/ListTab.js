import React from 'react'



function ListTab({ element, tabRef, link }) {

    return (
        <div className="tab-pane"
            ref={tabRef}
            role="tabpanel"> {
                element.split('\n').map((text, index) => (
                    <React.Fragment key={`${text}-${index}`}>
                        {text}
                        <br />
                    </React.Fragment>
                ))}
            <button className="btnDetail" 
            onClick={(e) => {
                e.preventDefault();
                fetch(`${link}/${element.id}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                   }).then(response => response.json())
            }}
            >🠚</button>
        </div>
    );
}
export default ListTab;