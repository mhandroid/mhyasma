package com.android.mh.yasma.exception;

import java.io.IOException;

/**
 * Class for network exception
 */
public class NoNetworkException extends IOException {

    @Override
    public String getMessage() {
        return "Please check your internet connection.";
    }
}
