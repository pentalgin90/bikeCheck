package com.bragin.bike_theft_check.model;

public enum BotState {
    CREATE,
    ENTER_FRAME_NUMBER,
    ENTER_VENDOR,
    ENTER_MODEL_NAME,
    ENTER_DESCRIPTION,
    CHECK_BIKE,
    START,
    MY_BIKES,
    ENTER_FRAME_NUMBER_FOR_CHECK,
    INFO,
    UPDATE_BIKES,
    STOP;
}
