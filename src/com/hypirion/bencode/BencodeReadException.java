package com.hypirion.bencode;

public class BencodeReadException extends Exception {
    public BencodeReadException(String message, Object... args) {
        super(String.format(message, args));
    }
}
