import java.util.Random;
import java.util.Scanner;

public class Main {
	private static final int BOARD_FIELDS_COUNT = 40;
	private static final int BOARD_FIELDS_PROPERTIES_COUNT = 7;
	private static final int CARD_COUNT = 5;
	private static final int CARD_PROPERTIES_COUNT = 4;
	private static final int STARTING_MONEY = 500;
	private static final int MAX_AMOUNT_OF_PLAYERS = 4;
	private static final int MIN_AMOUNT_OF_PLAYERS = 2;
	private static final int DEFAULT_STARTING_COUNT = 0;
	private static final int MAX_TURNS_TO_SPEND_IN_JAIL = 3;
	private static final int MAX_PAIRS_OF_DICE_THROWN_IN_A_ROW = 3;
	
	private static String unformattedBoard[] = new String[BOARD_FIELDS_COUNT];
	private static String formattedBoard[][] = new String[BOARD_FIELDS_COUNT][BOARD_FIELDS_PROPERTIES_COUNT];
	
	private static String fieldAction;
	private static String fieldOwner;
	private static String fieldCost;
	private static String fieldColor;
	private static String fieldBuildingsCount;
	
	private static String unformattedCommunityChestCards[] = new String[CARD_COUNT];
	private static String formattedCommunityChestCards[][] = new String[CARD_COUNT][CARD_PROPERTIES_COUNT];
	
	private static String unformattedLuckyDrawCards[] = new String[CARD_COUNT];
	private static String formattedLuckyDrawCards[][] = new String[CARD_COUNT][CARD_PROPERTIES_COUNT];
	//cardName, win/pay/escape/move(also used for imprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
	private static int cardNumber;
	private static String cardName;
	private static String cardAction;
	private static String cardWonOrLostMoney;
	private static String cardFieldToMoveTo;
	
