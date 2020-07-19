import React,{Fragment} from 'react';
import axios from 'axios';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import md5 from 'md5'

class Login extends React.Component{
	constructor(props)
	{
		super(props);
		this.state = 
		{
			userId : '',
			password : '',
			autoLogin: false,
			errorMsg : ''
		}
	}
	updateState = (e) => {
        this.setState({
            [e.target.name] : e.target.value 
        });

    }

    checkboxHandle = (e) => {
        const { target: { checked } } = e;
        this.setState({ autoLogin : checked});
    };

	ajaxLogin = (e) =>{
		let url = '/login';
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
		            	userId : this.state.userId,
		            	password : md5(this.state.password),
		            	autoLogin : this.state.autoLogin
		            }
		        };
		axios(options).then((response)=>{
		    if(response.status === 200)
		    {
		        let result = response.data;
		        if(result.code !== 200 )
		        {
		            this.setState({
		                errorMsg : result.message
		            });
		        }
		    }
		}).catch((error)=>
		{
		    console.log(error);
		});
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
    							<Form.Group controlId="formBasicId">
    								<Form.Label>Id</Form.Label>
    								<Form.Control type="text" placeholder="Enter Id" name="userId" onChange={this.updateState}/>
    							</Form.Group>
    							<Form.Group controlId="formBasicPassword">
    								<Form.Label>Password</Form.Label>
    								<Form.Control type="password" placeholder="Password" name="password" onChange={this.updateState}/>
    								<p className="text-danger" id="errorMsg">{this.state.errorMsg}</p>
    							</Form.Group>
    							<Form.Group controlId="formBasicCheckbox">
    								<Form.Check type="checkbox" label="Auto Login" name="autoLogin" defaultChecked={this.state.autoLogin} onChange={this.checkboxHandle}/>
    							</Form.Group>
    							<Button variant="primary" onClick={this.ajaxLogin}>Submit</Button>
    						</Form>
    					</Col>
                        <Col sm>
    					    <Alert variant="danger">
                                    This is a danger alert—check it out!
                            </Alert>
                        </Col>
    				</Row>
    			</Container>
    		</Fragment>
		);
	}
}

export default Login;
