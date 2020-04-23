import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useCookies } from "react-cookie";
import "./UserView.css";

import { Link } from "react-router-dom";

import { Card } from "../../containers";
import { CreateUserForm, LoginForm } from "../../components";

import axios from "axios";

import api from "../../../utils/api";

function UserView({ props, match }) {
  const [loggingIn, setLoggingIn] = useState(true);

  const [cookies, setCookie] = useCookies(["name"]);

  function login(form) {
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
      setCookie("hello", "hello", { path: "/unnamed" });
      console.log();
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
