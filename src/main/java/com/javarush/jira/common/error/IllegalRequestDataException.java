package com.javarush.jira.common.error;

import java.io.IOException;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }

    public IllegalRequestDataException(String msg, IOException ex) {
        super(msg, ex);
    }
}