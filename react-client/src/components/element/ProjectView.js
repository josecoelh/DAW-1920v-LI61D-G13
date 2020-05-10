import React, { useEffect } from 'react'
import links from '../../links'
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';
import GPHeader from '../Header';
import types from '../../type';
import BasicForm from '../forms/BasicForm';






export class ProjectView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            labels : ['a','v','x'],/*fetch(links.projectLabels(element.id), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(res =>  res.json()).then(labels => {
            return labels.map(it => it.properties.identifier)
        });*/
            addForm : false

        }
    }


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
                addForm: this.state.addForm
            }
        )
    };

    onAddLabel = (e, label) =>{
        e.preventDefault();
        //FETCH
        let newLabels = this.state.labels;//test
        newLabels.push(label);//test
        this.setState(
            {
                labels: newLabels,//fetch
                addForm: false
            }
        )
    }
 
    
     
    render(){
    return (
        <div className="cont">
            <GPHeader></GPHeader>
            <div className="cardContainer">
                <Card style={{ width: '50rem', borderRadius: '5%' }}>
                    <Card.Body>
                        <Card.Title style={{ fontWeight: '900' }}>{this.props.element.name}</Card.Title>
                        <Card.Text>
                            {this.props.element.description}
                        </Card.Text>
                        <ListGroup as="ul">
                            <ListGroup.Item style={{ backgroundColor: '#2386c8', color: 'white' }} as="li" >
                                Labels <button className="addButton" onClick={(e) => {
                                                e.preventDefault()
                                                this.setState({
                                                    labels: this.state.labels,
                                                    addForm: true
                                                })}}></button>
                            </ListGroup.Item>
                            {this.state.labels &&
                            this.state.labels.map((it) => {
                                return <ListGroup.Item as="li">{it}
                                 <button type="button" className="delButton" onClick={(e) => { this.deleteLabels(e, it) }}></button></ListGroup.Item>
                            })}
                        </ListGroup>
                        <Button style={{ backgroundColor: '#2386c8' }} variant="primary" onClick={
                            () => { }
                        }>Issues</Button>
                    </Card.Body>
                </Card>
            </div>
            {this.state.addForm && <BasicForm formRef={types.label} onChange = 
                {((e, label)=>{this.onAddLabel(e,label)})}></BasicForm>}
        </div>
    )
}
}
