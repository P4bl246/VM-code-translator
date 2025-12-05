package AuxClass.Parser;

import java.util.*;
/**
 * @author Pablo Riveros Perea
 * <p>This class is thought for the function of the class 'sintxParsing', called 'CompareCommandWithArgs', this function like a wrapper of arguments</p>
 *<b>This wrapper class are created for abstract the arguments used in the function are a lot,
 *and put thats in a individual form are so extended and reduces adaptability, maintainability, and customizability</b>
 */

public class CommandArgRule {
    private HashMap<String, Integer> commandTable = new HashMap<>();
    private HashMap<String, Integer> argTable = new HashMap<>();
    private int commandLength = 0;
    private int argLength = 0;

    // Formato más largo y más corto permitidos para las instrucciones con argumentos
    //Argumets format most Long and most less accepted for the instructions with arguments
    private String formatPatternMostLong = null;
    private String formatPatternLessLong = null;

    // Múltiples patrones permitidos
    // Multiples formats patterns accpeted
    private Map<String, String> multipleFormatsPatterns = new HashMap<>();
    //excpeciones(instrucciones que no son permitidas)
    //excepcions (instruction not accepted)
    private ArrayList<String> exceptions = new ArrayList<>();
    
    private ArrayList<String>commandsWithoutPatterns = new ArrayList<>();
    //for flexibles commands
    //para comandos flexibles
    private ArrayList<String>commandsWithFlexiblePattern = new ArrayList<>();
    private String formatPatternFlexible = null;
    private ArrayList<String>multiplesFlexiblesFormatsPatterns = new ArrayList<>();
    private ArrayList<Character>specialCharsForIdentifyInTheFlexibleFormat = new ArrayList<>();
    private ArrayList<String> commandsWithFlexiblePatternForResultConflicts = new ArrayList<>();
    private boolean thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns = false;
    private Character ORgateForFlexible = null;
    private Character stopForFlexible = null;
    private Map<Character, Integer> mapForFlexible = new HashMap<>();
    private Character stopForFlexibleForConflicts = null;
    private boolean theLineAreInTheFormatExpected = false;
    //Constructor
       /**
     * <p>This is the <b>constructor</b> for this class.</p>
     * <p><code>**</code>This is a <code>simple explanation of what expected each parameter</code>, but if you <code>want know how you can use this and what makes and why this parameters are created</code> <code>go to the class 'sintaxParsing'</code></p>
     * <p><code>IMPORTANT:</code> I chose these parameter types because they are necessary and I plan to use this in other similar projects. If you don't need all of them, you can customize the implementation (of the function that uses this class) and the class itself.</p>
     * 
     * @param commandTable A HashMap (<String, Integer>) used to search for accepted commands.
     * @param argTable A HashMap (<String, Integer>) used to search for accepted arguments.
     * @param commandLength An integer used as a reference for the <code>maximum length of the command part</code>.
     * @param argLength An integer used as a reference for the <code>maximum length of the argument part</code>.
     * <p><code>***</code>Parameters for <b>Strict Commands</b></p>
     * <p><code>***</code>This is a <code>set</code> of parameters ("formatPatternMostLong" and "formatPatternLessLong"); you need to provide both or none.</p>
     * @param formatPatternMostLong A String used to specify the pattern with the most characters for an instruction (used when instructions have only a single format). This is needed because the format is converted to a number for the function <code>IdentifyTheStrictFormat</code> (in the class 'sintaxParsing'), where the number of characters, their type, and position are considered. Here, <code>provide all the information</code>: a String with the command, argument, and argument value.
     * @param formatPatternLessLong A String used to specify the pattern with the fewest characters for an instruction (used when instructions have only a single format). This is also needed for <code>IdentifyTheStrictFormat</code>. Here, <code>provide all the information</code>: a String with the command, argument, and argument value.
     * <p></p>
     * @param multipleFormatsPatterns A HashMap (<String, String>) where the first String (the <code>key</code>) is used for 'formatPatternMostLong' or 'formatPatternLessLong', and the second String (the <code>value</code>) is for the other pattern. Use this when instructions have multiple strict formats. This is similar to the above set of parameters, but allows you to provide multiple formats or patterns.
     * <p><code>***</code>Parameter for <b>Exceptions</b></p>
     * @param exceptions An ArrayList of Strings used to specify <code>exceptions</code>. For example, when an instruction can be written but is illegal, useless, or problematic and you don't want to allow it in the code. These are <code>illegal instructions</code>.
     * <p><code>***</code>Parameter for <b>Commands without Pattern</b></p>
     * @param commandsWithoutPatterns An ArrayList of Strings used to specify commands that do not require an expected pattern. In other words, these commands' arguments do not have a strict or flexible format and can contain any characters in any order.
     * <p><code>***</code>Parameters for <b>Flexible Commands</b></p>    
     * @param commandsWithFlexiblePattern An ArrayList of Strings used to specify commands with arguments that have a <code>flexible format</code>. In other words, commands whose arguments can have any number of characters but in an expected order.
     * @param formatPatternFlexible Used to provide an <code>example</code> of the expected flexible format. There are some recommended limitations, such as not using 'OR gates' or the expected format directly. For more information, see the method <b>identifyTheFlexibleFormat</b> in the class <b>sintaxParsing</b>.
     * @param multiplesFlexiblesFormatsPatterns Used to provide <code>multiple examples</code> or multiple expected flexible formats directly. This is similar to the above but for multiple formats.
     * @param specialCharsForIdentifyInTheFlexibleFormat An ArrayList of Characters used to identify special characters in a string and represent them in a different or special way, i.e., to flag or mark them.
     * @param thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns A Boolean used to indicate if the 'pattern' String must be converted or if its flexible format must be identified (because it is an example). In other words, the given string is used to compare the line's format or another format with this pattern, representing the expected format.
     * @param theLineAreInTheFormatExpected A Boolean used for the same purpose as the above parameter, but designed for the line or string to compare.
     * @param ORgateForFlexible A Character used to identify the character that indicates the OR gate in a given 'formatPattern'. It is recommended to use this only for direct format patterns, not examples.
     * @param stopForFlexible A Character used to stop analyzing a string when this character is found. In other words, process everything before this character and ignore everything after (including the character itself).
     * @param mapForFlexible A Map<Character, Integer> where the <code>key</code> indicates the character to remove, and the <code>value</code> indicates how many of these should remain in the file (removes from left to right, keeping those on the right). This is used by the <code>resolveConflicts</code> function. For more details, see the implementation in the class 'sintaxParsing'.
     * @param stopForFlexibleForConflicts A Character used for the same purpose as 'stopForFlexible', but specifically for the function in charge of <code>resolveConflicts</code>. It is recommended to use a character that appears in an exact format pattern or its conversion, i.e., the flexible format pattern generated or the string's representation in this format.
     *
     * <p>Usage example:</p>
     * <pre>
     * <code>
     * Map<String, Integer> acceptedCommands = new HashMap<>();
     * acceptedCommands.put("HI", 1);
     * acceptedCommands.put("BYE", 2);
     * Map<String, Integer> acceptedArgs = new HashMap<>();
     * acceptedArgs.put("MARIO", 3);
     * acceptedArgs.put("LUIGI12", 4);
     * ArrayList<String> excep = new ArrayList<>();
     * excep.add("BYEMARIO"); // This is possible but I don't want to accept it, so I define it as an "illegal" instruction
     * // Now create the object with this class
     * CommandArgRule rules = new CommandArgRule.Builder()
     *     .setCommandTable(acceptedCommands)
     *     .setArgTable(acceptedArgs)
     *     .setCommandLength(3)
     *     .setArgLength(7)
     *     .setFormatPatternMostLong("BYELUIGI12")
     *     .setFormatPatternLessLong("HIMARIO")
     *     .setMultipleFormatsPatterns(null)
     *     .setExceptions(excep)
     *     .build();
     *
     * // If you use multiple formats, you can do something like this.
     * Map<String, String> patterns = new HashMap<>();
     * patterns.put("BYELUIGI12HIMARIO", "BYELUIGIMARIO");
     * patterns.put("HIMARIOHILUIGI", "HILUIGI"); // and others if you want
     * // Now just put 'patterns' in the constructor for 'multipleFormatsPatterns', and that's it
     * CommandArgRule rules = new CommandArgRule.Builder()
     *     .setFormatPatternMostLong("BYELUIGI12")
     *     .setFormatPatternLessLong("HIMARIO")
     *     .setMultipleFormatsPatterns(patterns)
     *     .build();
     * // OR
     * CommandArgRule rules = new CommandArgRule.Builder()
     *     .setFormatPatternMostLong(null)
     *     .setFormatPatternLessLong(null)
     *     .setMultipleFormatsPatterns(patterns)
     *     .build();
     * // AND OTHERS...
     * </code>
     * </pre>
     * 
     */
    
