import java.util.Random;
import java.util.Scanner;

public class Main {
	private static final int BOARD_FIELDS_COUNT = 40;
	private static final int BOARD_FIELDS_PROPERTIES_COUNT = 6;
	private static final int CARD_COUNT = 5;
	private static final int CARD_PROPERTIES_COUNT = 4;
	private static final int STARTING_MONEY = 100;
	
	private static String unformattedBoard[] = new String[BOARD_FIELDS_COUNT];
	private static String formattedBoard[][] = new String[BOARD_FIELDS_COUNT][BOARD_FIELDS_PROPERTIES_COUNT];
	private static String unformattedCommunityChestCards[] = new String[CARD_COUNT];
	private static String formattedCommunityChestCards[][] = new String[CARD_COUNT][CARD_PROPERTIES_COUNT];
	private static String unformattedLuckyDrawCards[] = new String[CARD_COUNT];
	private static String formattedLuckyDrawCards[][] = new String[CARD_COUNT][CARD_PROPERTIES_COUNT];
	//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
	private static int cardNumber;
	private static String cardName;
	private static String cardAction;
	private static String cardWonOrLostMoney;
	private static String cardFieldToMoveTo;
	private static int playerAmount;
	private static int overallTurns = 1;
	private static String[] playerNames = new String[4];
	private static int[] playerMoney = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};
	private static int[] firstDiceRows = new int[4];
	private static int[] secondDiceRows = new int[4];
	private static int[] dicePairsThrownInARow = {0, 0, 0, 0};
	private static boolean[] isInJail = new boolean[4];
