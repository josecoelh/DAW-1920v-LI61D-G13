import React from "react";
import ListEntry from "./ListEntry";
import ListTab from "./ListTab";
import GPHeader from "../Header";
import type from "../../type";
import ProjectForm from "../forms/ProjectForm";
import IssueForm from "../forms/IssueForm";
import links from "../../links";
import Spinner from 'react-bootstrap/Spinner';

export class List extends React.Component {
    constructor(props) {
        super(props);
        this.entries = [];
        this.API_BASE_URL = "http://localhost:8080";
        this.tabs = [];
        this.formRef = null;
        this.projectId = window.location.pathname.split("/")[3];
        this.state = {
            elements: []
        }
    }


    entryClick = (e, index) => {
        e.preventDefault()
        this.entries.forEach((element, i) => {
            if (element) {
                if (i === index) {
                    this.tabs[i].classList.add('active')
                    element.classList.add('active')
                }
                else {
                    this.tabs[i].classList.remove('active')
                    element.classList.remove('active');
                }
            }
        });
    }

    deleteEntry = (e, element) => {
        e.preventDefault();
        this.entries.length = 0;
        this.tabs.length = 0;
        this.deleteElements(element).then(() => {
            return setTimeout(() => { return this.getElements() }, 500)
        })

    }

    onAddElement = (e, element) => {
        e.preventDefault();
        this.formRef.classList.add("hidden");
        this.addElements(element).then(() => {
            return setTimeout(() => { return this.getElements() }, 500)
        })



    }

    getElements() {
        const linkBase = (this.props.elemType === type.project) ? links.projects : `${links.issue(this.projectId)}`;
        return fetch(linkBase, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json()).then(elems => {
            console.log(elems);
            return this.setState({ elements: elems })
        })
    }


    addElements(elem) {
        const linkBase = (this.props.elemType === type.project) ? links.projects : `${links.issue(this.projectId)}`;
        return fetch(linkBase, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: elem.name,
                description: elem.description,
            })
        }).then(res => res.json())
    }

    deleteElements(elem) {
        const action = elem.actions[1];
        return fetch(this.API_BASE_URL + action.href, {
            method: action.method,
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(res => res.json())
    }



    async componentDidMount() {
        this.getElements()
    }



    render() {


        if (!this.state.elements)
            return (
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>);

        const linkBase = this.props.elemType === type.projects ? links.projects : `${links.issue(this.projectId)}`;
        return (
            <div className="cont">
                <GPHeader></GPHeader>
                <div className="row">
                    <div className="col-4">
                        <div className="list-group" id="list-tab" role="tablist">
                            {this.state.elements.map((it, i) => {
                                return <ListEntry
                                    entryRef={ref => this.entries[i] = ref}
                                    onClick={(e) => { this.entryClick(e, i) }}
                                    name={it.properties.name}
                                    onDelete={(e) => { this.deleteEntry(e, it) }}
                                ></ListEntry>
                            })}
                        </div>
                    </div>
                    <div className="col-8">
                        <div className="tab-content" id="nav-tabContent">
                            {this.state.elements.map((it, i) => {
                                return <ListTab
                                    tabRef={ref => (this.tabs[i] = ref)}
                                    element={it.properties.description || it.properties.state}
                                    link={it.actions[2].href}
                                >
                                </ListTab>
                            })
                            }
                        </div>

                    </div>
                    <button className="addEntry" onClick={(e) => {
                        this.formRef.classList.toggle("hidden");
                    }}>+</button>

                </div>
                {this.props.elemType === type.project &&
                    <ProjectForm
                        onChange={((e, label) => { this.onAddElement(e, label) })}
                        formRef={ref => this.formRef = ref} />}

                {this.props.elemType === type.issue &&
                    <IssueForm
                        onChange={((e, label) => { this.onAddElement(e, label) })}
                        formRef={ref => this.formRef = ref} />}
            </div>

        );
    }
}

