import java.util.Scanner;
import java.util.Random;

class GameState {
  char[] board;

  GameState() {
    board = new char[9];
    for(int i = 0; i < board.length; ++i) { board[i] = Character.forDigit(i + 1, 10); }//(char)Character.getNumericValue(i + 1); }
  }
}

class Main {

  // Compute the best move for the computer according to the Mini-Max algorithm
  // int miniMax(GameState g, int depth, boolean maximizingPlayer) {
  //   //if(depth == 0 || node == checkBoard()) { return heuristic; }
  //   int score = checkState(g);
  //   if(score == 1) return score - depth;
  //   else if(score == -1) return score + depth;
  //   else if(score != 3) return 0;
  //
  //   int bestValue;
  //   if(maximizingPlayer) {
  //     bestValue = -1000; // Supposed to be neg inf
  //
  //     for(int i = 0; i < g.board.length; ++i) {
  //       bestValue = Math.max(bestValue, miniMax(g.board[i], depth - 1, false));
  //     }
  //
  //     return bestValue;
  //   } else { // Minimizing player
  //     bestValue = 1000; // positive inf
  //     for(int i = 0; i < g.board.length; ++i) {
  //       bestValue = Math.min(bestValue, miniMax(g.board[i], depth - 1, true));
  //     }
  //
  //     return bestValue;
  //   }
  // }

  // Returns the computer's best move for use in the actual game
  int bestMove(GameState g) {
    int maxUtility = -1000;
    int maxMove = -1;
    for(int i = 0; i < g.board.length; ++i) {

    }
    return maxMove;
  }

  // Updates the gameboard with the new value
  boolean updateBoard(char[] board, boolean human, int move) {
    if(board[move] == 'X' || board[move] == 'O') return false;
    else {
      if(human) board[move] = 'X';
      else board[move] = 'O';
    }
    return true;
  }

  // Returns true if all positions in the game board are filled
  boolean boardFull(char[] board) {
    for(int i = 0; i < board.length; ++i) {
      if( !(board[i] == 'X') && !(board[i] == 'O') ) return false; // Game not over
    }
    return true;
  }

  // Checks for a terminal game state
  int checkState(GameState g) {
    if(g.board[0] == g.board[1] && g.board[1] == g.board[2]) { if(g.board[0] == 'X') return 1; else return -1; }
    if(g.board[3] == g.board[4] && g.board[4] == g.board[5]) { if(g.board[0] == 'X') return 1; else return -1; }
    if(g.board[6] == g.board[7] && g.board[7] == g.board[8]) { if(g.board[0] == 'X') return 1; else return -1; }

    if(g.board[0] == g.board[3] && g.board[3] == g.board[6]) { if(g.board[0] == 'X') return 1; else return -1; }
    if(g.board[1] == g.board[4] && g.board[4] == g.board[7]) { if(g.board[0] == 'X') return 1; else return -1; }
    if(g.board[2] == g.board[5] && g.board[5] == g.board[8]) { if(g.board[0] == 'X') return 1; else return -1; }

    if(g.board[0] == g.board[4] && g.board[4] == g.board[8]) { if(g.board[0] == 'X') return 1; else return -1; }
    if(g.board[6] == g.board[4] && g.board[4] == g.board[2]) { if(g.board[0] == 'X') return 1; else return -1; }

    if(!boardFull(g.board)) return 3; // Game not over
    else return 0; // Tie Game
  }

  // Draws a tic-tac-toe board
  void drawBoard(char[] values) {
    System.out.println(" " + values[0] + " | " + values[1] + " | " + values[2] + " ");
    System.out.println("---+---+---");
    System.out.println(" " + values[3] + " | " + values[4] + " | " + values[5] + " ");
    System.out.println("---+---+---");
    System.out.println(" " + values[6] + " | " + values[7] + " | " + values[8] + " ");
  }

  public static void main(String[] args) {
    Main main = new Main();
    Scanner keyboard = new Scanner(System.in);

    Random r = new Random();

    GameState g = new GameState();
    boolean human = true; // Let the human go first

    // Game loop
    while(main.checkState(g) == 3) {
      main.drawBoard(g.board);

      // Either the human plays or the computer
      int move = -1;
      if(human) {
        System.out.print("Your move: ");
        move = keyboard.nextInt();
      } else {
        // move = ComputerMove() + 1;
        move = r.nextInt(9) + 1;
      }
      System.out.println();

      if(!main.updateBoard(g.board, human, move - 1)) continue;
      human = !human; // take turns
    }

    main.drawBoard(g.board);
    if(main.checkState(g) == -1) System.out.println("AI Wins");
    else if(main.checkState(g) == 1) System.out.println("Human Wins");
    else System.out.println("Draw");
  }
}
