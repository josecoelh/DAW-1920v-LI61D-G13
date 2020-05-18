import React from 'react';


const MyContext = React.createContext();
class BaseProvider extends React.Component {
    constructor(props){
        super(props);
        this.state = {
           user : localStorage.getItem( 'user' ) || null
        }
    }
    render() {
        return (
            <MyContext.Provider
                value={{
                    state: this.state,
                    setUser: (user) => this.setState({ user: user }),
                    clearUser: () => this.setState({ user: null })
                }}>{this.props.children}</MyContext.Provider>)
    }
}

export {MyContext,
        BaseProvider}