	private static int playerAmount;
	private static String[] playerNames = new String[MAX_AMOUNT_OF_PLAYERS];
	private static int[] playerMoney = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};
	private static int overallTurns = DEFAULT_STARTING_COUNT;
	private static int[] firstDiceRows = new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] secondDiceRows = new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] dicePairsThrownInARow = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] hasTheGetOutOfJailCard = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] turnsSpentInJail = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT}; 
	private static boolean[] isInJail = new boolean[MAX_AMOUNT_OF_PLAYERS];
	
	private static boolean[] isBankrupted = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int countOfBankruptedPlayers = DEFAULT_STARTING_COUNT;
	
	private static char choice;
	
	private static boolean isReady = false;
	private static boolean hasWon = false;
	
	static Scanner input = new Scanner(System.in);
	static Random rand = new Random();
	
	public static void main(String[] args) {
		
		prepareBoard();
		
		inputThePlayerAmount();
		
		int[] playerLocation = new int[playerAmount];
				
		inputThePlayerNames();

		startGame(playerLocation);

	}

	private static void prepareBoard() {
		fillTheBoardWithTheNeededInformation();
		formatTheBoard();
		
		fillTheCommunityChestCardsInformation();
		formatTheCommunityChestCards();
		
		fillTheLuckyDrawCardsInformation();
		formatTheLuckyDrawCards();
	}

	private static void startGame(int[] playerLocation) {
		while (!hasWon) {	
			outputTurn();
			makeTurn(playerLocation);
		}
	}

	private static void outputTurn() {
		System.out.println("_____________________________________________________________________________________");
		System.out.println();
		
		System.out.println("TURN " + (++overallTurns));
	}

	private static void makeTurn(int[] playerLocation) {
		for (int i = 0; i < playerAmount; i++) {
			
			if(areAllPlayersBankrupted()) {
				System.out.println("All players have bankrupted, there is no winner.");
				hasWon = true;
				break;
			}
			seeIfThePlayerIsBankrupted(i);
			
			if (isBankrupted[i]) {
				continue;
			}

			isOnlyOnePlayerLeft(i);
			if (hasWon) {
				break;
			}
			
			System.out.println(playerNames[i] + "'s turn: ");
			
			for (int repeats = 0; repeats <= dicePairsThrownInARow[i]; repeats++) {
				if (dicePairsThrownInARow[i] < 3) {
					if (!isInJail[i]) {
						rowTheDices(i);
						
						checkForASeriesOfMatchingDiceThrows(playerLocation, i);
						
						calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
					}
					
					outputPlayerLocation(playerLocation, i);
					outputPlayerMoney(i);
					decideHowToProceedWithTurn(playerLocation, i);
					
				}
			}
		}

	}
	
	private static boolean areAllPlayersBankrupted() {
		boolean isEveryPlayerBankrupted = true;
		
		for (int i = 0; i < playerAmount; i++) {
			if (playerMoney[i] >= 0) {
				isEveryPlayerBankrupted = false;
				break;
			}
		}
		
		return isEveryPlayerBankrupted;
	}
	
	private static void seeIfThePlayerIsBankrupted(int i) {
		if (playerMoney[i] < 0) {
			if (!isBankrupted[i]) {
				countOfBankruptedPlayers++;
			}
			isBankrupted[i] = true;
		}
		
		if (isBankrupted[i]) {
			System.out.println(playerNames[i] + "'s turn came, but he has bankrupted and is unable to play.");
			System.out.println();
		}
	}
	
	private static void isOnlyOnePlayerLeft(int i) {
		if (playerAmount - countOfBankruptedPlayers <= 1) {
			for (int index = 0; index < playerAmount; index++) {
				if (!isBankrupted[index]) {
					System.out.println(playerNames[index] + " survived the longest and won the game.");
					System.out.println("Congratulations " + playerNames[index] + "!");
				}
			}
			System.out.println();
			hasWon = true;
		}
	}
	
	private static void rowTheDices(int i) {

		System.out.println("Press enter to row the dice: ");
		input.nextLine();
		String readString = null;
		readString = input.nextLine();
		
	    while(readString!=null) {
	        if (readString.isEmpty()) {
	    		firstDiceRows[i] = rowDice();
	    		outputTheResultOfTheFirstDicesRow(i);

	    		secondDiceRows[i] = rowDice();
	    		outputTheResultOfTheSecondDicesRow(i);
	    		
	    		break;
	        }

	        if (input.hasNextLine()) {
	            readString = input.nextLine();
	        } else {
	            readString = null;
	        }
	    }

	}

	private static void checkForASeriesOfMatchingDiceThrows(int[] playerLocation, int i) {
		if (firstDiceRows[i] == secondDiceRows[i]) {
			System.out.println(playerNames[i] + " threw a pair.");
			dicePairsThrownInARow[i]++;
		} else {
			dicePairsThrownInARow[i] = DEFAULT_STARTING_COUNT;
		}
		
		if (dicePairsThrownInARow[i] == MAX_PAIRS_OF_DICE_THROWN_IN_A_ROW) {
			System.out.println(playerNames[i] + " threw 3 pairs in a row and is sent to jail for cheating.");
			dicePairsThrownInARow[i] = DEFAULT_STARTING_COUNT;
			goToJail(playerLocation, i);
		} else if (dicePairsThrownInARow[i] < MAX_PAIRS_OF_DICE_THROWN_IN_A_ROW && dicePairsThrownInARow[i] >= 1){
			System.out.println(playerNames[i] + " can throw again.");
		}
		
	}
	
	private static void calculateCurrentPlayerLocationAfterTheDiceThrow(int[] playerLocation, int i) {
		if (!hasMadeAFullLapOfTheField(playerLocation, i)) {
			playerLocation[i] += firstDiceRows[i] + secondDiceRows[i];
		} else {
			playerLocation[i] = (-1) * (BOARD_FIELDS_COUNT - playerLocation[i] - firstDiceRows[i] - secondDiceRows[i]);
		}
	}

	private static void outputPlayerMoney(int i) {
		System.out.println("Money: " + playerMoney[i] + "$");
	}

	private static void decideHowToProceedWithTurn(int[] playerLocation, int i) {
		getTheFieldParameters(playerLocation, i);
		
		//System.out.println(fieldAction + " " + fieldCost + " " + fieldOwner);
		
		if (isOnGoToJailField() || isInJail[i]) {
			goToJail(playerLocation, i);
		}
		
		if (!isInJail[i]) {
			//cardName, win/pay/escape/move(also used for imprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
			
			if (fieldAction.equals("draw chest card")) {
				communityChestCardDraw();
				
				System.out.println(playerNames[i] + " drew the " + cardName + " card.");
				
				winCardDraw(i);
				payCardDraw(i);
				escapeCardDraw(i);
				moveCardDraw(playerLocation, i);
			}
			
			if (fieldAction.equals("draw luck card")) {
				luckyCardDraw();
				
				System.out.println(playerNames[i] + " drew the " + cardName + " card.");
				
				winCardDraw(i);
				payCardDraw(i);
				escapeCardDraw(i);
				moveCardDraw(playerLocation, i);
			}
	
		}
		
		if (!isInJail[i]) {
			winningMoneyOutput(i);
			
			payingMoneyOutput(i);
			
		}

		if (!isInJail[i]) {
			puchasableFieldOptions(i);
		}		
		System.out.println("Are you ready to proceed or do you want to look at your options? (p)roceed/(o)ptions.");

		isReady = false;	
		
		proceedOrLookIntoOptions(i);
		
		updateTheFieldParameters(playerLocation, i);
		
		System.out.println("-------------------------------------------------------------------------------------");

		System.out.println();
	}
	

	private static void winningMoneyOutput(int i) {
		if (fieldAction.equals("win")) {
			//works for tax and go fields
			System.out.println(playerNames[i] + " won " + fieldCost + "$");
			playerMoney[i] += Integer.parseInt(fieldCost);
		}
	}

	private static void payingMoneyOutput(int i) {
		if (fieldAction.equals("pay")) {
			System.out.println(playerNames[i] + " paid " + fieldCost + "$");
			playerMoney[i] -= Integer.parseInt(fieldCost);
		}
	}

	private static void puchasableFieldOptions(int i) {
		if (isOnPurchasableField()) {
			if (!isFieldBought()) {
				do {
					System.out.println();

					System.out.println("You can (b)uy the field for " + fieldCost + "$ or start an (a)uction for it.");
					choice = input.next().charAt(0);
				
					if (choice == 'b') {
						playerMoney[i] -= Integer.parseInt(fieldCost);
						fieldOwner = playerNames[i];
						isReady = true;
					} else if (choice == 'a') {
						System.out.println("DOING THE AUCTION...");
						doAuction();
						isReady = true;
					} else {
						System.out.println("Invalid input ((b)uy/(a)uction).");
						isReady = false;
					}
				} while (!isReady);
			} else {
				if (!fieldOwner.equals(playerNames[i])) {
					// we tax a tenth of the field cost plus a half of it for each building
					playerMoney[i] -= (Integer.parseInt(fieldCost) / 10) + ((Integer.parseInt(formattedBoard[i][6]) * (Integer.parseInt(fieldCost) / 2)));
					
					int fieldOwnerIndex = 0;
					while (playerNames[fieldOwnerIndex] != fieldOwner) {
						fieldOwnerIndex++;
					}
					System.out.println(playerNames[i] + " paid " + playerNames[fieldOwnerIndex] + " " + ((Integer.parseInt(fieldCost) / 10) + (Integer.parseInt(formattedBoard[i][6]) * (Integer.parseInt(fieldCost) / 2))) + "$");
					playerMoney[fieldOwnerIndex] += (Integer.parseInt(fieldCost) / 10);
				} else {
					System.out.println(playerNames[i] + " is waiting for their next turn on their property.");
				}
			}
		}		
	}

	private static void proceedOrLookIntoOptions(int i) {
		do {
			choice = input.next().charAt(0);
			
			System.out.println();
			
			
			if (doesWantToProceed()) {
				isReady = true;
			} else if (doesWantToLookAtTheOptions()) {
				System.out.println("The available options for building and selling houses (shown if you own all properties from at least one color):");
				goThroughOptions(i);
				isReady = false;
			} else {
				outputInvalidInputMessage();
			}
		} while (!isReady);
	
	}

	private static void moveCardDraw(int[] playerLocation, int i) {
		if (cardAction.equals("move")) {
			if (Integer.parseInt(cardFieldToMoveTo) > -1) {
				playerLocation[i] = Integer.parseInt(cardFieldToMoveTo);
				
				if (playerLocation[i] == 30) {
					goToJail(playerLocation, i);
				}
			} else if (Integer.parseInt(cardFieldToMoveTo) < -1) {
				//never overwrites (5 - 3 > 0)
				playerLocation[i] += Integer.parseInt(cardFieldToMoveTo);
			}
		}
	}

	private static void escapeCardDraw(int i) {
		if (cardAction.equals("escape")) {
			//can have only one Get Out Of Jail card
			hasTheGetOutOfJailCard[i] = true;
		}
	}

	private static void payCardDraw(int i) {
		if (cardAction.equals("pay")) {
			System.out.println("Lost " + cardWonOrLostMoney + "$");
			playerMoney[i] -= Integer.parseInt(cardWonOrLostMoney);
		}
	}

	private static void winCardDraw(int i) {
		if (cardAction.equals("win")) {
			System.out.println("Won " + cardWonOrLostMoney + "$");
			playerMoney[i] += Integer.parseInt(cardWonOrLostMoney);
		}
	}

	private static void luckyCardDraw() {
		cardNumber = rand.nextInt(4) + 0;
		cardName = formattedLuckyDrawCards[cardNumber][0];
		cardAction = formattedLuckyDrawCards[cardNumber][1];
		cardWonOrLostMoney = formattedLuckyDrawCards[cardNumber][2];
		cardFieldToMoveTo = formattedLuckyDrawCards[cardNumber][3];
	}

	private static void communityChestCardDraw() {
		cardNumber = rand.nextInt(4) + 0;
		cardName = formattedCommunityChestCards[cardNumber][0];
		cardAction = formattedCommunityChestCards[cardNumber][1];
		cardWonOrLostMoney = formattedCommunityChestCards[cardNumber][2];
		cardFieldToMoveTo = formattedCommunityChestCards[cardNumber][3];
	}

	private static void goThroughOptions(int i) {
		System.out.println("Under construction...");
	}
	
	private static void goToJail(int[] playerLocation, int i) {
		System.out.println(playerNames[i] + " has been in Jail for " + turnsSpentInJail[i] + " turns.");
		System.out.println("You can (t)ry to throw a pair, (b)ribe the guard with 100$, (w)ait it out (released after " + (3 - turnsSpentInJail[i]) +" turns), or (u)se a Get Out Of Jail card if you have one.");
		char decision;
		do {
			decision = input.next().charAt(0);
			if (decision == 't') {
				rowTheDices(i); 
				if(firstDiceRows[i] == secondDiceRows[i]) {
					System.out.println(playerNames[i] + " threw a pair and successfully escaped!");
					isInJail[i] = false;
					turnsSpentInJail[i] = 0;
					
					proceedWithTurnAsNormal(playerLocation, i);
					
				} else {
					turnsSpentInJail[i]++;
				}
			} else if (decision == 'b') {
				if(playerMoney[i] >= 100) {
					System.out.println(playerNames[i] + " successfully bribed the guards and escaped.");
					playerMoney[i] -= 100;
					isInJail[i] = false;
					turnsSpentInJail[i] = 0;
					
					rowTheDices(i);
					
					checkForASeriesOfMatchingDiceThrows(playerLocation, i);
					
					proceedWithTurnAsNormal(playerLocation, i);
				} else {
					System.out.println(playerNames[i] + " doesn't have enough money to bride the guards.");
					turnsSpentInJail[i]++;
				}
			} else if (decision == 'w') {
				turnsSpentInJail[i]++;
			} else if (decision == 'u') {
				if (hasTheGetOutOfJailCard[i]) {
					System.out.println(playerNames[i] + " used the Get Out Of Jail card and escaped.");
					isInJail[i] = false;
					turnsSpentInJail[i] = 0;
					hasTheGetOutOfJailCard[i] = false;
					
					rowTheDices(i);
					
					proceedWithTurnAsNormal(playerLocation, i);
				} else {
					System.out.println(playerNames[i] + " doesn't have the Get Out Of Jail card.");
					turnsSpentInJail[i]++;
				}
			}
			
		} while(decision != 't' && (decision != 'b' && playerMoney[i] < 100) && decision != 'w' && (decision != 'u' && !hasTheGetOutOfJailCard[i]));
		
		playerLocation[i] = 10;
		
		if (turnsSpentInJail[i] >= MAX_TURNS_TO_SPEND_IN_JAIL) {
			isInJail[i] = false;
			turnsSpentInJail[i] = DEFAULT_STARTING_COUNT;

			rowTheDices(i);
			
			proceedWithTurnAsNormal(playerLocation, i);
		}
	}

	private static void proceedWithTurnAsNormal(int[] playerLocation, int i) {

		calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
	
		outputPlayerLocation(playerLocation, i);
		outputPlayerMoney(i);
		decideHowToProceedWithTurn(playerLocation, i);
	
	}

	private static void updateTheFieldParameters(int[] playerLocation, int i) {
		if (!fieldOwner.equals("-") && !fieldCost.equals("-")) {
			formattedBoard[playerLocation[i]][4] = fieldOwner;
			formattedBoard[playerLocation[i]][3] = fieldCost;
		}
	}

	private static void getTheFieldParameters(int[] playerLocation, int i) {
		fieldAction = formattedBoard[playerLocation[i]][2];
		fieldOwner = formattedBoard[playerLocation[i]][4];
		fieldCost = formattedBoard[playerLocation[i]][3];
		fieldColor = formattedBoard[playerLocation[i]][5];
		fieldBuildingsCount = formattedBoard[playerLocation[i]][6];
	}

	private static boolean isOnGoToJailField() {
		boolean isOnGoToJailField = fieldAction.equals("imprisonment");
		return isOnGoToJailField;
	}

	private static void doAuction() {
		
		boolean[] isPlayerNotInTheAuction = new boolean[playerAmount];
		int[] bids = new int[playerAmount];
		for(int i = 0; i < isPlayerNotInTheAuction.length; i++) {
			isPlayerNotInTheAuction[i] = false;
			bids[i] = 0;
		}
		int highestBid = DEFAULT_STARTING_COUNT;
		char decision;
		int amountOfParticipatingPlayers = playerAmount;
		boolean isWinnerDeclared = false;
		
		while (amountOfParticipatingPlayers != 1 || !isWinnerDeclared) {
		
			for (int i = 0; i < playerAmount; i++) {
				if (isPlayerNotInTheAuction[i] == false) {
					System.out.println(playerNames[i] + "'s turn: ");
					System.out.println("Currently bidding... " + amountOfParticipatingPlayers);
					if (amountOfParticipatingPlayers <= 1) {
						System.out.println("Being the last player " + playerNames[i] + " won - buying the property for " + highestBid + ".");
						playerMoney[i] -= highestBid;
						fieldOwner = playerNames[i];
						isWinnerDeclared = true;
						break;
					}
					System.out.println("(b)idding/(l)eaving");
					
					do {
						decision = input.next().charAt(0);
						if (decision == 'b') {
							System.out.println("Enter your bid (has to be over " + highestBid + ", you can bail out by bidding below 0): ");
							do {
								bids[i] = input.nextInt();
		
								if(bids[i] < 0) {
									amountOfParticipatingPlayers--;
									isPlayerNotInTheAuction[i] = true;
									System.out.println(playerNames[i] + " left the auction.");
								} else {
									System.out.println(playerNames[i] + " bid " + bids[i]);
								}
							} while(bids[i] <= highestBid && !isPlayerNotInTheAuction[i]);
							if(highestBid < bids[i]){
								highestBid = bids[i];
							}
						} else if (decision == 'l') {
							amountOfParticipatingPlayers--;
							isPlayerNotInTheAuction[i] = true;
							System.out.println(playerNames[i] + " left the auction.");
						} else {
							System.out.println("Invalid input ((b)id/(l)eave)..");
						}
						System.out.println();
	 				} while(decision != 'b' && decision != 'l');	
				}	
			}
		}	
	}

	private static boolean isFieldBought() {
		boolean isFieldBought = !fieldOwner.equals("no owner");
		return isFieldBought;
	}

	private static boolean isOnPurchasableField() {
		boolean isOnPurchasableField = fieldAction.equals("buy");
		return isOnPurchasableField;
	}

	private static void outputInvalidInputMessage() {
		System.out.println("Invalid input ((p)roceed/(o)ptions).");
	}

	private static boolean doesWantToProceed() {
		boolean doesWantToProceed = choice == 'p';
		return doesWantToProceed;
	}

	private static boolean doesWantToLookAtTheOptions() {
		boolean doesWantToLookAtTheOptions = choice == 'o';
		return doesWantToLookAtTheOptions;
	}

	private static void outputPlayerLocation(int[] playerLocation, int i) {
		System.out.println("Location: " + playerLocation[i] + " " + formattedBoard[playerLocation[i]][0] + ", " + formattedBoard[playerLocation[i]][1] + ", " + formattedBoard[playerLocation[i]][2] + ", " + formattedBoard[playerLocation[i]][3] + ", " + formattedBoard[playerLocation[i]][4] + ", " + formattedBoard[playerLocation[i]][5] + ", " + formattedBoard[playerLocation[i]][6]/*unformattedBoard[playerLocation[i]]*/);
	}

	private static boolean hasMadeAFullLapOfTheField(int[] playerLocation, int i) {
		boolean hasMadeAFullLap = playerLocation[i] + firstDiceRows[i] + secondDiceRows[i] >= BOARD_FIELDS_COUNT;
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

	private static void checkForInvalidInput(int countOfInputAttempts) {
		if(countOfInputAttempts >= 1) {
			outputWarningUponInvalidPlayerAmountInput();
		}
	}

	private static void outputWarningUponInvalidPlayerAmountInput() {
		System.out.println("The players should be between 2 and 4.");
	}

	private static boolean isTheNumberOfPlayersItBelowTwoOrOverFour(/*int playerAmount*/) {
		if (playerAmount < MIN_AMOUNT_OF_PLAYERS || playerAmount > MAX_AMOUNT_OF_PLAYERS) {
			return true;
		} else {
			return false;
		}
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
		} while (isTheNumberOfPlayersItBelowTwoOrOverFour());
		
		System.out.println();
	}
	

	private static void formatTheLuckyDrawCards() {
		for(int i = 0; i < unformattedLuckyDrawCards.length; i++) {
			for(int j = 0; j < CARD_PROPERTIES_COUNT; j++) {
				formattedLuckyDrawCards[i][j] = unformattedLuckyDrawCards[i].split(", ")[j];
			}
		}
	}

	private static void formatTheCommunityChestCards() {
		for(int i = 0; i < unformattedCommunityChestCards.length; i++) {
			for(int j = 0; j < CARD_PROPERTIES_COUNT; j++) {
				formattedCommunityChestCards[i][j] = unformattedCommunityChestCards[i].split(", ")[j];
			}
		}
	}

	private static void fillTheLuckyDrawCardsInformation() {
		//cardName, win/pay/escape/move(also used for imprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
		unformattedLuckyDrawCards[0] = "ADVANCE TO CARIGRAD ROAD, move, -, 3";
		unformattedLuckyDrawCards[1] = "GO BACK 3 SPACES, move, -, -3";
		unformattedLuckyDrawCards[2] = "GO DIRECTLY TO JAIL, move, -, 30";
		unformattedLuckyDrawCards[3] = "BANK PAYS YOU DIVIDENT, win, 50, -1";
		unformattedLuckyDrawCards[4] = "ADVANCE TO BOIANA, move, -, 38";
	}

	private static void fillTheCommunityChestCardsInformation() {
		//cardName, win/pay/escape/move(also used for imprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
		unformattedCommunityChestCards[0] = "GET OUT OF JAIL, escape, -, -1";
		unformattedCommunityChestCards[1] = "ADVANCE TO GO, move, -, 0";
		unformattedCommunityChestCards[2] = "SECOND PRIZE IN A BEAUTY CONTEST, win, 10, -1";
		unformattedCommunityChestCards[3] = "PAY HOSPITAL, pay, 100, -1";
		unformattedCommunityChestCards[4] = "BANK ERROR IN YOUR FAVOR, win, 200, -1";
	}

	private static void fillTheBoardWithTheNeededInformation() {
		// board[locationOfTheField] = "FieldName, FieldType,
		// Win+/Pay-/Buy*/Upgrade=/DrawChestCard/DrawLuckyCard,
		// cost if any, owner if any, colour if any"
		fillTheProperties();
		fillTheStations();
		fillTheChests();
		fillTheFactories();
		fillTheLuckyDraws();
		fillTheTaxes();
		fillPrisons();
		fillParkplace();
		fillTheStart();
}

	private static void fillParkplace() {
		unformattedBoard[20] = "PARKING, parking, pay, 0, -, -, 0"; 
	}

	private static void fillTheStart() {
		unformattedBoard[0] = "COLLECT $200 AS YOU PASS, go, win, 200, -, -, 0";
	}

	private static void fillPrisons() {
		unformattedBoard[10] = "PRISON, prison, prison options, -, -, -, 0";
		unformattedBoard[30] = "GO TO JAIL, imprisonment, imprisonment, -, -, -, 0";
	}

	private static void fillTheTaxes() {
		unformattedBoard[4] = "TAX REWARD, reward, win, 200, -, -, 0";
		unformattedBoard[39] = "SUPER TAX, tax, pay, 100, -, -, 0";
	}

	private static void fillTheLuckyDraws() {
		unformattedBoard[6] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
		unformattedBoard[24] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
		unformattedBoard[36] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
	}

	private static void fillTheFactories() {
		unformattedBoard[14] = "CHEZ, factory, buy, 150, no owner, -, 0";
		unformattedBoard[26] = "VIK, factory, buy, 150, no owner, -, 0";
	}

	private static void fillTheChests() {
		unformattedBoard[1] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
		unformattedBoard[16] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
		unformattedBoard[34] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
	}

	private static void fillTheStations() {
		unformattedBoard[5] = "SOFIA STATION, station, buy, 200, no owner, -, 0";
		unformattedBoard[15] = "PLOVDIV STATION, station, buy, 200, no owner, -, 0";
		unformattedBoard[25] = "VARNA STATION, station, buy, 200, no owner, -, 0";
		unformattedBoard[35] = "BURGAS STATION, station, buy, 200, no owner, -, 0";
	}
	
	private static void formatTheBoard() {
		for(int i = 0; i < unformattedBoard.length; i++) {
			for(int j = 0; j < BOARD_FIELDS_PROPERTIES_COUNT; j++) {
				formattedBoard[i][j] = unformattedBoard[i].split(", ")[j];
			}
		}
	}

	private static void fillTheProperties() {
		fillTheDarkBlueProperties();
		fillTheBrownProperties();
		fillTheLightBlueProperties();
		fillTheGreenProperties();
		fillTheYellowProperties();
		fillThePurpleProperties();
		fillTheOrangeProperties();
		fillTheRedProperties();
	}
	
	private static void fillTheRedProperties() {
		unformattedBoard[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red, 0";
		unformattedBoard[38] = "BOIANA, property, buy, 400, no owner, red, 0";
	}

	private static void fillTheOrangeProperties() {
		unformattedBoard[31] = "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange, 0";
		unformattedBoard[32] = "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange, 0";
		unformattedBoard[33] = "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange, 0";
	}

	private static void fillThePurpleProperties() {
		unformattedBoard[27] = "LONDUKOV BULEVARD, property, buy, 280, no owner, purple, 0";
		unformattedBoard[28] = "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple, 0";
		unformattedBoard[29] = "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple, 0";
	}

	
	private static void fillTheYellowProperties() {
		unformattedBoard[21] = "SAN STEFANO STREET, property, buy, 220, no owner, yellow, 0";
		unformattedBoard[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow, 0";
		unformattedBoard[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow, 0";
	}

	private static void fillTheGreenProperties() {
		unformattedBoard[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green, 0";
		unformattedBoard[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green, 0";
		unformattedBoard[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green, 0";
	}
	
	private static void fillTheLightBlueProperties() {
		unformattedBoard[11] = "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue, 0";
		unformattedBoard[12] = "PIROTSKA STREET, property, buy, 150, no owner, lightblue, 0";
		unformattedBoard[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue, 0";
	}

	private static void fillTheBrownProperties() {
		unformattedBoard[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown, 0";
		unformattedBoard[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown, 0";
		unformattedBoard[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown, 0";
	}

	private static void fillTheDarkBlueProperties() {
		unformattedBoard[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue, 0";
		unformattedBoard[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue, 0";
	}
}
