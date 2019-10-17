# Assembler
Develop an assembler that translates programs written in Hack assembley language into the binary code understood by the Hack hardware platform.

### Todo
- Make it read symbols

## Parser.java
| Routine | Arguments | Returns | Function |
| ------- | --------- | ------- | -------- |
| Constuctor / initializer | Input file/stream | | Opens the input file/stream and gets ready to parse it |
| hasMoreCommands | | Boolean | Are there more commands in the input? |
| advance | | | Reads the next command from the input and makes it the current command. Should be called only if hasMoreCommands() is true. Initiailly there is no current command |
| cleanLine | | | Reads raw line from file and strips it of whitespace and comments |
| parseCommandType | | | Guesses which command type it is from the clean line |
| parse | | | Helper method: parses line depending on instruction type |
| parseSymbol | | | Parse symbol from A- and L-commands |
| parseDest | | | Helper method: parse line to get dest part |
| parseComp | | | Helper method: parse line to get comp part |
| parseJump | | | Helper methid: parse line to get jump part |
| getCommandType | | Command | Getter for the command type of the current line |
| getSymbol | | String | Getter for the symbol parsed from the line |
| getDest | | String | Get the dest part of the C-instruction. May be empty. |
| getComp | | String | Get the comp part of the C-instruction. |
| getJump | | String | Get the jump part of the C-instruction. May be empty. |
| getRawLine | | String | Get the current line from the file |
| getCleanLine | | String | Get the clean version of the raw line |
| getLineNumber | | int | Get the line number of the symbol encountered |