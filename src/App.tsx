import { useState } from "react";
import axios from "axios";

type Card = {
    id: number;
    value: number;
    suite: string;
    filename: string;
};


function App() {
    const [loading, setLoading] = useState(false);
    const [card, setCard] = useState<Card | null>(null);
    const drawCard = async () => {
        setLoading(true);
        try {
            const response = await axios.get<Card>("/api/drawCard");
            setCard(response.data);  // <-- store drawn card
        } catch (err) {
            console.error("Error drawing card:", err);
        }
        setLoading(false);
    };


    return (
        <div style={{ padding: "2rem" }}>
            <h1>Card Game</h1>

            <button onClick={drawCard} disabled={loading}>
                {loading ? "Drawing..." : "Draw Card"}
            </button>



            <h2>Drawn card</h2>
            <div>
                {card ? (
                    <>
                        <p>{card.suite} {card.value}</p>
                        <img src={`/card_svg/${card.filename}`} alt={`${card.value} of ${card.suite}`} />
                    </>
                ) : (
                    "No card drawn yet"
                )}
            </div>

        </div>
    );
}

export default App;
