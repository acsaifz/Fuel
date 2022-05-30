package hu.progmatic;

import hu.progmatic.model.FuelPrice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
    private static final double EURHUF_PRICE = 307.7;

    public static void main(String[] args) {
        Set<FuelPrice> fuelPriceSet = new TreeSet<>();
        readFuelPriceData(fuelPriceSet);
        int priceDifference = minPriceDifference(fuelPriceSet);

        System.out.print("3. feladat: ");
        System.out.println("Változások száma: " + fuelPriceSet.size());
        System.out.print("4. feladat: ");
        System.out.println("Legkisebb különbség: " + priceDifference);
        System.out.print("5. feladat: ");
        System.out.println("Legkisebb különbség előfordulása: " + priceDifferenceOccurenceCount(fuelPriceSet,priceDifference));
        System.out.print("6. feladat: ");
        if (isPriceChangeOnLeapDay(fuelPriceSet)){
            System.out.println("Volt változás szökőnapon!");
        }else{
            System.out.println("Nem volt változás szökőnapon!");
        }
        writeEuroFuelPriceToFile(fuelPriceSet);
        int year = getYearFromUser();
        System.out.print("10. feladat: ");
        System.out.println(year + " évben a leghosszabb időszak " + getLongestPriceChangeInterval(fuelPriceSet,year) + " nap volt.");
    }
    private static int daysBeetweenFuelPriceChanges(LocalDate actualDate, LocalDate previousDate){
        /*int[] daysCount = {31,28,31,30,31,30,31,31,30,31,30,31};
        if (actualDate.getMonthValue() == previousDate.getMonthValue()){
            return actualDate.getDayOfMonth() - previousDate.getDayOfMonth();
        }else{
            if (actualDate.isLeapYear()) daysCount[1] = 29;
            return daysCount[previousDate.getMonthValue() - 1] - previousDate.getDayOfMonth() + actualDate.getDayOfMonth();
        }*/
        return actualDate.getDayOfYear()-previousDate.getDayOfYear();
    }

    private static int getLongestPriceChangeInterval(Set<FuelPrice> fuelPriceSet, int year){
        int interval = 0;
        Set<FuelPrice> filteredFuelPrice = filterFuelPriceByYear(fuelPriceSet,year);

        for (int i = 1; i < filteredFuelPrice.size(); i++){
            int days = daysBeetweenFuelPriceChanges(
                    filteredFuelPrice.stream().toList().get(i).getDate(),
                    filteredFuelPrice.stream().toList().get(i-1).getDate()
            );
            if (interval < days){
                interval = days;
            }
        }

        return interval;
    }

    private static Set<FuelPrice> filterFuelPriceByYear(Set<FuelPrice> fuelPriceSet, int year){
        Set<FuelPrice> filteredFuelPrice = new TreeSet<>();
        for (FuelPrice fuelPrice: fuelPriceSet){
            if (fuelPrice.getDate().getYear() == year){
                filteredFuelPrice.add(fuelPrice);
            }
        }
        return filteredFuelPrice;
    }

    public static int getYearFromUser(){
        Scanner scanner = new Scanner(System.in);
        int year;
        do {
            System.out.print("8. feladat: Kérem adja meg az évszámot[2011..2016]: ");
            try {
                year = scanner.nextInt();
            }catch (InputMismatchException e){
                year = 0;
                scanner.nextLine();
            }
        }while(year > 2016 || year < 2011);
        return year;
    }
    private static void readFuelPriceData(Set<FuelPrice> fuelPriceSet){
        try(Scanner fileReader = new Scanner(new File("src/main/resources/uzemanyag.txt"))){
            while(fileReader.hasNext()){
                fuelPriceSet.add(new FuelPrice(fileReader.nextLine()));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void writeEuroFuelPriceToFile(Set<FuelPrice> fuelPriceSet){
        try(PrintWriter writer = new PrintWriter("src/main/resources/euro.txt")){
            for (FuelPrice fuelPrice: fuelPriceSet){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                writer.println(
                        fuelPrice.getDate().format(formatter) +
                                ";" +
                                String.format("%.2f", fuelPrice.getGasoline() / EURHUF_PRICE) +
                                ";" +
                                String.format("%.2f", fuelPrice.getDiesel() / EURHUF_PRICE)
                );
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private static boolean isPriceChangeOnLeapDay(Set<FuelPrice> fuelPriceSet){
        for(FuelPrice fuelPrice: fuelPriceSet){
            if (fuelPrice.getDate().isLeapYear() && fuelPrice.getDate().getMonthValue() == 2 && fuelPrice.getDate().getDayOfMonth() == 24){
                return true;
            }
        }
        return false;
    }

    private static int priceDifferenceOccurenceCount(Set<FuelPrice> fuelPriceSet, int priceDifference){
        int count = 0;
        for (FuelPrice fuelPrice: fuelPriceSet){
            if(fuelPrice.getPriceDifference() == priceDifference){
               count++;
            }
        }
        return count;
    }

    private static int minPriceDifference(Set<FuelPrice> fuelPriceSet){
        int minPriceDifference = Integer.MAX_VALUE;

        for (FuelPrice fuelPrice: fuelPriceSet){
            if(fuelPrice.getPriceDifference() < minPriceDifference){
                minPriceDifference = fuelPrice.getPriceDifference();
            }
        }

        return minPriceDifference;
    }
}
