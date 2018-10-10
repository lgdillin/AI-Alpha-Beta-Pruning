import java.util.Scanner;
import java.util.Random;

class GameState {
  char[] board;

  GameState() {
    board = new char[9];
    for(int i = 0; i < board.length; ++i) { board[i] = Character.forDigit(i + 1, 10); }//(char)Character.getNumericValue(i + 1); }
  }
}

class Main_Backup {
  static final char HUMAN_MOVE = 'X';
  static final char COMPUTER_MOVE = 'O';

  // Compute the best move for the computer according to the Mini-Max algorithm
  // int miniMax(GameState g, int depth, boolean maximizeUtility) {
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
  //         bestValue = Math.max(bestValue, miniMax(g, depth + 1, !maximizeUtility));
  //         g.board[i] = Character.forDigit(i + 1, 10);
  //       }
  //     }
  //     return bestValue;
  //
  //   } else { // Minimizing player
  //     bestValue = 10000; // positive inf
  //     for(int i = 0; i < g.board.length; ++i) {
  //       if(g.board[i] == Character.forDigit(i + 1, 10)) {
  //         g.board[i] = HUMAN_MOVE;
  //         bestValue = Math.min(bestValue, miniMax(g, depth + 1, !maximizeUtility));
  //         g.board[i] = Character.forDigit(i + 1, 10);
  //       }
  //     }
  //     return bestValue;
  //
  //   }
  // }

  int alphaBetaPruning(GameState g, int depth, int alpha, int beta, boolean maximizeUtility) {
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

  // Returns the computer's best move for use in the actual game
  int bestMove(GameState g) {
    int maxUtility = -1000;
    int maxMove = -1;
    for(int i = 0; i < g.board.length; ++i) { // For all spaces in the game
      if(g.board[i] == Character.forDigit(i + 1, 10)) { // If we find an empty space
        g.board[i] = COMPUTER_MOVE; // Put a temporary move there
        //int utility = miniMax(g, 0, false); // Look into the future and see if its a good idea
        int utility = alphaBetaPruning(g, 0, -10000, 10000, false);
        g.board[i] = Character.forDigit(i + 1, 10); // reset it back

        if(utility > maxUtility) { // Find the best move possible
          maxMove = i;
          maxUtility = utility;
        }
      }
    }
    return maxMove; // return it
  }

  // Updates the gameboard with the new value
  boolean updateBoard(char[] board, boolean human, int move) {
    if(board[move] == HUMAN_MOVE || board[move] == COMPUTER_MOVE) return false;
    else {
      if(human) board[move] = HUMAN_MOVE;
      else board[move] = COMPUTER_MOVE;
    }
    return true;
  }

  // Returns true if all positions in the game board are filled
  boolean boardFull(char[] board) {
    for(int i = 0; i < board.length; ++i) {
      if( !(board[i] == HUMAN_MOVE) && !(board[i] == COMPUTER_MOVE) ) return false; // Game not over
    }
    return true;
  }

  // Checks for a terminal game state
  int checkState(GameState g) {
    if(g.board[0] == g.board[1] && g.board[1] == g.board[2]) { if(g.board[0] == HUMAN_MOVE) return -10; else return 10; }
    if(g.board[3] == g.board[4] && g.board[4] == g.board[5]) { if(g.board[3] == HUMAN_MOVE) return -10; else return 10; }
    if(g.board[6] == g.board[7] && g.board[7] == g.board[8]) { if(g.board[6] == HUMAN_MOVE) return -10; else return 10; }

    if(g.board[0] == g.board[3] && g.board[3] == g.board[6]) { if(g.board[0] == HUMAN_MOVE) return -10; else return 10; }
    if(g.board[1] == g.board[4] && g.board[4] == g.board[7]) { if(g.board[1] == HUMAN_MOVE) return -10; else return 10; }
    if(g.board[2] == g.board[5] && g.board[5] == g.board[8]) { if(g.board[2] == HUMAN_MOVE) return -10; else return 10; }

    if(g.board[0] == g.board[4] && g.board[4] == g.board[8]) { if(g.board[0] == HUMAN_MOVE) return -10; else return 10; }
    if(g.board[6] == g.board[4] && g.board[4] == g.board[2]) { if(g.board[6] == HUMAN_MOVE) return -10; else return 10; }

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
    Main_Backup main = new Main_Backup();
    Scanner keyboard = new Scanner(System.in);

    GameState g = new GameState();
    boolean human = true; // Let the human go first

    // Game loop
    main.drawBoard(g.board);
    while(main.checkState(g) == 3) {

      // Either the human plays or the computer
      int move = -1;
      if(human) {
        System.out.print("Your move: ");
        move = keyboard.nextInt();
      } else {
        move = main.bestMove(g) + 1;
        System.out.println("Computer move: " + move);
      }
      System.out.println();

      if(!main.updateBoard(g.board, human, move - 1)) continue;
      main.drawBoard(g.board);
      human = !human; // take turns
    }
    System.out.println();

    if(main.checkState(g) == 10) System.out.println("AI Wins");
    else if(main.checkState(g) == -10) System.out.println("Human Wins");
    else System.out.println("Draw");
    main.drawBoard(g.board);
  }
}
