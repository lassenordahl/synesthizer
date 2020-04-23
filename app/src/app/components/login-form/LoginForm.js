import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import "./LoginForm.css";

import { Button } from "../../components";

function LoginForm(props) {
  const { handleSubmit, register, errors } = useForm();

  return (
    <form>
      <div>
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
        {errors.email && "invalid email address"}
        <input
          type="password"
          name="password"
          placeholder="password"
          ref={register({
            required: true,
            validate: (value) => value && value.length > 0,
          })}
        ></input>
        {errors.password && "password required"}
      </div>
      <Button
        style={{ width: "200px" }}
        type="submit"
        isPrimary={true}
        onClick={handleSubmit(props.onSubmit)}
      >
        Login
      </Button>
    </form>
  );
}

export default LoginForm;
