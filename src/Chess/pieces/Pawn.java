package Chess.pieces;

import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.Color;
import boardGame.Board;
import boardGame.Position;

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;

	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);

		if (getColor() == Color.WHITE) {
			p.setValue(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() - 2, position.getColumn());
			Position pos2 = new Position(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(pos2) && getBoard().positionExists(pos2)
					&& !getBoard().thereIsAPiece(pos2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}

			// en passant white
			if (position.getRow() == 3) {
				Position leftOf = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(leftOf) && isThereOpponentPiece(leftOf)
						&& getBoard().piece(leftOf) == chessMatch.getEnPassantDanger()) {
					matrix[leftOf.getRow() - 1][leftOf.getColumn()] = true;
				}
			}
			if (position.getRow() == 3) {
				Position rightOf = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(rightOf) && isThereOpponentPiece(rightOf)
						&& getBoard().piece(rightOf) == chessMatch.getEnPassantDanger()) {
					matrix[rightOf.getRow() - 1][rightOf.getColumn()] = true;
				}
			}
		} else {

			p.setValue(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() + 2, position.getColumn());
			Position pos2 = new Position(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(pos2) && getBoard().positionExists(pos2)
					&& !getBoard().thereIsAPiece(pos2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValue(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}

			
			// en passant black
			if (position.getRow() == 4) {
				Position leftOf = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(leftOf) && isThereOpponentPiece(leftOf)
						&& getBoard().piece(leftOf) == chessMatch.getEnPassantDanger()) {
					matrix[leftOf.getRow() + 1][leftOf.getColumn()] = true;
				}
			}
			if (position.getRow() == 4) {
				Position rightOf = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(rightOf) && isThereOpponentPiece(rightOf)
						&& getBoard().piece(rightOf) == chessMatch.getEnPassantDanger()) {
					matrix[rightOf.getRow() + 1][rightOf.getColumn()] = true;
				}
			}

		}

		return matrix;
	}

	@Override
	public String toString() {
		return "P";
	}

}
