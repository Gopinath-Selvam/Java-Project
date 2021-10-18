import React from 'react';
import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { ProtectedRoute } from './components/Route/ProtectedRoute';
import User from './components/user/User';
import Admin from './components/admin/Admin';
import Login from './components/login/Login';

function App() {
  return (
    <div>
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={Login} />
          <ProtectedRoute path="/user" component={User} />
          <ProtectedRoute path="/admin" component={Admin} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
