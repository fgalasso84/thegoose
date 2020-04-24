package it.fgalasso.thegoose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GiocoOca {
	
	static List<Player> players = new ArrayList<Player>();
	static final int theBridge = 6;
	static final List<Integer> theGoose = new ArrayList<Integer>(Arrays.asList(5, 9, 14, 18, 23, 27));

	public static void main(String[] args) {
		
		boolean winner = false;
		System.out.println("Actions:");
		System.out.println("Add new player, e.g. 'add player Pippo'");
		System.out.println("Move a player, e.g. 'move Pippo 4, 2' or simply 'move pippo'");
		System.out.println("Exit, you can exit with 'Exit'");
		System.out.println("");
		
		String action = "";
		while(!action.equalsIgnoreCase("exit")) {
			
			Scanner input = new Scanner(System.in);
			action = input.nextLine();
			if (action.toLowerCase().startsWith("add")) {
				
				String[] tokens = action.split(" ");
				
				if(tokens.length != 3) {
					System.out.println("WARNING: input '" + action + "' isn't correct!");
					continue;
				}
				
				Player newPlayer = new Player(tokens[2]);
				if (!playerExists(newPlayer)) {
					players.add(newPlayer);
					String outputAddPlayer = "Players: ";
					for (Player player : players) {
						outputAddPlayer += player.getName() + ", ";
					}
					outputAddPlayer = outputAddPlayer.trim();
					outputAddPlayer = outputAddPlayer.substring(0, outputAddPlayer.length() - 1);
					
					System.out.println(outputAddPlayer);
					
				} else {
					System.out.println(newPlayer.getName() + ": already existing player");
				}
				System.out.println("");
			} else if (action.toLowerCase().startsWith("move")) {
				
				String[] tokens = action.split(" ");
				
				if(tokens.length < 2) {
					System.out.println("WARNING: input '" + action + "' isn't correct!");
					continue;					
				}
				
				String playerName = tokens[1];
				Player player = getPlayer(playerName);
				
				if(null == player) {
					System.out.println("WARNING: Player '" + playerName + "' doesn't exist!");
					continue;
				}
				
				int valDiceOne;
				int valDiceTwo;
				if(tokens.length == 2 ) { // automatic dice
					valDiceOne = getDiceValue();
					valDiceTwo = getDiceValue();
				} else { // user dice
					if(tokens.length != 4) {
						System.out.println("WARNING: input '" + action + "' isn't correct!");
						continue;
					}
					String primoValore = tokens[2].replace(",","").trim();
					String secondoValore = tokens[3].replace(",","").trim();
					valDiceOne = Integer.valueOf(primoValore);
					valDiceTwo = Integer.valueOf(secondoValore);
				}
				
				int newPosition = valDiceOne+valDiceTwo;
				
				String msgMove = "";
				player.setPosition(newPosition);
				
				if(player.getPosition() == 63) { // wins
					msgMove = " moves from " + (player.getPrevPosition() == 0 ? "Start" : String.valueOf(player.getPrevPosition())) + " to " + player.getPosition() + ". " + player.getName() + " Wins!!";
					
					// the winner!!
					winner = true;
				} else if(player.getPosition() > 63) { // bounces
			
					int tmp = player.getPosition()-63;
					newPosition = 63-tmp;
					msgMove = " moves from " + (player.getPrevPosition() == 0 ? "Start" : String.valueOf(player.getPrevPosition())) + " to 63. "+player.getName()+" bounces! "+player.getName() + " returns to " + newPosition;
					player.setPositionLight(newPosition);
					
				} else {
					if(player.getPosition() == theBridge) { // The Bridge
						
						msgMove = " moves from " + (player.getPrevPosition() == 0 ? "Start" : String.valueOf(player.getPrevPosition())) + " to The Bridge. " + player.getName() + " jumps to 12";
						player.setPositionLight(12);
						
					} else if(theGoose.contains(player.getPosition())) { // The Goose
						
						msgMove = " moves from " + (player.getPrevPosition() == 0 ? "Start" : String.valueOf(player.getPrevPosition())) + " to " + player.getPosition();
						
						while(theGoose.contains(player.getPosition())){
							newPosition = player.getPosition() + valDiceOne+valDiceTwo;
							msgMove += ", The Goose. " + player.getName() + " moves again and goes to " + newPosition;
							player.setPositionLight(newPosition);
						}
						
					} else {
						msgMove = " moves from " + (player.getPrevPosition() == 0 ? "Start" : String.valueOf(player.getPrevPosition())) + " to " + player.getPosition();
					}
				}
				
				System.out.print(player.getName() + " rolls " + String.valueOf(valDiceOne) + ", " + String.valueOf(valDiceTwo) + ". " + player.getName() + msgMove);
				
				Player prankPl = getPlayerOnPosition(player);
				if(null != prankPl) { // Prank
					
					prankPl.setPositionLight(player.getPrevPosition());
					msgMove = ". On " + player.getPosition() + " there is " + prankPl.getName() + " who returns to " + prankPl.getPosition();
					System.out.println(msgMove);
					
				}
				
				if(winner) { // if there is a winner the game ends
					action = "Exit";
					continue;
				}
				
				System.out.println("");
				System.out.println("");
			} else {
				System.out.println("WARNING: input '" + action + "' isn't correct!");
				System.out.println("");
			}
			
		}
	}

	/*
	 * Check if player already exists
	 * 
	 * @param player that wants to check if exists
	 * @return true if the Player already exists, false otherwise
	 * */
	private static boolean playerExists(Player player) {
		for (Player existingPlayer : players) {
			if(player.getName().equalsIgnoreCase(existingPlayer.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Retrive a player by name
	 * 
	 * @param name of the Player that wants to retrieve
	 * @return the Player if exists, null otherwise
	 * */
	private static Player getPlayer(String name) {
		for (Player player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}
	
	/*
	 * Get another player on the same position of param Player
	 * 
	 * @param player to check if there is another player on the same position
	 * @return the Player who is on the same position of pl, null if doesn't exist
	 * */
	private static Player getPlayerOnPosition(Player pl) {
		for (Player player : players) {
			if(!pl.equals(player) && player.getPosition() == pl.getPosition()) {
				return player;
			}
		}
		return null;
	}
	
	/*
	 * Get random value of a dice
	 * 
	 * @return the random value of the dice
	 * */
	private static int getDiceValue() {
		Random random = new Random();
		int rand = 0;
		while (true){
		    rand = random.nextInt(7);
		    if(rand !=0) break;
		}
		return rand;
	} 
}
