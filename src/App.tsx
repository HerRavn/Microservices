import React, { useState } from 'react';

function App() {
  const [gameState, setGameState] = useState<any>(null);
  const [drawnCard, setDrawnCard] = useState<any>(null);
  const [selectedCardIndex, setSelectedCardIndex] = useState<number | null>(null);
  const [drawFrom, setDrawFrom] = useState<string | null>(null);

  const startGame = async () => {
    const response = await fetch('http://localhost:8080/api/game/start', {
      method: 'POST'
    });
    const data = await response.json();
    console.log('Game state:', data);
    setGameState(data);
    setDrawnCard(null);
    setSelectedCardIndex(null);
  };

  const drawCard = async (source: 'mainDeck' | 'openTable') => {
    const response = await fetch(`http://localhost:8080/api/game/${gameState.gameId}/draw?from=${source}`, {
      method: 'POST'
    });
    const card = await response.json();
    console.log('Drew card:', card);
    setDrawnCard(card);
    setDrawFrom(source);
  };

  const swapCard = async () => {
    if (selectedCardIndex === null) {
      alert('Please select a card from your hand to swap');
      return;
    }

    console.log('Swapping - selectedCardIndex:', selectedCardIndex);
    console.log('Selected card:', gameState.playerHand[selectedCardIndex]);

    const requestBody = {
      drawnCard: drawnCard,
      swap: true,
      cardIndexToSwap: selectedCardIndex,
      drawFrom: drawFrom
    };
    console.log('Sending request body:', JSON.stringify(requestBody));

    const response = await fetch(`http://localhost:8080/api/game/${gameState.gameId}/complete-turn`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestBody)
    });
    const data = await response.json();
    console.log('After swap:', data);
    setGameState(data);
    setDrawnCard(null);
    setSelectedCardIndex(null);
    setDrawFrom(null);
  };

  const discardCard = async () => {
    const response = await fetch(`http://localhost:8080/api/game/${gameState.gameId}/complete-turn`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        drawnCard: drawnCard,
        swap: false,
        cardIndexToSwap: null,
        drawFrom: drawFrom
      })
    });
    const data = await response.json();
    console.log('After discard:', data);
    setGameState(data);
    setDrawnCard(null);
    setSelectedCardIndex(null);
    setDrawFrom(null);
  };

   const endGame = async () => {
     const response = await fetch(`http://localhost:8080/api/game/${gameState.gameId}/end`, {
       method: 'POST'
     });
     const data = await response.json();
     setGameState(data); // This will trigger the game over screen
   };

    const saveGameResult = async (playerName: string) => {
      await fetch(`http://localhost:8080/api/game/${gameState.gameId}/save-result`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          playerName: playerName
        })
      });
    };

  return (
    <div style={{ padding: '20px' }}>
      <h1>Card Game</h1>

      <button onClick={startGame}>Start Game</button>

      {gameState && (
        <div>
          <div style={{ marginBottom: '20px' }}>
            <button onClick={() => drawCard('mainDeck')}>Draw from Main Deck</button>
            <button onClick={() => drawCard('openTable')} disabled={!gameState.topOpenTableCard}>
              Draw from Open Table
            </button>
          </div>

          {drawnCard && (
            <div style={{ margin: '20px 0', padding: '20px', background: '#f0f0f0', borderRadius: '8px' }}>
              <h3>Drawn Card</h3>
              <img
                src={`./public/card_svg/${drawnCard.filename}`}
                alt={`${drawnCard.value} of ${drawnCard.suite}`}
                style={{ width: '100px' }}
              />
              <p>Select a card from your hand to swap, or click Discard</p>
              <div style={{ marginTop: '10px' }}>
                <button onClick={swapCard}>Swap with Selected Card</button>
                <button onClick={discardCard}>Discard</button>
              </div>
            </div>
          )}
      <div style={{ marginBottom: '20px' }}>
        {!gameState.gameOver && (
          <button onClick={endGame} style={{ marginLeft: '10px' }}>
            End Game
          </button>
        )}
    {gameState && gameState.gameOver && (
                <div>
                  <h2>🎉 Game Over!</h2>
                  <button onClick={() => {
                    const name = prompt('Enter your name:');
                    if (name) {
                      saveGameResult(name);
                      alert('Result saved!');
                    }
                  }}>
                    Save Result
                  </button>
                  <button onClick={startGame}>Start New Game</button>
                </div>
              )}
      </div>

          <h2>Player Hand (Click to select)</h2>
          <div style={{ display: 'flex', gap: '10px' }}>
            {gameState.playerHand.map((card: any, index: number) => (
              <img
                key={card.id}
                src={`./public/card_svg/${card.filename}`}
                alt={`${card.value} of ${card.suite}`}
                style={{
                  width: '100px',
                  cursor: 'pointer',
                  border: selectedCardIndex === index ? '3px solid blue' : 'none'
                }}
                onClick={() => setSelectedCardIndex(index)}
              />
            ))}
          </div>

          <h2>Computer Hand</h2>
          <div style={{ display: 'flex', gap: '10px' }}>
            {[...Array(gameState.computerHandSize)].map((_, i) => (
              <div
                key={i}
                style={{
                  width: '100px',
                  height: '140px',
                  background: '#333',
                  borderRadius: '8px'
                }}
              />
            ))}
          </div>

          <h2>Open Table</h2>
          {gameState.topOpenTableCard ? (
            <img
              src={`./public/card_svg/${gameState.topOpenTableCard.filename}`}
              alt={`${gameState.topOpenTableCard.value} of ${gameState.topOpenTableCard.suite}`}
              style={{ width: '100px' }}
            />
          ) : (
            <p>Empty</p>
          )}

          <p>Scores - You: {gameState.playerScore} | Computer: {gameState.computerScore}</p>
          {gameState.message && <p style={{ marginTop: '10px', fontStyle: 'italic' }}>{gameState.message}</p>}
        </div>
      )}
    </div>
  );
}

export default App;