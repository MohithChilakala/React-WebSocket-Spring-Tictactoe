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
        const roomUrl = `/game/${data}?${query}`;

        const textArea = document.createElement("textarea");
        textArea.value = 'http://tic-tac-toe-4124.s3-website.ap-south-1.amazonaws.com' + roomUrl;
        document.body.appendChild(textArea);
        textArea.focus();textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);

        alert('roomurl copied share to your friends');
        setPassword('');
        navigate(roomUrl);
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
