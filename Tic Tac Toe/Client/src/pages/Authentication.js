/* eslint-disable react-hooks/exhaustive-deps */
import React, { useState, useEffect } from "react";
import { isLoggined } from "../services/AccountService";
import { getCookie } from "../services/CookieService";
import { useNavigate } from "react-router-dom";
import Signup from "../component/authentication/Signup";
import Login from "../component/authentication/Login";
import '../styles/authentication.css'

const Authentication = () => {
  const navigate = useNavigate();
  const [showLogin, setShowLogin] = useState(true);
  const [value, setValue] = useState('Signup');

  useEffect(() => {
    const checkCredentials = async () => {
      if (await isLoggined(getCookie("authorization"))) navigate("/home");
    };
    checkCredentials();
  });

  const handleClick = () => {
    setShowLogin(!showLogin);
    if(value === 'Signup') setValue('Login');
    else setValue('Signup');
  }

  return (
    <>
      {showLogin ? <Login handleClick={handleClick} /> : <Signup handleClick={handleClick} />}
    </>
  );
};

export default Authentication;
