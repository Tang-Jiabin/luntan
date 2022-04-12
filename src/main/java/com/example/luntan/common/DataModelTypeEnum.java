package com.example.luntan.common;

public enum DataModelTypeEnum {
    CK(1), DZ(3), PL(7), SC(15);

    private final int score;
    DataModelTypeEnum(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
