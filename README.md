# Assembler
Develop an assembler that translates programs written in Hack assembley language into the binary code understood by the Hack hardware platform.

### Todo
- Make it read symbols


| Routine | Arguments | Returns | Function |
| ------- | --------- | ------- | -------- |
| Constuctor / initializer | Input file/stream | | Opens the input file/stream and gets ready to parse it |
| hasMoreCommands | | Boolean | Are there more commands in the input? |
| advance | | | Reads the next command from the input and makes it the current command. Should be called only if hasMoreCommands() is true. Initiailly there is no current command |