package Chess.pieces;

import Chess.ChessPiece;
import Chess.Color;
import boardGame.Board;
import boardGame.Position;

public class Knight extends ChessPiece{

	public Knight(Board board, Color color) {
		super(board, color);
	}
	
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		
		return p == null || p.getColor() != getColor();
	}

	@Override
	public String toString() {
		return "N";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		p.setValue(position.getRow() -1 , position.getColumn() - 2);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		

		p.setValue(position.getRow() - 2 , position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		

		
		p.setValue(position.getRow() - 2, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		

		
		p.setValue(position.getRow() - 1 , position.getColumn() + 2);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		

		p.setValue(position.getRow() + 1 , position.getColumn() + 2);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
	
		p.setValue(position.getRow() + 2, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
	
		p.setValue(position.getRow() + 2, position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		

		p.setValue(position.getRow() + 1, position.getColumn() - 2);
		if(getBoard().positionExists(p) && canMove(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}
		
		return matrix;
	}
	
	
}
