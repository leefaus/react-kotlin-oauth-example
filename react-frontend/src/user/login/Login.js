import React, { Component } from 'react';
import './Login.css';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, ACCESS_TOKEN } from '../../constants';
import { login } from '../../util/APIUtils';
import { Link, Redirect } from 'react-router-dom'
import {Card, Divider, Row, Col, Button, Form, Input, message} from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const google = <FontAwesomeIcon icon={['fab', 'google']} />
const github = <FontAwesomeIcon icon={['fab', 'github']} />
const facebook = <FontAwesomeIcon icon={['fab', 'facebook']} />
const cardTitle = <h1>Login to Spring Social</h1>

class Login extends Component {
    componentDidMount() {
        // If the OAuth2 login encounters an error, the user is redirected to the /login page with an error.
        // Here we display the error and then remove the error query parameter from the location.
        if(this.props.location.state && this.props.location.state.error) {
            setTimeout(() => {
                message.error(this.props.location.state.error, {
                    timeout: 5000
                });
                this.props.history.replace({
                    pathname: this.props.location.pathname,
                    state: {}
                });
            }, 100);
        }
    }

    render() {
        if(this.props.authenticated) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: { from: this.props.location }
                }}/>;
        }

        return (
            <Row align="middle">
                <Col span={6} offset={9} >
                <Card title={cardTitle} className="login-card">
                    <SocialLogin />
                    <Divider>OR</Divider>
                    <LoginForm {...this.props} />
                    <span className="signup-link login-form-button">New user? <Link to="/signup">Sign up!</Link></span>
                </Card>
                </Col>
            </Row>
        );
    }
}

class SocialLogin extends Component {
    render() {
        return (
            <div>
                <Button href={GOOGLE_AUTH_URL} icon={ google } size={"large"} block>
                    &nbsp; Log in with Google
                </Button> <p/>
                <Button href={FACEBOOK_AUTH_URL} icon={ facebook } size={"large"} block>
                    &nbsp; Log in with Facebook
                </Button> <p/>
                <Button href={GITHUB_AUTH_URL} icon={github} size={"large"} block>
                    &nbsp; Log in with GitHub
                </Button>


            </div>
        );
    }
}


class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: ''
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        const loginRequest = Object.assign({}, this.state);

        login(loginRequest)
            .then(response => {
                localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                message.success("You're successfully logged in!");
                this.props.history.push("/");
            }).catch(error => {
            message.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    render() {
        return (
            < Form onFinish={this.handleSubmit}>
                <Form.Item label={"Username"} name={"username"} rules={[{ required: true, message: 'Please input your username!' }]}><Input/></Form.Item>
                <Form.Item
                    label="Password"
                    name="password"
                    rules={[{ required: true, message: 'Please input your password!' }]}
                >
                    <Input.Password />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        );
    }
}

export default Login