//	private static boolean isTheOwnerOfAllRedProperties;
//	private static boolean isTheOwnerOfAllOrangeFields;
//	private static boolean isTheOwnerOfAllPurpleFields;
//	private static boolean isTheOwnerOfAllYellowProperties;
//	private static boolean isTheOwnerOfAllGreenFields;
//	private static boolean isTheOwnerOfAllBrownFields;
//	private static boolean isTheOwnerOfAllDarkBlueProperties;
	
	//HAS TO BE CONFIGURED
	private static boolean[] hasTheGetOutOfJailCard = new boolean[4];
	private static int[] turnsSpentInJail = {0, 0, 0, 0}; 
	private static boolean[] isBankrupted = new boolean[4];
	private static int countOfBankruptedPlayers = 0;
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

		fillTheBoardWithTheNeededInformation(/*unformattedBoard*/);
		formatTheBoard();
		
		fillTheCommunityChestCardsInformation();
		formatTheCommunityChestCards();
		
		fillTheLuckyDrawCardsInformation();
		formatTheLuckyDrawCards();
		
		inputThePlayerAmount();
		
		int[] playerLocation = new int[playerAmount];
				
		inputThePlayerNames();

		startGame(playerLocation);

	}

	private static void startGame(int[] playerLocation) {
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
//			fieldAction = board[playerLocation[i]].split(", ")[2];
//			fieldOwner = board[playerLocation[i]].split(", ")[4];
//			fieldCost = board[playerLocation[i]].split(", ")[3];
			
			seeIfThePlayerIsBankrupted(i);
			//continue can't be used outside of a loop
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

	private static void seeIfThePlayerIsBankrupted(int i) {
		if (playerMoney[i] < 0) {
			//does the ++ only once
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
	
	private static void rowTheDices(int i) {
		firstDiceRows[i] = rowDice();
		outputTheResultOfTheFirstDicesRow(i);

		secondDiceRows[i] = rowDice();
		outputTheResultOfTheSecondDicesRow(i);

	}

	private static void calculateCurrentPlayerLocationAfterTheDiceThrow(int[] playerLocation, int i) {
		if (!hasMadeAFullLapOfTheField(playerLocation, i)) {
			playerLocation[i] += firstDiceRows[i] + secondDiceRows[i];
		} else {
			playerLocation[i] = (-1) * (BOARD_FIELDS_COUNT - playerLocation[i] - firstDiceRows[i] - secondDiceRows[i]);
		}
	}

	private static void checkForASeriesOfMatchingDiceThrows(int[] playerLocation, int i) {
		if (firstDiceRows[i] == secondDiceRows[i]) {
			System.out.println(playerNames[i] + " threw a pair.");
			dicePairsThrownInARow[i]++;
		} else {
			dicePairsThrownInARow[i] = 0;
		}
		
		if (dicePairsThrownInARow[i] == 3) {
//			board[10] = "PRISON, prison, prison options, -, -, -";
//			board[30] = "GO TO JAIL, inprisonment, inprisonment, -, -, -";
			System.out.println(playerNames[i] + " threw 3 pairs in a row and is sent to jail for cheating.");
			dicePairsThrownInARow[i] = 0;
			goToJail(playerLocation, i);
		} else if (dicePairsThrownInARow[i] < 3 && dicePairsThrownInARow[i] >= 1){
			System.out.println(playerNames[i] + " can throw again.");
		}
		
	}


	private static void outputPlayerMoney(int i) {
		System.out.println("Money: " + playerMoney[i]);
	}

	private static void decideHowToProceedWithTurn(int[] playerLocation, int i) {
		getTheFieldParameters(playerLocation, i);
		
		System.out.println(fieldAction + " " + fieldCost + " " + fieldOwner);

		if (isOnGoToJailField()) {
			goToJail(playerLocation, i);
		}
		
		if (!isInJail[i]) {
			//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
			
			if (fieldAction.equals("draw chest card")) {
				cardNumber = rand.nextInt(4) + 0;
				cardName = formattedCommunityChestCards[cardNumber][0];
				cardAction = formattedCommunityChestCards[cardNumber][1];
				cardWonOrLostMoney = formattedCommunityChestCards[cardNumber][2];
				cardFieldToMoveTo = formattedCommunityChestCards[cardNumber][3];
				
				System.out.println(playerNames[i] + " drew the " + cardName + " card.");
				
				if (cardAction.equals("win")) {
					System.out.println("Won " + cardWonOrLostMoney);
					playerMoney[i] += Integer.parseInt(cardWonOrLostMoney);
				} else if (cardAction.equals("pay")) {
					System.out.println("Lost " + cardWonOrLostMoney);
					playerMoney[i] -= Integer.parseInt(cardWonOrLostMoney);
				} else if (cardAction.equals("escape")) {
					//can have only one
					hasTheGetOutOfJailCard[i] = true;
				} else if (cardAction.equals("move")) {
					if (Integer.parseInt(cardFieldToMoveTo) > -1) {
						playerLocation[i] = Integer.parseInt(cardFieldToMoveTo);
						//MAYBE PLUS ONE
						//WE CHECK IF HE DREW THE GO DIRECTLY YO JAIL CARD
						if (isOnGoToJailField()) {
							goToJail(playerLocation, i);
						}
					} else if (Integer.parseInt(cardFieldToMoveTo) < -1) {
						//WE DO NOT CHECK IF IT OVERWRITES (example -> field[3] - 5 field[-2] ?)
						playerLocation[i] += Integer.parseInt(cardFieldToMoveTo);
					}
				}
			}
			
			if (fieldAction.equals("draw luck card")) {
				cardNumber = rand.nextInt(4) + 0;
				cardName = formattedLuckyDrawCards[i][0];
				cardAction = formattedLuckyDrawCards[i][1];
				cardWonOrLostMoney = formattedLuckyDrawCards[i][2];
				cardFieldToMoveTo = formattedLuckyDrawCards[i][3];
				
				System.out.println(playerNames[i] + " drew the " + cardName + " card.");
				
				if (cardAction.equals("win")) {
					System.out.println("Won " + cardWonOrLostMoney);
					playerMoney[i] += Integer.parseInt(cardWonOrLostMoney);
				} else if (cardAction.equals("pay")) {
					System.out.println("Lost " + cardWonOrLostMoney);
					playerMoney[i] -= Integer.parseInt(cardWonOrLostMoney);
				} else if (cardAction.equals("escape")) {
					//can have only one
					hasTheGetOutOfJailCard[i] = true;
				} else if (cardAction.equals("move")) {
					if (Integer.parseInt(cardFieldToMoveTo) > -1) {
						playerLocation[i] = Integer.parseInt(cardFieldToMoveTo);
						//MAYBE PLUS ONE
					} else if (Integer.parseInt(cardFieldToMoveTo) < -1) {
						//WE DO NOT CHECK IF IT OVERWRITES (example -> field[3] - 5 field[-2] ?)
						playerLocation[i] -= Integer.parseInt(cardFieldToMoveTo);
					}
				}
			}
	
		}
		
		//repeated because of the card draws SHOULD BE ONLY ONCE
		if (isOnGoToJailField()) {
			goToJail(playerLocation, i);
		}

		if (!isInJail[i]) {
			if (fieldAction.equals("win")) {
				//works for tax and go fields
				System.out.println(playerNames[i] + " won " + fieldCost);
				playerMoney[i] += Integer.parseInt(fieldCost);
			}
			
			if (fieldAction.equals("pay")) {
				System.out.println(playerNames[i] + " paid " + fieldCost);
				playerMoney[i] -= Integer.parseInt(fieldCost);
			}
			
//			unformattedBoard[4] = "TAX REWARD, reward, win, 200, -, -";
//			unformattedBoard[39] = "SUPER TAX, tax, pay, 100, -, -";
		}

		if (!isInJail[i]) {
			
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
				} else /*if(isFieldBought())*/ {
					if (!fieldOwner.equals(playerNames[i])) {
						playerMoney[i] -= Integer.parseInt(fieldCost) / 10;
						
						int fieldOwnerIndex = 0;
						//should check if the fieldOwner has bankrupted? probably not, cause all of his properties go to the bank then
						while (playerNames[fieldOwnerIndex] != fieldOwner) {
							fieldOwnerIndex++;
						}
						System.out.println(playerNames[i] + " paid " + playerNames[fieldOwnerIndex] + " " + (Integer.parseInt(fieldCost) / 10));
						playerMoney[fieldOwnerIndex] += Integer.parseInt(fieldCost) / 10;
					} else {
						System.out.println(playerNames[i] + " is waiting for their next turn on their property.");
					}
				}
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
	
		//Saving the changes made to the board 
		//(don't know if it works that way)
		updateTheFieldParameters(playerLocation, i);
	}
	

	private static void goToJail(int[] playerLocation, int i) {
		System.out.println(playerNames[i] + " has been in Jail for " + turnsSpentInJail[i] + " turns.");
		System.out.println("You can (t)ry to throw a pair, (b)ribe the guard with 100, (w)ait it out (released after " + (3 - turnsSpentInJail[i]) +" turns), or (u)se a Get Out Of Jail card if you have one.");
		char decision;
		do {
			decision = input.next().charAt(0);
			if (decision == 't') {
				rowTheDices(i); 
				if(firstDiceRows[i] == secondDiceRows[i]) {
					System.out.println(playerNames[i] + " threw a pair and successfully escaped!");
					isInJail[i] = false;
					turnsSpentInJail[i] = 0;
					
					//we don't look for a pair here I think
					
					calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
				
					outputPlayerLocation(playerLocation, i);
					outputPlayerMoney(i);
					decideHowToProceedWithTurn(playerLocation, i);
				}
			} else if (decision == 'b') {
				if(playerMoney[i] >= 100) {
					System.out.println(playerNames[i] + " successfully bribed the guards and escaped.");
					playerMoney[i] -= 100;
					isInJail[i] = false;
					turnsSpentInJail[i] = 0;
					
					rowTheDices(i);
					
					checkForASeriesOfMatchingDiceThrows(playerLocation, i);
					
					calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
					
					outputPlayerLocation(playerLocation, i);
					outputPlayerMoney(i);
					decideHowToProceedWithTurn(playerLocation, i);
				} else {
					System.out.println(playerNames[i] + " doesn't have enough money to bride the guards.");
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
					
					checkForASeriesOfMatchingDiceThrows(playerLocation, i);
					
					calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
					
					outputPlayerLocation(playerLocation, i);
					outputPlayerMoney(i);
					decideHowToProceedWithTurn(playerLocation, i);
				} else {
					System.out.println(playerNames[i] + " doesn't have the Get Out Of Jail card.");
				}
			}
			
		} while(decision != 't' && (decision != 'b' && playerMoney[i] < 100) && decision != 'w' && (decision != 'u' && !hasTheGetOutOfJailCard[i]));
		
		isInJail[i] = true;
		playerLocation[i] = 10;
		
		if (turnsSpentInJail[i] >= 3) {
			isInJail[i] = false;
			turnsSpentInJail[i] = 0;

			rowTheDices(i);
			
			checkForASeriesOfMatchingDiceThrows(playerLocation, i);
			
			calculateCurrentPlayerLocationAfterTheDiceThrow(playerLocation, i);
			
			outputPlayerLocation(playerLocation, i);
			outputPlayerMoney(i);
			decideHowToProceedWithTurn(playerLocation, i);
		}
	}

	private static void updateTheFieldParameters(int[] playerLocation, int i) {
		formattedBoard[playerLocation[i]]/*.split(", ")*/[2] = fieldAction;
		formattedBoard[playerLocation[i]]/*.split(", ")*/[4] = fieldOwner;
		formattedBoard[playerLocation[i]]/*.split(", ")*/[3] = fieldCost;
	}

	private static void getTheFieldParameters(int[] playerLocation, int i) {
		fieldAction = formattedBoard[playerLocation[i]][2];//.split(", ")[2];
		fieldOwner = formattedBoard[playerLocation[i]][4];//.split(", ")[4];
		fieldCost = formattedBoard[playerLocation[i]][3];//.split(", ")[3];
	}

	private static boolean isOnGoToJailField() {
		boolean isOnGoToJailField = fieldAction.equals("inprisonment");
		return isOnGoToJailField;
	}

	private static void doAuction() {
		//default false
		boolean[] isPlayerNotInTheAuction = new boolean[playerAmount];
		int[] bids = new int[playerAmount];
		for(int i = 0; i < isPlayerNotInTheAuction.length; i++) {
			isPlayerNotInTheAuction[i] = false;
			bids[i] = 0;
		}
		int highestBid = 0;
		char decision;
		int amountOfParticipatingPlayers = playerAmount;
		boolean isWinnerDeclared = false;
		//int bid;
		
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
								//bid = bids[i];
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
							//!!doesn't quite work
							System.out.println("Invalid input ((b)id/(l)eave)..");
						}
	//					boolean isB = decision != 'b';
	//					boolean isL = decision != 'l';
	//					boolean validDecision = isB || isL;
	//					System.out.println(isB + " " + isL + " result " + validDecision);
						//some interesting stuff with the while condition with an ||
	 				} while(decision != 'b' && decision != 'l');	
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
		System.out.println("Location: " + playerLocation[i] + " " + formattedBoard[playerLocation[i]][0] + ", " + formattedBoard[playerLocation[i]][1] + ", " + formattedBoard[playerLocation[i]][2] + ", " + formattedBoard[playerLocation[i]][3] + ", " + formattedBoard[playerLocation[i]][4] + ", " + formattedBoard[playerLocation[i]][5]/*unformattedBoard[playerLocation[i]]*/);
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
		if (playerAmount < 2 || playerAmount > 4) {
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
		} while (isTheNumberOfPlayersItBelowTwoOrOverFour(/*playerAmount*/));
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
		//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
		unformattedLuckyDrawCards[0] = "ADVANCE TO CARIGRAD ROAD, move, -, 3";
		unformattedLuckyDrawCards[1] = "GO BACK 3 SPACES, move, -, -3";
		unformattedLuckyDrawCards[2] = "GO DIRECTLY TO JAIL, move, -, 30";
		unformattedLuckyDrawCards[3] = "BANK PAYS YOU DIVIDENT, win, 50, -1";
		unformattedLuckyDrawCards[4] = "ADVANCE TO BOIANA, move, -, 38";
	}

	private static void fillTheCommunityChestCardsInformation() {
		//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
		unformattedCommunityChestCards[0] = "GET OUT OF JAIL, escape, -, -1";
		unformattedCommunityChestCards[1] = "ADVANCE TO GO, move, -, 0";
		unformattedCommunityChestCards[2] = "SECOND PRIZE IN A BEAUTY CONTEST, win, 10, -1";
		unformattedCommunityChestCards[3] = "PAY HOSPITAL, pay, 100, -1";
		unformattedCommunityChestCards[4] = "BANK ERROR IN YOUR FAVOR, win, 200, -1";
	}

	private static void fillTheBoardWithTheNeededInformation(/*String[] unformattedBoard*/) {
		// board[locationOfTheField] = "FieldName, FieldType,
		// Win+/Pay-/Buy*/Upgrade=/DrawChestCard/DrawLuckyCard,
		// cost if any, owner if any, colour if any"
		fillTheProperties(/*unformattedBoard*/);
		fillTheStations(/*unformattedBoard*/);
		fillTheChests(/*unformattedBoard*/);
		fillTheFactories(/*unformattedBoard*/);
		fillTheLuckyDraws(/*unformattedBoard*/);
		fillTheTaxes(/*unformattedBoard*/);
		fillPrisons(/*unformattedBoard*/);
		fillParkplace(/*unformattedBoard*/);
		fillTheStart(/*unformattedBoard*/);
}

	private static void fillParkplace(/*String[] unformattedBoard*/) {
		unformattedBoard[20] = "PARKING, parking, pay, 0, -, -"; 
	}

	private static void fillTheStart(/*String[] unformattedBoard*/) {
		unformattedBoard[0] = "COLLECT $200 AS YOU PASS, go, win, 200, -, -";
	}

	private static void fillPrisons(/*String[] unformattedBoard*/) {
		unformattedBoard[10] = "PRISON, prison, prison options, -, -, -";
		unformattedBoard[30] = "GO TO JAIL, inprisonment, inprisonment, -, -, -";
	}

	private static void fillTheTaxes(/*String[] unformattedBoard*/) {
		unformattedBoard[4] = "TAX REWARD, reward, win, 200, -, -";
		unformattedBoard[39] = "SUPER TAX, tax, pay, 100, -, -";
	}

	private static void fillTheLuckyDraws(/*String[] unformattedBoard*/) {
		unformattedBoard[6] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		unformattedBoard[24] = "LUCKY DRAW, luck, draw luck card, -, -, -";
		unformattedBoard[36] = "LUCKY DRAW, luck, draw luck card, -, -, -";
	}

	private static void fillTheFactories(/*String[] unformattedBoard*/) {
		unformattedBoard[14] = "CHEZ, factory, buy, 150, no owner, -";
		unformattedBoard[26] = "VIK, factory, buy, 150, no owner, -";
	}

	private static void fillTheChests(/*String[] unformattedBoard*/) {
		unformattedBoard[1] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		unformattedBoard[16] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
		unformattedBoard[34] = "COMMUNITY CHEST, chest, draw chest card, -, -, -";
	}

	private static void fillTheStations(/*String[] unformattedBoard*/) {
		unformattedBoard[5] = "SOFIA STATION, station, buy, 200, no owner, -";
		unformattedBoard[15] = "PLOVDIV STATION, station, buy, 200, no owner, -";
		unformattedBoard[25] = "VARNA STATION, station, buy, 200, no owner, -";
		unformattedBoard[35] = "BURGAS STATION, station, buy, 200, no owner, -";
	}
	
	private static void formatTheBoard() {
		for(int i = 0; i < unformattedBoard.length; i++) {
			for(int j = 0; j < BOARD_FIELDS_PROPERTIES_COUNT; j++) {
				formattedBoard[i][j] = unformattedBoard[i].split(", ")[j];
			}
		}
	}

	private static void fillTheProperties(/*String[] unformattedBoard*/) {
		fillTheDarkBlueProperties(/*unformattedBoard*/);
		fillTheBrownProperties(/*unformattedBoard*/);
		fillTheLightBlueProperties(/*unformattedBoard*/);
		fillTheGreenProperties(/*unformattedBoard*/);
		fillTheYellowProperties(/*unformattedBoard*/);
		fillThePurpleProperties(/*unformattedBoard*/);
		fillTheOrangeProperties(/*unformattedBoard*/);
		fillTheRedProperties(/*unformattedBoard*/);
	}
	
	private static void fillTheRedProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red";
		unformattedBoard[38] = "BOIANA, property, buy, 400, no owner, red";
	}

	private static void fillTheOrangeProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[31] = "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange";
		unformattedBoard[32] = "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange";
		unformattedBoard[33] = "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange";
	}

	private static void fillThePurpleProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[27] = "LONDUKOV BULEVARD, property, buy, 280, no owner, purple";
		unformattedBoard[28] = "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple";
		unformattedBoard[29] = "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple";
	}

	
	private static void fillTheYellowProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[21] = "SAN STEFANO STREET, property, buy, 220, no owner, yellow";
		unformattedBoard[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow";
		unformattedBoard[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow";
	}

	private static void fillTheGreenProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green";
		unformattedBoard[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green";
		unformattedBoard[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green";
	}
	
	private static void fillTheLightBlueProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[11] = "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue";
		unformattedBoard[12] = "PIROTSKA STREET, property, buy, 150, no owner, lightblue";
		unformattedBoard[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue";
	}

	private static void fillTheBrownProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown";
		unformattedBoard[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown";
		unformattedBoard[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown";
	}

	private static void fillTheDarkBlueProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue";
		unformattedBoard[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue";
	}
}
