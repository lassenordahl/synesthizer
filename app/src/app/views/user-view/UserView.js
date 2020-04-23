import React, { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./UserView.css";

import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { LoggedInContext } from "../../context/LoggedInContext";

import { Card } from "../../containers";
import { CreateUserForm, LoginForm } from "../../components";

import axios from "axios";

import api from "../../../utils/api";

function UserView({ props, match }) {
  let history = useHistory();
  const [loggedIn, setLoggedIn] = useContext(LoggedInContext);
  const [cookies, setCookie] = useCookies([]);

  function login(form) {
    axios
      .post(api.login, form)
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          setLoggedIn(true);
          setCookie("logged_in", true);
          // Redirect to main page
          history.push("/app/explore/songs");
        }
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function createUser(form) {
    axios
      .post(api.login, form)
      .then(function (response) {
        console.log(response);
        // Redirect to main page
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  useEffect(() => {}, [match.params.mode]);

  function renderHeader() {
    if (match.params.mode === "login") {
      return "Member Login";
    } else if (match.params.mode === "create") {
      return "Sign Up";
    } else if (match.params.mode === "update") {
      return "Update Account Info";
    }
  }

  function renderForm() {
    if (match.params.mode === "login") {
      return <LoginForm onSubmit={login} />;
    } else if (match.params.mode === "create") {
      return <CreateUserForm onSubmit={createUser} />;
    } else if (match.params.mode === "update") {
      return <h1>Update User</h1>;
    }
  }

  function renderFooter() {
    if (match.params.mode === "login") {
      return (
        <React.Fragment>
          Don't have an account?
          <Link to="/app/user/account/create"> Sign Up</Link>
        </React.Fragment>
      );
    } else if (match.params.mode === "create") {
      return (
        <React.Fragment>
          Have an account?
          <Link to="/app/user/account/login"> Login</Link>
        </React.Fragment>
      );
    }
  }

  return (
    <div className="login-view">
      <Card className="login-view-card">
        <div className="login-view-header">{renderHeader()}</div>
        {renderForm()}
        <div>{renderFooter()}</div>
      </Card>
    </div>
  );
}

export default UserView;
