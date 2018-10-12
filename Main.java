import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;


class Main {

  // int alphaBetaPruning(ChessState s, int depth, int alpha, int beta, boolean maximizeUtility) {
  //   int score = checkState(g);
  //   if(score == 10) return score - depth;
  //   else if(score == -10) return score + depth;
  //   else if(score != 3) return 0; // Tie game
  //
  //   int bestValue;
  //   if(maximizeUtility) {
  //     bestValue = -10000; // Supposed to be neg inf
  //
  //     for(int i = 0; i < g.board.length; ++i) {
  //       if(g.board[i] == Character.forDigit(i + 1, 10)) {
  //         g.board[i] = COMPUTER_MOVE;
  //         bestValue = Math.max(bestValue, alphaBetaPruning(g, depth + 1, alpha, beta, !maximizeUtility));
  //         g.board[i] = Character.forDigit(i + 1, 10);
  //
  //         alpha = Math.max(alpha, bestValue);
  //         if(alpha >= beta) break;
  //       }
  //     }
  //     return bestValue;
  //
  //   } else { // Minimizing player
  //     bestValue = 10000; // positive inf
  //     for(int i = 0; i < g.board.length; ++i) {
  //       if(g.board[i] == Character.forDigit(i + 1, 10)) {
  //         g.board[i] = HUMAN_MOVE;
  //         bestValue = Math.min(bestValue, alphaBetaPruning(g, depth + 1, alpha, beta, !maximizeUtility));
  //         g.board[i] = Character.forDigit(i + 1, 10);
  //
  //         beta = Math.min(beta, bestValue);
  //         if(alpha >= beta) break;
  //       }
  //     }
  //     return bestValue;
  //
  //   }
  // }

  String bestMove(ChessState s, boolean light, int lookAhead) {
    int alpha = -10000;
    int beta = 10000;
    int maxUtility = -1000;
    int maxMove = -1;

    // Its easier just to create a deep copy for the recursion
    ChessState movePlan = new ChessState(s);

    ChessState.ChessMoveIterator it = movePlan.iterator(false);
    while(it.hasNext()) {
      // Get the next piece
      ChessState.ChessMove m = it.next();
      ArrayList<Integer> moves = movePlan.moves(m.xSource, m.ySource);
      for(int i = 0; i < moves.size(); ++i) {
        System.out.println(moves.get(i));
        break;
      }
      break;
      // Move it
      // get the score
      // put it back in its place

      //int utility = alphaBetaPruning(movePlan, lookAhead, alpha, beta, false);

      // if(utility > maxUtility) {
      //   // save the best move
      //   maxUtility = utility;
      // }

    }
    // return the best move
    return "";
  }

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

  static int[] moveTranslation(String move) {
    if(!validateHumanMove(move)) return null;

    int[] translatedMove = new int[4];
    translatedMove[1] = Character.getNumericValue(move.charAt(1)) - 1;
    translatedMove[3] = Character.getNumericValue(move.charAt(3)) - 1;


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
    ChessState.ChessMoveIterator it = s.iterator(true); // Iterator for valid moves

    // Game Loop
    ChessState.ChessMove m;
    boolean lightPlayer = true;
    String move;
    while(true) {
      s.printBoard(System.out);

      if(lightPlayer) {
        if(lightHuman) {
          System.out.print("Light move: ");
          move = keyboard.nextLine();
        } else {
          move = main.bestMove(s, lightPlayer, depthLight);
        }
      } else {
        if(darkHuman) {
          System.out.print("Light move: ");
          move = keyboard.nextLine();
        } else {
          move = main.bestMove(s, lightPlayer, depthDark);
        }
      }
      s.move(move.charAt(0), move.charAt(1), move.charAt(2), move.charAt(3));


    }
    //ChessState.ChessMove m = new ChessState.ChessMove(); // for the light player

    //s.move(m.xSource, m.ySource, m.xDest, m.yDest);
    //int h = s.heuristic(r);


  }
}
