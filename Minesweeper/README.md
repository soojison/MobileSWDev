# Minesweeper 

A simple 5x5 minesweeper game with 3 bombs;

## Installation

Android Studio > Build > Build apk

## How to play

The game is simple, with 5x5 grid and 3 bombs. 
Choose an action, either Explore or Flag, from the two buttons under the
minesweeper grid. Then, click on a tile to perform the action.

1. If you chose explore, and the tile contains a bomb, the game ends.
2. If you chose explore, and the tile does not contain a bomb, the field
   expands with relevant hints.
3. If you chose flag, and the tile contains a bomb, the flag is placed.
4. If you chose flag, and the tile does not contain a bomb, the game ends.

When the game ends by triggering the bomb, you lose.
When the game ends by successfully flagging all the bombs and exploring all the
tiles, you win, and the game informs you how long it took to finish the game.

## Credits

* Few code blocks adapted from the TicTacToe in-class project
* Icon designed by Sooji Son, internal design elements sourced from the original
  minesweeper game.
