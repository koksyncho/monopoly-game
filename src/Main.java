import java.util.Random;
import java.util.Scanner;

public class Main {
	private static final int BOARD_FIELDS_COUNT = 40;
	private static final int BOARD_FIELDS_PROPERTIES_COUNT = 7;
	private static final int CARD_COUNT = 5;
	private static final int CARD_PROPERTIES_COUNT = 4;
	private static final int STARTING_MONEY = 10000;
	private static final int MAX_AMOUNT_OF_PLAYERS = 4;
	private static final int MIN_AMOUNT_OF_PLAYERS = 2;
	private static final int DEFAULT_STARTING_COUNT = 0;
	private static final int MAX_TURNS_TO_SPEND_IN_JAIL = 3;
	
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
	//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
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
	
	private static boolean[] isTheOwnerOfAllRedProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedRedProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnRedProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllOrangeProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedOrangeProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnOrangeProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllPurpleProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedPurpleProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnPurpleProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllYellowProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedYellowProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnYellowProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllGreenProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedGreenProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnGreenProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllBrownProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedBrownProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnBrownProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllLightBlueProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedLightBlueProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnLightBlueProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	private static boolean[] isTheOwnerOfAllDarkBlueProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedDarkBlueProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfBuildingsOnDarkBlueProperties = {DEFAULT_STARTING_COUNT, DEFAULT_STARTING_COUNT};
	
