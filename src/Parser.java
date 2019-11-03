/**
 * @author  Raul Aguilar
 * @date    03 November 2019
 * Parser: Encapsulates access to the input code. Reads an assembly language command, parses it, and provides
 * convenient access to the command's components (fields and symbols). In addition, removes all white space
 * and comments.
 */

 import java.io.*;
 import java.util.Scanner;

 public class Parser {

    private Scanner inputFile;
    private int lineNumber;
    private String rawLine;

    private String cleanLine;
    private Command commandType;
    private String symbol;
    private String destMnemonic;
    private String compMnemonic;
    private String jumpMnemonic;

    private Code c = new Code();

    /**
     * Opens input file and prepares to parse
     * If file cannot be found ends program with error message
     * @param inFileName Name of the file to parse
     */
    public void Parser(String inFileName) {
        try {
            inputFile = new Scanner(new FileReader(inFileName));
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found. Ending program.");
            System.exit(0);
        }
    }

    /**
     * Returns boolean if more commands left, closes stream if not
     * @return True if more commands, else closes stream and returns false
     */
    public boolean hasMoreCommands() {
        if(inputFile.hasNextLine()) {
            return true;
        } else {
            inputFile.close();
            return false;
        }
    }

    /**
     * Reads the next command from the input and makes it the current command.
     * Should only be called if hasMoreCommands() is true.
     * Initially there is no current command.
     */
    public void advance() {
        lineNumber++;
        rawLine = inputFile.nextLine();
        cleanLine();
        parseCommandType();
        parse();
    }

    /**
     * Reads a line from the file and strips it of whitespace and comments
     */
    private void cleanLine() {
        int commentIndex;

        if(rawLine == null) {
            cleanLine = "";
        } else {
            // remove whitespace
            cleanLine = rawLine.replaceAll(" ", "");
            cleanLine = cleanLine.replaceAll("\t", "");

            //remove comments
            commentIndex = cleanLine.indexOf("/");
            if(commentIndex != -1) {
                cleanLine = cleanLine.substring(0, commentIndex);
            }
        }
    }

    /**
     * Guesses which command type the clean line is
     */
    private void parseCommandType() {
        if(cleanLine == null || cleanLine.length() == 0) {
            commandType = Command.NO_COMMAND;
        } else {
            char first = cleanLine.charAt(0);
            if(first == '(') {
                commandType = Command.L_COMMAND;
            } else if(first == '@') {
                commandType = Command.A_COMMAND;
            } else {
                commandType = Command.C_COMMAND;
            }
        }
    }

    /**
     * Parses the line depending on the assigned command type and the appropriate parts of the instruction are filled
     */
    private void parse() {
        if(commandType == Command.L_COMMAND || commandType == Command.A_COMMAND) {
            parseSymbol();
        } else if(commandType == Command.C_COMMAND) {
            parseComp();
            parseDest();
            parseJump();
        }
    }

    /**
     * Parses symbol from A- or L-Commands
     */
    private void parseSymbol() {
        if(commandType == Command.L_COMMAND) {
            int begin = cleanLine.indexOf('(');
            int end = cleanLine.indexOf(')');
            symbol = cleanLine.substring(begin+1, end);
        }
        if(commandType == Command.A_COMMAND) {
            symbol = cleanLine.substring(1);
        }
    }

    /**
     * Parses C-commands for the dest mnemonic. The dest mnemonic is used to look-up the appropriate binary code
     * from the Code table
     */
    private void parseDest() {
        c.Code();
        int equals = cleanLine.indexOf('=');
        if(equals != -1) {
            destMnemonic = cleanLine.substring(0, equals);
            destMnemonic = c.getDest(destMnemonic);
        } else {
            destMnemonic = null;
            destMnemonic = c.getDest(destMnemonic);
        }
    }

    /**
     * Parses C-commands for the comp mnemonic. The comp mnemonic is used to look-up the appropriate binary code
     * from the Code table
     */
    private void parseComp() {
        c.Code();
        int equals = cleanLine.indexOf('=');
        int semicolon = cleanLine.indexOf(';');
        if(semicolon == -1 && equals > 0) {
            compMnemonic = cleanLine.substring(equals+1);
            compMnemonic = c.getComp(compMnemonic);
        } else if(semicolon > 0 && equals > 0) {
            compMnemonic = cleanLine.substring(equals+1, semicolon);
            compMnemonic = c.getComp(compMnemonic);
        } else if(semicolon > 0 && equals == -1) {
            compMnemonic = cleanLine.substring(0, semicolon);
            compMnemonic = c.getComp(compMnemonic);
        }
    }

    /**
     * Parses C-commands for the jump mnemonic. The jump mnemonic is used to look-up the appropriate binary code
     * from the Code table
     */
    private void parseJump() {
        c.Code();
        int semicolon = cleanLine.indexOf(';');
        if(semicolon != -1) {
            jumpMnemonic = cleanLine.substring(semicolon+1);
            jumpMnemonic = c.getJump(jumpMnemonic);
        } else {
            jumpMnemonic = null;
            jumpMnemonic = c.getJump(jumpMnemonic);
        }
    }

    /**
     * Gets the command type of the current line
     * @return Command enum for command type
     */
    public Command getCommandType() {
        return commandType;
    }

    /**
     * Gets the symbol parsed from the current line
     * @return String for symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the dest part of C-instruction
     * May be empty or already initialized
     * @return the dest mnemonic
     */
    public String getDest() {
        return destMnemonic;
    }

    /**
     * Gets the comp part of the C-instruction
     * @return the comp mnemonic
     */
    public String getComp() {
        return compMnemonic;
    }

    /**
     * Gets the jump part of the C-instruction
     * May be empty or already initialized
     * @return the jump mnemonic
     */
    public String getJump() {
        return jumpMnemonic;
    }

    /**
     * Get the current line from the file
     * @return line from the file
     */
    public String getRawLine() {
        return rawLine;
    }

    /**
     * Get the clean version of the raw line
     * @return cleaned up line
     */
    public String getCleanLine() {
        return cleanLine;
    }

    /**
     * Get the line number of the symbol encountered
     * @return current line number
     */
    public int getLineNumber() {
        return lineNumber;
    }
 }