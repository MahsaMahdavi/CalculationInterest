package com.company;


import com.exception.DepositBalanceException;
import com.exception.DepositTypeException;
import com.exception.DurationInDaysException;
import com.exception.FileException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    static int customerNumber;
    static String depositTypeStr;
    static BigDecimal depositBalance;
    static int durationDays;

    public static void main(String[] args) {
        ArrayList<Deposit> depositArrayList = new ArrayList<Deposit>();
        try {
            NodeList nList = parsXml().getElementsByTagName("deposit");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    customerNumber = Integer.parseInt(element.getElementsByTagName("customerNumber").item(0).getTextContent());
                    depositTypeStr = element.getElementsByTagName("depositType").item(0).getTextContent();
                    depositBalance = new BigDecimal(element.getElementsByTagName("depositBalance").item(0).getTextContent());
                    durationDays = Integer.parseInt(element.getElementsByTagName("durationInDays").item(0).getTextContent());
                    makeDepositObject(depositArrayList);
                }
            }
        }catch(DepositTypeException depositTypeException){
                    depositTypeException.printStackTrace();
                }
        catch (FileException fileException){
            fileException.printStackTrace();
        }
            }

    public static Document parsXml() throws FileException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse("test.xml");
        } catch (ParserConfigurationException e) {
            throw new FileException("The system cannot find your xml file!");
        } catch (SAXException e) {
            throw new FileException("The system cannot find your xml file!");
        } catch (IOException e) {
            throw new FileException("The system cannot find your xml file!");
        }
        return document;
    }

    public static void makeDepositObject(ArrayList<Deposit> depositArrayList) throws DepositTypeException {
        try {
            Class depositTypeClass = Class.forName("com.company." + depositTypeStr);
            DepositType depositTypeObj = (DepositType) depositTypeClass.newInstance();
            validateDepositBalance(depositBalance);
            validateDepositDurationInDays(durationDays);
            Deposit deposit = new Deposit(customerNumber, depositTypeObj, depositBalance, durationDays);
            deposit.calculatePaidInterest();
            depositArrayList.add(deposit);
            descendingSort(depositArrayList);
            writeFile(depositArrayList);
        } catch (ClassNotFoundException e) {
            throw new DepositTypeException("This type of deposit does not exist !");
        } catch (InstantiationException e) {
            throw new DepositTypeException("This type of deposit does not exist !");
        } catch (IllegalAccessException e) {
            throw new DepositTypeException("This type of deposit does not exist !");
        } catch (DepositBalanceException e) {
            e.printStackTrace();
        } catch (DurationInDaysException e) {
            e.printStackTrace();
        } catch (FileException fileException){
            fileException.printStackTrace();
        }
    }

    public static void validateDepositBalance(BigDecimal depositBalance) throws DepositBalanceException {
        if (depositBalance.compareTo(new BigDecimal("0")) != 1)
            throw new DepositBalanceException("Sorry! Deposit Balance is invalid.");
    }

    public static void validateDepositDurationInDays(int duration) throws DurationInDaysException {
        if (duration <= 0)
            throw new DurationInDaysException("Sorry! Duration In Days is invalid.");
    }

    public static void descendingSort(ArrayList<Deposit> depostArrayList) {
        Collections.sort(depostArrayList);
    }

    public static void writeFile(ArrayList<Deposit> depositArrayList) throws FileException {
        File outputFile = new File("output.txt");
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(outputFile, "rw");
            for (Deposit depositArray : depositArrayList) {
                randomAccessFile.writeBytes(depositArray.customerNumber + " # " + depositArray.paidInterest);
                randomAccessFile.write('\r');
                randomAccessFile.write('\n');
            }
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            throw new FileException("The system cannot find your xml file!!");
        } catch (IOException e) {
            throw new FileException("The system cannot find your xml file!!");
            }
        }
    }
