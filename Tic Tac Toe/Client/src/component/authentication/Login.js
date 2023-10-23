import { useState } from 'react';
import { isLoggined } from '../../services/AccountService'
import { setCookie } from '../../services/CookieService'
import { useNavigate } from 'react-router-dom';

function Login({ handleClick }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const credentials = `${username}:${password}`;
    const authorization = "Basic " + btoa(credentials);

    const response = await isLoggined(authorization);
    if (response) {
      setCookie('username', username, 30);
      setCookie('authorization', authorization, 30);

      navigate('/home');
    } else alert('Invalid Credentials');
  };

  return (
    <div className='login'>
      <h2 className='login-title'>Login</h2>
      <form className='login-form' onSubmit={handleFormSubmit}>
        <label>Username:</label>
        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        <br />
        <label>Password:</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <br />
        <button type="submit">Login</button>
        <p style={{ cursor: 'pointer' }} onClick={handleClick}> SignUp </p>
      </form>
    </div>
  );
}

export default Login;
