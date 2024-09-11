The provided Java code implements a simple Snake game using Swing for GUI. Let's go through each function and variable used in the code:

Imports
java.awt.*: Provides classes for graphics and GUI components (e.g., Color, Dimension, Graphics).
java.awt.event.*: Provides classes for event handling (e.g., ActionListener, KeyListener, ActionEvent).
java.util.ArrayList: A resizable array implementation, used to store the snake's body.
java.util.Random: Provides methods to generate random numbers, used to place food randomly on the board.
javax.swing.*: Provides classes for creating a Swing-based GUI (e.g., JPanel, JOptionPane, Timer).
Class SnakeGame
SnakeGame is a class that extends JPanel and implements ActionListener and KeyListener.

Inner Class Tile
Represents a tile on the game board.
Variables:
int x, y: Coordinates of the tile.
Constructor:
Tile(int x, int y): Initializes a tile with given x and y coordinates.
Instance Variables
int boardWidth, boardHeight: Dimensions of the game board.
int tileSize = 16: Size of each tile (in pixels).
Tile snakeHead: Represents the head of the snake.
ArrayList<Tile> snakeBody: List storing the snake's body segments.
Tile food: Represents the food item.
Random random: Used to generate random positions for food.
Timer gameLoop: A timer that triggers the game logic at regular intervals.
int velocityX, velocityY: Represents the snake's velocity in the x and y directions.
boolean gameOver = false: Flag to check if the game is over.
int highScore = 0: Stores the highest score achieved.
Constructor: SnakeGame(int boardWidth, int boardHeight)
Initializes the game with a given board width and height.

Sets up the GUI properties and initializes the game state, such as snake's head, body, and food.
Initializes velocity and starts the Timer for game logic.
Method: paintComponent(Graphics g)
Overrides JPanel's paintComponent method to draw the game components.

Calls draw(g) to draw the snake, food, and score.
Method: draw(Graphics g)
Draws the game components on the screen.

Draw Food: Draws the food tile in orange.
Draw Snake Head: Draws the snake's head in blue.
Draw Snake Body: Iterates through snakeBody and draws each segment.
Draw Score and Game Over Message: Displays the score or a "Game Over" message.
Method: placeFood()
Randomly places the food on the board.

Uses random.nextInt() to generate random coordinates for the food within the board boundaries.
Method: collision(Tile tile1, Tile tile2)
Checks if two tiles occupy the same position.

Returns true if the x and y coordinates of both tiles match.
Method: move()
Handles the snake's movement and game logic.

Eat Food: If the snake's head collides with the food, it adds a new segment to the body and places new food.
Move Snake Body: Moves each segment of the snake's body to the position of the previous segment.
Move Snake Head: Updates the head position based on the current velocity.
Check Game Over Conditions:
Collides with its own body.
Goes out of bounds.
Method: actionPerformed(ActionEvent e)
Triggered by the Timer every 100 milliseconds to update the game state.

Calls move() and repaint() to refresh the GUI.
Stops the game if gameOver is true and checks for a new high score.
Method: showGameOverDialog()
Displays a dialog box when the game is over.

Allows the player to choose to restart or exit the game.
Method: resetGame()
Resets the game to its initial state.

Resets the snake's position, body, food, velocity, and game state.
Key Listener Methods
Handles keyboard input for controlling the snake's direction.

keyPressed(KeyEvent e): Changes the snake's velocity based on arrow key presses (UP, DOWN, LEFT, RIGHT) or W, A, S, D keys.
keyTyped(KeyEvent e): Empty implementation.
keyReleased(KeyEvent e): Empty implementation.
Summary
This code is a complete implementation of a simple Snake game. The main components include:

Game Logic: Handled in the move() method.
Drawing Logic: Handled in draw(Graphics g).
User Input: Handled in keyPressed(KeyEvent e).
Game Loop: Managed by a Timer (gameLoop).