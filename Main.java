import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner input = new Scanner(System.in);
    public static Random random = new Random();
    public static int balance = 1000;
    public static int borrowed_money = 0;
    public static int bet;
    public static int total = 0;
    public static int dealer_total = 0;
    public static int dealer_hiddencard = 0;
    public static int[] card_holder = new int[10];
    public static int[] card_values = {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}; // array for card values

    public static void main(String[] args) {
        while (true) {
            if(balance <= 0 && borrowed_money == 1000){ //setting condition if balance is at 0 and dollars and I lost all the borrowed money the game will end immediately
                System.out.println("You're out of moeny have reached the borrowing limited, Game over!(but damn, your luck or skill is horrible you should never gamble in real life)");
                break;
            }

            System.out.println("Menu:"); // user menu 
            System.out.println("1. Play Game");
            System.out.println("2. Instructions");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = input.nextInt(); //prompting user for their choice
            input.nextLine();

            while (choice < 1 || choice > 3) { // checking if the user put anything else other than the given options, if not, it prompts user again
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                System.out.print("Enter your choice again: ");
                choice = input.nextInt();
                input.nextLine();
            }

            if (choice == 1) { // choice one is to play the game, and it will call the method for loading money if the user lost all their initial amount, the game will end if they chose not to
                if (balance <= 0){
                    loan_money();
                    if(balance <=0){
                        System.out.println("You have no money left and chose not to borrow. Game over! Better luck next time");
                        break;
                    }
                }
                play_game();

            } else if (choice == 2) { // calls method for showing the instructions if user chose 2
                show_instructions();
            } else { //choosing 3 will end the game
                System.out.println("Thanks for playing my game :)");
                break;
            }
        }
    }


    public static void loan_money(){ //method for loaning money
        System.out.println("You have no money left, but you have a chance to redeem yourself");
        System.out.println("Would you like to borrow up to $" + (1000 - borrowed_money) + "(y/n)"); //prompting user if they want to loan money or not
        String choice = input.nextLine();
        while(true){
            if (choice.equalsIgnoreCase("y")){ //if they user chose yes, it prompots user how much do they wanna loan
                while(true){
                    System.out.println("enter the amount you want to borrow, the maximum you can borrow is $" + (1000 - borrowed_money));
                    int borrow = input.nextInt();
                    input.nextLine();

                    if(borrow > 0 && borrow <= (1000 - borrowed_money)){ // checks if the user entered valid amount 
                        balance += borrow; //adds the borrowed amount to their balance
                        borrowed_money += borrow; //also has a different variable for checking the user's debt
                        System.out.println("You borrowed $" + borrow + " Your balance is now $" + balance);
                        System.out.println("Your future winnings will be used to repay this amount.");
                        //break;
                        return;
                    } else { //if user entered an invalid amount it tells user theres an error and will ask for them to enter again
                        System.out.println("Invalid amount. you can only borrow up to $" + (1000 - borrowed_money));
                    }
                }
            } else if (choice.equalsIgnoreCase("n")) { // if user chose no, it goes back to the main method and ends the game
                System.out.println("you chose not to borrow money");
                return;
            } else { //anything other than yes or no would be invalid and it will prompt the user again
                System.out.println("invalid input, choose between y/n");
                choice = input.nextLine();
            }
        }
    }
    public static void play_game() { //method for gameplay
        reset_game();
        System.out.println("Your current balance is: $" + balance);

        while (true){
            System.out.print("Enter your bet: "); //prompts user for their bet
            bet = input.nextInt();
            input.nextLine();

            if (bet > 0 && bet <= balance) { //checks if user entered a valid amount 
                break;
            } else {
                System.out.println("Invalid bet. Enter a valid amount.");
            }
        }
        balance -= bet; //updates my balance

        user_draw(); //calls method for user's hand
        dealer_draw(); //calls method for dealer's hand

        if (total == 21){ //checks if user blackjacks at the start
            System.out.println("You got BlackJack!");
            if(dealer_total + dealer_hiddencard == 21){ //if dealer also has a blackjack its a tie
                System.out.println("Dealer also has Blackjack, it's a tie!");
                balance += bet;
                return;
            } else { //if dealer doesn't have a blackjack, calculates the amount I win and paying off my debt if I have one
                int winnings = (int)(bet * 3);
                System.out.println("You win automatically with Blackjack!");
                if(borrowed_money > 0){
                int repayment = Math.min(winnings, borrowed_money);
                borrowed_money -= repayment;
                balance += (winnings - repayment);
                System.out.println("Repaying $" + repayment + " of your borrowed amount");
                System.out.println("Remaining debt: $" + borrowed_money);
                } else {
                    balance += winnings; //updates my balance
                }
                return;
        }
    } else if(dealer_total + dealer_hiddencard == 21){ //checks if dealer has blackjack
        System.out.println("Dealer has Blackjack! You lose");
        return;
    }

        while (true) { // shows user their hand's total value and prompts user if they want to hit or stand
            System.out.println("Your total: " + total);
            display_dealer_hand(false);
            //System.out.println("Dealer's visible card: " + dealer_total + ", Hidden card = ?");
            System.out.print("Would you like to (h)it or (s)tand? ");
            String action = input.nextLine();

            if (!action.equalsIgnoreCase("h") && !action.equalsIgnoreCase("s")) { //checks if user enters a valid answer
                System.out.println("Invalid choice. Please enter 'h' to hit or 's' to stand.");
                continue;
            }

            if (action.equalsIgnoreCase("h")) { //if user total goes over 21 if they chose to hit, the user loses
                hit();
                if (total > 21) {
                    System.out.println("Bust! You lose. Better luck next time.");
                    return;
                }
            } else { //if the user doesn't bust, the dealer will show their second card, and calls method for dealer's turn 
                System.out.println("Dealer reveals hidden card");
                display_dealer_hand(true);
                dealer_turn();
                check_winner(); //calls method for checking the winner
                return;
            }
        }
    }
    
    public static void display_dealer_hand(boolean showHidden){
        System.out.print("dealer's hand: [");
        if(showHidden){
            if(dealer_total == 11){
                System.out.print("A");
            } else {
                System.out.print(dealer_total);
            }
            System.out.print(", ");
            if(dealer_hiddencard == 11){
                System.out.print("A");
            } else {
                System.out.print(dealer_hiddencard);
            }
        } else {
            if (dealer_total == 11){
                System.out.print("A");
            } else {
                System.out.print(dealer_total);
            }
            System.out.print(", ?");
        }
        System.out.println("]");
    }

    public static void user_draw() { // draws 2 card from the draw_card method, and calculates the total
        for (int i = 0; i < 2; i++) {
            int card = draw_card();
            card_holder[i] = card;
            total += card;
        }
        ace_check(); //calls method for ace check

        System.out.print("Your cards: ["); //printing out the user's hand
        for (int i = 0; i < 2; i++) {
            if(card_holder[i] ==1){
                System.out.print("A");
            } else {
                System.out.print(card_holder[i]);
            }    
            if (i < 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void dealer_draw() { //method for calling method to draw dealer's first card and second card
        dealer_total = draw_card();
        dealer_hiddencard = draw_card();

        int initial_total = dealer_total + dealer_hiddencard; //calculating dealer's total

        if(dealer_total == 1){ //setting the initial worth for ace to be 11 for the first card
            dealer_total = 11; 
        }

        if(dealer_hiddencard == 1){ //setting the initial worth for ace to be 11 for the second card as well
            dealer_hiddencard = 11;
        }

        if(dealer_total+dealer_hiddencard == 21){ //blackjack check for dealer
            //System.out.println("Dealer has BlackJack! You Lose");

        }
    }

    public static void hit() { //method for hit if user chose to hit during the game
        int card = draw_card(); //calls method for drawing card
        if (card == 1){
            System.out.println("You drew A");
        } else {
            System.out.println("You drew: " + card); //shows user their card
        }
        
        for (int i = 0; i < card_holder.length; i++){
            if (card_holder[i] == 0){
                card_holder[i] = card;
                break;
            }
        }
        total += card; //adds to the total of their hand
        ace_check(); //calls method for ace check
    }

    public static void dealer_turn() { //method for dealer and deciding if it should draw more cards
        while (dealer_total < 17) { //if dealer's total is less than 17 it will continue drawing cards and keep calculating the total 
            int card = draw_card();
            if(card == 1){
                System.out.println("Dealer drew: A");
            } else {
                System.out.println("Dealer drew: " + card);
            }    
            dealer_total += card;
            if(dealer_total > 21 && card == 11){
                dealer_total -= 10;
            }
        }
        System.out.println("Dealer's total: " + dealer_total);
    }

    public static void check_winner() { // method for checking the winner 
        if (dealer_total > 21 || total > dealer_total) { // if dealer busted or user's total is higher, the user wins and calculates their win and pay their debt off if they have one
            int winnings = bet * 2;
            System.out.println("You win! You earn $" + winnings);
            if(borrowed_money > 0){
                int repayment = Math.min(winnings, borrowed_money);
                borrowed_money -= repayment;
                balance += (winnings - repayment);
                System.out.println("Repaying $" + repayment + " of your borrowed amount");
                System.out.println("Remaining debt: $" + borrowed_money);
            } else {
                balance += winnings; //if they have no debt it goes straight to their balance
            }
        } else if (dealer_total == total) { //if its a tie, the bet amount is added back to their balance
            System.out.println("It's a tie! Your bet has been returned.");
            balance += bet;
        } else { //otherwise, the user loses
            System.out.println("Dealer wins! You lose. Better luck next time.");
        }
    }

    public static void ace_check(){  //method for ace check
        int num_aces = 0;
        for (int i = 0; i < card_holder.length; i++) { 
            if (card_holder[i] == 1) {
                num_aces++;

            }
        }
        total = 0;
            for (int i = 0; i < card_holder.length; i++){
                total += card_holder[i];
            }
        while(num_aces > 0){
            if(total + 10 <= 21){
                total += 10;
            }
            num_aces --;
        }
    }

    public static int draw_card() { //method for drawing card
        int index = random.nextInt(card_values.length); //generate random index from the length of my array which is 52
        return card_values[index]; //return the value back to whichever line the code called this method from
    }

    public static void reset_game() { // method for resetting all the values to zero except debt owed or balance
        total = 0;
        dealer_total = 0;
        dealer_hiddencard = 0;
        for (int i = 0; i < card_holder.length; i++) {
            card_holder[i] = 0;
        }
    }

    public static void show_instructions() { //method for shoowing instructions to user
        System.out.println("Instructions:");
        System.out.println("1. Place your bet.");
        System.out.println("2. Choose 'hit' to draw more cards, or 'stand' to stop drawing cards.");
        System.out.println("3. Beat the dealer's total without going over 21 to win!");
    }
}