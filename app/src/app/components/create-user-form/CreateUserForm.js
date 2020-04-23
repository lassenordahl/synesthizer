import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";

import { Button } from "../../components";

function CreateUserForm(props) {
  const { handleSubmit, register, errors } = useForm();

  return (
    <form>
      <div>
        <input
          name="first_name"
          placeholder="first name"
          ref={register({
            required: true,
          })}
        ></input>
        {errors.first_name && "first name required"}
        <input
          name="last_name"
          placeholder="last name"
          ref={register({
            required: true,
          })}
        ></input>
        {errors.last_name && "last name required"}
        <input
          name="address"
          placeholder="address"
          ref={register({
            required: true,
          })}
        ></input>
        {errors.address && "address required"}
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

export default CreateUserForm;
