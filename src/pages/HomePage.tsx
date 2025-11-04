import React from "react";
import { useNavigate } from "react-router-dom";
import "./HomePage.css";

const HomePage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <div className="home-container">
            <h1 className="home-title">Welcome to Arabian Cards!</h1>
            <p className="home-description">
                A digital card game where strategy meets luck. Log in or jump right into a local match!
            </p>

            <div className="button-group">
                <button onClick={() => navigate("/login")} className="home-button login">
                    Login / Register
                </button>
                <button onClick={() => navigate("/game")} className="home-button play">
                    Start Game
                </button>
                <button onClick={() => navigate("/rules")} className="home-button rules">
                    Rules
                </button>
            </div>
        </div>
    );
};

export default HomePage;