    private CommandArgRule(Builder builder) {
        this.commandTable = builder.commandTable;
        this.argTable = builder.argTable;
        this.commandLength = builder.commandLength;
        this.argLength = builder.argLength;
        this.formatPatternMostLong = builder.formatPatternMostLong;
        this.formatPatternLessLong = builder.formatPatternLessLong;
        this.multipleFormatsPatterns = builder.multipleFormatsPatterns;
        this.exceptions = builder.exceptions;
        this.commandsWithoutPatterns = builder.commandsWithoutPatterns;
        this.commandsWithFlexiblePattern = builder.commandsWithFlexiblePattern;
        this.formatPatternFlexible = builder.formatPatternFlexible;
        this.multiplesFlexiblesFormatsPatterns = builder.multiplesFlexiblesFormatsPatterns;
        this.specialCharsForIdentifyInTheFlexibleFormat = builder.specialCharsForIdentifyInTheFlexibleFormat;
        this.thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns = builder.thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns;
        this.commandsWithFlexiblePatternForResultConflicts = builder.commandsWithFlexiblePatternForResultConflicts;
        this.ORgateForFlexible = builder.ORgateForFlexible;
        this.stopForFlexible = builder.stopForFlexible;
        this.mapForFlexible = builder.mapForFlexible;
        this.stopForFlexibleForConflicts = builder.stopForFlexibleForConflicts;
        this.theLineAreInTheFormatExpected = builder.theLineAreInTheFormatExpected;
    }
    //class for build a object use the Builder pattern
    //clase para crear un objeto usando el patrón Builder
public static class Builder{
    private HashMap<String, Integer> commandTable = new HashMap<>();
    private HashMap<String, Integer> argTable = new HashMap<>();
    private int commandLength = 0;
    private int argLength = 0;

