package util.parse;

/**
 * Parses a string from a starting bracket to the matching final bracket or parenthesis. Used primarily as a utility to extract text contained within two brackets.
 */
public class BracketedExpressionParser implements Parser<String> {
    public String parse(String text) {
        if (text.length() < 1) return null;
        char firstBracket = text.charAt(0);
        char finalBracket = getBracketComplement(firstBracket);
        if ( finalBracket == 'e' )
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(text.charAt(0));

        // Used as a sort of semaphore that increments when a firstBracket is found and decraments when an finalBracket is found
        int count = 1;
        for (int i = 1; count > 0 && i < text.length(); i++) {
            if ( text.charAt(i) == firstBracket && finalBracket != firstBracket )
                count++;
            else if ( text.charAt(i) ==  finalBracket )
                count--;
            sb.append(text.charAt(i));
        }

        return count == 0 ? sb.toString() : null;
    }

    // Returns e on none found
    private char getBracketComplement(char bracket) {
            switch ( bracket ){
            case '[':
                return ']';
            case '(':
                return ')';
            case '{':
                return '}';
            case '"':
                return '"';
            case '\'':
                return '\'';
            default:
                return 'e';
        }
    }
}