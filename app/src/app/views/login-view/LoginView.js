import React, { useState, useEffect } from "react";
import "./LoginView.css";

import { Card } from "./../../containers";
import { Button } from "./../../components";

import axios from "axios";

import api from "../../../api";

function LoginView(props) {
  const [loggingIn, setLoggingIn] = useState(false);

  // Login Form
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);

  return (
    <div className="login-view">
      <Card className="login-view-card">
        <div className="login-view-header">
          {loggingIn ? "Member Login" : "Sign Up"}
        </div>
        {loggingIn ? (
          <div className="login-view-login">
            <form>
              <input placeholder="email" type="email" value={email}></input>
              <input
                placeholder="password"
                type="password"
                value={password}
              ></input>
            </form>
            <Button style={{ width: "200px" }} isPrimary={true}>
              Login
            </Button>
          </div>
        ) : (
          <div className="login-view-signup">
            <form>
              <div className="login-view-signup-names">
                <input placeholder="first name"></input>
                <input placeholder="last name"></input>
              </div>
              <input placeholder="address"></input>
              <input placeholder="email"></input>
              <input placeholder="password"></input>
            </form>
            <Button style={{ width: "200px" }} isPrimary={true}>
              Login
            </Button>
          </div>
        )}
      </Card>
    </div>
  );
}

export default LoginView;
