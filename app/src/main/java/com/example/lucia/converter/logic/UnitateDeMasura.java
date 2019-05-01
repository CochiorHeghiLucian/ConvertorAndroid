package com.example.lucia.converter.logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Set;

public class UnitateDeMasura {
    private String nume;
    private BigDecimal rate;
    private BigDecimal value;

    public UnitateDeMasura(String nume, BigDecimal rate) {
        this.nume = nume;
        this.rate = rate;
        value = new BigDecimal(0);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getNume() {
        return nume;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal convertTo(UnitateDeMasura currency, BigDecimal amount) {
        BigDecimal amountInEur = amount.divide(this.rate, 20, RoundingMode.CEILING);
        BigDecimal result = amountInEur.multiply(currency.rate);
        currency.setValue(result);
        return result;
    }

    public BigDecimal specialConvertTo(UnitateDeMasura currency, BigDecimal amount) {
        BigDecimal amountInBase = amount.multiply(this.rate);
        BigDecimal result = amountInBase.divide(currency.rate,10, RoundingMode.CEILING);
        currency.setValue(result);
        return result;
    }

    public BigDecimal specialConvertTemperatureTo(UnitateDeMasura currency, BigDecimal amount) {

        BigDecimal amountInBase = transformToCelsius(this, amount);
        BigDecimal result = transformCelsiusTo(currency, amountInBase);
        currency.setValue(result);
        return result;
    }

    @Override
    public String toString() {
        return "UnitateDeMasura{" +
                "nume='" + nume + '\'' +
                ", rate=" + rate +
                ", value=" + value +
                '}';
    }

    public static UnitateDeMasura findUnitateDeMasuraByName(Set<UnitateDeMasura> setOfUnits, String name) {
        for (UnitateDeMasura unitateDeMasura : setOfUnits) {
            if (unitateDeMasura.getNume().equals(name)) {
                return unitateDeMasura;
            }
        }
        return null;
    }

    private static BigDecimal transformToCelsius(UnitateDeMasura tempUnit, BigDecimal amount){
        if (tempUnit.getNume().equals("F")){
            return amount.multiply(BigDecimal.valueOf(1.8)).add(BigDecimal.valueOf(32.0));
        }

        if (tempUnit.getNume().equals("K")){
            return amount.add(BigDecimal.valueOf(273.15));
        }

        if (tempUnit.getNume().equals("R")){
            return amount.add(BigDecimal.valueOf(273.15)).multiply(BigDecimal.valueOf(1.8));
        }

        return amount;
    }

    private static BigDecimal transformCelsiusTo(UnitateDeMasura tempUnit, BigDecimal amount){
        if (tempUnit.getNume().equals("F")){
            return amount.add(BigDecimal.valueOf(-32)).multiply(BigDecimal.valueOf(0.555555555556));
        }

        if (tempUnit.getNume().equals("K")){
            return amount.add(BigDecimal.valueOf(-273.15));
        }

        if (tempUnit.getNume().equals("R")){
            return amount.add(BigDecimal.valueOf(-491.67)).multiply(BigDecimal.valueOf(0.555555555556));
        }

        return amount;
    }

}
