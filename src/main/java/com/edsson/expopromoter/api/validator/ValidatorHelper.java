package com.edsson.expopromoter.api.validator;

public class ValidatorHelper {


    static final int MAX_FIRST_NAME_SIZE = 32;
    static final int MAX_SECOND_NAME_SIZE = 32;

    static final int MAX_LOGIN_SIZE = 32;
    static final int MIN_LOGIN_SIZE = 4;

    static final int MIN_PASSWORD_SIZE = 8;
    static final int MAX_PASSWORD_SIZE = 32;

    //(?=.*[@#$%^&+=])
    static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{"+MIN_PASSWORD_SIZE+","+MAX_PASSWORD_SIZE+"}$";

    static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

}
