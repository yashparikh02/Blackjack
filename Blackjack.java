/**************************************
* Author: Yash Parikh                 *
* Date: 12/22/2021                    *
**************************************/
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


// class Deck represents a standard deck of card
class Deck {
    private Card[] deck; // an array of cards (the deck)
    private int cardsUsed; // number of cards that have been dealt out

    public Deck() {
        deck = new Card[52];
        cardsUsed = 0;
        int i = 0;
        
        // intialize all cards and place them into the deck
        for (int value = 0; value <= 12; value++) {
          for (int suit = 0; suit <= 3; suit++) {
                deck[i] = new Card(suit, value);
                i++;
            }
        }
        
        // shuffle the deck
        shuffle();

    }

        // randomize the elements by calling Collections.shuffle()
    void shuffle() {
        List < Card > cardList = Arrays.asList(deck);
        Collections.shuffle(cardList);
        cardList.toArray(deck);
    }

    
    // deal out one card, the next unused card
    Card deal() {
        cardsUsed++;
        if (cardsUsed % 52 == 0) shuffle();
        return deck[cardsUsed];
    }
} // end class Deck


class Card {
  private final int suit; // the index of the suit array, used to det. suit
  private final int value; // index of value array used to det. rank
  
   // list of all suits that are available
    private final String[] cardType = {
        "Clubs",
        "Spades",
        "Diamonds",
        "Hearts"
    };
    // list of all ranks that are available
    private final String[] cardValue = {
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "Jack",
        "Queen",
        "King",
        "Ace"
    };


    // constructor for card class, initializes local variables  
    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;

    }

    // returns the card as a string
    public String asString() {
        String output = cardValue[value] + " of " + cardType[suit];
        return output;
    }
    
    // returns the value private variable
    int getValue() {
        return this.value;
    }

} //end class Card


// this class represents a player of Blackjack
class Player{
  private final Deck deck; // the deck that this player uses
  private ArrayList<Hand> allHands; // all the hands the player has
  private int money; // the wealth of the player, defaults to 1000
  private boolean splitted; // whether or not the player has split
  
  // constructor for this class
  public Player(Deck deck) {
      this.deck = deck;
      
      // all players have one hand and some wealth
      allHands = new ArrayList<Hand>();
      allHands.add(new Hand(this.deck));
      money = 1000;
  }
  
  // sets all hands to empty
  void clearHands(){
    for (int i = 0; i < allHands.size(); i++){
      allHands.get(i).clearHand();
      if (i > 0) allHands.remove(i);
    }
    splitted = false;
  }
  
  // getter method for the player's wealth
  int getMoney() {
    return money;
  }
  
  // setter method for the player's wealth
  void setMoney(int amount){
    money = amount;
  }
  
  // amount of hands the player has, at this point in time
  int handCount(){
    return allHands.size();
  }
  
  // returns the hand at position i in the set of hands the player has
  Hand getHand(int i){
    return allHands.get(i);
  }
  
  // adds a hand to the set of hands the player has
  void addHand(){
    allHands.add(new Hand(deck));
  }
  
  // getter method for splitted
  boolean getSplitted(){
    return splitted;
  }
  // setter method for splitted
  void setSplitted(boolean value){
    splitted = value;
  }

}



class Hand{
  
    // instance variables
    private final Deck deck; // the deck provided for the hand to be drawn from
    private ArrayList <Card> hand; // the cards in the hand
    private boolean ace; // whether or not the hand contains an ace
    private boolean busted; // whether or not the hand's value is > 21

    
    // constructor for the hand, takes in a deck
    public Hand(Deck deck) {
        this.deck = deck;
        this.hand = new ArrayList<Card>();
    }
    
    
    // adds a random card to the hand
    void hit() {
        Card c = deck.deal();
        if (c.getValue() == 13) ace = true;
        hand.add(c);
    }


    // retrieves the value of the hand. 
    // aces are counted as 11s, but they can be converted to ones 
    // if the user hits
    int handValue() {
        int value = 0;
        for (int i = 0; i < hand.size(); i++) {
            int cardValue = hand.get(i).getValue();
            if (cardValue == 12) {
                value += 1;
                ace = true;
            } else if (cardValue >= 9) value += 10;
            else value += cardValue + 2;
        }

        if (ace == true && value + 10 <= 21) {
            value = value + 10;
        }

        return value;
    }

    
    // returns the hand as a string
    String handCards() {
        String handCards = "";
        for (int i = 0; i < hand.size(); i++) {
            handCards += hand.get(i).asString() + " ";
        }
        return handCards;
    }
    
    // getter method for busted
    boolean getBusted() {
      return (busted == true);
    }
    
    // setter method for busted
    void setBusted(boolean val){
      this.busted = val;
    }
    
