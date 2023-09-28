package com.example.jobconnect.Validations;

public class VD_Registration {
    static String namePattern="^[A-Za-z]+(?: [A-Za-z]+)*$";
    static String phonePattern="^[0-9]{10}$";
    static String emailPattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    static String passwordPattern = "(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*[^a-zA-Z0-9]).{6}";




    public static boolean isValidName(String Name) {
        return !Name.isEmpty() && Name.length() >=3 && Name.matches(namePattern);
    }

    public static boolean isValidEmail(String Email) {
        return !Email.isEmpty() && Email.matches(emailPattern) ;
    }

    public static boolean isValidPassword(String password) {
        return !password.isEmpty() && password.matches("(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).*") &&
                String.valueOf(password.charAt(0)).matches("[a-zA-Z0-9\\W]");    }
    public static boolean isValidPhone(String phone) {
        return !phone.isEmpty() && phone.matches(phonePattern) && phone.length()==10;
    }

    public static boolean isValidRePassword(String Password, String RePassword) {

        return !Password.isEmpty() && Password.equals(RePassword);
    }


}
