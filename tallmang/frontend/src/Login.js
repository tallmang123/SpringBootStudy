import React,{Fragment} from 'react';
import axios from 'axios';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import md5 from 'md5'

class Login extends React.Component{
	constructor(props)
	{
		super(props);
		this.state = 
		{
			id : '',
			passwd : ''
		}
		//this.ajaxLogin = this.ajaxLogin.bind(this); -> replace arrow function
	}
	updateState = (e) => {
        this.setState({
            [e.target.name] : e.target.value 
        });

    }
	
	
	ajaxLogin = (e) =>{
        console.log(md5(this.state.passwd));
		let url = '/test';
		let options = {
		            method: 'POST',
		            url: url,
		            headers: 
		            {
		                'Accept': 'application/json',
		                'Content-Type': 'application/json;charset=UTF-8'
		            },
		            data: 
		            {
		            	id : this.state.id,
		            	passwd : this.state.passwd
		              
		            }
		        };
		let response = axios(options);
		let responseOK = response && response.status === 200 && response.statusText === 'OK';
		if (responseOK) {
		    let data = response.data;
		    console.log(data);
		    // do something with data
		}
	}

	render() {
		return (    		
    		<Fragment>
    			<br/><br/><br/><br/><br/><br/>
    			<Container>
    				<Row>
    					<Col sm></Col>
    					<Col sm>
    						<Form>
    							<Form.Group controlId="formBasicEmail">
    								<Form.Label>Email address</Form.Label>
    								<Form.Control type="text" placeholder="Enter Id" name="id" onChange={this.updateState}/>
    								<Form.Text className="text-muted">We'll never share your email with anyone else.</Form.Text>
    							</Form.Group>
    							<Form.Group controlId="formBasicPassword">
    								<Form.Label>Password</Form.Label>
    								<Form.Control type="password" placeholder="Password" name="password" onChange={this.updateState}/>
    							</Form.Group>
    							<Form.Group controlId="formBasicCheckbox">
    								<Form.Check type="checkbox" label="Auto Login" />
    							</Form.Group>
    							<Button variant="primary" onClick={this.ajaxLogin}>Submit</Button>
    						</Form>
    					</Col>
    					<Col sm></Col>
    				</Row>
    			</Container>
    		</Fragment>
		);
	}
}

export default Login;
