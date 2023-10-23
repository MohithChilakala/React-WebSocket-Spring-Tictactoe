import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createRoom } from '../../services/GameService'
import { getCookie } from "../../services/CookieService";

export const CreateRoom = () => {
  const navigate = useNavigate();
  const [password, setPassword] = useState("");

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const response = await createRoom(getCookie('username'), password, getCookie('authorization'));
    if(response.status === 201) {
      response.text().then((data) => {
        const propsToPass = {
          host: getCookie('username'),
          password: password
        };
        const query = new URLSearchParams(propsToPass).toString();
        
        const roomUrl = 'http://localhost:3000/game/' + data + `/?${query}`;
        navigator.clipboard.writeText(roomUrl);
        alert('room url: ' + roomUrl + ' copied');
        setPassword('');
        setTimeout(() => {
          window.open(roomUrl, '_blank', 'noreferrer');
        }, 100);
      });
    }
    else navigate('/');
  };

  return (
    <div className="create-room">
      <form className="create-room-form" onSubmit={handleFormSubmit}>
      <h3> Create room: </h3>
        <div>
          <label>Room password: </label>
          <input type="text" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit">Create Room</button>
      </form>
    </div>
  );
};

export default CreateRoom;
