import React from "react";

import Recaptcha from "./Recaptcha";
import makeAsyncScriptLoader from "react-async-script";

let url = "https://www.google.com/recaptcha/api.js?onload=grecaptcha&render=explicit";

function RecaptchaWrapper(props) {
  return (
    <Recaptcha handleSubmit={props.handleSubmit}></Recaptcha>
  )
}

export default makeAsyncScriptLoader(url, {
  callbackName: "onloadcallback",
  globalName: "grecaptcha"
})(RecaptchaWrapper);