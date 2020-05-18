import React from "react";
import ListEntry from "./ListEntry";
import ListTab from "./ListTab";
import GPHeader from "../Header";
import type from "../../type";
import ProjectForm from "../forms/ProjectForm";
import IssueForm from "../forms/IssueForm";
import links from "../../links";

export class List extends React.Component {
    constructor(props) {
        super(props);
        this.entries = [];
        this.tabs = [];
        this.formRef = null;
        this.state = {
            elements: this.props.elements
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
        fetch(`links.projects${element.id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        this.setState({
            elements: this.state.elements.filter(it => it !== element)
        })
    }

    onAddElement = (e, element) =>{
        e.preventDefault();
        //FETCH
        let newElems = this.state.elements;//test
        newElems.push(element);//test
        this.formRef.classList.add("hidden");
        this.setState(
            {
                elements: newElems
            }
        )
    } 


    elementStringify(element) {
        const keys = Object.keys(element);
        let toRet = "";
        keys.forEach(it => toRet += `${it}: ${element[it]} \n`)
        return toRet;
    }

    render() {
        const linkBase = this.props.elemType === type.projects ? links.projects : links.issues;
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
                                    name={it.name}
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
                                    element={this.elementStringify(it)}
                                    link={linkBase}
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
                        onChange = {((e, label)=>{this.onAddElement(e,label)})}
                        formRef={ref => this.formRef = ref} />}
            </div>

        );
    }
}

