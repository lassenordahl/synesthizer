import React, { useContext } from "react";
import { Route, Redirect } from "react-router-dom";

import { LoggedInContext } from "../../context/LoggedInContext";

function PrivateRoute({ component, ...rest }) {
  const [loggedIn, setLoggedIn] = useContext(LoggedInContext);

  return (
    <Route
      {...rest}
      component={
        loggedIn ? component : () => <Redirect to="/app/user/account/login" />
      }
    />
  );
}

export default PrivateRoute;
