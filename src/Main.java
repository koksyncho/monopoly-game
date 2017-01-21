import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String board[] = new String[40];
		fillTheBoardWithTheNeededInformation(board);
		int playerAmount;
		String[] playerNames = new String[4];
		int[] firstDiceRows = new int[4];
		int[] secondDiceRows = new int[4];
		char confirm;

		boolean isReady = false;
		boolean hasWon = false;

		Random rand = new Random();
		Scanner input = new Scanner(System.in);

		System.out.println("Enter the number of players: ");

		// should be at least 2
		playerAmount = input.nextInt();
		
		int[] playerLocation = new int[playerAmount];

		System.out.println("Enter your names: ");

		input.nextLine();

		// nextLine() works in mysterious ways

		for (int i = 0; i < playerAmount; i++) {
			System.out.println("Name player " + (i + 1) + ": ");
			playerNames[i] = input.nextLine();

			System.out.println();
		}

		do {
			for (int i = 0; i < playerAmount; i++) {
				System.out.println(playerNames[i] + "'s turn: ");
				firstDiceRows[i] = rand.nextInt(6) + 1;
				System.out.println("First dice row: " + firstDiceRows[i]);

				secondDiceRows[i] = rand.nextInt(6) + 1;
				System.out.println("Second dice row: " + secondDiceRows[i]);

				if (playerLocation[i] + firstDiceRows[i] + secondDiceRows[i] <= 40) {
					playerLocation[i] += firstDiceRows[i] + secondDiceRows[i];
				} else {
					playerLocation[i] = (-1)
							* (40 - playerLocation[i] - firstDiceRows[i] - secondDiceRows[i]);
				}
				System.out.println("Location: " + playerLocation[i] + " "
						+ board[playerLocation[i]]);
				System.out.println("Are you ready to proceed? Y/N.");

				do {
					System.out.println();
					confirm = input.next().charAt(0);
					System.out.println("Testing the -> " + confirm
							+ " <- symbol");
					if (confirm == 'Y') {
						isReady = true;
					} else if (confirm == 'N' || confirm != 'Y') {
						// should probably be a list of the options
						isReady = false;
					}
				} while (isReady == false);
			}
		} while (hasWon == false);

	}

	private static void fillTheBoardWithTheNeededInformation(String[] board) {
		// board[locationOfTheField] = "FieldName, FieldType,
		// Win+/Pay-/Buy*/Upgrade=/DrawChestCard/DrawLuckyCard,
		// cost if any, owner if any, colour if any"
		fillTheProperties(board);
		fillTheStations(board);
		fillTheChests(board);
		fillTheFactories(board);
		fillTheLuckyDraws(board);
		fillTheTaxes(board);
		fillPrisons(board);
		fillParkplace(board);
		fillTheStart(board);

		// board[0] = "COLLECT $200 AS YOU PASS, go, win, 200, -, -";
		// board[1] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		// board[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue";
		// board[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue";
		// board[4] = "TAX REWARD, reward, win, 200, -, -";
		// board[5] = "SOFIA STATION, station, buy, 200, -, -";
		// board[6] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		// board[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown";
		// board[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown";
		// board[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown";
		// board[10] = "PRISON, prison, prison options, -, -, -";
		// board[11] =
		// "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue";
		// board[12] =
		// "PIROTSKA STREET, property, buy, 150, no owner, lightblue";
		// board[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue";
		// board[14] = "CHEZ, factory, buy, 150, no owner, -";
		// board[15] = "PLOVDIV STATION, station, buy, 200, -, -";
		// board[16] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		// board[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green";
		// board[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green";
		// board[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green";
		// board[20] = "PARKING, parking, pay, 0, -, -";
		// board[21] =
		// "SAN STEFANO STREET, property, buy, 220, no owner, yellow";
		// board[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow";
		// board[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow";
		// board[24] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		// board[25] = "VARNA STATION, station, buy, 200, -, -";
		// board[26] = "VIK, factory, buy, 150, no owner, -";
		// board[27] =
		// "LONDUKOV BULEVARD, property, buy, 280, no owner, purple";
		// board[28] =
		// "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple";
		// board[29] =
		// "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple";
		// board[30] = "GO TO JAIL, inprisonment, inprisonment, -, -, -";
		// board[31] =
		// "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange";
		// board[32] =
		// "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange";
		// board[33] =
		// "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange";
		// board[34] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		// board[35] = "BURGAS STATION, station, buy, 200, -, -";
		// board[36] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		// board[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red";
		// board[38] = "BOIANA, property, buy, 400, no owner, red";
		// board[39] = "SUPER TAX, tax, pay, 100, -, -";
	}

	private static void fillParkplace(String[] board) {
		board[20] = "PARKING, parking, pay, 0, -, -";
	}

	private static void fillTheStart(String[] board) {
		board[0] = "COLLECT $200 AS YOU PASS, go, win, 200, -, -";
	}

	private static void fillPrisons(String[] board) {
		board[10] = "PRISON, prison, prison options, -, -, -";
		board[30] = "GO TO JAIL, inprisonment, inprisonment, -, -, -";
	}

	private static void fillTheTaxes(String[] board) {
		board[4] = "TAX REWARD, reward, win, 200, -, -";
		board[39] = "SUPER TAX, tax, pay, 100, -, -";
	}

	private static void fillTheLuckyDraws(String[] board) {
		board[6] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		board[24] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		board[36] = "LUCKY DRAW, luck, draw luck card, -, -, -";
	}

	private static void fillTheFactories(String[] board) {
		board[14] = "CHEZ, factory, buy, 150, no owner, -";
		board[26] = "VIK, factory, buy, 150, no owner, -";
	}

	private static void fillTheChests(String[] board) {
		board[1] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		board[16] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		board[34] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
	}

	private static void fillTheStations(String[] board) {
		board[5] = "SOFIA STATION, station, buy, 200, -, -";
		board[15] = "PLOVDIV STATION, station, buy, 200, -, -";
		board[25] = "VARNA STATION, station, buy, 200, -, -";
		board[35] = "BURGAS STATION, station, buy, 200, -, -";
	}

	private static void fillTheProperties(String[] board) {
		board[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue";
		board[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue";

		board[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown";
		board[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown";
		board[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown";

		board[11] = "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue";
		board[12] = "PIROTSKA STREET, property, buy, 150, no owner, lightblue";
		board[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue";

		board[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green";
		board[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green";
		board[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green";

		board[21] = "SAN STEFANO STREET, property, buy, 220, no owner, yellow";
		board[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow";
		board[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow";

		board[27] = "LONDUKOV BULEVARD, property, buy, 280, no owner, purple";
		board[28] = "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple";
		board[29] = "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple";

		board[31] = "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange";
		board[32] = "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange";
		board[33] = "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange";

		board[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red";
		board[38] = "BOIANA, property, buy, 400, no owner, red";

	}
}