    // adds a card to the hand
    void addCard(Card c){
      hand.add(c);
    }
    
    // retrieves the card at position in i in the hand
    Card getCard(int i){
      return hand.get(i);
    }
    
    // return the amount of cards in the hand
    int handSize(){
      return hand.size();
    }
    
    // removes all cards from the hand
    void clearHand(){
      ace = false;
      busted = false;
      hand.clear();
    }
    
} //end class Player



public class Blackjack {
  
    //  Main method, prompts the user for game input then begins the game
    public static void main(String[] args) {
      
      // Welcome the User to blackjack
       System.out.println("*------------------------*");
       System.out.println("*  WELCOME TO BLACKJACK  *");
       System.out.println("*------------------------*");
        Scanner reader = new Scanner(System.in);



        //Initialize deck and dealer
        Deck deck = new Deck();
        Player dealer = new Player(deck);
        Hand dealerHand = dealer.getHand(0);
        dealerHand.hit();
        dealerHand.hit();



        //creates a table of players
        ArrayList <Player> set = new ArrayList < Player > ();
        System.out.println(">>> How many people would like to be seated at the blackjack table? <<<");
        int count;
        do {
            count = reader.nextInt();
            if (count <= 0 || count > 7)
                System.out.println("Please enter a valid amount. The house only has up to seven seats");
        } while (count <= 0 || count > 7);

        String names;
        for (int i = 0; i < count; i++) {
            set.add(new Player(deck));
        }
        
        

        // Play the game
        System.out.println("*------------------------*");
        System.out.println("*        Let's Play      *");
        System.out.println("*------------------------*");
        int[] bets = playBlackjack(set, dealer);
        determineWinners(set, dealer, bets);

        // ask all players if they want to keep playing, remove those who do not
        while (true) {
          System.out.println("*------------------------*");
          System.out.println("*       Play Again?      *");
          System.out.println("*------------------------*");
            System.out.println("All players must decide if they want to play again.");
            
            // use a while loop to handle issue of set.size() changing depending on input
            int i = 0;
            while (i < set.size()){
                Player user = set.get(i);
                user.clearHands();
                if (user.getMoney() <= 0) {
                    System.out.println("You can't afford to play \n");
                    set.remove(i);
                    continue;
                }
                System.out.println(">>> Would you like to play again? Type yes if so, no otherwise <<<");
                String input;
                do {
                    input = reader.nextLine();
                    if (!input.equals("yes") && !input.equals("no"))
                        System.out.println("Please respond yes or no");
                } while (!input.equals("yes") && !input.equals("no"));
        
        
        
                // ensure that they have confirmed they want to keep playing
                if (input.equals("yes")){
                  i++;
                }
        
                // if they do not, remove them and print how much they won
                else if (input.equals("no")) {
                    System.out.println(" *** Thanks for coming today, your final wealth is " + set.get(i).getMoney() + " *** ");
                    set.remove(i);
                }
        
            }
        
            // end the game when no one wants to continue to play
            if ((set.size() == 0)) break;
            else System.out.println("Everyone will be reseated before the next round");
            System.out.println("-----------------------------------");
        
        
            // reinitialize fields, and play game again
            deck.shuffle();
            dealer.clearHands();
            dealerHand.hit();
            dealerHand.hit();
            bets = playBlackjack(set, dealer);
            determineWinners(set, dealer, bets);
        
        }

        // thank the user for playing
        System.out.println("*------------------------*");
        System.out.println("*   THANKS FOR PLAYING   *");
        System.out.println("*      SEE YOU SOON      *");
        System.out.println("*------------------------*");
    }



