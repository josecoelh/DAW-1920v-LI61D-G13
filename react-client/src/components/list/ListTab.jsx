import React from 'react'



function ListTab({ element }, ref) {

    return (
            <div className="tab-pane"
                ref={ref}
                role="tabpanel"> {
                    element.split('\n').map((text, index) => (
                        <React.Fragment key={`${text}-${index}`}>
                            {text}
                            <br />
                        </React.Fragment>
                    ))}
            </div>
        );
}
export default React.forwardRef(ListTab);