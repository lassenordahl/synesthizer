import React, { useContext } from "react";
import { Route, Redirect } from "react-router-dom";

import { LoggedInContext } from "../../context/LoggedInContext";

function PrivateRoute({
  component,
  employeeOnly,
  employeeNotAllowed,
  ...rest
}) {
  const [loggedIn, setLoggedIn, isEmployee, setIsEmployee] = useContext(
    LoggedInContext
  );

  function evalCondition() {
    console.log("running eval");
    if (employeeOnly) {
      console.log("isEmployee");
      return loggedIn && isEmployee;
    } else if (employeeNotAllowed) {
      return loggedIn && !isEmployee;
    }
    return loggedIn;
  }

  return (
    <Route
      {...rest}
      component={
        evalCondition()
          ? component
          : () => <Redirect to="/app/user/account/login" />
      }
    />
  );
}

export default PrivateRoute;
