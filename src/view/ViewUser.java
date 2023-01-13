package view;

import controller.UserController;
import exception.DateFormatException;
import model.User;

import java.time.LocalDate;
import java.util.Scanner;

public class ViewUser {
    private static final byte NUMBER_OF_INPUT_DATA_FIELDS = 6;   // Число входных данных, разделённых пробелом
    private static final byte NUMBER_OF_DATE_DATA_FIELDS = 3;   // Число полей даты, разделенных точкой (dd.mm.yyyy)
    private static final byte DAY_FIELD_SIZE = 2;    // Размерность поля день (dd)
    private static final byte MONTH_FIELD_SIZE = 2;    // Размерность поля месяц (mm)
    private static final byte YEAR_FIELD_SIZE = 4;    // Размерность поля год (yyyy)
    private UserController userController;

    public ViewUser(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        while (true) {
            System.out.println("Введите данные пользователя через пробел в формате");
            System.out.println("Фамилия Имя Отчество дата_рождения(dd.mm.yyyy) номер_телефона(без +) пол(м/ж)");
            Scanner iScanner = new Scanner(System.in);
            System.out.println("Введите данные пользователя:");
            String inputString = iScanner.nextLine();
            String[] inputData = inputString.split(" ");
            int errorCode = checkInputString(inputData);
            if (errorCode == -1) {
                System.out.println("Внимание! Введено данных меньше, чем необходимо!");
            } else if (errorCode == -2) {
                System.out.println("Внимание! Введено данных больше, чем необходимо!");
            } else {
                try {
                    this.checkBirthDate(inputData[3]);
                    this.checkPhone(inputData[4]);
                    this.checkSex(inputData[5]);
                    this.userController.saveUser(new User(inputData[0], inputData[1], inputData[2], inputData[3], Long.parseLong(inputData[4]), inputData[5].charAt(0)));
                    System.out.println("Создать нового пользователя (д/н)?: ");
                    inputString = iScanner.nextLine();
                    if (!inputString.equalsIgnoreCase("д")) {
                        return;
                    }
                } catch (DateFormatException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    public Integer checkInputString(String[] inputData) {
        int errorCode = 0;      // 0 - OK, -1 - входных данных меньше, чем должно быть, -2 - входных данных больше, чем должно быть
        if (inputData.length < NUMBER_OF_INPUT_DATA_FIELDS) {
            errorCode = -1;
        } else if (inputData.length > NUMBER_OF_INPUT_DATA_FIELDS) {
            errorCode = -2;
        }
        return errorCode;
    }

    public void checkBirthDate(String birthDateString) throws DateFormatException {
        String[] birthDateData = birthDateString.split("\\.");
        if (birthDateData.length != NUMBER_OF_DATE_DATA_FIELDS) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): количество полей не равно трём!");
        }
        checkDay(birthDateData[0]);
        checkMonth(birthDateData[1]);
        checkYear(birthDateData[2]);
    }

    public void checkPhone(String phone) throws DateFormatException {
        if (!isNumeric(phone)) {
            throw new DateFormatException("Внимание! Неверный формат телефонного номера: должны быть только цифры!");
        }
    }

    public void checkSex(String sex) throws DateFormatException {
        if (!sex.equalsIgnoreCase("ж") && !sex.equalsIgnoreCase("м")) {
            throw new DateFormatException("Внимание! Неверный формат пола: должен быть 'ж' или 'м'");
        }
    }

    public void checkDay(String day) throws DateFormatException {
        if (day.length() != DAY_FIELD_SIZE) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): длина поля dd не равна двум!");
        }
        if (!isNumeric(day)) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле dd должно быть числом!");
        }
        if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле dd должно быть задано от 01 до 31");
        }
    }
    public void checkMonth(String month) throws DateFormatException {
        if (month.length() != MONTH_FIELD_SIZE) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): длина поля mm не равна двум!");
        }
        if (!isNumeric(month)) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле mm должно быть числом!");
        }
        if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле mm должно быть задано от 01 до 12");
        }
    }

    public void checkYear(String year) throws DateFormatException {
        if (year.length() != YEAR_FIELD_SIZE) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): длина поля yyyy не равна двум!");
        }
        if (!isNumeric(year)) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле yyyy должно быть числом!");
        }
        LocalDate localDate = LocalDate.now();
        if (Integer.parseInt(year) > localDate.getYear()) {
            throw new DateFormatException("Внимание! Неверный формат даты (dd.mm.yyyy): поле год yyyy должно быть задано от 1900 до " + localDate.getYear());
        }
    }

    public Boolean isNumeric(String str) {
        for (char symbol : str.toCharArray()) {
            if (!Character.isDigit(symbol)) {
                return false;
            }
        }
        return true;
    }
}