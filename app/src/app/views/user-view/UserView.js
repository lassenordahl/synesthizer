import React, { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./UserView.css";

import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { LoggedInContext } from "../../context/LoggedInContext";

import { useToast } from "../../../hooks";

import { Card } from "../../containers";
import { UserForm, LoginForm } from "../../components";

import axios from "axios";

import { api } from "../../../utils/api";

function UserView({ props, match }) {
  const [showSuccess, showError, renderToaster] = useToast();

  let history = useHistory();
  const [loggedIn, setLoggedIn] = useContext(LoggedInContext);
  const [cookies, setCookie] = useCookies([]);

  const [user, setUser] = useState({});

  function getUser() {
    axios
      .get(api.user)
      .then(function (response) {
        setUser(response.data);
        if (response !== undefined && response.status === 200) {
          showSuccess("Successfully loaded user");
        } else {
          showError("Error loading user");
        }
      })
      .catch(function (error) {
        showError("Error loading user");
      });
  }

  function login(form) {
    axios
      .post(api.login, form)
      .then(function (response) {
        console.log(response);
        if (response !== undefined && response.status === 200) {
          setLoggedIn(true);
          setCookie("logged_in", true, { path: "/unnamed", expires: 0 });
          showSuccess("Successfully logged in");
          history.push("/app/explore/songs");
        } else {
          showError("Incorrect email or password");
        }
      })
      .catch(function (error) {
        showError("Error logging in");
      });
  }

  function createUser(form) {
    axios
      .post(api.user, form)
      .then(function (response) {
        console.log(response);
        if (response !== undefined && response.status === 200) {
          showSuccess("Successfully created user");
          history.push("/app/user/account/login");
        } else {
          showError("Email already in use");
        }
      })
      .catch(function (error) {
        showError("Error creating account");
      });
  }

  function updateUser(form) {
    axios
      .put(api.user, form)
      .then(function (response) {
        console.log(response);
        if (response !== undefined && response.status === 200) {
          showSuccess("Successfully updated account");
        } else {
          showError("Error already in use");
        }
      })
      .catch(function (error) {
        console.log(error);
        showError("Error updating account");
      });
  }

  useEffect(() => {
    if (match.params.mode === "update") {
      getUser();
    }
  }, [match.params.mode]);

  function renderHeader() {
    if (match.params.mode === "login") {
      return <h2>Member Login</h2>;
    } else if (match.params.mode === "create") {
      return <h2>Sign Up</h2>;
    } else if (match.params.mode === "update") {
      return <h2>Update Account Info</h2>;
    }
  }

  function renderForm() {
    if (match.params.mode === "login") {
      return <LoginForm onSubmit={login} />;
    } else if (match.params.mode === "create") {
      return <UserForm onSubmit={createUser} action="Sign Up" />;
    } else if (match.params.mode) {
      return <UserForm onSubmit={updateUser} action="Update" defaults={user} />;
    }
  }

  function renderFooter() {
    if (match.params.mode === "login") {
      return (
        <React.Fragment>
          <span>Don't have an account? </span>
          <Link to="/app/user/account/create">Sign Up</Link>
        </React.Fragment>
      );
    } else if (match.params.mode === "create") {
      return (
        <React.Fragment>
          <span>Have an account?</span>
          <Link to="/app/user/account/login">Login</Link>
        </React.Fragment>
      );
    }
  }

  return (
    <div className="account-view">
      {renderToaster()}
      <Card className="account-view-card">
        <div className="account-view-header">{renderHeader()}</div>
        <div className="account-view-form">{renderForm()}</div>
        <div className="account-view-footer">{renderFooter()}</div>
      </Card>
    </div>
  );
}

export default UserView;
