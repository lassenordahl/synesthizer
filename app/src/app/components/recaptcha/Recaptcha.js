import React, { useState } from "react";

import { Button } from "../../components";

function Recaptcha(props) {

  const [captcha, setCaptcha] = useState(null);
  const [widgetId, setWidgetId] = useState(1);

  function handleRecaptchaRef(elem) {
    setCaptcha(elem);
  }

  function handleRecaptchaSubmit() {
    debugger;
    if (props.grecaptcha && props.grecaptcha.getResponse() === "") {

    } else {
      props.handleSubmit(props.onSubmit)
    }
    // props.onSubmit(props.grecaptcha.getResponse());
  }

  function execute() {
    if (props.grecaptcha) {
      props.grecaptcha.reset(widgetId);
    }
  }

  return (
    <React.Fragment>
      <div ref={handleRecaptchaRef} className="g-recaptcha" data-sitekey="6LecPfEUAAAAAKjBkF-aqpo56xWnLuQG0pjqK-Uc"/>
      <Button
        type="submit"
        style={{ width: "65px", height: "40px" }}
        isPrimary={true}
        onClick={() => handleRecaptchaSubmit()}
      >
        {props.action}
      </Button>
    </React.Fragment>
  );
}

export default Recaptcha;