    // this methods returns the bets that are placed by each player, 
    // and it also allows the players to make their decisions before the 
    // dealer makes his/hers
    public static int[] playBlackjack(ArrayList<Player> table, Player dealer) {

        //init vars
        Scanner reader = new Scanner(System.in);
        int[] betSizes = new int[table.size()];

        for (int i = 0; i < table.size(); i++) {


            // all users initially start with one hand,
            Player user = table.get(i);
            Hand userHand = user.getHand(0);
            Hand dealerHand = dealer.getHand(0);
            
            // collect the initial bet
            System.out.println("Hello, player " + i + " your wealth is " + user.getMoney());
            System.out.println(">>> How much would you like to bet? Enter the amount below <<<");
            int betSize;
            
            //validate betsize input
            do {
                betSize = reader.nextInt();
                if (betSize < 0 || betSize > user.getMoney())
                    System.out.println("Please enter a valid size (0 to your wealth)");
            } while (betSize < 0 || betSize > user.getMoney());
            betSizes[i] = betSize;


            // give the user two cards
            userHand.hit();
            userHand.hit();
            
            // print out the cards the players received
            System.out.println("Your cards are: " + userHand.handCards());
            System.out.println("Your total is " + userHand.handValue());
            System.out.println("Dealer is showing " + dealerHand.getCard(0).asString());


            // if the two cards are of equal value, splitting is an option
            if (userHand.getCard(0).getValue() == userHand.getCard(1).getValue()){
              
              // offer the choice to split into two hands
              System.out.println("Would you like to split? Enter 'split' if so, 'no' if not");
              String choice;
              reader.nextLine();
              
              
              // validate the user's choice
              do {
                  choice = reader.nextLine();
                  if (!choice.equals("split") && !choice.equals("no"))
                      System.out.println("Please respond split or no");
              } while (!choice.equals("split") && !choice.equals("no"));
              
              
              
              if(choice.equals("split")){
                
                //they can only split if they can afford to split
                if (betSizes[i] * 2 <= user.getMoney()){
                  

                  System.out.println("Split. Make decisions for each of your hands seperately");
                  
                  // make two hands, adding the one card from the original to both
                  user.addHand();
                  Card c1 = userHand.getCard(0);
                  Card c2 = userHand.getCard(1);
                  userHand.clearHand();
                  userHand.addCard(c1);
                  userHand.hit();
                  user.getHand(1).addCard(c2);
                  user.getHand(1).hit();
                  
                  // increase betSize by a factor of two, and indicate the user has split
                  user.setSplitted(true);
                  betSizes[i] *= 2;
                }
                else System.out.println("No option to split for you, you can't afford it!");
              }
            }
            
            // Note: At this casino, users cannot double down after splitting
            // provide the option to double down
            else {
              
              // prompt the user to make a choice
              System.out.println(">>> Would you like to double down? Enter 'double down' if so, 'no' if not");
              String input;
              reader.nextLine();
              
              //validate user input
              do {
                  input = reader.nextLine();
                  if (!input.equals("double down") && !input.equals("no"))
                      System.out.println("Please respond 'double down' or 'no'");
              } while (!input.equals("double down") && !input.equals("no"));
              
              // if they've decided to double down
              if (input.equals("double down")) {
                  System.out.println(">>> By how much would you you like to increase your bet? <<<");

                  int increase;
                  do {
                      increase = reader.nextInt();
                      if (increase < 0 || increase > betSize || increase + betSize > user.getMoney())
                          System.out.println("Please enter a valid size (0 to your bet size), assuming you can afford it");
                  } while (increase < 0 || increase > betSize || increase + betSize > user.getMoney());

                  //as a result of doubling down, their bet size has changed
                  betSizes[i] += increase;


                  // User takes one card then continues
                  userHand.hit();
                  System.out.println("You've hit.");
                  System.out.println("Your total is now " + userHand.handValue());
                  
                  // it is possible that they have busted now with their third
                  if (userHand.handValue() > 21) {
                      System.out.println("You busted!");
                      userHand.setBusted(true);
                  }
                  System.out.println();
                  continue;
              }
            }


            // iterate through all the hands the player has (2 if split, 1 otherwise)
            for (int j = 0; j < user.handCount(); j++){
              userHand = user.getHand(j);
              System.out.println();
              
              
              // if the user has blackjack or got to 21 after doubling down
              if (userHand.handValue() == 21) {
                  System.out.println("your total is already 21, thus, you have no decisions to make \n");
                  continue;
              }


              // the user can decide whether or not to hit or stand while their total < 21
              while (true) {

                  // Show users cards and allow them to choose to hit or stand
                    System.out.println("Your cards are: " + userHand.handCards());
                    System.out.println("Your total is " + userHand.handValue());
                    System.out.println("Dealer is showing " + dealerHand.getCard(0).asString());
                    System.out.println(">>> Hit (hit) or stand (stand)? <<< ");
                    String choice;
                    
                  // validate the user's choice
                  do {
                      choice = reader.nextLine();
                      if (!choice.equals("hit") && !choice.equals("stand"))
                          System.out.println("Please respond hit or stand");
                  } while (!choice.equals("hit") && !choice.equals("stand"));


                  // stand: player has opted to stop taking cards
                  if (choice.equals("stand")) {

                      System.out.println();
                      break;
                  } 
                  // hit: the player wants another card
                  else { 
                      userHand.hit();
                      System.out.println("You've hit.");
                      System.out.println("Your total is now " + userHand.handValue());
                      
                      // ensure that they have not busted with the addition of this card
                      if (userHand.handValue() > 21) {
                          System.out.println("You busted! \n");
                          userHand.setBusted(true);
                          break;
                      }
                      System.out.println();
                  }

              } // end while loop regarding hit or stand
              System.out.println("-----");
            } //end for loop iterating through hands
          } //end for loop iterating through players
          


        return betSizes;

    } // end playBlackjack


    
    public static void determineWinners(ArrayList<Player> table, Player dealer, int[] bets) {
        
        // some formatting 
        System.out.println("*------------------------*");
        System.out.println("*      RESULTS BELOW     *");
        System.out.println("*------------------------*");
        
        // go through all the players at the table
        for (int i = 0; i < table.size(); i++) {
        
            Player user = table.get(i);
            
            // iterate through all the hands they have
            for (int j  =0; j < user.handCount(); j++){
              Hand userHand = user.getHand(j);
              Hand dealerHand = dealer.getHand(0);
          
          
              // Blackjack: defined as a 2 card 21.
              
              if(userHand.handValue() == 21 && userHand.handSize() == 2){
                
                // if the dealer and player both have blackjack, the outcome is a push (tie)
                if(dealerHand.handValue() == 21 && dealerHand.handSize()== 2){
                  System.out.println(" *** PUSH. You win 0. Both you and dealer had blackjack. \n");
                  continue;
                }
                
                // otherwise, if just the player does, the player wins
                else{
                  System.out.println(" *** BLACKJACK. You win 1.5x your bet of " + (bets[i] / user.handCount()) + ". *** \n");
                  user.setMoney(user.getMoney() + (int) (1.5 * (bets[i] / user.handCount())));
                  continue;
                }
              }
          
          
              // Print out player seat
              System.out.println("Hello, Player " + i + "\n");
              
              // Handle cases for when the user busts
              
              // user busts, dealer doesn't -- dealer wins
              if (userHand.getBusted() && !dealerHand.getBusted()) {

                  System.out.println(" *** DEALER WINS. You lose your bet of " + (bets[i] / user.handCount()) + " . You busted! *** \n");
                  user.setMoney(user.getMoney() - (bets[i] / user.handCount()));
                  continue;
              }
              // dealer busts, user doesn't -- user wins
              else if (dealerHand.getBusted() && !userHand.getBusted()) {

                  System.out.println(" *** YOU WIN. You win your bet of " + (bets[i] / user.handCount()) + " . Dealer busted! *** \n");
                  user.setMoney(user.getMoney() + (bets[i] / user.handCount()));
                  continue;
              } 
              // user and dealer both bust: dealer wins
              else if (userHand.getBusted() && dealerHand.getBusted()) {
                  System.out.println(" *** DEALER WINS. You lose your bet of " + (bets[i] / user.handCount()) + " . Both you and dealer busted! *** \n");
                  user.setMoney(user.getMoney() - (bets[i] / user.handCount()));
                  continue;
              } 
              
              // with no busts, we can move to comparing their values
              else {
                
                  // remind the user what the dealer has and what they have
                  System.out.println("Player " + i + " As a reminder, your cards are the " + userHand.handCards() + "for a value of " + userHand.handValue() + "\n");
                  System.out.println("Dealer's cards are " + dealerHand.handCards());
          
                  // Dealer still needs to draw as long as the dealer's value is <=16, as defined
                  while (dealerHand.handValue() <= 16) {
          
                      dealerHand.hit();
                      System.out.println("The dealer hit to now have a hand of " + dealerHand.handCards());
                      
                  }
                  
                  // if the dealer has busted, there is no reason to compare values, the player wins!
                  if (dealerHand.handValue() > 21) {
                      System.out.println(" *** YOU WIN. You win your bet of " + (bets[i] / user.handCount()) + " . Dealer busted! *** \n");
                      user.setMoney(user.getMoney() + (bets[i] / user.handCount()));
                      dealerHand.setBusted(true);
                      continue;  
                    }
                    
                  // report the value that the dealer has settled upon.
                  System.out.println("Dealer's total is " + dealerHand.handValue());
          
          
                  // Compare the numeric value of the dealers hand with the users hand
                  // if the dealer has the same value as the dealer, it's a push: a tie
                  if (dealerHand.handValue() == userHand.handValue()) {
                      System.out.println(" *** PUSH. You win 0. Both you and the dealer had the same final value. *** \n");
                  } 
                  //if the dealer has a greater hand value than the user, the dealer wins
                  else if (dealerHand.handValue() > userHand.handValue()) {
                      System.out.println(" *** DEALER WINS. You lose your bet of " + (bets[i] / user.handCount()) + " . Dealer had a higher final value. *** \n");
                      user.setMoney(user.getMoney() - (bets[i] / user.handCount()));
                  }
                  //if the dealer has a greater lesser value than the user, the dealer wins
                  else {
                      System.out.println(" *** YOU WIN. You win your bet of " + (bets[i] / user.handCount()) + " . You had a higher final value. *** \n");
                      user.setMoney(user.getMoney() + (bets[i] / user.handCount()));
                  }
          
              } //end the case where no one has busted
          
            } // end for loop iterating through hands
      
          System.out.println("----");
        } // end for loop iterating through players
      } // end result generation
    } // end the Blackjack class


