/**
 * @author  Raul Aguilar
 * @date    October 14, 2019
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
    Command command;

    /**
     * Opens input file and prepares to parse
     * If file cannot be found ends program with error message
     * @param inFileName
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
     * @return True if more commands, else closes stream
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
     * Reads the next command from the input and makes it the
     * current command.
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
     * Reads raw line from file and strips line of whitespace
     * and comments
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
     * Guesses which command type it is from clean line
     */
    private void parseCommandType() {
        if(cleanLine == null || cleanLine.length() == 0) {
            commandType = command.NO_COMMAND;
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
     * Helper method parses line depending on instuction type
     * Appropriate parts of instruction filled
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
     * Helper method parses line to get dest part
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
     * Helper method parses line to get comp part
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
     * Helper method parses line to get jump part
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
     * Getter for command type
     * @return Command enum for command type
     */
    public Command getCommandType() {
        return commandType;
    }

    /**
     * Getter for symbol
     * @return String for symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Getter for dest part of C-instuctruction
     * @return 
     */
    public String getDest() {
        return destMnemonic;
    }

    public String getComp() {
        return compMnemonic;
    }

    public String getJump() {
        return jumpMnemonic;
    }

    public String getRawLine() {
        return rawLine;
    }

    public String getCleanLine() {
        return cleanLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }
 }