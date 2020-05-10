import React, { useState, useEffect } from "react";
import "./LoginForm.css";

import { useForm } from "react-hook-form";
import Recaptcha from "react-recaptcha";

import { Button } from "../../components";

function LoginForm(props) {
  const { handleSubmit, register, errors, setValue  } = useForm();

  const [captcha, setCaptcha] = useState(null);

  function onVerify(recaptchaResponse) {
    setCaptcha(recaptchaResponse);
    setValue("captcha", recaptchaResponse);
  }

  function onLoadCallback() {
    register({ name: "captcha", }, { required: true})
  }

  return (
    <div className="login-form">
      <form>
        <input
          name="email"
          placeholder="email"
          ref={register({
            required: true,
            pattern: {
              value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
            },
          })}
        ></input>
        <span className="login-form-val">{errors.email && "invalid email address"}</span>
        <input
          type="password"
          name="password"
          placeholder="password"
          ref={register({
            required: true,
            validate: (value) => value && value.length > 0,
          })}
        ></input>
        <span className="login-form-val">{errors.password && "password required"}</span>
        <div className="login-form-button">
          <Recaptcha
            sitekey="6LecPfEUAAAAAKjBkF-aqpo56xWnLuQG0pjqK-Uc"
            render="explicit"
            verifyCallback={onVerify}
            onloadCallback={onLoadCallback}
          />
          <div style={{ height: "36px" }}></div>
          <Button
            style={{ width: "65px" }}
            type="submit"
            isPrimary={true}
            onClick={handleSubmit(props.onSubmit)}
          >
            Login
          </Button>
        </div>
      </form>
    </div>
  );
}

export default LoginForm;
