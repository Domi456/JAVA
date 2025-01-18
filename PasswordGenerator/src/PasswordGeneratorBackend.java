import java.util.Random;

public class PasswordGeneratorBackend {
    public static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String SYMBOLS = "?!+@#ß&~<>[{}];.:_-*/|";

    private final Random rnd;

    public PasswordGeneratorBackend() {
        rnd = new Random();
    }

    public String generatePassword(int len, boolean upper, boolean lower, boolean numbers, boolean symbols){
        StringBuilder passwordBuilder = new StringBuilder();
        String validCharacters = "";

        if (upper) validCharacters += UPPERCASE_CHARACTERS;
        if (lower) validCharacters += LOWERCASE_CHARACTERS;
        if (numbers) validCharacters += NUMBERS;
        if (symbols) validCharacters += SYMBOLS;

        for (int i = 0; i < len; i++) {
            int randomIndex = rnd.nextInt(validCharacters.length());

            char randomChar = validCharacters.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }
}
