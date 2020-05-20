import React from 'react';
import links from '../../links';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';
import Spinner from 'react-bootstrap/Spinner';
import GPHeader from '../Header';
import types from '../../type';
import BasicForm from '../forms/BasicForm';






export class ProjectView extends React.Component {
    constructor(props) {
        super(props);
        this.API_BASE_URL = "http://localhost:8080";
        this.state = {
            labels: [],
            element: null,
            addForm: false

        }
    }


    onDeleteLabels = (e, label) => {
        e.preventDefault();
        this.deleteLabels(label).then( ()=>{
            return setTimeout(()=>{ return this.getLabels()},500)
        })
    };

    onAddLabel = (e, label) => {
        e.preventDefault();
        this.addLabels(label).then( ()=>{
            return setTimeout(()=>{ return this.getLabels()},500)
        })
    }

    getElement() {
        fetch(this.API_BASE_URL + window.location.pathname, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }
        ).then(res =>res.json()).then(it => this.setState({ element: it })
        )
    }


    addLabels(label){
        return fetch(links.projectLabels(window.location.pathname.split("/")[3]), {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body : JSON.stringify( [
                 label
            ])
        }).then(res => res.json())
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

    getLabels() {
        fetch(links.projectLabels(window.location.pathname.split("/")[3]), {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }
        ).then(res => res.json()).then(labelSiren => this.setState({ labels: labelSiren })
        )
    }


    async componentDidMount() {
        this.getElement();
        this.getLabels();
        
    }

    render() {
        if (!this.state.element || !this.state.labels)
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
                            <Card.Text>
                                {this.state.element.properties.description}
                            </Card.Text>
                            <ListGroup as="ul">
                                <ListGroup.Item style={{ backgroundColor: '#2386c8', color: 'white' }} as="li" >
                                    Labels <button className="addButton" onClick={(e) => {
                                        e.preventDefault()
                                        
                                        this.setState({
                                            labels: this.state.labels,
                                            addForm: true
                                        })
                                    }}></button>
                                </ListGroup.Item>
                                {this.state.labels &&
                                    this.state.labels.map((it) => {
                                        return <ListGroup.Item as="li">{it.properties.identifier}
                                            <button type="button" className="delButton" onClick={(e) => { this.onDeleteLabels(e, it) }}></button></ListGroup.Item>
                                    })}
                            </ListGroup>
                            <Button style={{ backgroundColor: '#2386c8' }} variant="primary" onClick={
                                () => { window.location = this.state.element.entities[0].href  }
                            }>Issues</Button>
                        </Card.Body>
                    </Card>
                </div>
                {this.state.addForm && <BasicForm formRef={types.label} onChange=
                    {((e, label) => { this.onAddLabel(e, label) })}></BasicForm>}
            </div>
        )
    }
}
