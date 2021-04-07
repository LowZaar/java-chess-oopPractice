package Chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Chess.pieces.Bishop;
import Chess.pieces.King;
import Chess.pieces.Knight;
import Chess.pieces.Pawn;
import Chess.pieces.Queen;
import Chess.pieces.Rook;
import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantDanger;
	private ChessPiece promoted;

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;

		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	private Color opponent(Color color) {
		if (color == Color.WHITE) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantDanger() {
		return enPassantDanger;
	}

	public ChessPiece getPromoted() {
		return promoted;
	}

	private ChessPiece kingPos(Color color) {
		List<ChessPiece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	public boolean TestCheck(Color color) {
		Position kingPos = kingPos(color).getChessPosition().toPosition();
		List<Piece> oppPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == opponent(color))
				.collect(Collectors.toList());
		for (Piece p : oppPieces) {
			boolean[][] matrix = p.possibleMoves();
			if (matrix[kingPos.getRow()][kingPos.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean TestCheckMate(Color color) {
		if (!TestCheck(color)) {
			return false;
		}
		List<ChessPiece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] matrix = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (matrix[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = TestCheck(color);

						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void nextTurn() {
		turn++;
		if (currentPlayer == Color.WHITE) {
			this.currentPlayer = Color.BLACK;
		} else {
			this.currentPlayer = Color.WHITE;
		}
	}

	public ChessPiece[][] getPieces() {

		ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				matrix[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matrix;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}

	public boolean[][] possibleMoves(ChessPosition sourcePos) {
		Position position = sourcePos.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePos, ChessPosition targetPos) {

		Position source = sourcePos.toPosition();
		Position target = targetPos.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (TestCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't play that move, it will put yourself in check");
		}

		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		// promotion
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0
					|| movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}

		if (TestCheck(opponent(currentPlayer))) {
			check = true;
		} else {
			check = false;
		}
		if (TestCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		// en passant

		if (movedPiece instanceof Pawn
				&& (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantDanger = movedPiece;
		} else {
			enPassantDanger = null;
		}

		return (ChessPiece) capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String pieceName) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted.");
		}
		if (!pieceName.equals("B") && !pieceName.equals("N") && !pieceName.equals("R") && !pieceName.equals("Q")) {
			return promoted;
		}

		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(pieceName, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private ChessPiece newPiece(String piece, Color color) {
		if (piece.equals("B"))
			return (ChessPiece) new Bishop(board, color);
		if (piece.equals("N"))
			return (ChessPiece) new Knight(board, color);
		if (piece.equals("R"))
			return (ChessPiece) new Rook(board, color);
		return (ChessPiece) new Queen(board, color);
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}

		// castle king side
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sTower = new Position(source.getRow(), source.getColumn() + 3);
			Position tTower = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sTower);
			board.placePiece(rook, tTower);
			rook.increaseMoveCount();
		}
		// castle queen side
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sTower = new Position(source.getRow(), source.getColumn() - 4);
			Position tTower = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sTower);
			board.placePiece(rook, tTower);
			rook.increaseMoveCount();
		}
		// en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnTaken;
				if (p.getColor() == Color.WHITE) {
					pawnTaken = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnTaken = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnTaken);
				capturedPieces.add((ChessPiece) capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add((ChessPiece) capturedPiece);
		}

		// castle king side
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sTower = new Position(source.getRow(), source.getColumn() + 3);
			Position tTower = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(tTower);
			board.placePiece(rook, sTower);
			rook.decreaseMoveCount();
		}
		// castle queen side
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sTower = new Position(source.getRow(), source.getColumn() - 4);
			Position tTower = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(tTower);
			board.placePiece(rook, sTower);
			rook.decreaseMoveCount();
		}

		// en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantDanger) {
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnTaken;
				if (p.getColor() == Color.WHITE) {
					pawnTaken = new Position(3, target.getColumn());
				} else {
					pawnTaken = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnTaken);

			}
		}

	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position.");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece isn't yours!");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece cant move to the target position.");
		}
	}

}
