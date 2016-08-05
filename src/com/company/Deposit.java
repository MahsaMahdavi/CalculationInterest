package com.company;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by mahsa on 02/08/2016.
 */
public class Deposit implements Comparable<Deposit>{
    int customerNumber;
    DepositType depositType;
    BigDecimal depositBalance;
    int durationDays;
    float paidInterest;

    public Deposit() {
    }

    public Deposit(int customerNumber, DepositType depositType, BigDecimal depositBalance, int durationDays) {
        this.customerNumber = customerNumber;
        this.depositType = depositType;
        this.depositBalance = depositBalance;
        this.durationDays = durationDays;
    }

    public void calculatePaidInterest() {

        MathContext mathContext = new MathContext(6);
        paidInterest = ((new BigDecimal(durationDays).multiply(depositBalance).multiply(new BigDecimal(depositType.interestRate))).divide(new BigDecimal("36500"),mathContext)).floatValue();
        //System.out.println(paidInterest);
    }


    @Override
    public int compareTo(Deposit comparableDeposit) {
    if(paidInterest == comparableDeposit.paidInterest)
        return 0;
    else if(paidInterest > comparableDeposit.paidInterest)
        return 1;
        else
            return -1;
    }
}
