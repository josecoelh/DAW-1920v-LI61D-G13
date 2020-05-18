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
                window.location = link;
            }}
            >🠚</button>
        </div>
    );
}
export default ListTab;