import React, { useState, useEffect, createContext } from "react";
import { useCookies } from "react-cookie";

// Create Context Object
export const LoggedInContext = createContext();

// Create a provider for components to consume and subscribe to changes
export const LoggedInContextProvider = (props) => {
  const [cookies, setCookie] = useCookies([]);
  const [loggedIn, setLoggedIn] = useState(cookies.logged_in === "true" ? true : false);
  const [isEmployee, setIsEmployee] = useState(cookies.isEmployee === "true" ? true : false);

  return (
    <LoggedInContext.Provider value={[loggedIn, setLoggedIn, isEmployee, setIsEmployee]}>
      {props.children}
    </LoggedInContext.Provider>
  );
};
