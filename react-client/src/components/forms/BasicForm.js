import React from "react";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';


function BasicForm({formRef, onChange}) {
let text = null;

return(
    <div className="formCont">
<Form>
  <Form.Group controlId="formBasicText">
    <Form.Label>{formRef} to add</Form.Label>
    <Form.Control type="text" onChange = {(e) => text = e.target.value }/>
  </Form.Group>
  <Button  onClick = {(e ) => {onChange(e,text)}} variant="primary" type="submit">
    Submit
  </Button>
</Form></div>
);
}
export default BasicForm;
