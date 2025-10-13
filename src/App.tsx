import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import GamePage from "./pages/GamePage";

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                {/* Startsiden */}
                <Route path="/" element={<HomePage />} />

                {/* Spill-siden */}
                <Route path="/game" element={<GamePage />} />
            </Routes>
        </Router>
    );
};

export default App;
