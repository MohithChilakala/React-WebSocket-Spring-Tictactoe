import { getApiUrl } from "./GetApiUrl";

export const createRoom = async (host, password, authorization) => {
  const response = await fetch(getApiUrl('game/create-room'), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: authorization
    },
    body: JSON.stringify({
      host: host,
      password: password,
    })
  });

  return response;
};

export const joinRoom = async (game_id, password, player, authorization) => {
  const response = await fetch(getApiUrl('game/join'), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: authorization
    },
    body: JSON.stringify({
      game_id: game_id,
      password: password,
      player: player
    })
  });

  return response;
}