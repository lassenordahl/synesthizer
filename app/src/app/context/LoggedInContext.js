import React, { useState, useEffect, createContext } from "react";
import { useCookies } from "react-cookie";

// Create Context Object
export const LoggedInContext = createContext();

// Create a provider for components to consume and subscribe to changes
export const LoggedInContextProvider = (props) => {
  const [cookies, setCookie] = useCookies([]);
  console.log(cookies.logged_in);
  const [loggedIn, setLoggedIn] = useState(cookies.logged_in);

  return (
    <LoggedInContext.Provider value={[loggedIn, setLoggedIn]}>
      {props.children}
    </LoggedInContext.Provider>
  );
};
