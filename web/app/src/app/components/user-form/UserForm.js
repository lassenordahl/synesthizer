import React, { useState, useEffect } from "react";
import "./UserForm.css";

import { useForm } from "react-hook-form";
import Recaptcha from "react-recaptcha";

import { Button } from "../../components";
import { useToast } from "../../../hooks";

function UserForm(props) {
  const { handleSubmit, register, errors, setValue } = useForm();
  const [showSuccess, showError, renderToast] = useToast();

  const [captcha, setCaptcha] = useState(null);

  function onVerify(recaptchaResponse) {
    setCaptcha(recaptchaResponse);
    setValue("captcha", recaptchaResponse);
  }

  function onLoadCallback() {
    register({ name: "captcha", }, { required: true})
  }

  return (
    <div className="user-form">
      {renderToast()}
      <form>
        <input
          name="first_name"
          placeholder="first name"
          defaultValue={props.defaults ? props.defaults.first_name : undefined}
          ref={register({
            required: true,
          })}
        ></input>
        <span className="user-form-val">{errors.first_name && "first name required"}</span>
        <input
          name="last_name"
          placeholder="last name"
          defaultValue={props.defaults ? props.defaults.last_name : undefined}
          ref={register({
            required: true,
          })}
        ></input>
        <span className="user-form-val">{errors.last_name && "last name required"}</span>
        <input
          name="address"
          placeholder="address"
          defaultValue={props.defaults ? props.defaults.address : undefined}
          ref={register({
            required: true,
          })}
        ></input>
        <span className="user-form-val">{errors.address && "address required"}</span>
        <input
          name="email"
          placeholder="email"
          defaultValue={props.defaults ? props.defaults.email : undefined}
          ref={register({
            required: true,
            pattern: {
              value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
            },
          })}
        ></input>
        <span className="user-form-val">{errors.email && "invalid email address"}</span>
        <input
          type="password"
          name="password"
          placeholder={props.action === "Update" ? "new password" : "password"}
          ref={
            props.action === "Update"
              ? register({
                  required: false,
                })
              : register({
                  required: true,
                  validate: (value) => value && value.length > 0,
                })
          }
        ></input>
        <span className="user-form-val">{errors.password && "password required"}</span>
        <div className="user-form-button">
          <Recaptcha
            sitekey="6LecPfEUAAAAAKjBkF-aqpo56xWnLuQG0pjqK-Uc"
            render="explicit"
            verifyCallback={onVerify}
            onloadCallback={onLoadCallback}
          />
          <div style={{height: "24px"}}/>
          <Button
            type="submit"
            style={{ width: "65px", height: "40px" }}
            isPrimary={true}
            onClick={captcha !== null 
              ? handleSubmit(props.onSubmit) 
              : () => {
                  showError("Captcha not completed")
                }
            }
          >
            {props.action}
          </Button>
        </div>
      </form>
    </div>
  );
}

export default UserForm;
