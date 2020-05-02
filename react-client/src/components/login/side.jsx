import React from "react";




 function Side({onClick, current}, ref){
     
    return (
            <div
            className="side"
            ref = {ref}
            onClick={onClick}
            >
            <div className="inner-container">
                <div className="text"> {current} </div>
            </div>
            </div>
        );
    }

const fowardedRef = React.forwardRef(Side)
export default fowardedRef;