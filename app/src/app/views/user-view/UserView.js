import React, { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./UserView.css";

import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { LoggedInContext } from "../../context/LoggedInContext";

import { Card } from "../../containers";
import { UserForm, LoginForm } from "../../components";

import axios from "axios";

import { api } from "../../../utils/api";

function UserView({ props, match }) {
  let history = useHistory();
  const [loggedIn, setLoggedIn] = useContext(LoggedInContext);
  const [cookies, setCookie] = useCookies([]);

  const [user, setUser] = useState({});

  function getUser() {
    axios
      .get(api.user)
      .then(function (response) {
        setUser(response.data);
        console.log(response);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function login(form) {
    axios
      .post(api.login, form)
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          setLoggedIn(true);
          setCookie("logged_in", true, { path: "/unnamed", expires: 0 });
          history.push("/app/explore/songs");
        }
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function createUser(form) {
    axios
      .post(api.user, form)
      .then(function (response) {
        console.log(response);
        if (response.status == 200) {
          history.push("/app/user/account/login");
        } else {
        }
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function updateUser(form) {
    axios
      .put(api.user, form)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  useEffect(() => {
    if (match.params.mode == "update") {
      getUser();
    }
  }, [match.params.mode]);

  function renderHeader() {
    if (match.params.mode === "login") {
      return <h3>Member Login</h3>;
    } else if (match.params.mode === "create") {
      return <h3>Sign Up</h3>;
    } else if (match.params.mode === "update") {
      return <h3>Update Account Info</h3>;
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
      <Card className="account-view-card">
        <div className="account-view-header">{renderHeader()}</div>
        <div className="account-view-form">{renderForm()}</div>
        <div className="account-view-footer">{renderFooter()}</div>
      </Card>
    </div>
  );
}

export default UserView;
