package tablut;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

import static tablut.Square.sq;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Weijie Yuan
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        System.out.print("* ");
        System.out.println(findMove());
        return findMove().toString();
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        int sense = myPiece() == WHITE ? 1 : -1;
        findMove(b, maxDepth(b), true, sense, -INFTY, INFTY);
        return _lastFoundMove;
    }

    /** Number of possible first move for BLACK.*/
    static final int NUM_MOVE = 80;

    /** Weight of different metrics.*/
    static final int
            WEIGHT_PIECE = 50,
            WEIGHT_TOEDGE = 10,
            WEIGHT_NUMAROUNDK = 20;

    /** Return winning moves from the current KINGPOS to the edge.*/
    private Move.MoveList winningMove(Square kingPos) {
        Move.MoveList winningMove = new Move.MoveList();
        winningMove.add(Move.mv(kingPos, Square.sq(kingPos.col(), 0)));
        winningMove.add(Move.mv(kingPos, Square.sq(kingPos.col(), 8)));
        winningMove.add(Move.mv(kingPos, Square.sq(0, kingPos.row())));
        winningMove.add(Move.mv(kingPos, Square.sq(8, kingPos.row())));
        return winningMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (sense == 1) {
            return findMax(board, depth, saveMove, alpha, beta);
        } else {
            return findMin(board, depth, saveMove, alpha, beta);
        }
    }

    /** Return a best move of maximizing player for current BOARD with up to
     * DEPTH levels. Any move with value >= BETA is also "good enough".
     * Recording the move found in _lastFoundMove iff SAVEMOVE.
     * Current alpha value is ALPHA. */
    private int findMax(Board board, int depth, boolean saveMove,
                              int alpha, int beta) {
        if (depth == 0) {
            return staticScore(board);
        }
        Move bestSoFar = null;
        int bestScoreSoFar = -INFTY;

        for (Move legalMove : board.legalMoves(WHITE)) {
            Board boardNext = new Board(board);
            boardNext.makeMove(legalMove);
            if (boardNext.winner() == WHITE) {
                if (saveMove) {
                    _lastFoundMove = legalMove;
                }
                return WINNING_VALUE;
            }
            int responseScore = findMin(boardNext, depth - 1,
                    false, alpha, beta);
            if (responseScore > bestScoreSoFar) {
                bestSoFar = legalMove;
                bestScoreSoFar = responseScore;
                alpha = max(alpha, responseScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = bestSoFar;
        }
        return bestScoreSoFar;
    }

    /** Return a best move of minimizing player for current BOARD with up to
     * DEPTH levels. Any move with value <= ALPHA is also "good enough".
     * Recording the move found in _lastFoundMove iff SAVEMOVE.
     * Current beta value is BETA. */
    private int findMin(Board board, int depth, boolean saveMove,
                              int alpha, int beta) {
        if (depth == 0) {
            return staticScore(board);
        }
        Move bestSoFar = null;
        int bestScoreSoFar = INFTY;

        List<Move> legalMoveB = board.legalMoves(BLACK);
        Collections.shuffle(legalMoveB,
                new Random(this._controller.randInt(NUM_MOVE)));
        for (Move legalMove : legalMoveB) {
            Board boardNext = new Board(board);
            boardNext.makeMove(legalMove);
            if (boardNext.winner() == BLACK) {
                if (saveMove) {
                    _lastFoundMove = legalMove;
                }
                return -WINNING_VALUE;
            }
            int responseScore = findMax(boardNext, depth - 1,
                    false, alpha, beta);
            if (responseScore < bestScoreSoFar) {
                bestSoFar = legalMove;
                bestScoreSoFar = responseScore;
                beta = min(beta, responseScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = bestSoFar;
        }
        return bestScoreSoFar;
    }


    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        if (board.moveCount() <= 4) {
            return 2;
        } else if (board.moveCount() <= 12) {
            return 3;
        } else {
            return 4;
        }
    }

    /** Return the number of enemy pieces around the king for the
     * current BOARD. */
    private int enemyPiecesAroundKing(Board board) {
        int numPiece = 0;
        Square kingPos = board.kingPosition();
        Square.SqList kingNeighbors = kingPos.getNeighbors();
        for (Square s : kingNeighbors) {
            if (board.isHostile(s, WHITE)) {
                numPiece += 1;
            }
        }
        return numPiece;
    }

    /** Return the number of pieces in the edge for the current
     * BOARD. */
    private int piecesInEdge(Board board) {
        int numInEdge = 0;
        for (Square sq : board.pieceLocations(WHITE)) {
            if (sq.isEdge()) {
                numInEdge += 1;
            }
        }
        for (Square sq : board.pieceLocations(BLACK)) {
            if (sq.isEdge()) {
                numInEdge += 1;
            }
        }
        return numInEdge;
    }

    /** Return the number of clear path of king to the edge for
     * the current BOARD. */
    private int clearPathsToEdge(Board board) {
        Square kingPos = board.kingPosition();
        int numClearPath = 0;
        Move.MoveList winningMoves = winningMove(kingPos);
        for (Move mv : winningMoves) {
            if (mv != null && board.isUnblockedMove(mv.from(), mv.to())) {
                numClearPath += 1;
            }
        }
        return numClearPath;
    }

    /** Return the score of different clear paths of king to the
     * edge for the current BOARD. */
    private int clearKingToEdge(Board board) {
        Square kingPosition = board.kingPosition();
        Move.MoveList kingMoves = getLegalKingMovesForPosition(
                kingPosition, board);
        int score = 0;
        if (!kingMoves.isEmpty()) {
            Square.SqList allEdges = new Square.SqList();
            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    if (Square.sq(i, j).isEdge()) {
                        allEdges.add(sq(i, j));
                    }
                }
            }
            int[] distances = new int[Board.SIZE * 4 - 4];
            int edgeIndex = 0;
            for (Square s: allEdges) {
                distances[edgeIndex++] = minMovesToEdge(board, s,
                        1, kingPosition);
            }

            for (int i = 0; i < distances.length; i++) {
                switch (distances[i]) {
                case 1:
                    score += 10;
                    break;
                case 2:
                    score += 2;
                    break;
                default:
                    score += 0;
                    break;
                }
            }
        }
        return score;
    }

    /** Return legal moves for the current king position S and BOARD.*/
    private static Move.MoveList getLegalKingMovesForPosition(Square s,
                                                              Board board) {
        Move.MoveList legalMoves = new Move.MoveList();
        Square.SqList kingCandidates = new Square.SqList();

        kingCandidates.addAll(legalSquareInDirection(s, -1, 0, board));
        kingCandidates.addAll(legalSquareInDirection(s, 0, -1, board));
        kingCandidates.addAll(legalSquareInDirection(s, 1, 0, board));
        kingCandidates.addAll(legalSquareInDirection(s, 0, 1, board));

        for (Square dest: kingCandidates) {
            legalMoves.add(Move.mv(s, dest));
        }
        return legalMoves;
    }

    /** Return the legal square list in direction (X,Y) from START
     * for the current BOARD.*/
    private static Square.SqList legalSquareInDirection(
            Square start, int x, int y, Board board) {
        Square.SqList sqs = new Square.SqList();
        assert (!(x != 0 && y != 0));
        int startPos = (x != 0) ? start.col() : start.row();
        int incr = (x != 0) ? x : y;
        int endIdx = (incr == 1) ? 9 - 1 : 0;

        for (int i = startPos + incr; incr * i <= endIdx; i += incr) {
            Square s = (x != 0) ? Square.sq(i, start.row())
                    : Square.sq(start.col(), i);
            if (board.get(s).side() == EMPTY) {
                sqs.add(s);
            } else {
                break;
            }
        }
        return sqs;
    }

    /** Return the minimum moves for the current KINGPOSITION to EDGE.
     * Current board position is BOARD and limit move count is MOVECNT. */
    private static int minMovesToEdge(Board board, Square edge,
                                      int moveCnt, Square kingPosition) {
        if (moveCnt == 3 || kingPosition.isEdge()) {
            return moveCnt;
        }
        Move.MoveList kingMoves = getLegalKingMovesForPosition(kingPosition,
                board);
        int[] moveCounts = new int[kingMoves.size()];
        int moveIdx = 0;
        for (Move m : kingMoves) {
            if (movesToEdge(m.from(), edge) > movesToEdge(m.to(), edge)) {
                moveCounts[moveIdx++] = minMovesToEdge(board, edge,
                        moveCnt + 1, m.to());
            }
        }
        int minMoves = 100;
        for (int i = 0; i < moveCounts.length; i++) {
            int current = moveCounts[i];
            if (current != 0 && current < minMoves) {
                minMoves = current;
            }
        }
        return minMoves;
    }

    /** Return manhattan distance of the move from S to E. */
    private static int movesToEdge(Square s, Square e) {
        return Math.abs(s.col() - e.col()) + Math.abs(s.row() - e.row());
    }

    /** Return a heuristic value for BOARD. */
    int staticScore(Board board) {
        if (board.winner() ==  BLACK) {
            return -WINNING_VALUE;
        }
        if (board().winner() == WHITE) {
            return WINNING_VALUE;
        }
        int whiteNum = board.pieceLocations(WHITE).size();
        int blackNum = board.pieceLocations(BLACK).size();
        Square kingPos = board.kingPosition();
        int numEnemyAroundK = enemyPiecesAroundKing(board);
        int toEdge = clearKingToEdge(board);
        int numInEdge = piecesInEdge(board);
        int numClearPaths = clearPathsToEdge(board);
        if (numClearPaths >= 2) {
            return WILL_WIN_VALUE;
        }
        Move.MoveList winningMoves = winningMove(kingPos);
        for (Move mv : winningMoves) {
            if (mv != null && board.isLegal(mv)) {
                return WILL_WIN_VALUE;
            }
        }
        Square.SqList neighOfKing = kingPos.getNeighbors();
        for (Square neigh : neighOfKing) {
            if (board.get(neigh) == BLACK && board.kingOutThrone()) {
                int dir = kingPos.direction(neigh);
                int turn = (dir + 2) & 3;
                Square oppositeSq = kingPos.rookMove(turn, 1);
                for (Square blackPos : board.pieceLocations(BLACK)) {
                    if (board.isLegal(blackPos, oppositeSq)) {
                        return -WILL_WIN_VALUE;
                    }
                }
            } else if (numEnemyAroundK == 3 && !board.kingOutThrone()) {
                if (!board.isHostile(neigh, WHITE)) {
                    for (Square blackPos : board.pieceLocations(BLACK)) {
                        if (board.isLegal(blackPos, neigh)) {
                            return -WILL_WIN_VALUE;
                        }
                    }
                }
            }
        }
        int score = WEIGHT_PIECE * whiteNum - WEIGHT_PIECE * blackNum
                + WEIGHT_TOEDGE * toEdge - WEIGHT_NUMAROUNDK * numEnemyAroundK
                - numInEdge;
        return score;
    }
}
