package example.CodeInterviewPrepAPI.Security;

import java.util.HashSet;
import java.util.Arrays;

import example.CodeInterviewPrepAPI.Exceptions.InvalidPasswordException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public static void validate(String password) {
        int passwordLength = password.length();
        Character[] specialChars = {' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', 
                               '*', '+', ',', '-', '.', '/', ':', ';', '<', '=',
                               '>', '?', '@', '[', ']', '\\', '^', '_', '`', '{',
                               '|', '}', '~'};
        HashSet<Character> specialCharsSet = new HashSet<>(Arrays.asList(specialChars));

        /* length constraint */
        if ((passwordLength < 8) || 
            (passwordLength > 20)) {
            throw new InvalidPasswordException("Invalid password format");
        }

        /* special char constraint */
        boolean containsSpecialChar = false;
        boolean containsUppercase = false;
        for (int i = 0; i < passwordLength; ++i) {
            char curr = password.charAt(i);
            if (specialCharsSet.contains(curr)) {
                containsSpecialChar = true;
            }
            else {
                if (Character.isUpperCase(curr)) {
                    containsUppercase = true;
                }
            }
        }

        if (!containsSpecialChar) {
            throw new InvalidPasswordException("Invalid password format");
        }

        if (!containsUppercase) {
            throw new InvalidPasswordException("Invalid password format");
        }
    }
}
