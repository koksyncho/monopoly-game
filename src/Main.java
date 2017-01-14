import java.util.Random;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int playerAmount;
		String[] playerNames = new String[4];
		int[] diceRows = new int[4];
		char confirm;
		boolean isReady = false;
		boolean hasWon = false;

		Random rand = new Random();
		Scanner input = new Scanner(System.in);

		System.out.println("Enter the number of players: ");
		playerAmount = input.nextInt();
		int[] playerLocation = new int[playerAmount];

		System.out.println("Enter your name/s: ");

		System.out.println();

		// nextLine() works in mysterious ways
		playerNames[0] = input.nextLine();

		for (int i = 0; i < playerAmount; i++) {
			System.out.println("Name player " + (i + 1) + ": ");
			playerNames[i] = input.nextLine();

			System.out.println();
		}

		do {
			for (int i = 0; i < playerAmount; i++) {
				System.out.println(playerNames[i] + "'s turn: ");
				diceRows[i] = rand.nextInt(6) + 1;
				System.out.println("Dice row: " + diceRows[i]);
				if (playerLocation[i] + diceRows[i] <= 40) {
					playerLocation[i] += diceRows[i];
				} else {
					playerLocation[i] = (-1)
							* (40 - playerLocation[i] - diceRows[i]);
				}
				System.out.println("Location: " + playerLocation[i]);
				System.out.println("Are you ready to proceed? Y/N.");

				do {
					System.out.println();
					confirm = input.next().charAt(0);
					System.out.println("akdaosd " + confirm + " sadasdasd");
					if (confirm == 'Y') {
						isReady = true;
					} else if (confirm == 'N') {
						isReady = false;
					}
				} while (isReady == false);
			}
		} while (hasWon == false);

	}
}
