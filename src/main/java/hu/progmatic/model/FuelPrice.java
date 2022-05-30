package hu.progmatic.model;

import java.time.LocalDate;
import java.util.Objects;

public class FuelPrice implements Comparable<FuelPrice>{
    private LocalDate date;
    private int gasoline;
    private int diesel;

    public FuelPrice(String input){
        String[] fields = input.split(";");
        String[] dateSplitter = fields[0].split("\\.");
        this.date = LocalDate.of(Integer.parseInt(dateSplitter[0]),Integer.parseInt(dateSplitter[1]),Integer.parseInt(dateSplitter[2]));
        this.gasoline = Integer.parseInt(fields[1]);
        this.diesel = Integer.parseInt(fields[2]);
    }

    public int getPriceDifference(){
        return Math.abs(gasoline-diesel);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getGasoline() {
        return gasoline;
    }

    public void setGasoline(int gasoline) {
        this.gasoline = gasoline;
    }

    public int getDiesel() {
        return diesel;
    }

    public void setDiesel(int diesel) {
        this.diesel = diesel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelPrice fuelPrice = (FuelPrice) o;
        return date.equals(fuelPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public int compareTo(FuelPrice o) {
        if (date.isBefore(o.getDate())){
            return -1;
        }else{
            return 1;
        }
    }
}
