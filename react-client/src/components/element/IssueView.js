import React, { useEffect } from 'react'
import links from '../../links'
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';
import GPHeader from '../Header';
import types from '../../type';
import BasicForm from '../forms/BasicForm';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';






export class IssueView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comments: [{ id: 1, value: 'abc', date: "12-3-69" }, { id: 2, value: 'kappa', date: "12-3-69" }, { id: 3, value: 'bitch', date: "12-3-69" }],
            /*fetch(links.issueComments(element.id), {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res =>  res.json()).then(comments => {
                return comments.map(it => {{id : it.properties.id ,value : it.properties.value, date: it.properties.date}})
             });*/
            labels: ['a', 'v', 'x'],/*fetch(links.projectLabels(element.id), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(res =>  res.json()).then(labels => {
            return labels.map(it => it.properties.identifier)
        });*/
            addForm: null,
            state : this.props.element.state
        }
    }



    onAddLabel = (e, label) => {
        e.preventDefault();
        //FETCH
        let newLabels = this.state.labels;//test
        newLabels.push(label);//test
        this.setState(
            { //fetch
                labels: newLabels,
                addForm: null
            }
        )
    }

    onAddComment = (e, comment) => {
        e.preventDefault();
        //FETCH
        let newComm = this.state.comments;//test
        newComm.push({ id: 2, date: '12', value: comment });//test
        this.setState(
            {
                comments: newComm,//fetch
                addForm: null
            }
        )
    }

    deleteComment = (e, comment) => {
        e.preventDefault();
        /*fetch(links.comment(projectId,element.id,comment.id), {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })*/
        this.setState(
            {
                comments: this.state.comments.filter(it => it.id !== comment.id),
            }
        )
    };

    deleteLabels = (e, label) => {
        e.preventDefault();
        /*fetch(links.issueLabels(projectId,element.id,comment.id), {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })*/
        this.setState(
            {
                labels: this.state.labels.filter(it => it !== label),
            }
        )
    };

    render() {
        return (
            <div className="cont">
                <GPHeader></GPHeader>
                <div className="cardContainer">
                    <Card style={{ width: '50rem', borderRadius: '5%' }}>
                        <Card.Body>
                            <Card.Title style={{ fontWeight: '900' }}>{this.props.element.name}</Card.Title>
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
                                            comments: this.state.comments,
                                            labels: this.state.labels,
                                            addForm: types.label
                                        })
                                    }}></button>
                                </ListGroup.Item>
                                {this.state.labels &&
                                    this.state.labels.map((it) => {
                                        return <ListGroup.Item as="li">{it}
                                            <button type="button" className="delButton" onClick={(e) => { this.deleteLabels(e, it) }}></button>
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
                                        return <ListGroup.Item as="li">{it.date}: {it.value}
                                            <button type="button" className="delButton" onClick={(e) => { this.deleteComment(e, it) }}></button>
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
