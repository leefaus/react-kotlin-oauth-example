import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Signup from '../user/signup/Signup';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import {getCurrentUser} from '../util/APIUtils';
import {ACCESS_TOKEN} from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import {Layout, message} from 'antd';
import './App.css';
import { library } from '@fortawesome/fontawesome-svg-core'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { faCheckSquare, faCoffee } from '@fortawesome/free-solid-svg-icons'

const {Header, Footer, Content} = Layout;

library.add(fab, faCheckSquare, faCoffee)

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            authenticated: false,
            currentUser: null,
            loading: false
        }

        this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    loadCurrentlyLoggedInUser() {
        this.setState({
            loading: true
        });

        getCurrentUser()
            .then(response => {
                this.setState({
                    currentUser: response,
                    authenticated: true,
                    loading: false
                });
            }).catch(error => {
            this.setState({
                loading: false
            });
        });
    }

    handleLogout() {
        localStorage.removeItem(ACCESS_TOKEN);
        this.setState({
            authenticated: false,
            currentUser: null
        });
        message.success("You're safely logged out!");
    }

    componentDidMount() {
        this.loadCurrentlyLoggedInUser();
    }

    render() {
        if (this.state.loading) {
            return <LoadingIndicator/>
        }

        return (
            <div className="app">
                <Layout style={{height:"100vh"}}>
                    <Header>
                        <AppHeader authenticated={this.state.authenticated} onLogout={this.handleLogout}/>
                    </Header>
                    <Content>
                        <Switch>
                            <Route exact path="/" component={Home}></Route>
                            <PrivateRoute path="/profile" authenticated={this.state.authenticated}
                                          currentUser={this.state.currentUser}
                                          component={Profile}></PrivateRoute>
                            <Route path="/login"
                                   render={(props) => <Login
                                       authenticated={this.state.authenticated} {...props} />}></Route>
                            <Route path="/signup"
                                   render={(props) => <Signup
                                       authenticated={this.state.authenticated} {...props} />}></Route>
                            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>
                            <Route component={NotFound}></Route>
                        </Switch>
                    </Content>
                    <Footer>kubikl.io &copy;2020</Footer>
                </Layout>
            </div>
        );
    }
}

export default App;
