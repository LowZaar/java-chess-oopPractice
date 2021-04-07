package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Chess.ChessException;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;

public class mainClass {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		ChessMatch chessmatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();

		while (!chessmatch.getCheckMate()) {

			try {
				UI.clearScreen();
				UI.printMatch(chessmatch, captured);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(input);

				boolean[][] possibleMoves = chessmatch.possibleMoves(source);

				UI.clearScreen();

				UI.printBoard(chessmatch.getPieces(), possibleMoves);

				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(input);

				ChessPiece capturedPiece = chessmatch.performChessMove(source, target);

				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
				if (chessmatch.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String pieceName = input.nextLine().toUpperCase();
					while (!pieceName.equals("B") && !pieceName.equals("N") && !pieceName.equals("R") && !pieceName.equals("Q")) {
						System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
						pieceName = input.nextLine().toUpperCase();
					}
					chessmatch.replacePromotedPiece(pieceName);
				}
			} catch (ChessException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessmatch, captured);
	}
}
