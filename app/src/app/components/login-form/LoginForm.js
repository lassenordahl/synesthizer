import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import "./LoginForm.css";

import { Button } from "../../components";

function LoginForm(props) {
  const { handleSubmit, register, errors } = useForm();

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
