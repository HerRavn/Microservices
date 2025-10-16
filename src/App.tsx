import React, { useState, useEffect } from 'react';
import { Play, RefreshCw, X } from 'lucide-react';


export default function CardGame() {
  const [gameId, setGameId] = useState(null);
  const [gameState, setGameState] = useState(null);
  const [drawnCard, setDrawnCard] = useState(null);
  const [selectedCardIndex, setSelectedCardIndex] = useState(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [waitingForAction, setWaitingForAction] = useState(false);

  // Initialize cards in the database
  const initializeCards = async () => {
    try {
      await fetch(`${API_BASE_URL}/cards/init`, { method: 'POST' });
    } catch (error) {
      console.error('Error initializing cards:', error);
    }
  };

  // Start a new game
  const startNewGame = async () => {
    setLoading(true);
    try {
      await initializeCards();
      const response = await fetch(`api/game/start`, { method: 'POST' });
      const data = await response.json();
      setGameId(data.gameId);
      setGameState(data);
      setMessage(data.message);
      setDrawnCard(null);
      setSelectedCardIndex(null);
      setWaitingForAction(false);
    } catch (error) {
      setMessage('Error starting game: ' + error.message);
    }
    setLoading(false);
  };

  // Draw a card (player's turn)
  const drawCard = async () => {
    if (!gameId || waitingForAction) return;

    setLoading(true);
    setWaitingForAction(true);

    try {
      const response = await fetch(`api/game/${gameId}/turn`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ swap: false, cardIndexToSwap: null })
      });
      const data = await response.json();
      setDrawnCard(data.drawnCard);
      setMessage(`You drew: ${data.drawnCard.value} of ${data.drawnCard.suite}`);
      // Don't update full game state yet - wait for player decision
    } catch (error) {
      setMessage('Error drawing card: ' + error.message);
      setWaitingForAction(false);
    }
    setLoading(false);
  };

  // Discard the drawn card
  const discardCard = async () => {
    if (!gameId || !drawnCard) return;

    setLoading(true);
    try {
      const response = await fetch(`api/game/${gameId}/turn`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ swap: false, cardIndexToSwap: null })
      });
      const data = await response.json();
      setGameState(data);
      setMessage(data.message);
      setDrawnCard(null);
      setSelectedCardIndex(null);
      setWaitingForAction(false);
    } catch (error) {
      setMessage('Error discarding card: ' + error.message);
    }
    setLoading(false);
  };

  // Swap the drawn card with a selected card from hand
  const swapCard = async () => {
    if (!gameId || selectedCardIndex === null || !drawnCard) return;

    setLoading(true);
    try {
      const response = await fetch(`api/game/${gameId}/turn`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ swap: true, cardIndexToSwap: selectedCardIndex })
      });
      const data = await response.json();
      setGameState(data);
      setMessage(data.message);
      setDrawnCard(null);
      setSelectedCardIndex(null);
      setWaitingForAction(false);
    } catch (error) {
      setMessage('Error swapping card: ' + error.message);
    }
    setLoading(false);
  };

  const getCardDisplay = (card) => {
    const rankMap = {
      1: 'A',
      11: 'J',
      12: 'Q',
      13: 'K'
    };
    const rank = rankMap[card.value] || card.value;

    const suitSymbols = {
      HEARTS: '♥',
      DIAMONDS: '♦',
      CLUBS: '♣',
      SPADES: '♠'
    };
    const suit = suitSymbols[card.suite] || card.suite;

    const isRed = card.suite === 'HEARTS' || card.suite === 'DIAMONDS';

    return { rank, suit, isRed };
  };

  const CardComponent = ({ card, onClick, selected, isDrawn }) => {
    const { rank, suit, isRed } = getCardDisplay(card);

    return (
      <div
        onClick={onClick}
        className={`
          relative w-24 h-32 bg-white rounded-lg border-2 flex flex-col items-center justify-center
          cursor-pointer transition-all duration-200 shadow-md
          ${selected ? 'border-blue-500 -translate-y-2 shadow-lg' : 'border-gray-300 hover:border-gray-400'}
          ${isDrawn ? 'border-green-500' : ''}
          ${isRed ? 'text-red-600' : 'text-gray-900'}
        `}
      >
        <div className="text-3xl font-bold">{rank}</div>
        <div className="text-4xl">{suit}</div>
      </div>
    );
  };

  const CardBack = () => (
    <div className="w-24 h-32 bg-gradient-to-br from-blue-600 to-blue-800 rounded-lg border-2 border-blue-900 shadow-md flex items-center justify-center">
      <div className="text-white text-2xl">🂠</div>
    </div>
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-800 to-green-600 p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold text-white text-center mb-8">Card Swap Game</h1>

        {/* Game Controls */}
        <div className="bg-white rounded-lg p-6 mb-8 shadow-xl">
          <div className="flex justify-between items-center mb-4">
            <button
              onClick={startNewGame}
              disabled={loading}
              className="flex items-center gap-2 bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 transition-colors"
            >
              {gameId ? <RefreshCw size={20} /> : <Play size={20} />}
              {gameId ? 'New Game' : 'Start Game'}
            </button>

            {gameState && (
              <div className="text-right">
                <div className="text-sm text-gray-600">Round {gameState.roundNumber}</div>
                <div className="text-sm text-gray-600">Deck: {gameState.mainDeckSize} cards</div>
              </div>
            )}
          </div>

          {message && (
            <div className="bg-blue-50 border border-blue-200 rounded p-3 text-sm text-gray-700">
              {message}
            </div>
          )}
        </div>

        {gameState && (
          <>
            {/* Computer Hand */}
            <div className="mb-12">
              <h2 className="text-2xl font-bold text-white mb-4">Computer's Hand</h2>
              <div className="flex gap-4 justify-center">
                {Array.from({ length: gameState.computerHandSize }).map((_, i) => (
                  <CardBack key={i} />
                ))}
              </div>
            </div>

            {/* Drawn Card Area */}
            {drawnCard && (
              <div className="mb-12">
                <h2 className="text-2xl font-bold text-white mb-4 text-center">Drawn Card</h2>
                <div className="flex justify-center mb-4">
                  <CardComponent card={drawnCard} isDrawn={true} />
                </div>
                <div className="flex gap-4 justify-center">
                  <button
                    onClick={discardCard}
                    disabled={loading}
                    className="flex items-center gap-2 bg-red-600 text-white px-6 py-3 rounded-lg hover:bg-red-700 disabled:bg-gray-400 transition-colors"
                  >
                    <X size={20} />
                    Discard
                  </button>
                  <button
                    onClick={swapCard}
                    disabled={loading || selectedCardIndex === null}
                    className="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 disabled:bg-gray-400 transition-colors"
                  >
                    Swap with Selected
                  </button>
                </div>
                {selectedCardIndex === null && (
                  <p className="text-white text-center mt-2 text-sm">
                    Select a card from your hand to swap with
                  </p>
                )}
              </div>
            )}

            {/* Player Hand */}
            <div>
              <h2 className="text-2xl font-bold text-white mb-4">Your Hand</h2>
              <div className="flex gap-4 justify-center mb-6">
                {gameState.playerHand.map((card, index) => (
                  <CardComponent
                    key={card.id}
                    card={card}
                    onClick={() => drawnCard && setSelectedCardIndex(index)}
                    selected={selectedCardIndex === index}
                  />
                ))}
              </div>

              {!drawnCard && !waitingForAction && !gameState.gameOver && (
                <div className="flex justify-center">
                  <button
                    onClick={drawCard}
                    disabled={loading}
                    className="bg-yellow-500 text-white px-8 py-4 rounded-lg text-xl font-bold hover:bg-yellow-600 disabled:bg-gray-400 transition-colors shadow-lg"
                  >
                    Draw Card
                  </button>
                </div>
              )}
            </div>

            {gameState.gameOver && (
              <div className="mt-8 bg-red-100 border-2 border-red-500 rounded-lg p-6 text-center">
                <h2 className="text-2xl font-bold text-red-800 mb-2">Game Over!</h2>
                <p className="text-red-700">{message}</p>
              </div>
            )}
          </>
        )}

        {!gameState && (
          <div className="text-center text-white text-xl">
            Click "Start Game" to begin playing!
          </div>
        )}
      </div>
    </div>
  );
}