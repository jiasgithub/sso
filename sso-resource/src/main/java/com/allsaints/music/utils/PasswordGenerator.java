package com.allsaints.music.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
	
	public static final char[] allowedSpecialCharactors = {
            '`', '~', '@', '#', '$', '%', '^', '&',
            '*', '(', ')', '-', '_', '=', '+', '[',
            '{', '}', ']', '\\', '|', ';', ':', '"',
            '\'', ',', '<', '.', '>', '/', '?'};
    private static final int letterRange = 26;
    private static final int numberRange = 10;
    private static final int spCharactorRange = allowedSpecialCharactors.length;
    private static final Random random = new Random();
    private int passwordLength;
    private int minVariousType;

    public PasswordGenerator(int passwordLength, int minVariousType) {
        if (minVariousType > CharactorType.values().length) minVariousType = CharactorType.values().length;
        if (minVariousType > passwordLength) minVariousType = passwordLength;
        this.passwordLength = passwordLength;
        this.minVariousType = minVariousType;
    }

    public String generateRandomPassword() {
        char[] password = new char[passwordLength];
        List<Integer> pwCharsIndex = new ArrayList<Integer>();
        for (int i = 0; i < password.length; i++) {
            pwCharsIndex.add(i);
        }
        List<CharactorType> takeTypes = new ArrayList<CharactorType>(Arrays.asList(CharactorType.values()));
        List<CharactorType> fixedTypes = Arrays.asList(CharactorType.values());
        int typeCount = 0;
        while (pwCharsIndex.size() > 0) {
            int pwIndex = pwCharsIndex.remove(random.nextInt(pwCharsIndex.size()));
            Character c;
            if (typeCount < minVariousType) {
                c = generateCharacter(takeTypes.remove(random.nextInt(takeTypes.size())));
                typeCount++;
            } else {
                c = generateCharacter(fixedTypes.get(random.nextInt(fixedTypes.size())));
            }
            password[pwIndex] = c.charValue();
        }
        return String.valueOf(password);
    }

    private Character generateCharacter(CharactorType type) {
        Character c = null;
        int rand;
        switch (type) {
            case LOWERCASE:
                rand = random.nextInt(letterRange);
                rand += 97;
                c = new Character((char) rand);
                break;
            case UPPERCASE:
                rand = random.nextInt(letterRange);
                rand += 65;
                c = new Character((char) rand);
                break;
            case NUMBER:
                rand = random.nextInt(numberRange);
                rand += 48;
                c = new Character((char) rand);
                break;
            case SPECIAL_CHARACTOR:
                rand = random.nextInt(spCharactorRange);
                c = new Character(allowedSpecialCharactors[rand]);
                break;
        }
        return c;
    }

    enum CharactorType {
        LOWERCASE,
        UPPERCASE,
        NUMBER,
        SPECIAL_CHARACTOR
    }

}
