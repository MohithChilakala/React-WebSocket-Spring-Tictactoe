/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { getProfile } from "../../services/AccountService";
import { deleteCookie, getCookie } from "../../services/CookieService";
import { useNavigate } from "react-router-dom";

export const UserProfile = () => {
  const [profile, setProfile] = useState({
    username: null,
    gamesPlayed: null,
    gamesWon: null,
    gamesLost: null,
    gamesDraw: null,
    winPercentage: null
  });
  const navigate = useNavigate();

  const updateProfile = async () => {
    const response = await getProfile(
      getCookie('username'),
      getCookie('authorization')
    );

    if (response.status !== 302) {
      deleteCookie('username');
      deleteCookie('authorization');

      navigate("/");
    }
    else {
      response.json().then((data) => {
        const played = data.won + data.loss + data.draw;

        const newProfile = {
          username: data.username,
          gamesWon: data.won,
          gamesLost: data.loss,
          gamesDraw: data.draw,
          gamesPlayed: played,
          winPercentage: played === 0 ? 0 : (data.won * 100) / played,
        };
        setProfile(newProfile);
      });
    }
  };

  useEffect(() => { updateProfile(); }, []);

  return (
    <div className="userprofile">
      <h1 className="username">{profile.username}</h1>
      <div>
        <h2 className="stats">Game Stats</h2>
        <ul className="playerStats">
          <li><span>Games Played:</span> {profile.gamesPlayed} </li>
          <li><span>Games Won:</span> {profile.gamesWon} </li>
          <li><span>Games Lost:</span> {profile.gamesLost} </li>
          <li><span>Games Draw:</span> {profile.gamesDraw} </li>
          <li><span>Win Percentage: </span> {profile.winPercentage}% </li>
        </ul>
      </div>
    </div>
  );
};
