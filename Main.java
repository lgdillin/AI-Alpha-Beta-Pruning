import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;


class Main {
  Random r;

  Main() {
    r = new Random(123456);
  }

  int alphaBetaPruning(ChessState s, int depth, int alpha, int beta, boolean maximizeUtility, boolean light) {
    int score = s.heuristic(r);
    if(depth == 0 || s.gameOver()) return score;

    int bestValue;
    if(maximizeUtility) {
      bestValue = -1000000; // Supposed to be neg inf

      // Find all movable pieces for dark
      ChessState.ChessMoveIterator it = s.iterator(light);
      while(it.hasNext()) {
        // Get the next piece
        ChessState.ChessMove m = it.next();

        // Deep copy the board
        ChessState newBoard = new ChessState(s);

        // Move the next piece to its legal destination
        newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);

        bestValue = Math.max(bestValue, alphaBetaPruning(newBoard, depth - 1, alpha, beta, !maximizeUtility, !light));

        // compare to our level
        alpha = Math.max(alpha, bestValue);
        if(alpha >= beta) break;
      }
      return bestValue;

    } else { // Minimizing player
      bestValue = 1000000; // positive inf

      // Find all movable pieces for light
      ChessState.ChessMoveIterator it = s.iterator(light);
      while(it.hasNext()) {
        ChessState.ChessMove m = it.next(); // Get the next piece

        // Deep copy the board
        ChessState newBoard = new ChessState(s);

        // Move the next piece to its legal destination
        newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);

        bestValue = Math.min(bestValue, alphaBetaPruning(newBoard, depth - 1, alpha, beta, !maximizeUtility, !light));

        beta = Math.min(beta, bestValue);
        if(alpha >= beta) break;
      }
      return bestValue;

    }
  }

  // Entry point for alpha-beta pruning
  ChessState.ChessMove bestMove(ChessState s, boolean light, int lookAhead) {
    int alpha = -1000000;
    int beta = 1000000;
    int maxUtility = -100000;
    ChessState.ChessMove maxMove = new ChessState.ChessMove();

    ChessState.ChessMoveIterator it = s.iterator(light);
    while(it.hasNext()) {
      // Get the next piece
      ChessState.ChessMove m = it.next();

      // Its easier just to create a deep copy for the recursion
      // Than to try to put pieces back
      ChessState newBoard = new ChessState(s);

      // Move the piece to its next legal position
      newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);

      // Test for utility
      int utility = alphaBetaPruning(newBoard, lookAhead, alpha, beta, false, !light);

      if(utility > maxUtility) {
        maxMove = m;
        maxUtility = utility;
      }
    }
    // return the best move
    return maxMove;
  }

  // parse the input from the human and ensure it is a valid move
  static boolean validateHumanMove(String move) {
    String validator = "abcdefgh";
    boolean xMove1 = false;
    boolean xMove2 = false;

    // Check if the move is the right length
    if(move.length() != 4) return false;

    // Check if the xCoords fall within proper bounds
    for(int i = 0; i < validator.length(); ++i) {
      if(move.charAt(0) == validator.charAt(i)) xMove1 = true;
      if(move.charAt(2) == validator.charAt(i)) xMove2 = true;
    }
    if(!xMove1 || !xMove2) return false;

    // Check that the yCoords fall within proper bounds
    int yMove1 = Character.getNumericValue(move.charAt(1));
    int yMove2 = Character.getNumericValue(move.charAt(3));
    if(yMove1 < 1 || yMove1 > 8) return false;
    if(yMove2 < 1 || yMove2 > 8) return false;

    return true;
  }

  // Translate the move from a char string to an array of positions
  static int[] moveTranslation(String move) {
    if(!validateHumanMove(move)) return null;

    // Translate the numbers
    int[] translatedMove = new int[4];

    translatedMove[1] = Character.getNumericValue(move.charAt(1)) - 1;
    translatedMove[3] = Character.getNumericValue(move.charAt(3)) - 1;

    // Translate the Characters
    translatedMove[0] = Character.getNumericValue(move.charAt(0)) - 10;
    translatedMove[2] = Character.getNumericValue(move.charAt(2)) - 10;

    //for(int i = 0; i < 4; ++i) { System.out.print(translatedMove.get(i) + " "); } System.out.println();

    return translatedMove;
  }


  public static void main(String[] args) {
    // Parse command line arguments
    if(args.length != 2) throw new IllegalArgumentException("Wrong number of args");
    if(args[0].length() != 1) throw new IllegalArgumentException("invalid args[0] length");
    if(args[1].length() != 1) throw new IllegalArgumentException("invalid args[1] length");
    if(Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 5) throw new IllegalArgumentException("args[0] out of bounds");
    if(Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 5) throw new IllegalArgumentException("args[1] out of bounds");

    // NOTE: light is on the left, dark is on the right
    int depthLight = 0;
    boolean lightHuman = false;
    if(args[0].equals("0")) {
      lightHuman = true;
      // Left player is human
    } else {
      // Assign left player an AI difficulty
      depthLight = Integer.parseInt(args[0]);
    }

    int depthDark = 0;
    boolean darkHuman = false;
    if(args[1].equals("0")) {
      darkHuman = true;
      // Right player is human
    } else {
      depthDark = Integer.parseInt(args[1]);
      // Assign right player AI difficulty
    }

    // Initialize GameState
    Main main = new Main();
    Random r = new Random(123456);
    Scanner keyboard = new Scanner(System.in);
    ChessState s = new ChessState(); // Give us a new board
    s.resetBoard(); // Prepare the board for play

    // Game Loop
    boolean lightPlayer = true;
    boolean gameOver = false;
    while(true) {
      s.printBoard(System.out);

      ChessState.ChessMove move = new ChessState.ChessMove();
      if(lightPlayer) {
        if(lightHuman) {
          System.out.print("Light move: ");
          int[] tempMove = Main.moveTranslation(keyboard.nextLine());
          move.xSource = tempMove[0];
          move.ySource = tempMove[1];
          move.xDest = tempMove[2];
          move.yDest = tempMove[3];

          // Handle invalid moves
          try {
            gameOver = s.move(move.xSource, move.ySource, move.xDest, move.yDest);
            lightPlayer = !lightPlayer;
          } catch(Exception e) {
            System.out.println("Invalid move");
          }
        } else {
          move = main.bestMove(s, lightPlayer, depthLight);
          gameOver = s.move(move.xSource, move.ySource, move.xDest, move.yDest);
          lightPlayer = !lightPlayer;
        }
      } else {
        if(darkHuman) {
          System.out.print("Dark move: ");
          int[] tempMove = Main.moveTranslation(keyboard.nextLine());
          move.xSource = tempMove[0];
          move.ySource = tempMove[1];
          move.xDest = tempMove[2];
          move.yDest = tempMove[3];

          // Handle invalid moves
          try {
            gameOver = s.move(move.xSource, move.ySource, move.xDest, move.yDest);
            lightPlayer = !lightPlayer;
          } catch(Exception e) {
            System.out.println("Invalid move");
          }
        } else {
          move = main.bestMove(s, lightPlayer, depthDark);
          gameOver = s.move(move.xSource, move.ySource, move.xDest, move.yDest);
          lightPlayer = !lightPlayer;
        }
      }

      if(gameOver) break;
    }

    System.out.println();
    System.out.println();
    s.printBoard(System.out);
    if(!lightPlayer) {  // Since the flag switches after a turn, we need to invert the victory speech
      System.out.println("Light wins!");
    } else {
      System.out.println("Dark wins!");
    }
  }

}