    // Formato más largo y más corto permitidos para las instrucciones con argumentos
    //Argumets format most Long and most less accepted for the instructions with arguments
    private String formatPatternMostLong = null;
    private String formatPatternLessLong = null;

    // Múltiples patrones permitidos
    // Multiples formats patterns accpeted
    private Map<String, String> multipleFormatsPatterns = new HashMap<>();
    //excpeciones(instrucciones que no son permitidas)
    //excepcions (instruction not accepted)
    private ArrayList<String> exceptions = new ArrayList<>();
    
    private ArrayList<String>commandsWithoutPatterns = new ArrayList<>();
    //for flexibles commands
    //para comandos flexibles
    private ArrayList<String>commandsWithFlexiblePattern = new ArrayList<>();
    private String formatPatternFlexible = null;
    private ArrayList<String>multiplesFlexiblesFormatsPatterns = new ArrayList<>();
    private ArrayList<Character>specialCharsForIdentifyInTheFlexibleFormat = new ArrayList<>();
    private ArrayList<String> commandsWithFlexiblePatternForResultConflicts = new ArrayList<>();
    private boolean thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns = false;
    private Character ORgateForFlexible = null;
    private Character stopForFlexible = null;
    private Map<Character, Integer> mapForFlexible = new HashMap<>();
    private Character stopForFlexibleForConflicts = null;
    private boolean theLineAreInTheFormatExpected = false;

    //setters for the builder

    public Builder setFormatPatternMostLong(String formatPatternMostLong){
                this.formatPatternMostLong = formatPatternMostLong;
                return this;
       }
    public Builder setFormatPatternLessLong(String formatPatternLessLong){
                this.formatPatternLessLong = formatPatternLessLong;
                return this;
       }
    public Builder setCommandTable(HashMap<String, Integer> commandTable){
                this.commandTable = commandTable;
                return this;
       }
    public Builder setArgTable(HashMap<String, Integer> argTable){
                this.argTable = argTable;
                return this;
       }
    public Builder setCommandLength(int commandLength){
                this.commandLength = commandLength;
                return this;
    }      
    public Builder setArgLength(int argLength){
                this.argLength = argLength;
                return this;   
    }
    public Builder setMultipleFormatsPatterns(Map<String, String> multipleFormatsPatterns){
                this.multipleFormatsPatterns = multipleFormatsPatterns;
                return this;
       }
    public Builder setExceptions(ArrayList<String> exceptions){
                this.exceptions = exceptions;
                return this;
       }
    public Builder setCommandsWithoutPatterns(ArrayList<String> commandsWithoutPatterns){
                this.commandsWithoutPatterns = commandsWithoutPatterns;
                return this;
       }

