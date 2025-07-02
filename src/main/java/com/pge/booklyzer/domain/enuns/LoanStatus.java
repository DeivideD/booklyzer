package com.pge.booklyzer.domain.enuns;

public enum LoanStatus {
  ACTIVE, RETURNED, OVERDUE;
  public static LoanStatus fromString(String text) {
    for (LoanStatus status : LoanStatus.values()) {
      if (status.name().equalsIgnoreCase(text)) {
        return status;
      }
    }
    throw new IllegalArgumentException("No enum constant " + text);
  }

}
