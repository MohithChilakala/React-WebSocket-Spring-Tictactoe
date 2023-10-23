import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Authentication from "./pages/Authentication.js";
import Home from "./pages/Home.js";
import Game from "./pages/Game";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Authentication />} />
        <Route path="/home" element={<Home />} />
        <Route path="/game/:game_id" element={<Game />} />
      </Routes>
    </Router>
  );
}

export default App;
