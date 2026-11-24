package com.interview.ledger.exception;

public class ExerciseNotImplementedException extends RuntimeException {
    public ExerciseNotImplementedException(String exercise) { super("Exercise " + exercise + " has not been implemented"); }
}
