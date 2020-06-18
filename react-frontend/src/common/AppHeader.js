import React, {Component} from 'react';
import {Link, NavLink} from 'react-router-dom';
import {Button} from 'antd';
import './AppHeader.css';

class AppHeader extends Component {
    render() {
        return (
            <div className="container">
                <div className="app-branding">
                    <Link to="/" className="app-title">Spring Social</Link>
                </div>
                <div className="app-options">
                    <nav className="app-nav">
                        {this.props.authenticated ? (
                            <ul>
                                <li>
                                    <NavLink to="/profile">Profile</NavLink>
                                </li>
                                <li>
                                    <Button type="primary" onClick={this.props.onLogout}>Logout</Button>
                                </li>
                            </ul>
                        ) : (
                            <ul>
                                <li>
                                    <NavLink to="/login">Login</NavLink>
                                </li>
                                <li>
                                    <NavLink to="/signup">Signup</NavLink>
                                </li>
                            </ul>
                        )}
                    </nav>
                </div>
            </div>

        )
    }
}

export default AppHeader;