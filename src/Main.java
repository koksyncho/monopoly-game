import java.util.Random;
import java.util.Scanner;

public class Main {
	private static String board[] = new String[40];
	private static int playerAmount;
	private static int overallTurns = 1;
	private static String[] playerNames = new String[4];
	private static int[] playerMoney = {2000, 2000, 2000, 2000};
	private static int[] firstDiceRows = new int[4];
	private static int[] secondDiceRows = new int[4];
	private static char confirm;
	private static String fieldAction;
	private static String fieldOwner;
	private static String fieldCost;
	
	private static boolean isReady = false;
	private static boolean hasWon = false;
	
	static Scanner input = new Scanner(System.in);
	static Random rand = new Random();
	
	//due to the variables being outside of the main most functions can manage without a return
	
	public static void main(String[] args) {

		fillTheBoardWithTheNeededInformation(board);
		inputThePlayerAmount();
		
		int[] playerLocation = new int[playerAmount];
				
		inputThePlayerNames();


		while (!hasWon) {	
			outputTurn();
			makeTurn(playerLocation);
		}

	}

	private static void outputTurn() {
		System.out.println("TURN " + overallTurns++);
	}

	private static void makeTurn(int[] playerLocation) {
		for (int i = 0; i < playerAmount; i++) {
			System.out.println(playerNames[i] + "'s turn: ");
			
			rowTheDices(i);
			
			if (!hasMadeAFullLapOfTheField(playerLocation, i)) {
				playerLocation[i] += firstDiceRows[i] + secondDiceRows[i];
			} else {
				playerLocation[i] = (-1) * (40 - playerLocation[i] - firstDiceRows[i] - secondDiceRows[i]);
			}
			outputPlayerLocation(playerLocation, i);
			outputPlayerMoney(i);
			decideHowToProceedWithTurn(playerLocation, i);
		}

	}

	private static void outputPlayerMoney(int i) {
		System.out.println("Money: " + playerMoney[i]);
	}

	private static void decideHowToProceedWithTurn(int[] playerLocation, int i) {
		fieldAction = board[playerLocation[i]].split(", ")[2];
		fieldOwner = board[playerLocation[i]].split(", ")[4];
		fieldCost = board[playerLocation[i]].split(", ")[3];
		
		System.out.println(fieldAction + " " + fieldCost + " " + fieldOwner);
		if (isOnPurchasableField(/*fieldAction*/)) {
			if (!isFieldBought()) {
				do {
					System.out.println();

					System.out.println("You can (b)uy the field for " + fieldCost + " or start an (a)uction for it.");
					confirm = input.next().charAt(0);
					//System.out.println("Testing the -> " + confirm + " <- symbol");
			
					if (confirm == 'b'/*doesWantToBuy()*/) {
						playerMoney[i] -= Integer.parseInt(fieldCost);
						fieldOwner = playerNames[i];
						isReady = true;
					} else if (confirm == 'a'/*doesWantToAuction()*/) {
						System.out.println("DOING THE AUCTION...");
						doAuction();
						isReady = true;
					} else {
						System.out.println("Invalid input ((b)uy/(a)uction).");
						isReady = false;
					}
				} while (!isReady);
			}
		}
		
		System.out.println("Are you ready to proceed or do you want to look at your options? (p)roceed/(o)ptions.");

		isReady = false;	
		do {
			System.out.println();
			
			confirm = input.next().charAt(0);
			//System.out.println("Testing the -> " + confirm + " <- symbol");
	
			if (doesWantToProceed()) {
				isReady = true;
			} else if (doesWantToLookAtTheOptions()) {
				// should probably be a list of the options
				isReady = false;
			} else {
				outputInvalidInputMessage();
			}
		} while (!isReady);
	
	}

	private static void doAuction() {
		//default false
		boolean[] isPlayerNotInTheAuction = new boolean[playerAmount];
		int[] bids = new int[playerAmount];
		int highestBid = 0;
		char decision;
		int amountOfParticipatingPlayers = playerAmount; 
		//int bid;
		
		while (amountOfParticipatingPlayers != 1) {
		
			for (int i = 0; i < playerAmount; i++) {
				if(!isPlayerNotInTheAuction[i]) {
					System.out.println(playerNames[i] + "'s turn: ");
					if (amountOfParticipatingPlayers == 1) {
						System.out.println("Being the last player " + playerNames[i] + " won - buying the property for " + highestBid + ".");
						break;
					}
					System.out.println("(b)idding/(l)eaving");
					decision = input.next().charAt(0);
					
					do {
						if (decision == 'b') {
							System.out.println("Enter your bid (has to be over " + highestBid + ", you can bail out by bidding below 0): ");
							do {
								bids[i] = input.nextInt();
								//bid = bids[i];
								if(bids[i] < 0) {
									isPlayerNotInTheAuction[i] = true;
									amountOfParticipatingPlayers--;
								}
								System.out.println(playerNames[i] + " bid " + bids[i]);
							} while(bids[i] <= highestBid || isPlayerNotInTheAuction[i]);
							if(highestBid < bids[i]){
								highestBid = bids[i];
							}
						} else if (decision == 'l') {
							isPlayerNotInTheAuction[i] = true;
							amountOfParticipatingPlayers--;
						} else {
							//!!doesn't quite work
							System.out.println("Invalid input ((b)id/(l)eave)..");
						}
	//					boolean isB = decision != 'b';
	//					boolean isL = decision != 'l';
	//					boolean validDecision = isB || isL;
	//					System.out.println(isB + " " + isL + " result " + validDecision);
						//some interesting stuff with the while condition with an ||
	 				} while(!(decision != 'b' || decision != 'l'));	
				}	
			}
		}
	}

//	private static void inputWhetherToBuyFieldOrStartAnAuctionForIt() {
//		System.out.println("Do you buy this field for " + fieldCost + " or try to auction it with the other players? (b)uy/(a)uction");
//		do {
//			confirm = input.next().charAt(0);
//		} while (confirm != 'b' || confirm != 'a');
//	}

