import React, { useRef} from 'react';
import { Authentication } from "./components/login/authentication";
import { List } from './components/list/List';
import './App.scss';
import './components/Header.scss';
import Types from './type';
import { Route, Switch, Redirect } from "react-router-dom";
import { ProjectView } from './components/element/ProjectView';
import { IssueView } from './components/element/IssueView';
import { MyContext, BaseProvider } from './Context.js';


const projects = [
    {
        "id": "3779a41b-7920-4f9c-b3aa-75d9302a1abe",
        "name": "Issue ",
        "description": "The quick brown fox  jumped over the lazy dog ",
        "state": "OPEN"
    }]
function App() {
    const sideRef = useRef();
    return (
        <BaseProvider>
            <div className="App">
                {<RouteRenderer sideRef={sideRef} />}
            </div>
        </BaseProvider>
    )
}

function RouteRenderer({ sideRef }) {
    return (
        <Switch>
            <Route exact path="/login">
                <MyContext.Consumer>
                    {(context) => {
                        return (!context.state.user && <Authentication sideRef={sideRef}></Authentication>)
                    }}
                </MyContext.Consumer>
            </Route>
            <Route exact path="/projects">
                {/*(context) => context.state.user &&*/ <List elemType={Types.project} > </List>}
            </Route>
            <Route path="/">
                <Redirect to="/login" />
                {/*(context) => context.state.user &&*/ <Redirect to="/projects" />}
                        )}
                    </Route>
        </Switch>
    )
}

export default App;