package com.techelevator.meal;

import com.sun.security.jgss.GSSUtil;
import com.techelevator.meal.model.Dish;
import com.techelevator.meal.model.Veggie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DinnerPlan {

    private final Scanner userInput = new Scanner(System.in);
    private static DinnerPlan application;

    private File outputFile = new File("Output File for Print.txt");

    private PrintWriter writer;


    public static void main(String[] args) {
        application = new DinnerPlan();
        application.run();
    }


    private void run(){
        // printWrite to a new file

        List<Dish> dishesVeggie = new ArrayList<>();
        List<Dish> dishesMeat = new ArrayList<>();
        List<Dish> dishesSoup = new ArrayList<>();
        File dishFileVeggie = new File("Meals List - Veggie.txt");
        File dishFileMeat = new File("Meals List - Meat.txt");
        File dishFileSoup = new File("Meals List - Soup.txt");

        addDishesToList(dishesVeggie,dishFileVeggie );
        addDishesToList(dishesMeat,dishFileMeat );
        addDishesToList(dishesSoup,dishFileSoup );

        displayBanner();
        boolean running = true;
        while (running){


            System.out.println();
            System.out.println("******** Main menu ********");
            displayMainMenu();
            int selection = promptForInt("Please select an option: ");
            if (selection == 1){
                displayAllDishes(dishesVeggie, dishesMeat,dishesSoup );
            } else if (selection == 2){
                layoutSchedule(dishesVeggie, dishesMeat,dishesSoup,dishFileVeggie, dishFileMeat, dishFileSoup);
            } else if (selection == 3) {
                addNewDishes(dishesVeggie, dishesMeat,
                        dishesSoup, dishFileVeggie, dishFileMeat, dishFileSoup);
            } else if (selection == 4) {
                running = false;
            } else{
                displayError("Invalid option selected");
            }
        }
    }

    private void displayAllDishes(List<Dish> dishesVeggie, List<Dish> dishesMeat,List<Dish> dishesSoup  ) {
        Dish dish;
        System.out.println("*** Veggie dishes ***");
        promptMeals(dishesVeggie);
        System.out.println("*** Meat dishes ***");
        promptMeals(dishesMeat);
        System.out.println("*** Soup dishes ***");
        promptMeals(dishesSoup);
        System.out.println();
    }


    private void addDishesToList(List<Dish> dishesVeggie, File dishFileVeggie){
        Dish dish;
        try(Scanner fileScanner = new Scanner(dishFileVeggie) ){
            while (fileScanner.hasNextLine()){
                String nextLine = fileScanner.nextLine();
                String[] dishVeggieArray = nextLine.split("\\|");
//                System.out.println(Arrays.toString(dishVeggieArray));

                int i=0;
                for (String eachDish : dishVeggieArray){
                    dish = new Veggie();
                    dish.setDishName(eachDish) ;
                    dish.setDishId(i+1);
                    dishesVeggie.add(dish);
                    i++;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void promptMeals(List<Dish> dishesVeggie) {
       /*Loop through the list*/
        for (Dish dish : dishesVeggie){
            System.out.println(dish.getDishId() + ") " +  dish.getDishName());
        }
    }

    private int promptForInt(String prompt) {
        return (int) promptForDouble(prompt);
    }

    private double promptForDouble(String prompt) {
        while(true){
            System.out.println(prompt);
            String response = userInput.nextLine();
            try{
                return Double.parseDouble(response);
            }catch (NumberFormatException e){
                if (response.isBlank()){
                    return -1; // assumes negative numbers are never valid entries.
                } else{
                    displayError("Numbers only, please.");
                }
            }
        }
    }

    private void displayError(String message) {
        System.out.println();
        System.out.println("***" + message + "***");
        System.out.println();
    }


    private void displayBanner(){
        System.out.println("------------------------------------------------");
        System.out.println("|         Dinner Planner and Management        |");
        System.out.println("------------------------------------------------");
    }

    private void displayMainMenu(){
        System.out.println("1. List all dishes ");
        System.out.println("2. Layout Schedule ");
        System.out.println("3. Add New Dish ");
        System.out.println("4. Exit ");
    }

    private String promptForString(String prompt){
        System.out.println(prompt);
        return userInput.nextLine();
    }

    private void layoutSchedule(List<Dish> dishesVeggie, List<Dish> dishesMeat,
                                List<Dish> dishesSoup, File dishFileVeggie, File dishFileMeat, File dishFileSoup  ){
        Random rand = new Random();
        int responseInNumber = 1;
        int responseVeggie = 0;
        int responseMeat = 0;
        int responseSoup = 0;

        try {responseInNumber = Integer.parseInt(promptForString("How many days of dinner you want to layout? "));
            responseVeggie = Integer.parseInt(promptForString("How many Veggie dishes?"));
            responseMeat = Integer.parseInt(promptForString("How many Meat dishes?"));
            responseSoup = Integer.parseInt(promptForString("How many Soup dishes?"));
        } catch (NumberFormatException e){
            System.out.println("Please enter a whole number. ");
        }

        // due to list to be renewed every 3 days, need to make sure if the numbers of dishes are enough to be displayed
        if (responseVeggie *3 > dishesVeggie.size()){
            System.out.println("******** Warning: Not enough dish options to choose from ********");
        } else{
            try (PrintWriter writer = new PrintWriter(outputFile)) {

                for (int i = 1; i <= responseInNumber; i++) {

                  if (i>3 && i % 3 ==1){
                        //update the list every 3 days
                      dishesVeggie = new ArrayList<>();
                      dishesMeat = new ArrayList<>();
                      dishesSoup = new ArrayList<>();
                        addDishesToList(dishesVeggie,dishFileVeggie );
                        addDishesToList(dishesMeat,dishFileMeat );
                        addDishesToList(dishesSoup,dishFileSoup );
                    }
                    System.out.println();
                    System.out.println();
                    writer.println();
                    writer.println();
                    System.out.println("***** Day" + i + " Meal Plan *****");
                    writer.println("***** Day" + i + " Meal Plan *****");
                    System.out.println();
                    writer.println();
                    System.out.println("*** Veggie Dish ***");
                    writer.println("*** Veggie Dish ***");
                    randomScheduleForOneMeal(dishesVeggie, rand, responseVeggie,writer);
                    System.out.println("*** Meat Dish ***");
                    writer.println("*** Meat Dish ***");
                    randomScheduleForOneMeal(dishesMeat, rand, responseMeat,writer);
                    System.out.println("*** Soup Dish ***");
                    writer.println("*** Soup Dish ***");
                    randomScheduleForOneMeal(dishesSoup, rand, responseSoup,writer);

                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void randomScheduleForOneMeal(List<Dish> dishesVeggie, Random rand, int responseVeggie, PrintWriter writer) {
        Dish randomElement;
        Dish randomNewElement = null;
        if (responseVeggie == 0){
            System.out.println("There is nothing for today.");
        }else{
            for (int i = 0; i < responseVeggie; i++) {
                int randomIndex = rand.nextInt(dishesVeggie.size());
                randomElement = dishesVeggie.get(randomIndex);
//                if (randomElement.equals(randomNewElement)){
//                    randomElement = dishesVeggie.get(randomIndex);
//                }
                dishesVeggie.remove(randomIndex);
                randomNewElement = randomElement;
//                System.out.println("No." + randomNewElement.getDishId() + " - " + randomNewElement.getDishName());
                System.out.println(randomNewElement.toString());
                writer.println("No." + randomNewElement.getDishId() + " - " + randomNewElement.getDishName());
            }
        }
    }

    private void addNewDishes(List<Dish> dishesVeggie, List<Dish> dishesMeat,
                              List<Dish> dishesSoup,  File dishFileVeggie, File dishFileMeat, File dishFileSoup){

        Dish newDish = promptForNewDish(dishesVeggie, dishesMeat,
                dishesSoup, dishFileVeggie, dishFileMeat, dishFileSoup);

    }

    private Dish promptForNewDish(List<Dish> dishesVeggie, List<Dish> dishesMeat,
                                  List<Dish> dishesSoup,  File dishFileVeggie, File dishFileMeat, File dishFileSoup) {
        Dish dish = new Dish();
        // add dish to list
        String name = "";
        while (name.isBlank()){
            name = promptForString("Dish name: ");
        }
        dish.setDishName(name);

        String dishType = promptForString("What type of dish is this? (M)eat? (V)eggie? (S)oup?" );
        if (dishType.equalsIgnoreCase("m")){
            dishesMeat.add(dish);
            dish.setDishId(dishesMeat.size());

            writeDishToFile(dishFileMeat, dish);

        } else if (dishType.equalsIgnoreCase("v")){
            dishesVeggie.add(dish);
            dish.setDishId(dishesVeggie.size());

            writeDishToFile(dishFileVeggie, dish);

        } else if (dishType.equalsIgnoreCase("s")){
            dishesSoup.add(dish);
            dish.setDishId(dishesSoup.size());

            writeDishToFile(dishFileSoup, dish);

        }

      return dish;
    }

    private void writeDishToFile(File dishFileMeat, Dish dish) {
        // add dish to txt file
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(dishFileMeat, true))){
            writer.print("|" + dish.getDishName());
            System.out.println(dish.getDishName() + " has been added in " + dishFileMeat.getName());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