	private static boolean isFieldBought() {
		boolean isFieldBought = !fieldOwner.equals("no owner");
		return isFieldBought;
	}

	private static boolean isOnPurchasableField(/*String fieldAction2*/) {
		boolean isOnPurchasableField = fieldAction.equals("buy");
		return isOnPurchasableField;
	}

	private static void outputInvalidInputMessage() {
		System.out.println("Invalid input ((p)roceed/(o)ptions).");
	}

	private static boolean doesWantToProceed() {
		boolean doesWantToProceed = confirm == 'p';
		return doesWantToProceed;
	}

	private static boolean doesWantToLookAtTheOptions() {
		boolean doesWantToLookAtTheOptions = confirm == 'o';
		return doesWantToLookAtTheOptions;
	}

	private static void outputPlayerLocation(int[] playerLocation, int i) {
		System.out.println("Location: " + playerLocation[i] + " " + board[playerLocation[i]]);
	}

	private static void rowTheDices(int i) {
		firstDiceRows[i] = rowDice();
		outputTheResultOfTheFirstDicesRow(i);

		secondDiceRows[i] = rowDice();
		outputTheResultOfTheSecondDicesRow(i);

	}

	private static boolean hasMadeAFullLapOfTheField(int[] playerLocation, int i) {
		boolean hasMadeAFullLap = playerLocation[i] + firstDiceRows[i] + secondDiceRows[i] >= 40;
		return hasMadeAFullLap;
	}

	private static void outputTheResultOfTheSecondDicesRow(int index) {
		System.out.println("Second dice row: " + secondDiceRows[index]);
	}

	private static void outputTheResultOfTheFirstDicesRow(int index) {
		System.out.println("First dice row: " + firstDiceRows[index]);
	}

	private static int rowDice() {
		int diceRow = rand.nextInt(6) + 1;
		return diceRow;
	}

	private static void inputThePlayerNames() {
		System.out.println("Enter your names: ");

		input.nextLine();

		for (int i = 0; i < playerAmount; i++) {
			System.out.println("Name player " + (i + 1) + ": ");
			playerNames[i] = input.nextLine();
			System.out.println();
		}
	}

	private static void inputThePlayerAmount() {		
		System.out.println("Enter the number of players (minimum 2, maximum 4): ");
		int countOfPlayerAmountInputAttempts = 0;
		
		do {
			checkForInvalidInput(countOfPlayerAmountInputAttempts);
			
			playerAmount = input.nextInt();
			
			countOfPlayerAmountInputAttempts++;
		} while (isTheNumberOfPlayersItBelowTwoOrOverFour(/*playerAmount*/));
	}

	private static void checkForInvalidInput(int countOfInputAttempts) {
		if(countOfInputAttempts >= 1) {
			outputWarningUponInvalidPlayerAmountInput();
		}
	}

	private static void outputWarningUponInvalidPlayerAmountInput() {
		System.out.println("The players should be between 2 and 4.");
	}

	private static boolean isTheNumberOfPlayersItBelowTwoOrOverFour(/*int playerAmount*/) {
		if (playerAmount < 2 || playerAmount > 4) {
			return true;
		} else {
			return false;
		}
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
		board[5] = "SOFIA STATION, station, buy, 200, no owner, -";
		board[15] = "PLOVDIV STATION, station, buy, 200, no owner, -";
		board[25] = "VARNA STATION, station, buy, 200, no owner, -";
		board[35] = "BURGAS STATION, station, buy, 200, no owner, -";
	}

	private static void fillTheProperties(String[] board) {
		fillTheDarkBlueProperties(board);
		fillTheBrownProperties(board);
		fillTheLightBlueProperties(board);
		fillTheGreenProperties(board);
		fillTheYellowProperties(board);
		fillThePurpleProperties(board);
		fillTheOrangeProperties(board);
		fillTheRedProperties(board);
	}

	private static void fillTheRedProperties(String[] board) {
		board[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red";
		board[38] = "BOIANA, property, buy, 400, no owner, red";
	}

	private static void fillTheOrangeProperties(String[] board) {
		board[31] = "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange";
		board[32] = "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange";
		board[33] = "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange";
	}

	private static void fillThePurpleProperties(String[] board) {
		board[27] = "LONDUKOV BULEVARD, property, buy, 280, no owner, purple";
		board[28] = "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple";
		board[29] = "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple";
	}

	private static void fillTheYellowProperties(String[] board) {
		board[21] = "SAN STEFANO STREET, property, buy, 220, no owner, yellow";
		board[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow";
		board[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow";
	}

	private static void fillTheGreenProperties(String[] board) {
		board[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green";
		board[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green";
		board[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green";
	}

	private static void fillTheLightBlueProperties(String[] board) {
		board[11] = "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue";
		board[12] = "PIROTSKA STREET, property, buy, 150, no owner, lightblue";
		board[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue";
	}

	private static void fillTheBrownProperties(String[] board) {
		board[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown";
		board[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown";
		board[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown";
	}

	private static void fillTheDarkBlueProperties(String[] board) {
		board[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue";
		board[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue";
	}
}
