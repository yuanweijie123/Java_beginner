package tablut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author Weijie Yuan
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /** Throne position and its surrounding sqs. */
    static final SqList THRONE_LIST = new SqList(
            Arrays.asList(NTHRONE, STHRONE, WTHRONE, ETHRONE, THRONE)
    );

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        _turn = model._turn;
        _winner = model._winner;
        _moveCount = model._moveCount;
        _repeated = model._repeated;
        _moveLimit = model._moveLimit;
        _board = new Piece[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                _board[r][c] = model._board[r][c];
            }
        }
        _boardHistory = new HashSet<>();
        _boardHistory.addAll(model.boardHistory());
        _undoPiece = new Stack<>();
        _undoPiece.addAll(model.undoPiece());
        _undoSquare = new Stack<>();
        _undoSquare.addAll(model.undoSquare());
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = BLACK;
        _winner = null;
        _moveCount = 0;
        _repeated = false;
        _moveLimit = Integer.MAX_VALUE / 2;
        _board = new Piece[SIZE][SIZE];
        _boardHistory = new HashSet<>();
        _undoPiece = new Stack<>();
        _undoSquare = new Stack<>();

        for (Square s : SQUARE_LIST) {
            put(EMPTY, s);
        }
        put(KING, THRONE);
        for (Square s : INITIAL_ATTACKERS) {
            put(BLACK, s);
        }
        for (Square s : INITIAL_DEFENDERS) {
            put(WHITE, s);
        }

        _boardHistory.add(encodedBoard());
    }

    /** Set the move limit to N.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        _moveLimit = n;
        if (moveCount() >= _moveLimit * 2) {
            throw new Error("Move limit has been exceeded.");
        }
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the current board state. */
    Piece[][] board() {
        return _board;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Return the board position currently encountered. */
    Set<String> boardHistory() {
        return _boardHistory;
    }

    /** Return stack of piece for undoing. */
    Stack<Piece> undoPiece() {
        return _undoPiece;
    }

    /** Return stack of square for undoing. */
    Stack<Square> undoSquare() {
        return _undoSquare;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {
        if (_boardHistory.contains(encodedBoard())) {
            _winner = _turn;
            _repeated = true;
        } else {
            _boardHistory.add(encodedBoard());
        }
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (_board[r][c].equals(KING)) {
                    return sq(c, r);
                }
            }
        }
        return null;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return _board[row][col];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(row - '1', col - 'a');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _board[s.row()][s.col()] = p;
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        put(p, s);
        _undoPiece.push(p);
        _undoSquare.push(s);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        if (!from.isRookMove(to)) {
            return false;
        }
        int dir = from.direction(to);
        int dist;
        if (from.col() == to.col()) {
            dist = Math.abs(from.row() - to.row());
        } else {
            dist = Math.abs(from.col() - to.col());
        }
        for (int step = 1; step <= dist; step++) {
            Square checkSquare = from.rookMove(dir, step);
            assert checkSquare != null;
            if (get(checkSquare) != EMPTY) {
                return false;
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (!isLegal(from)) {
            return false;
        }
        if (isUnblockedMove(from, to)) {
            if (to == THRONE) {
                return get(from) == KING;
            }
            return true;
        }
        return false;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        Move curMove = mv(from, to);
        if (curMove != null) {
            _undoSquare.push(from);
            revPut(get(from), to);
            put(EMPTY, from);
            _moveCount++;
            if (get(to) == KING && to.isEdge()) {
                _winner = WHITE;
            }
            for (int dir = 0; dir < 4; dir++) {
                Square sq2 = to.rookMove(dir, 2);
                capture(to, sq2);
            }
            if (_moveCount >= 2 * _moveLimit && _winner == null) {
                _winner = _turn;
            }
            _turn = _turn.opponent();
            if (_winner == null) {
                checkRepeated();
            }
            if (!hasMove(_turn) && _winner == null) {
                _winner = _turn.opponent();
            }
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Return true iff the input square SQ is hostile to SIDE.*/
    boolean isHostile(Square sq, Piece side) {
        if (get(sq).side() == side.opponent()
                || (sq.equals(THRONE) && get(THRONE) == EMPTY)) {
            return true;
        }
        if (side == WHITE) {
            if (sq.equals(THRONE) && get(THRONE) == KING) {
                int countBlack = 0;
                for (int dir = 0; dir < 4; dir++) {
                    Square surrounding = sq.rookMove(dir, 1);
                    assert surrounding != null;
                    if (get(surrounding) == BLACK) {
                        countBlack += 1;
                    }
                }
                return countBlack == 3;
            }
        }
        return false;
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        boolean kingCap = false;
        if (sq2 != null) {
            Square cap = sq0.between(sq2);
            if (get(cap) == KING && THRONE_LIST.contains(cap)) {
                int cntHostile = 0;
                for (int dir = 0; dir < 4; dir++) {
                    Square surSq = cap.rookMove(dir, 1);
                    if (surSq != null && isHostile(surSq, WHITE)) {
                        cntHostile += 1;
                    }
                }
                if (cntHostile == 4) {
                    kingCap = true;
                    _undoPiece.push(get(cap));
                    revPut(EMPTY, cap);
                    _winner = BLACK;
                }
            }
            if (!kingCap) {
                boolean enclose = isHostile(sq0, get(cap).side())
                        && isHostile(sq2, get(cap).side());
                if ((enclose && get(cap) != KING)
                        || (enclose && get(cap) == KING && kingOutThrone())) {
                    _undoPiece.push(get(cap));
                    if (get(cap) == KING) {
                        _winner = BLACK;
                    }
                    revPut(EMPTY, cap);
                }
            }
        }
    }

    /** Return true iff the king is out of the throne or its surrounding sqs. */
    boolean kingOutThrone() {
        Square king = kingPosition();
        for (Square loc : THRONE_LIST) {
            if (king.equals(loc)) {
                return false;
            }
        }
        return true;
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            Piece curP = _undoPiece.pop();
            if (curP == EMPTY) {
                Piece prePiece = _undoPiece.pop();
                Square capSq = _undoSquare.pop();
                put(prePiece, capSq);
                undo();
            } else {
                Square toSq = _undoSquare.pop();
                Square fromSq = _undoSquare.pop();
                put(EMPTY, toSq);
                put(curP, fromSq);
                _turn = _turn.opponent();
                _moveCount = _moveCount - 1;
            }
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        if (!repeatedPosition() && _moveCount > 0) {
            String currBoard = encodedBoard();
            _boardHistory.remove(currBoard);
        }
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        _boardHistory = new HashSet<>();
        _undoPiece = new Stack<>();
        _undoSquare = new Stack<>();
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        Set<Square> sideLocation = pieceLocations(side);
        List<Move> resMove = new ArrayList<>();
        if (side == WHITE) {
            Square from = kingPosition();
            if (from != null) {
                for (int i = 0; i < SIZE; i++) {
                    Move horizontalMove = mv(from, sq(from.col(), i));
                    Move verticalMove = mv(from, sq(i, from.row()));
                    if (horizontalMove != null && isLegal(horizontalMove)) {
                        resMove.add(horizontalMove);
                    }
                    if (verticalMove != null && isLegal(verticalMove)) {
                        resMove.add(verticalMove);
                    }
                }
            }
        }
        for (Square from : sideLocation) {
            if (from != kingPosition()) {
                for (int i = 0; i < SIZE; i++) {
                    Move horizontalMove = mv(from, sq(from.col(), i));
                    Move verticalMove = mv(from, sq(i, from.row()));
                    if (horizontalMove != null && isLegal(horizontalMove)) {
                        resMove.add(horizontalMove);
                    }
                    if (verticalMove != null && isLegal(verticalMove)) {
                        resMove.add(verticalMove);
                    }
                }
            }
        }
        return resMove;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return !legalMoves(side).isEmpty();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        SqList sideLocations = new SqList();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (_board[r][c].side() == side) {
                    sideLocations.add(sq(c, r));
                }
            }
        }
        return new HashSet<>(sideLocations);
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Number of (still undone) moves since initial position. */
    private int _moveCount;

    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;

    /** Limit of move count. */
    private int _moveLimit;

    /** Piece on the board. */
    private Piece[][] _board;

    /** Board position history. */
    private HashSet<String> _boardHistory;

    /** Stack of piece for undoing. */
    private Stack<Piece> _undoPiece;

    /** Stack of Squares for undoing. */
    private Stack<Square> _undoSquare;

}
