import { useState } from 'react';
import { createAccount } from '../../services/AccountService'

function Signup({ handleClick }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    const response = await createAccount(username, password);

    if (response.status === 201) handleClick();
    else alert("Registration failed");
  };

  return (
    <div className='signup'>
      <h2 className='signup-title'>Signup</h2>
      <form className='signup-form' onSubmit={handleFormSubmit}>
        <label>Username:</label>
        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        <br />
        <label>Password:</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <br />
        <button type="submit">Signup</button>
        <p style={{ cursor: 'pointer' }} onClick={handleClick}> LogIn </p>
      </form>
    </div>
  );
}

export default Signup;
