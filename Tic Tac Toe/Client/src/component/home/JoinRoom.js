import { useState } from "react";
import { useNavigate } from "react-router-dom";

export const JoinRoom = () => {
  const navigate = useNavigate();
  const [roomUrl, setRoomUrl] = useState("");

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const path = roomUrl.substring(roomUrl.indexOf('/game'));
    setRoomUrl('');
    navigate(path);
  };

  return (
    <div className="join-room">
      <form className="join-room-form" onSubmit={handleFormSubmit}>
      <h3> Join room: </h3>
        <div>
          <label>Room url: </label>
          <input type="text" value={roomUrl} onChange={(e) => setRoomUrl(e.target.value)} />
        </div>
        <button type="submit">Join Room</button>
      </form>
    </div>
  );
};

export default JoinRoom;
