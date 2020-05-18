import React, { useEffect } from 'react'
import links from '../../links'
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';
import Spinner from 'react-bootstrap/Spinner';
import GPHeader from '../Header';
import types from '../../type';
import BasicForm from '../forms/BasicForm';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';






export class IssueView extends React.Component {
    constructor(props) {
        super(props);
        this.API_BASE_URL = "http://localhost:8080";
        this.projectId = window.location.pathname.split("/")[3];
        this.issueId = window.location.pathname.split("/")[5];
        this.state = {
            comments: null,
            labels: null,
            element: null,
            addForm: null,
            state: "OPEN"
        }
    }

     getElement() {
        fetch(this.API_BASE_URL + window.location.pathname, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }
        ).then(res => res.json()).then(it => this.setState({ element: it })
        )
    }


    addLabels(label){
        return fetch(`${links.issueLabels(this.projectId,this.issueId)}/${label}`, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },

        }).then(res => res.json()).catch( alert("label not allowed"))
    }

    deleteLabels(label){
        const action = label.actions[0];
        return fetch((this.API_BASE_URL + action.href), {
            method: action.method,
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json())
    }

    addComments(comment){
        return fetch(links.issueComments(this.projectId,this.issueId), {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body : JSON.stringify( {
                 "value" : comment
            })
        }).then(res => res.json())
    }

    deleteComments(comment){
        const action = comment.actions[0];
        return fetch((this.API_BASE_URL + action.href), {
            method: action.method,
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json())
    }

    getLabels() {
        fetch(links.issueLabels(this.projectId,this.issueId), {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }
        ).then(res => res.json()).then(labelSiren => this.setState({ labels: labelSiren })
        )
    }

    getComments(){
        fetch(links.issueComments(this.projectId,this.issueId), {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }
        ).then(res => res.json()).then(comments => this.setState({ comments: comments })
        )
    }


    async componentDidMount() {
        this.getElement();
        this.getLabels();
        this.getComments();
        
    }         


    onAddLabel = (e, label) => {
        e.preventDefault();
       this.addLabels(label).then(()=> setTimeout(()=> this.getLabels(),500))
    }

    onAddComment = (e, comment) => {
        e.preventDefault();
        this.addComments(comment).then(()=> setTimeout(()=> this.getComments(),500))
    }

    onDeleteComment = (e, comment) => {
        e.preventDefault();
        this.deleteComments(comment).then(()=> setTimeout(()=> this.getComments(),500))
    };

    onDeleteLabels = (e, label) => {
        e.preventDefault();
        this.deleteLabels(label).then(()=> setTimeout(()=> this.getLabels(),500))
    };

    render() {


        if (!this.state.element || !this.state.comments || !this.state.labels)
            return (
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>);



        return (
            <div className="cont">
                <GPHeader></GPHeader>
                <div className="cardContainer">
                    <Card style={{ width: '50rem', borderRadius: '5%' }}>
                        <Card.Body>
                            <Card.Title style={{ fontWeight: '900' }}>{this.state.element.properties.name}</Card.Title>
                            <Card.Text  >
                                {this.state.state}
                                <DropdownButton id="dropdown-basic-button" title="set state">
                                    {this.state.state !== 'OPEN' &&
                                        <Dropdown.Item
                                            onClick={(e => {
                                                this.setState({
                                                    state: 'OPEN'
                                                })
                                            })}>OPEN
                                         </Dropdown.Item>}
                                    {this.state.state !== 'CLOSED' && <Dropdown.Item onClick={(e => {
                                        this.setState({
                                            state: 'CLOSED'
                                        })
                                    })}>CLOSED</Dropdown.Item>}
                                    {this.state.state !== 'ARCHIVED' && <Dropdown.Item onClick={(e => {
                                        this.setState({
                                            state: 'ARCHIVED'
                                        })
                                    })}>ARCHIVED</Dropdown.Item>}
                                </DropdownButton>
                            </Card.Text>
                            <ListGroup as="ul">
                                <ListGroup.Item style={{ backgroundColor: '#2386c8', color: 'white' }} as="li" >
                                    Labels
                                <button className="addButton" onClick={(e) => {
                                        e.preventDefault()
                                        this.setState({
                                            addForm: types.label
                                        })
                                    }}></button>
                                </ListGroup.Item>
                                {this.state.labels &&
                                    this.state.labels.map((it) => {
                                        return <ListGroup.Item as="li">{it.properties.identifier}
                                            <button type="button" className="delButton" onClick={(e) => { this.onDeleteLabels(e, it) }}></button>
                                        </ListGroup.Item>
                                    })}
                            </ListGroup>
                            <ListGroup as="ul">
                                <ListGroup.Item style={{ backgroundColor: '#2386c8', color: 'white' }} as="li" >
                                    Comments<button className="addButton" onClick={(e) => {
                                        e.preventDefault()
                                        this.setState({
                                            comments: this.state.comments,
                                            labels: this.state.labels,
                                            addForm: types.comment
                                        })
                                    }}></button>
                                </ListGroup.Item>
                                {this.state.comments &&
                                    this.state.comments.map((it) => {
                                        return <ListGroup.Item as="li">{it.properties.date}: {it.properties.value}
                                            <button type="button" className="delButton" onClick={(e) => { this.onDeleteComment(e, it) }}></button>
                                        </ListGroup.Item>
                                    })}
                            </ListGroup>

                        </Card.Body>
                    </Card>
                </div>
                {this.state.addForm === types.label && <BasicForm formRef={this.state.addForm} onChange=
                    {((e, label) => { this.onAddLabel(e, label) })}></BasicForm>}
                {this.state.addForm === types.comment && <BasicForm formRef={this.state.addForm} onChange=
                    {((e, comment) => { this.onAddComment(e, comment) })}></BasicForm>}
            </div>
        );
    }
}

      