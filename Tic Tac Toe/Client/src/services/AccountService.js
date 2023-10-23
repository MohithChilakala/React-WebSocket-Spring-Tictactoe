export const createAccount = async (username, password) => {
  const response = await fetch('http://localhost:8080/account', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      'username': username,
      'password': password
    })
  });

  return response;
}

export const isLoggined = async (authorization) => {
  const response = await fetch('http://localhost:8080/account/login', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization' : authorization
    }
  });
  return (response.status === 200);
}

export const getProfile = async (username, authorization) => {
  const response = await fetch('http://localhost:8080/account/' + username, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization' : authorization
    }
  });

  return response;
}