        public Builder setCommandsWithFlexiblePattern(ArrayList<String> commandsWithFlexiblePattern){
            this.commandsWithFlexiblePattern = commandsWithFlexiblePattern;
            return this;
        }
        public Builder setFormatPatternFlexible(String formatPatternFlexible){
            this.formatPatternFlexible = formatPatternFlexible;
            return this;
        }
        public Builder setMultiplesFlexiblesFormatsPatterns(ArrayList<String> multiplesFlexiblesFormatsPatterns){
            this.multiplesFlexiblesFormatsPatterns = multiplesFlexiblesFormatsPatterns;
            return this;
        }
        public Builder setSpecialCharsForIdentifyInTheFlexibleFormat(ArrayList<Character> specialCharsForIdentifyInTheFlexibleFormat){
            this.specialCharsForIdentifyInTheFlexibleFormat = specialCharsForIdentifyInTheFlexibleFormat;
            return this;
        }
        public Builder setCommandsWithFlexiblePatternForResultConflicts(ArrayList<String> commandsWithFlexiblePatternForResultConflicts){
            this.commandsWithFlexiblePatternForResultConflicts = commandsWithFlexiblePatternForResultConflicts;
            return this;
        }
        public Builder setThePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns(boolean thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns){
            this.thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns = thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns;
            return this;
        }
        public Builder setORgateForFlexible(Character ORgateForFlexible){
            this.ORgateForFlexible = ORgateForFlexible;
            return this;
        }
        public Builder setStopForFlexible(Character stopForFlexible){
            this.stopForFlexible = stopForFlexible;
            return this;
        }
        public Builder setMapForFlexible(Map<Character, Integer> mapForFlexible){
            this.mapForFlexible = mapForFlexible;
            return this;
        }
        public Builder setStopForFlexibleForConflicts(Character stopForFlexibleForConflicts){
            this.stopForFlexibleForConflicts = stopForFlexibleForConflicts;
            return this;
        }
        public Builder setTheLineAreInTheFormatExpected(boolean theLineAreInTheFormatExpected){
            this.theLineAreInTheFormatExpected = theLineAreInTheFormatExpected;
            return this;
       }  
    public CommandArgRule build(){
        return new CommandArgRule(this);
      }
    }
    //getters for the class
    public HashMap<String, Integer> getCommandTable() {
        return commandTable;
    }
    public HashMap<String, Integer> getArgTable() {
        return argTable;
    }
    public int getCommandLength() {
        return commandLength;
    }
    public int getArgLength() {
        return argLength;
    }
    public String getFormatPatternMostLong() {
        return formatPatternMostLong;
    }
    public String getFormatPatternLessLong() {
        return formatPatternLessLong;
    }
    public Map<String, String> getMultipleFormatsPatterns() {
        return multipleFormatsPatterns;
    }
    public ArrayList<String> getExceptions() {
        return exceptions;
    }
    public ArrayList<String> getCommandsWithoutPatterns() {
        return commandsWithoutPatterns;
    }
    public ArrayList<String> getCommandsWithFlexiblePattern() {
        return commandsWithFlexiblePattern;
    }
    public String getFormatPatternFlexible() {
        return formatPatternFlexible;
    }
    public ArrayList<String> getMultiplesFlexiblesFormatsPatterns() {
        return multiplesFlexiblesFormatsPatterns;
    }
    public ArrayList<Character> getSpecialCharsForIdentifyInTheFlexibleFormat() {
        return specialCharsForIdentifyInTheFlexibleFormat;
    }
    public ArrayList<String> getCommandsWithFlexiblePatternForResultConflicts() {
        return commandsWithFlexiblePatternForResultConflicts;
    }
    public boolean getThePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns() {
        return thePatternsAreBeInTheFormatExpectedOrNeedBeConvert_ForFlexiblePatterns;
    }
    public Character getORgateForFlexible() {
        return ORgateForFlexible;
    }
    public Character getStopForFlexible() {
        return stopForFlexible;
    }
    public Map<Character, Integer> getMapForFlexible() {
        return mapForFlexible;
    }
    public Character getStopForFlexibleForConflicts() {
        return stopForFlexibleForConflicts;
    }
    public boolean getTheLineAreInTheFormatExpected() {
        return theLineAreInTheFormatExpected;
    }  
}
