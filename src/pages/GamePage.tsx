import React, { useState } from "react";
import axios from "axios";
import "./GamePage.css";

type Card = {
    id: number;
    value: number;
    suite: string;
    filename: string;
};

const GamePage: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [card, setCard] = useState<Card | null>(null);

    const drawCard = async () => {
        setLoading(true);
        try {
            const response = await axios.get<Card>("/api/drawCard");
            setCard(response.data);
        } catch (err) {
            console.error("Error drawing card:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="game-container">
            <h1 className="game-title">Card Game</h1>

            <button onClick={drawCard} disabled={loading} className="draw-button">
                {loading ? "Drawing..." : "Draw Card"}
            </button>

            <h2 className="section-title">Drawn Card</h2>
            <div className="card-display">
                {card ? (
                    <>
                        <p className="card-text">
                            {card.suite} {card.value}
                        </p>
                        <img
                            src={`/card_svg/${card.filename}`}
                            alt={`${card.value} of ${card.suite}`}
                            className="card-image"
                        />
                    </>
                ) : (
                    <p className="no-card-text">No card drawn yet</p>
                )}
            </div>
        </div>
    );
};

export default GamePage;
