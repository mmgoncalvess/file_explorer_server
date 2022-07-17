package com.example;

public enum Requests {
    ONGOING_DIRECTORY,
    PARENT,
    DIRECTORY,
    DELETE,
    NEW_DIRECTORY,
    RENAME,
    COPY,                           // Copy a file from remote host to the same remote host
    RECEIVE,                        // Local host send a file to remote
    SEND;                           // Remote host send a file to local
}
