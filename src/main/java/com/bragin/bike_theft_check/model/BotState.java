package com.bragin.bike_theft_check.model;

public enum BotState {
    CREATE,
    ENTER_FRAME_NUMBER,
    ENTER_VENDOR,
    ENTER_MODEL_NAME,
    ENTER_DESCRIPTION,
    ENTER_WANTED,
    FIND_BIKE,
    START,
    ENTER_FRAME_NUMBER_FOR_SEARCH;
}