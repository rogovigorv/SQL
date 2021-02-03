package com.foxminded.SQL.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QueryInput implements Input {
    @Override
    public String input() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String chose = null;

        try {
            chose = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chose;
    }
}
