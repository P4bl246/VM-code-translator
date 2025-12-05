package AuxClass.Parser;
import java.util.ArrayList;

// import Parser; // Removed invalid import. If Parser is in another package, import it with the correct package name.

import java.io.*;

/**
 * @author Pablo Riveros Perea
 * <p>Esta es una clase especial pensada para una función de la clase 'Parser', llamada 'RemoveNestedBlockComments', funciona como un encapsulador de argumentos</p>
 * <p>This class is thought for the function of the class 'Parser', called 'RemoveNestedBlockComments', this function like a wrapper of arguments</p>
 *<b>This wrapper class are created for abstract the arguments used in the function are a lot,
 *and put thats in a individual form are so extended and reduces adaptability, maintainability, and customizability</b>
 */
public class ProccessBlockComments{

    public Parser.ReadmodeBlock mode;
    public Parser.MutableTypeData<String> line;
    public Reader ReadFile;
    public String NumberOfLine;
    public String DelimiterStart;
    public String DelimiterEnd;
    public Character DelimiterNumLine;
    public Parser.MutableTypeData<Integer>indexActualInTheLine;
    public Parser.MutableTypeData<Integer>countOfLineProcessed;
    public boolean itsMultiLine;
    public ArrayList<String> BetweenComments;
    public Parser.MutableTypeData<Integer> lastEndofCommentDelimiter;
    public boolean recursiveCall;
    public Parser.MutableTypeData<Boolean> lastRecursiveCallFlag;
    /**
     * This is a **contructor** for this class
     * <p><b>IMPORTANT:</b> If you want know, 'why' have all this arguments you can arrive in the function 'RemoveNestedBlockComments' where all are explain</p>
     * <p><b>NOTE:</b> All this set of parameters are thought for give information about the 'state' or 'where are' the proces, in "real time into the process" or how change and what is the actual parameters into the function</p>
     * @param mode This is a type 'ReadmodeBlock', you can select a mode (SingleEnd, NestedEnd) for trate the Block comments, each mode trate of different form this, if you want know what make each mode you can see thath in the function 'RemoveNestedBlockComments' or 'RemoveBlockBlockComments'(this is the API for the user)
     * @param line This is a type 'MutableTypeData<String>', is this type, because that simulate the 'pass for reference', this are use for stay the line between the calls change his value like a "global form" if this change in some call, here you put the line where you find a Block comment in the file, put this line here, this are use because the comment can be multiline, and the function support this, and upload the line into the file, or upload the "point" after the comment into the file, so after the comment are processed the line are 'automaticly' upload for be after this block comment
     * @param ReadFile This is a type 'Reader', here you put that file what you are reading, is used for stay the point in the file upload, and to the same time verified errors when are found the EOF and not found the end of Block Comment(like a "limit")
     * @param NumberOfLine This line is a type 'String', is an 'optional' parameter, just are use for give more details about when is the error if have
     * @param DelimiterStart This is the 'identificator' for know what 'String' is considerated a start of block comment
     * @param DelimierEnd This is the 'identificator' for know what 'String' is considerated an end of block comment
     * @param DelimiterNumLine This is a 'delimiter' for know before of what are considered other String apart of the line or something you like ignore or not want be processed, like the number of Line,  is optional equal than the 'NumberOfLine' parameter because are design or thought for be complemetary, because if the comment its 'multiline' with this parameter upload the line 
     * @param indexActualInTheLine This is a type 'MutableTypeData<Integer>', is this type because thats is use for pass and stay the values between calls, independient are in different calls this parameter upload if change in any call, like a "global variable", just this are defined in local form. This be like this, for give to other calls where they start analize the line
     * @param countOfLineProcessed This is a type 'MutableTypeData<Integer>', is this type because need stay upload the number of lines procced if the comment are multiline, independent if are other calls, and stay the struct of the file
     * @param itsMultiLine This is a 'boolean', use for know if the comment are multiLine, because if are true, trate to different form this
     * @param BetweenComments This is a 'array of Strings', use for get the parts between block comments, not nested, conserved the "executable code", cases like this <-ThisisABlockComment->codeExecutable<-hi->NeedWriteThis<-<-hello->OtheBlockComment<-This is other->->, in this example the string 'codeExecutableNeedWrite' are conserved, because 'OtheBlockComment' are part of a block comment
     * @param lastEndofCommentDelimiter This is a type 'MutableTypeData<Integer>', is use for storage the index of last 'end block comment identificator' in the line
     * @param recursiveCall This is 'boolean', use for take a flag for know, if this is a recursive call, becuase behavior change if are a recursive call, and to same time for know if the line have nestes block comments
     * @param lastRecursiveCallFlag This is a type 'MutableTypeData<Boolean>', if a "flag" use for know if that is the last recursiveCall and get the 'lastEndofCommentDelimiter' index
     */
     public ProccessBlockComments(Parser.ReadmodeBlock mode, Parser.MutableTypeData<String> line, Reader ReadFile, String NumberOfLine, String DelimiterStart, String DelimiterEnd, 
     Character DelimiterNumLine, Parser.MutableTypeData<Integer>indexActualInTheLine, Parser.MutableTypeData<Integer>countOfLineProcessed, boolean itsMultiLine,
     ArrayList<String> BetweenComments, Parser.MutableTypeData<Integer> lastEndofCommentDelimiter, boolean recursiveCall, Parser.MutableTypeData<Boolean> lastRecursiveCallFlag){
        this.mode = mode;
        this.line = line;
        this.ReadFile = ReadFile;
        this.NumberOfLine = NumberOfLine;
        this.DelimiterStart = DelimiterStart;
        this.DelimiterEnd = DelimiterEnd;
        this.DelimiterNumLine = DelimiterNumLine;
        this.indexActualInTheLine = indexActualInTheLine;
        this.countOfLineProcessed = countOfLineProcessed;
        this.itsMultiLine = itsMultiLine;
        this.BetweenComments = BetweenComments;
        this.lastEndofCommentDelimiter = lastEndofCommentDelimiter;
        this.recursiveCall = recursiveCall;
        this.lastRecursiveCallFlag = lastRecursiveCallFlag;
     }
}
