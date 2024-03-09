package game2048;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        board = new Board(rawValues);
        this.score = score;
        this.maxScore = maxScore;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists(board) || !atLeastOneMoveExists(board);
    }

    /** Checks if the game is over and sets the maxScore variable
     *  appropriately.
     */
    private void checkGameOver() {
        if (gameOver()) {
            maxScore = Math.max(score, maxScore);
        }
    }
    
    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for (int row = 0; row < b.size(); row++){
            for (int column = 0; column < b.size(); column++){
                if (b.tile(row, column) == null) {
                    return true;
                }
            }
        }


        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for (int row = 0; row < b.size(); row++) {
            for (int column = 0; column < b.size(); column++) {
                if (b.tile(row, column) == null){} //This line prevents the null tiles mess up the whole things
                else if (b.tile(row, column).value() == MAX_PIECE ) {
                    return true;
                }
            }
        }

//        for (int row = 0; row < b.size(); row++) {
//            for (int column = 0; column < b.size(); column++) {
//                System.out.println(b.tile(row, column));
//                System.out.println(b.tile(row, column).value());
//                System.out.println("----------");
//            }
//        }


        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        /*  1) Check for empty space and adjacent value if equal.
            2) if col == 0, check for to right, up, down
            3) if row == 0, check for bottom, right, left
            4) if tile(0,0) check right, down, tile (size(), 0)) check up, right
            5) if tile(0, size()) check left, down, tile(size(),size() check left, up
         */

        for (int row = 0; row < b.size(); row++) {  // Check for null tile
            for (int column = 0; column < b.size(); column++) {
                if (b.tile(row, column) == null) {
                    return true;
                }
            }
        }


        for (int row = 0; row < b.size(); row++) {                                  // iterate row
            for (int column = 0; column < b.size(); column++) {                     // iterate column
                int current_tile_value = b.tile(row, column).value();

                try {
                    for (int next_row = -1; next_row < 2; next_row += 2) {               // check for up down
                        if (b.tile((row + next_row), column).value() == current_tile_value) {
                            return true;
                        }
                    }
                }  catch (ArrayIndexOutOfBoundsException e) {continue;}

                try {
                    for (int next_column = -1; next_column < 2; next_column += 2) {      // check for left right
                        if (b.tile(row, (column + next_column)).value() == current_tile_value) {
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {continue;}
            }
        }
        return false;
    }

    public static boolean check_for_null_tile(Tile t){
        System.out.println(t.value());
        return false;
    }

    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public void tilt(Side side) {
        // TODO: Modify this.board (and if applicable, this.score) to account
        // for the tilt to the Side SIDE.
        board.setViewingPerspective(side);
//        System.out.println(side);
        for (int col = 0; col < board.size(); col++){                                       // iterate tiles from left to right
            int limit = board.size() - 1;

            for (int row = board.size() - 2; row >= 0; row --){                             // iterate tiles from top to bottom
                //only move the tile when it is not empty
                Tile current_tile = board.tile(col, row);
                Tile top_tile = board.tile(col, limit);
                if (current_tile != null){
                    if(top_tile == null) {
                        board.move(col, limit, current_tile);
//                        top_tile = board.tile(col, board.size() - 1);

                    } else if (top_tile.value() == current_tile.value()) {
                        score += top_tile.value() + current_tile.value();
                        board.move(col, limit, current_tile);
                        limit -= 1;
                        //update top_tile
                    } else if (top_tile != null && top_tile.value() != current_tile.value()) {
                        board.move(col, limit - 1, current_tile);
//                        top_tile = board.tile(col, top_tile.row() - 1);
                        limit -= 1;
                    }
                }
            }
        }
        board.setViewingPerspective(Side.NORTH);




        checkGameOver();
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
