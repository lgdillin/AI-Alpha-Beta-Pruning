import java.util.Random;
import java.util.Scanner;


class Main {

  int alphaBetaPruning(ChessState s, int depth, int alpha, int beta, boolean maximizeUtility) {
    int score = checkState(g);
    if(score == 10) return score - depth;
    else if(score == -10) return score + depth;
    else if(score != 3) return 0; // Tie game

    int bestValue;
    if(maximizeUtility) {
      bestValue = -10000; // Supposed to be neg inf

      for(int i = 0; i < g.board.length; ++i) {
        if(g.board[i] == Character.forDigit(i + 1, 10)) {
          g.board[i] = COMPUTER_MOVE;
          bestValue = Math.max(bestValue, alphaBetaPruning(g, depth + 1, alpha, beta, !maximizeUtility));
          g.board[i] = Character.forDigit(i + 1, 10);

          alpha = Math.max(alpha, bestValue);
          if(alpha >= beta) break;
        }
      }
      return bestValue;

    } else { // Minimizing player
      bestValue = 10000; // positive inf
      for(int i = 0; i < g.board.length; ++i) {
        if(g.board[i] == Character.forDigit(i + 1, 10)) {
          g.board[i] = HUMAN_MOVE;
          bestValue = Math.min(bestValue, alphaBetaPruning(g, depth + 1, alpha, beta, !maximizeUtility));
          g.board[i] = Character.forDigit(i + 1, 10);

          beta = Math.min(beta, bestValue);
          if(alpha >= beta) break;
        }
      }
      return bestValue;

    }
  }

  int bestMove(boolean light) {

  }




  public static void main(String[] args) {
    // Parse command line arguments
    if(args.length != 2) throw new IllegalArgumentException("Wrong number of args");
    if(args[0].length() != 1) throw new IllegalArgumentException("invalid arg length");
    if(args[0].length() != 1) throw new IllegalArgumentException("invalid arg length");
    if(Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 5) throw new IllegalArgumentException("args[0] out of bounds");
    if(Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 5) throw new IllegalArgumentException("args[1] out of bounds");

    // NOTE: light is on the left, dark is on the right
    int depthLeft;
    boolean leftHuman = false;
    if(args[0] == "0") {
      leftHuman = true;
      // Left player is human
    } else {
      // Assign left player an AI difficulty
      depthLeft = Integer.parseInt(args[0]);
    }

    int depthRight;
    boolean rightHuman = false;
    if(args[1] == "0") {
      rightHuman = true;
      // Right player is human
    } else {
      depthRight = Integer.parseInt(args[1]);
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
        if(leftHuman) {
          System.out.print("Light move: ");
          move = keyboard.nextLine();
        } else {
          main.bestMove(lightPlayer);
        }
      } else {
        if(rightHuman) {
          System.out.print("Light move: ");
          move = keyboard.nextLine();
        } else {
          main.bestMove(lightPlayer);
        }
      }
    }
    //ChessState.ChessMove m = new ChessState.ChessMove(); // for the light player

    // Gameloop
    while(it.hasNext()) {
      m = it.next();
    }

    s.move(m.xSource, m.ySource, m.xDest, m.yDest);
    int h = s.heuristic(r);


  }
}
