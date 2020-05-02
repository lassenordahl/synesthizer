import recaptcha from "./Recaptcha";
import makeAsyncScriptLoader from "react-async-script";

let url = "https://www.google.com/recaptcha/api.js?onload=grecaptcha&render=explicit";

export default makeAsyncScriptLoader(url, {
  callbackName: "onloadcallback",
  globalName: "grecaptcha"
})(recaptcha);