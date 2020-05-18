import React, { useRef } from 'react';
import { Authentication } from "./components/login/authentication";
import { List } from './components/list/List';
import './App.scss';
import './components/Header.scss';
import Types from './type';
import { Route, Switch, Redirect } from "react-router-dom";
import { ProjectView } from './components/element/ProjectView';
import { IssueView } from './components/element/IssueView';
import Cookies from 'js-cookie';



function App() {
    const sideRef = useRef();
    return (
    <RouteRenderer sideRef={sideRef}/>
    )
}

function RouteRenderer({ sideRef }) {
    return (
        <Switch>
            <Route exact path="/githubPremium/login">
                {Cookies.get('username') ? <Redirect to="/githubPremium/projects" /> : <Authentication sideRef={sideRef}></Authentication>}
            </Route>
            <Route exact path="/githubPremium/projects/:projectId/issues">
                {Cookies.get('username') ? <List elemType={Types.issue} > </List> : <Redirect to="/githubPremium/login" />}
            </Route>
            <Route exact path="/githubPremium/projects/:projectId/issues/:issueId">
                {Cookies.get('username') ?<IssueView /> : <Redirect to="/githubPremium/login" />}
            </Route>
            <Route exact path="/githubPremium/projects">
                {Cookies.get('username') ? <List elemType={Types.project} > </List> : <Redirect to="/githubPremium/login" />}
            </Route>
            <Route exact path="/githubPremium/projects/:projectId">
                {Cookies.get('username') ? <ProjectView /> : <Redirect to="/githubPremium/login" />}
            </Route>
            <Route path="/">
                <Redirect to="/githubPremium/login" />
            </Route>
        </Switch>
    )
}
export default App;