	//HAS TO BE CONFIGURED
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
		System.out.println("TURN " + (++overallTurns));
	}

	private static void makeTurn(int[] playerLocation) {
		for (int i = 0; i < playerAmount; i++) {
//			fieldAction = board[playerLocation[i]].split(", ")[2];
//			fieldOwner = board[playerLocation[i]].split(", ")[4];
//			fieldCost = board[playerLocation[i]].split(", ")[3];
			if(areAllPlayersBankrupted()) {
				System.out.println("All players have bankrupted, there is no winner.");
				hasWon = true;
				break;
			}
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
			
			//we can throw the dice 
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

		if (isOnGoToJailField() || isInJail[i]) {
			goToJail(playerLocation, i);
		}
		
		if (!isInJail[i]) {
			//cardName, win/pay/escape/move(also used for inprison)/move, (win or pay how much), (if you move, move to which field (index -> -1 if not a move card)) 
			
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
		
		//repeated because of the card draws SHOULD BE ONLY ONCE
		if (isOnGoToJailField() || isInJail[i]) {
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
						choice = input.next().charAt(0);
						//System.out.println("Testing the -> " + choice + " <- symbol");
				
						if (choice == 'b'/*doesWantToBuy()*/) {
							playerMoney[i] -= Integer.parseInt(fieldCost);
							fieldOwner = playerNames[i];
							isReady = true;
						} else if (choice == 'a'/*doesWantToAuction()*/) {
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
						// we tax a tenth of the field cost plus a half of it for each building
						playerMoney[i] -= (Integer.parseInt(fieldCost) / 10) + ((Integer.parseInt(formattedBoard[i][6]) * (Integer.parseInt(fieldCost) / 2)));
						
						int fieldOwnerIndex = 0;
						//should check if the fieldOwner has bankrupted? probably not, cause all of his properties go to the bank then
						while (playerNames[fieldOwnerIndex] != fieldOwner) {
							fieldOwnerIndex++;
						}
						System.out.println(playerNames[i] + " paid " + playerNames[fieldOwnerIndex] + " " + ((Integer.parseInt(fieldCost) / 10) + (Integer.parseInt(formattedBoard[i][6]) * (Integer.parseInt(fieldCost) / 2))));
						playerMoney[fieldOwnerIndex] += (Integer.parseInt(fieldCost) / 10);
					} else {
						System.out.println(playerNames[i] + " is waiting for their next turn on their property.");
					}
				}
			}		
		}
		
		countTheNumberOfOwnedColoredPropertiesForEachPlayer();
				
		System.out.println("Are you ready to proceed or do you want to look at your options? (p)roceed/(o)ptions.");

		isReady = false;	
		
		do {
			System.out.println();
			
			choice = input.next().charAt(0);
			//System.out.println("Testing the -> " + choice + " <- symbol");
	
			if (doesWantToProceed()) {
				isReady = true;
			} else if (doesWantToLookAtTheOptions()) {
				// should probably be a list of the options
				//should be outside of the options case
				System.out.println("The available options for building and selling houses (shown if you own all properties from at least one color):");
				goThroughOptions(i);
				isReady = false;
			} else {
				outputInvalidInputMessage();
			}
		} while (!isReady);
	
		//Saving the changes made to the board 
		//(don't know if it works that way)
		updateTheFieldParameters(playerLocation, i);
		
		prepareTheNumberOfOwnedColoredPropertiesForEachPlayerForTheNextCountByNullifyingThem();
	}
	

	private static void moveCardDraw(int[] playerLocation, int i) {
		if (cardAction.equals("move")) {
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

	private static void escapeCardDraw(int i) {
		if (cardAction.equals("escape")) {
			//can have only one
			hasTheGetOutOfJailCard[i] = true;
		}
	}

	private static void payCardDraw(int i) {
		if (cardAction.equals("pay")) {
			System.out.println("Lost " + cardWonOrLostMoney);
			playerMoney[i] -= Integer.parseInt(cardWonOrLostMoney);
		}
	}

	private static void winCardDraw(int i) {
		if (cardAction.equals("win")) {
			System.out.println("Won " + cardWonOrLostMoney);
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
		buildingHousesOptions(i);
		sellingHousesOptions(i);
	}

	private static void sellingHousesOptions(int i) {
		int fieldDecision = 0;
		int sellingAHouseOnTheFirstSecondOrThirdField = 0;
		int propertiesOnFirstField = 0, propertiesOnSecondField = 0, propertiesOnThirdField = 0;
		
		redHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		greenHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		 
		lightBlueHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		brownHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		darkBlueHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		yellowHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		purpleHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		
		orangeHouseSellingDecisions(i, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField, fieldDecision, sellingAHouseOnTheFirstSecondOrThirdField); 
		 
		// green, lightblue, brown, darkblue 2, yellow, purple, orange, red 2

	}

	private static void orangeHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllOrangeProperties[i]  && (numberOfBuildingsOnOrangeProperties[0] > 0 || numberOfBuildingsOnOrangeProperties[1] > 0 || numberOfBuildingsOnOrangeProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all orange properties (fields 31, 32 and 33).");
			propertiesOnFirstField = numberOfBuildingsOnOrangeProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnOrangeProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnOrangeProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 31, 32, 33, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 31, 32, 33);
			numberOfBuildingsOnOrangeProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);	
		}
	}

	private static void purpleHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllPurpleProperties[i] && (numberOfBuildingsOnPurpleProperties[0] > 0 || numberOfBuildingsOnPurpleProperties[1] > 0 || numberOfBuildingsOnPurpleProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all purple properties (fields 27, 28 and 29).");
			propertiesOnFirstField = numberOfBuildingsOnPurpleProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnPurpleProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnPurpleProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 27, 28, 29, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 27, 28, 29);
			numberOfBuildingsOnPurpleProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void yellowHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {

		if (isTheOwnerOfAllYellowProperties[i] && (numberOfBuildingsOnYellowProperties[0] > 0 || numberOfBuildingsOnYellowProperties[1] > 0 || numberOfBuildingsOnYellowProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all yellow properties (fields 21, 22 and 23).");
			propertiesOnFirstField = numberOfBuildingsOnYellowProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnYellowProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnYellowProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 21, 22, 23, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 21, 22, 23);
			numberOfBuildingsOnYellowProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);	
		} 
	}

	private static void darkBlueHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {

		if (isTheOwnerOfAllDarkBlueProperties[i] && (numberOfBuildingsOnDarkBlueProperties[0] > 0 || numberOfBuildingsOnDarkBlueProperties[1] > 0)) {
			System.out.println(playerNames[i] + " owns all darkblue properties (fields 2 and 3).");
			propertiesOnFirstField = numberOfBuildingsOnDarkBlueProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnDarkBlueProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnDarkBlueProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 2, 3, -1, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 2, 3, -1);
			numberOfBuildingsOnDarkBlueProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);
		} 
	}

	private static void brownHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllBrownProperties[i] && (numberOfBuildingsOnBrownProperties[0] > 0 || numberOfBuildingsOnBrownProperties[1] > 0 || numberOfBuildingsOnBrownProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all brown properties (fields 7, 8 and 9).");
			propertiesOnFirstField = numberOfBuildingsOnBrownProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnBrownProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnBrownProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 7, 8, 9, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 7, 8, 9);
			numberOfBuildingsOnBrownProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);
		} 
	}

	private static void lightBlueHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {

		if (isTheOwnerOfAllLightBlueProperties[i] && (numberOfBuildingsOnLightBlueProperties[0] > 0 || numberOfBuildingsOnLightBlueProperties[1] > 0 || numberOfBuildingsOnLightBlueProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all lightblue properties (fields 11, 12 and 13).");
			propertiesOnFirstField = numberOfBuildingsOnLightBlueProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnLightBlueProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnLightBlueProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 11, 12, 13, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 11, 12, 13);
			numberOfBuildingsOnLightBlueProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);	
		}
	}

	private static void greenHouseSellingDecisions(int i,
			int propertiesOnFirstField, int propertiesOnSecondField,
			int propertiesOnThirdField, int fieldDecision,
			int sellingAHouseOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllGreenProperties[i] && (numberOfBuildingsOnGreenProperties[0] > 0 || numberOfBuildingsOnGreenProperties[1] > 0 || numberOfBuildingsOnGreenProperties[2] > 0)) {
			System.out.println(playerNames[i] + " owns all green properties (fields 17, 18 and 19).");
			propertiesOnFirstField = numberOfBuildingsOnGreenProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnGreenProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnGreenProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 17, 18, 19, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 17, 18, 19);
			numberOfBuildingsOnGreenProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void redHouseSellingDecisions(int i, int propertiesOnFirstField, 
			int propertiesOnSecondField, int propertiesOnThirdField, 
			int fieldDecision, int sellingAHouseOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllRedProperties[i] && (numberOfBuildingsOnRedProperties[0] > 0 || numberOfBuildingsOnRedProperties[1] > 0)) {
			System.out.println(playerNames[i] + " owns all red properties (fields 37 and 38).");
			propertiesOnFirstField = numberOfBuildingsOnRedProperties[0];
			propertiesOnSecondField = numberOfBuildingsOnRedProperties[1];
			propertiesOnThirdField = numberOfBuildingsOnRedProperties[2];
			
			fieldDecision = sellingHouseFieldDecision(i, 37, 38, -1, propertiesOnFirstField, propertiesOnSecondField, propertiesOnThirdField);
			sellingAHouseOrNotDoingAnything(fieldDecision, i);
			sellingAHouseOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 37, 38, -1);
			numberOfBuildingsOnRedProperties[sellingAHouseOnTheFirstSecondOrThirdField - 1] = sellingAHouseOrNotDoingAnything(fieldDecision, i);	
		}
	}

	private static int sellingAHouseOrNotDoingAnything(int fieldDecision, int i) {
		int buildingCountDecreaseInCaseOfSelling;
		
		if (fieldDecision != -1) {
			System.out.println(playerNames[i] + " erected a house on field " + fieldDecision);
			formattedBoard[fieldDecision][6] = Integer.toString(Integer.parseInt(formattedBoard[fieldDecision][6]) - 1); 
			playerMoney[i] += Integer.parseInt(formattedBoard[fieldDecision][3]) / 4;
			buildingCountDecreaseInCaseOfSelling = 1;
		} else {
			System.out.println(playerNames[i] + " didn't sell a house.");
			buildingCountDecreaseInCaseOfSelling = 0;
		}
		
		return buildingCountDecreaseInCaseOfSelling;
	}

	private static int sellingHouseFieldDecision(int i, int j, int k, int l, int propertiesOnFirstField, int propertiesOnSecondField, int propertiesOnThirdField) {
		char actionDecision;
		int sellHouseOnWhichField = -1;
		
		System.out.println("(S)ell a house on one of the properties' fields, or (d)o nothing.");
		System.out.println("The first property has " + propertiesOnFirstField + ", the second " + propertiesOnSecondField + " and the third " +  + propertiesOnThirdField);
		do {
			System.out.println("(S)ell/(D)o nothing.");
			actionDecision = input.next().charAt(0);
			
			if (actionDecision == 's') {
				if (l == -1) {
					System.out.println("Sell a house on which field (" + j + " or " + k + ").");
					do {
						sellHouseOnWhichField = input.nextInt();
					} while ((sellHouseOnWhichField != j && propertiesOnFirstField <= 0) && (sellHouseOnWhichField != k && propertiesOnSecondField <= 0));
				} else {
					System.out.println("Sell a house on which field (" + j + ", " + k + " or " + l + ").");
					do {
						sellHouseOnWhichField = input.nextInt();
						
					} while ((sellHouseOnWhichField != j && propertiesOnFirstField <= 0) && (sellHouseOnWhichField != k && propertiesOnSecondField <= 0) && (sellHouseOnWhichField != l && propertiesOnThirdField <= 0));
				}
			} else if (actionDecision == 'd') {
				sellHouseOnWhichField = -1;
			}
		} while(actionDecision != 'e' && actionDecision !='d');
		
		return sellHouseOnWhichField;
	}

	private static void buildingHousesOptions(int i) {
		int fieldDecision = 0;
		int builtOnTheFirstSecondOrThirdField = 0;
		
		redHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);  
		
		greenHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);  
		
		lightBlueHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);   
		
		brownHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);   
		
		darkBlueHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);   
		
		yellowHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);   
		
		purpleHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);   
				
		orangeHouseBuildingDecisions(i, fieldDecision, builtOnTheFirstSecondOrThirdField);    
		// green, lightblue, brown, darkblue 2, yellow, purple, orange, red 2

	}

	private static void orangeHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllOrangeProperties[i]) {
			System.out.println(playerNames[i] + " owns all orange properties (fields 31, 32 and 33).");
			fieldDecision = erectingHousesFieldDecision(i, 31, 32, 33);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 31, 32, 33);
			numberOfBuildingsOnOrangeProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);	
		}
	}

	private static void purpleHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllPurpleProperties[i]) {
			System.out.println(playerNames[i] + " owns all purple properties (fields 27, 28 and 29).");
			fieldDecision = erectingHousesFieldDecision(i, 27, 28, 29);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 27, 28, 29);
			numberOfBuildingsOnPurpleProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void yellowHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllYellowProperties[i]) {
			System.out.println(playerNames[i] + " owns all yellow properties (fields 21, 22 and 23).");
			fieldDecision = erectingHousesFieldDecision(i, 21, 22, 23);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 21, 22, 23);
			numberOfBuildingsOnYellowProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void darkBlueHouseBuildingDecisions(int i,
			int fieldDecision, int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllDarkBlueProperties[i]) {
			System.out.println(playerNames[i] + " owns all darkblue properties (fields 2 and 3).");
			fieldDecision = erectingHousesFieldDecision(i, 2, 3, -1);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 2, 3, -1);
			numberOfBuildingsOnDarkBlueProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);			
		} 
	}

	private static void brownHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllBrownProperties[i]) {
			System.out.println(playerNames[i] + " owns all brown properties (fields 7, 8 and 9).");
			fieldDecision = erectingHousesFieldDecision(i, 7, 8, 9);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 7, 8, 9);
			numberOfBuildingsOnBrownProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);	
		}
	}

	private static void lightBlueHouseBuildingDecisions(int i,
			int fieldDecision, int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllLightBlueProperties[i]) {
			System.out.println(playerNames[i] + " owns all lightblue properties (fields 11, 12 and 13).");
			fieldDecision = erectingHousesFieldDecision(i, 11, 12, 13);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 11, 12, 13);
			numberOfBuildingsOnLightBlueProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void greenHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllGreenProperties[i]) {
			System.out.println(playerNames[i] + " owns all green properties (fields 17, 18 and 19).");
			fieldDecision = erectingHousesFieldDecision(i, 17, 18, 19);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 17, 18, 19);
			numberOfBuildingsOnGreenProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static void redHouseBuildingDecisions(int i, int fieldDecision,
			int builtOnTheFirstSecondOrThirdField) {
		if (isTheOwnerOfAllRedProperties[i]) {
			System.out.println(playerNames[i] + " owns all red properties (fields 37 and 38).");
			fieldDecision = erectingHousesFieldDecision(i, 37, 38, -1);
			buildingHouseOrNotDoingAnything(fieldDecision, i);
			builtOnTheFirstSecondOrThirdField = getTheNumberOfThePickedField(fieldDecision, 37, 38, -1);
			numberOfBuildingsOnRedProperties[builtOnTheFirstSecondOrThirdField - 1] = buildingHouseOrNotDoingAnything(fieldDecision, i);
			
		}
	}

	private static int getTheNumberOfThePickedField(int fieldDecision, int j, int k, int l) {
		int pickNumber;
		//fieldDecision is always equal to j, k or l
		
		if (l == -1) {
			if (fieldDecision == j) {
				pickNumber = 1;
			} else {//if (fieldDecision == k) {
				pickNumber = 2;
			}
		} else {
			if (fieldDecision == j) {
				pickNumber = 1;
			} else if (fieldDecision == k) {
				pickNumber = 2;
			} else {//if (fieldDecision == l) {
				pickNumber = 3;
			}
		}
		return pickNumber;
	}

	private static int buildingHouseOrNotDoingAnything(int fieldDecision, int i) {
		int buildingCountIncreaseInCaseOfErection;
		
		if (fieldDecision != -1) {
			System.out.println(playerNames[i] + " erected a house on field " + fieldDecision);
			formattedBoard[fieldDecision][6] = Integer.toString(Integer.parseInt(formattedBoard[fieldDecision][6]) + 1); 
			playerMoney[i] -= Integer.parseInt(formattedBoard[fieldDecision][3]) / 3;
			buildingCountIncreaseInCaseOfErection = 1;
		} else {
			System.out.println(playerNames[i] + " didn't erect a house.");
			buildingCountIncreaseInCaseOfErection = 0;
		}
		
		return buildingCountIncreaseInCaseOfErection;
	}

	private static int erectingHousesFieldDecision(int i, int j, int k, int l) {
		char actionDecision;
		int buildOnWhichField = -1;
		
		System.out.println("(E)rect a house on one of the properties' fields, or (d)o nothing.");
		do {
			System.out.println("(E)rect/(D)o nothing.");
			actionDecision = input.next().charAt(0);
			
			if (actionDecision == 'e') {
				if (l == -1) {
					System.out.println("Build on which field (" + j + " or " + k + ").");
					do {
						buildOnWhichField = input.nextInt();
					} while (buildOnWhichField != j && buildOnWhichField != k);
				} else {
					System.out.println("Build on which field (" + j + ", " + k + " or " + l + ").");
					do {
						buildOnWhichField = input.nextInt();
						
					} while (buildOnWhichField != j && buildOnWhichField != k && buildOnWhichField != l);
				}
			} else if (actionDecision == 'd') {
				buildOnWhichField = -1;
			}
		} while(actionDecision != 'e' && actionDecision !='d');
		
		return buildOnWhichField;
	}

	private static void prepareTheNumberOfOwnedColoredPropertiesForEachPlayerForTheNextCountByNullifyingThem() {
		for (int p = 0; p < playerAmount; p ++) {
			numberOfOwnedRedProperties[p] = 0;
			isTheOwnerOfAllRedProperties[p] = false;
			
			numberOfOwnedGreenProperties[p] = 0;
			isTheOwnerOfAllGreenProperties[p] = false;
		
			numberOfOwnedBrownProperties[p] = 0;
			isTheOwnerOfAllBrownProperties[p] = false;
		
			numberOfOwnedDarkBlueProperties[p] = 0;
			isTheOwnerOfAllDarkBlueProperties[p] = false;
		
			numberOfOwnedYellowProperties[p] = 0;
			isTheOwnerOfAllYellowProperties[p] = false;
		
			numberOfOwnedPurpleProperties[p] = 0;
			isTheOwnerOfAllPurpleProperties[p] = false;
		
			numberOfOwnedOrangeProperties[p] = 0;
			isTheOwnerOfAllOrangeProperties[p] = false;
		}// green, lightblue, brown, darkblue 2, yellow, purple, orange, red 2
		
	}

	private static void countTheNumberOfOwnedColoredPropertiesForEachPlayer() {
		String ownerOfTheField;
		String colorOfTheField;
		for (int i = 0; i < formattedBoard.length; i++) {
			//we only want the owner and the color
			//for (int j = formattedBoard[i].length - 2; j < formattedBoard[i].length; j++) {
				//we check for every player
			for (int p = 0; p < playerAmount; p ++) {
				ownerOfTheField = formattedBoard[i][3];
				colorOfTheField = formattedBoard[i][4];
				if (ownerOfTheField.equals(playerNames[p])) {
					//there are only 2 red and darkblue properties (the rest are 3)
					if (colorOfTheField.equals("red")) {
						numberOfOwnedRedProperties[p]++;
						//formattedBoard[i][6] = Integer.toString(numberOfOwnedRedProperties[p]);
						if (numberOfOwnedRedProperties[p] == 2) {
							isTheOwnerOfAllRedProperties[p] = true;
						}
					} else if (colorOfTheField.equals("darkblue")) {
						numberOfOwnedDarkBlueProperties[p]++;
						if (numberOfOwnedDarkBlueProperties[p] == 2) {
							isTheOwnerOfAllDarkBlueProperties[p] = true;
						}
					} else if (colorOfTheField.equals("green")) {
						numberOfOwnedGreenProperties[p]++;
						if (numberOfOwnedGreenProperties[p] == 3) {
							isTheOwnerOfAllGreenProperties[p] = true;
						}
					} else if (colorOfTheField.equals("lightblue")) {
						numberOfOwnedLightBlueProperties[p]++;
						if (numberOfOwnedLightBlueProperties[p] == 3) {
							isTheOwnerOfAllLightBlueProperties[p] = true;
						}
					} else if (colorOfTheField.equals("brown")) {
						numberOfOwnedBrownProperties[p]++;
						if (numberOfOwnedBrownProperties[p] == 3) {
							isTheOwnerOfAllBrownProperties[p] = true;
						}
					} else if (colorOfTheField.equals("yellow")) {
						numberOfOwnedYellowProperties[p]++;
						if (numberOfOwnedYellowProperties[p] == 3) {
							isTheOwnerOfAllYellowProperties[p] = true;
						}
					} else if (colorOfTheField.equals("purple")) {
						numberOfOwnedPurpleProperties[p]++;
						if (numberOfOwnedPurpleProperties[p] == 3) {
							isTheOwnerOfAllPurpleProperties[p] = true;
						}
					} else if (colorOfTheField.equals("orange")) {
						numberOfOwnedOrangeProperties[p]++;
						if (numberOfOwnedOrangeProperties[p] == 3) {
							isTheOwnerOfAllOrangeProperties[p] = true;
						}
					}
				}
			}// green, lightblue, brown, darkblue 2, yellow, purple, orange, red 2
			
			/*private static boolean[] isTheOwnerOfAllRedProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedRedProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllOrangeFields = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedOrangeProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllPurpleFields = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedPurpleProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllYellowProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedYellowProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllGreenFields = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedGreenProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllBrownFields = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedBrownProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllLightBlueProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedLightBlueProperties = {STARTING_MONEY, STARTING_MONEY, STARTING_MONEY, STARTING_MONEY};//new int[MAX_AMOUNT_OF_PLAYERS];
	private static boolean[] isTheOwnerOfAllDarkBlueProperties = new boolean[MAX_AMOUNT_OF_PLAYERS];
	private static int[] numberOfOwnedDarkBlueProperties*/
			//}
		}
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
		
		//isInJail[i] = true;
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
		//formattedBoard[playerLocation[i]]/*.split(", ")*/[2] = fieldAction;
		//^ doens't change
		if (!fieldOwner.equals("-") && !fieldCost.equals("-"))
		formattedBoard[playerLocation[i]]/*.split(", ")*/[4] = fieldOwner;
		System.out.println("FIELD OWNER: " + fieldOwner);
		formattedBoard[playerLocation[i]]/*.split(", ")*/[3] = fieldCost;
		System.out.println("FIELD COST: " + fieldCost);
		//done in the methods for buying and selling houses
		//formattedBoard[playerLocation[i]][6] = fieldBuildingsCount;
	}

	private static void getTheFieldParameters(int[] playerLocation, int i) {
		fieldAction = formattedBoard[playerLocation[i]][2];//.split(", ")[2];
		fieldOwner = formattedBoard[playerLocation[i]][4];//.split(", ")[4];
		fieldCost = formattedBoard[playerLocation[i]][3];//.split(", ")[3];
		fieldColor = formattedBoard[playerLocation[i]][5];
		fieldBuildingsCount = formattedBoard[playerLocation[i]][6];
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
		int highestBid = DEFAULT_STARTING_COUNT;
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
//			choice = input.next().charAt(0);
//		} while (choice != 'b' || choice != 'a');
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
		unformattedBoard[20] = "PARKING, parking, pay, 0, -, -, 0"; 
	}

	private static void fillTheStart(/*String[] unformattedBoard*/) {
		unformattedBoard[0] = "COLLECT $200 AS YOU PASS, go, win, 200, -, -, 0";
	}

	private static void fillPrisons(/*String[] unformattedBoard*/) {
		unformattedBoard[10] = "PRISON, prison, prison options, -, -, -, 0";
		unformattedBoard[30] = "GO TO JAIL, inprisonment, inprisonment, -, -, -, 0";
	}

	private static void fillTheTaxes(/*String[] unformattedBoard*/) {
		unformattedBoard[4] = "TAX REWARD, reward, win, 200, -, -, 0";
		unformattedBoard[39] = "SUPER TAX, tax, pay, 100, -, -, 0";
	}

	private static void fillTheLuckyDraws(/*String[] unformattedBoard*/) {
		unformattedBoard[6] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
		unformattedBoard[24] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
		unformattedBoard[36] = "LUCKY DRAW, luck, draw luck card, -, -, -, 0";
	}

	private static void fillTheFactories(/*String[] unformattedBoard*/) {
		unformattedBoard[14] = "CHEZ, factory, buy, 150, no owner, -, 0";
		unformattedBoard[26] = "VIK, factory, buy, 150, no owner, -, 0";
	}

	private static void fillTheChests(/*String[] unformattedBoard*/) {
		unformattedBoard[1] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
		unformattedBoard[16] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
		unformattedBoard[34] = "COMMUNITY CHEST, chest, draw chest card, -, -, -, 0";
	}

	private static void fillTheStations(/*String[] unformattedBoard*/) {
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
		unformattedBoard[37] = "BULEVARD VITOSHA, property, buy, 360, no owner, red, 0";
		unformattedBoard[38] = "BOIANA, property, buy, 400, no owner, red, 0";
	}

	private static void fillTheOrangeProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[31] = "G. S. RAKOVSKI STREET, property, buy, 300, no owner, orange, 0";
		unformattedBoard[32] = "GRAF IGNATIEV STREET, property, buy, 300, no owner, orange, 0";
		unformattedBoard[33] = "G. M. DIMITROV BULEVARD, property, buy, 320, no owner, orange, 0";
	}

	private static void fillThePurpleProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[27] = "LONDUKOV BULEVARD, property, buy, 280, no owner, purple, 0";
		unformattedBoard[28] = "PATRIARH EVTIMII BULEVARD, property, buy, 280, no owner, purple, 0";
		unformattedBoard[29] = "VASIL LEVSKI BULEVARD, property, buy, 300, no owner, purple, 0";
	}

	
	private static void fillTheYellowProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[21] = "SAN STEFANO STREET, property, buy, 220, no owner, yellow, 0";
		unformattedBoard[22] = "SHIPKA STREET, property, buy, 220, no owner, yellow, 0";
		unformattedBoard[23] = "OBORISHTE STREET, property, buy, 240, no owner, yellow, 0";
	}

	private static void fillTheGreenProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[17] = "EVLOGI GEORGIEV, property, buy, 200, no owner, green, 0";
		unformattedBoard[18] = "ORLOV BRIDGE, property, buy, 200, no owner, green, 0";
		unformattedBoard[19] = "BULGARIA BULEVARD, property, buy, 220, no owner, green, 0";
	}
	
	private static void fillTheLightBlueProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[11] = "MAKEDONSKI SQUARE, property, buy, 150, no owner, lightblue, 0";
		unformattedBoard[12] = "PIROTSKA STREET, property, buy, 150, no owner, lightblue, 0";
		unformattedBoard[13] = "HRISTO BOTEV, property, buy, 180, no owner, lightblue, 0";
	}

	private static void fillTheBrownProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[7] = "CHERNI MOUNT, property, buy, 100, no owner, brown, 0";
		unformattedBoard[8] = "LOMSKI ROAD, property, buy, 100, no owner, brown, 0";
		unformattedBoard[9] = "LUVOV BRIDGE, property, buy, 100, no owner, brown, 0";
	}

	private static void fillTheDarkBlueProperties(/*String[] unformattedBoard*/) {
		unformattedBoard[2] = "BLAGOEVGRAD ROAD, property, buy, 50, no owner, darkblue, 0";
		unformattedBoard[3] = "CARIGRAD ROAD, property, buy, 60, no owner, darkblue, 0";
	}
}
