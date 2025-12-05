package AuxClass.Parser;

//****Call classes individuals not is necesary because we call java.io.*, this import all classes from this package
//****Llamadas a clases individuales no son necesarias porque llamamos a java.io.*, esto importa todas las clases de este paquete

import java.io.*;

import java.util.ArrayList;

/*
FUNCTIONS FOR PREPARE FILES (FUNCIONES PARA LA PREPARACIÓN DE ARCHIVOS)---------------------------------------------------------------------------------

IMPORTANT:All market like a label 'internal method' refer this function are used for the principal(CleanFile) o other functions
*************************************************------------------------------------------------*******************************-------------------------------
    public int searchString(boolean searchAll, String line, String searchThis, int startIndex, Character delimiter, boolean WatchMessages);//method for search a string in a line
                                                                                                                                         //método para buscar una cadena en una linea

    public int removeString(String Read_File_In, String Delimiter);// This method is used to remove spaces from the input file(internal method)<br>
                                                            // Este método se utiliza para eliminar espacios del archivo de entrada(Método interno)

    public int removeSimpleComments(String Read_File_In, String Delmiter);// This method is used to remove simple comments from the input file(internal method)<br>
                                                                           // Este método se utiliza para eliminar comentarios simples del archivo de entrada(Método interno)

    public String get(Reader fileIn, Readmode mode, Character forNumberLine_Delimiter, MutableTypeData<Integer> actual, MutableTypeData<Boolean> containsBeforeEOForEndLine) throws IOException;// This method is used to obtain the line number or completely line from the input file(internal method)<br>
                                                            // Este método se utiliza para obtener el número de línea del archivo de entrada o su linea completa(Método interno)

    public int removeBlockComments(ReadmodeBlock mode, String Read_File_in, String Delimiter, String delimiterEnd, Character DelimiterNumLine);// This method is used to remove block comments from the input file, and can obtanin the number line if you want(internal method)
                                                                                                     // Este método se utiliza para eliminar comentarios de bloque del archivo de entrada, y puede obtener el numero de linea si lo desea(Método interno)

    public int removeNestedBlockComments(ProccessBlockComments dataForProccess) throws IOException;// This method is used to remove nested block comments from the input file(internal method)<br>
                                                                                                        // Este método se utiliza para eliminar comentarios de bloque anidados del archivo de entrada(Método interno)
    
    public int removeVoidChars(String Read_File_In, Character VoidCharacterStart);// This method is used to remove void lines or characters from the input file(internal method)<br>
                                                        // Este método se utiliza para eliminar líneas vacías o caracteres del archivo de entrada(Método interno)

    public int removeNLine(String file_in);// This method is used to remove the number line from the input file(internal method)<br>
                                                // Este método se utiliza para eliminar el número de línea del archivo de entrada(Método interno)

    public int cleanFile(String file_in);// Clean the file of the spaces and void lines and comments,integrat the above functions, and 'NumLines'<br>
                                         // Limpiar el archivo de espacios y líneas vacías y comentarios integrando las funciones anteriores y 'NumLines'

*************************************************------------------------------------------------*******************************------------------------------
    public int numLines(File Read_File_in, File Writte_File_out);// This method is used to add the line number to the input file(internal method)<br>
                                                            // Este método se utiliza para agregar el número de línea al archivo de entrada(Método interno)
------------------------------------------------------------------------------------------------------------------------------
    public int fileTo(String file_In, String nameOfNewFileWithFormat);// This method is used to convert the file to other format(internal method)<br>
                                             // Este método se utiliza para convertir el archivo a otro formato(Método interno)
------------------------------------------------------------------------------------------------------------------------------

END OF FUCNTION TO PREPARE FILES(FIN DE LAS FUNCIONES PARA LA PREPARACIÓN DE ARCHIVOS)---------------------------------------------------------------------------------
*/
/** 
 * @author <p>Pablo Riveros Perea</p>
 * The class <code>Parser</code> contains functions thought for pre-process of files, "normalize it" and "cleaning it", and just 1 function are designed for the project(cleanFile)
 * <p>Methods and classes inside this are:</p>
 * <p><code>fileTo</code></p>
 * <p><code>removeSimpleComments</code></p>
 * <p><code>removeBlockComments</code></p>
 * <p><code>removeNestedBlockCommets</code></p>
 * <p><code>removeVoidChars</code></p>
 * <p><code>numLines</code></p>
 * <p><code>removeNLine</code></p>
 * <p><code>get</code></p>
 * <p><code>removeString</code></p>
 * <p><code>searchString</code></p>
 * <p><code>trateSpecialsStrings</code></p>
 * <p><code>removeVoidLines</code></p>
 * <p>Son class: <code>MutableTypeData</code></p>
 * 
*/

public class Parser {
//STARTS THE PROCESS OF PREPARE FILES (INICIA EL PROCESO DE PREPARACIÓN DE ARCHIVOS)----------------------------------------------------------------- 
//Enum for return 
public enum Return {
    ERROR(-1),
    SUCCESS(0);

    private final int value;

    Return(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
/**
 * This method <code>fileTo</code> copy content from a file to other
 * @param file_In File to convert or pass, path or name(if are in the same path of the class)
 * @param nameOfNewFileWithFormat The file where you want to convert or pass with the format
 * <p><b>Example:</b></p>
 * <pre>
 * <code>
 * String myFile = "example.txt";
 * String fileCopy = "exampleCpy.asm";
 * Parser method = new Parser();
 * method.fileTo(myFile, fileCopy);//This make a copy from a file .txt to file .asm
 * </code>
 * </pre>
 * @return 0 = SUCCESS,  -1 = ERROR
 */
public int fileTo(String file_In, String nameOfNewFileWithFormat) {  
    // Open the file_In in reading and create a temporary file for writing
    // Abrir el archivo de entrada en modo lectura y el archivo temporal en modo escritura
    try (FileReader fileP = new FileReader(file_In); Writer tempFile = new FileWriter(nameOfNewFileWithFormat)) {
        int c;
        // Read character by character from the input file and write to the temporary file
        // Leer carácter por carácter del archivo de entrada y escribir en el archivo temporal
        while ((c = fileP.read()) != -1) {
            tempFile.write((char)c);
        }
        return Return.SUCCESS.getValue(); // Éxito
        }
      catch (IOException e) {
        // Manejar errores de entrada/salida
        // Handle input/output errors
        System.out.println("Error: " + e.getMessage());
        return Return.ERROR.getValue(); // Error
        }
  }
//--------------------------------------------------------------
/**
 * This function are an orchestrator of other functions, creating a cleaner with a predefinde proccess
 * <p><b>Inside method:</b></p>
 * <pre>
 * <code>
 *  public int cleanFile(String file_In){
    System.out.printf("\nCLEANING THE FILE: '%s'...\n\n", file_In);
    int n;
    ArrayList<String> specialsForIdentify = new ArrayList<>();
    specialsForIdentify.add("function");
    specialsForIdentify.add("call");
    try{
    trateSpecialsStrings(specialsForIdentify, file_In);
    

    removeVoidChars(file_In, null);
    
    
     numLines(file_In);
     

    n = removeBlockComments(ReadmodeBlock.NestedEnd, file_In, "/*", "~/", ' ');//put this because this are the normal or standar delimiter of a block comment but i can't put this because close the documentation, so just replace the '~' for a '*'
    if(n != 0) return n;
    
     removeSimpleComments(file_In, "//");
   
    
     removeNLine(file_In, ' ');
    

      removeVoidChars(file_In, null);
    
    }
    catch(IOException e){
        System.err.println("Error in the reading or writing file: " + e.getMessage());
        return Return.ERROR.getValue();
    }
    catch(ParsingException e){
        System.err.println("Error in the parsing: " + e.getMessage());
        return Return.ERROR.getValue();
    }
    System.out.printf("\nTHE FILE '%s' IS CLEAN\n", file_In);
    return Return.SUCCESS.getValue();
  }
 * </pre>
 * </code>
 * <p><b>IMPORTANT:</b>Can see the code are so enlazed and dependet of my personal project, so use this if you can</p>
 * @param file_In File to clean, path or name(if are in the same path of the class)
 * @return 0 = SUCCESS, -1 = ERROR
 */
public int cleanFile(String file_In){
    System.out.printf("\nCLEANING THE FILE: '%s'...\n\n", file_In);
    int n;
    ArrayList<String> specialsForIdentify = new ArrayList<>();
    specialsForIdentify.add("function");
    specialsForIdentify.add("call");
    try{
    trateSpecialsStrings(specialsForIdentify, file_In);
    

    removeVoidChars(file_In, null);
    
    
     numLines(file_In);
     

    n = removeBlockComments(ReadmodeBlock.NestedEnd, file_In, "/*", "*/", ' ');
    if(n != 0) return n;
    
     removeSimpleComments(file_In, "//");
   
    
     removeNLine(file_In, ' ');
    

      removeVoidChars(file_In, null);
    
    }
    catch(IOException e){
        System.err.println("Error in the reading or writing file: " + e.getMessage());
        return Return.ERROR.getValue();
    }
    catch(ParsingException e){
        System.err.println("Error in the parsing: " + e.getMessage());
        return Return.ERROR.getValue();
    }
    System.out.printf("\nTHE FILE '%s' IS CLEAN\n", file_In);
    return Return.SUCCESS.getValue();
}
//--------------------------------------------------------------
/**
 * The method <code>removeString</code> remove all the appears of a String in a file
 * @param readFileIn File to clean of Strings
 * @param delimiter String to remove from the file
 * <p><b>Example:</b></p>
 * <pre>
 * <code>
 * //my file contains "Hello, this is a example /¨djsi";
 * //if i want remove from my file the string "/¨djsi"
 * Parser method = new Parser();
 * method.removeString("myFile.txt", "/¨djsi");
 * //result are "Hello, this is a example" in my file
 * </code>
 * </pre>
 * @return void
 * @throws ParsingException If the parameters are null or empty or success some error in the reading or writing file
 */
public void removeString(String readFileIn, String delimiter) throws ParsingException {
    if(readFileIn == null){
        throw new ParsingException("Error: The 'readFileIn' parameter is null, need be a String with the file to proccess");
    }
    if(readFileIn.isEmpty()){
        throw new ParsingException("Error: The 'readFileIn' parameter is empty, need be a String with the file to proccess");
    }
    System.out.printf("\nREMOVING STRING '%s' FROM THE FILE: '%s'...\n\n", delimiter, readFileIn);

    try (BufferedReader reader = new BufferedReader(new FileReader(readFileIn));
         BufferedWriter writer = new BufferedWriter(new FileWriter("tempWithoutString.txt"))) {

        String line;
        while ((line = reader.readLine()) != null) {
            String cleanedLine = line.replace(delimiter, "");
            writer.write(cleanedLine);
            writer.newLine();
        }

    } catch (IOException e) {
        throw new ParsingException("Error in the reading or writing file: " + e.getMessage(), e);
    }

    File originalFile = new File(readFileIn);
    File tempFile = new File("tempWithoutString.txt");

    if (originalFile.delete()) {
        if (tempFile.renameTo(originalFile)) {
            System.out.printf("The file was cleaned and renamed to '%s'\n", readFileIn);
            return;
        } else {
           throw new ParsingException(String.format("Error trying to rename file 'tempWithoutString.txt' to '%s'", readFileIn));
        }
    } else {
        throw new ParsingException(String.format("Error trying to delete the file '%s'", readFileIn));
    }
}
//--------------------------------------------------------------
/**
 * Search a String in a line
 * @param searchAll Indicate if you want search all iterations and return the index of the first find, if is 'false', or how much are in the line
 * @param line Line to check
 * @param searchThis String to search in the line
 * @param startIndex Index in the line where start the searching
 * @param delimiter Stop to search when find this delimiter
 * @param WatchMessages For debug
 * @return >=0 = SUCCESS, -1 = NOT FIND, -2 = ERROR IN THE PARAMETERS, -3 = ERROR IN THE INDEX PARAMETER
 */
public int searchString(boolean searchAll, String line, String searchThis, int startIndex, Character delimiter, boolean WatchMessages) {
    if (line == null || searchThis == null || startIndex < 0) {
        System.err.printf("Error: Invalid input parameters.\nDETAILS: line: %s, searchThis: %s, Index: %d, sizeOfLine: %d\n", line, searchThis, startIndex, line.length());
        return -2;
    }
   if (startIndex >= line.length()){ 
      if(WatchMessages) System.err.println("Error: The index start is greather than the line");
      return -3;
    }

    int count = 0;
    for (int i = startIndex; i <= line.length() - searchThis.length(); i++) {
        if(delimiter != null) if (line.charAt(i) == delimiter) break;
        
        boolean match = true;
        for (int j = 0; j < searchThis.length(); j++) {
            if (i + j >= line.length() || line.charAt(i + j) != searchThis.charAt(j)) {
                match = false;
                break;
            }
        }

        if (match) {
            if (!searchAll) {
             if(WatchMessages)  System.out.println("String found at index " + i);
                return i;
            }
            count++;
            i += searchThis.length() - 1; // Avoid overlapping matches
        }
    }

    if (searchAll) return count;
    if(WatchMessages) System.out.println("String not found.");
    return Return.ERROR.getValue();
}
//--------------------------------------------------------------
/**
 * Remove simple comments from a file. Delete all before delimiter and the delimiter (not delete after)
 * @param Read_File_In File to check
 * @param SimpleCommentIdent Idetinficator for Simple comments start
 * @return void
 * @throws ParsingException If the parameters are null or empty or success some error in the reading or writing file
 */
public void removeSimpleComments(String Read_File_In, String SimpleCommentIdent) throws ParsingException {
    
    System.out.printf("\nREMOVING SIMPLE COMMENTS FROM THE FILE: '%s'...\n\n", Read_File_In);
     if(SimpleCommentIdent == null){
        throw new ParsingException("Error: The 'SimpleCommentIdent' parameter is null, need be a String with the start of simple comment");
    }
    if(Read_File_In == null){
        throw new ParsingException("Error: The 'Read_File_In' parameter is null, need be a String with the file to proccess");
    }
    if(SimpleCommentIdent.isEmpty()){
        throw new ParsingException("Error: The 'SimpleCommentIdent' parameter is empty, need be a String with the start of simple comment");
    }
    if(Read_File_In.isEmpty()){
        throw new ParsingException("Error: The 'Read_File_In' parameter is empty, need be a String with the file to proccess");
    }
     try (BufferedReader ReadFile = new BufferedReader(new FileReader(Read_File_In));
         BufferedWriter WritteFile = new BufferedWriter(new FileWriter("tempwithoutSimpleComments.txt"))) {
        
        String line;
        while ((line = ReadFile.readLine()) != null) {
            // Buscar comentario en la línea actual
            //Search the delimiter in the actual line
            int commentPosition = searchString(false, line, SimpleCommentIdent, 0, null, false);
            
            if (commentPosition != -1 && commentPosition != -2 && commentPosition != -3) {
                // Si hay un comentario, escribir solo la parte antes del comentario
                // if has a comment, writte just the part before of the delimiter
                String codePart = line.substring(0, commentPosition);
                WritteFile.write(codePart);
            } else {
                // Si no hay comentario, escribir la línea completa
                // If not has a comment, writte the line
                WritteFile.write(line);
            }
            
            // Añadir salto de línea (utiliza el separador de línea del sistema)
            // Add jump of line (use the separator of the line for the system)
            WritteFile.newLine();
        }
        
        WritteFile.flush();
        WritteFile.close();
        ReadFile.close();

    } catch (IOException e) {
        throw new ParsingException("Error in the reading or writing file: " + e.getMessage(), e);
    }
     //Upload the input file
    // Actualizar el archivo de entrada
    File infile = new File(Read_File_In);
    if (infile.delete()) {
        File temp = new File("tempwithoutSimpleComments.txt");
        if (temp.renameTo(infile)) {
            System.out.printf("The file 'tempwithoutSimpleComments.txt' is renamed to '%s'\n", Read_File_In);
            System.out.printf("\nTHE FILE '%s' IS CLEAN OF SIMPLE COMMENTS\n", Read_File_In);
            return;
        }
        throw new ParsingException(String.format("Error trying to rename file 'tempwithoutSimpleComments.txt' to '%s'", Read_File_In));
    }
    throw new ParsingException(String.format("Error trying to delete the file '%s'", Read_File_In));
}
//--------------------------------------------------------------
//class for simulate the pass for reference
//clase para simular el paso por referencia
/**
 * Class for simulate the 'pass for reference'
 * <p>This have 3 methods</p>
 * <pre>
 * <code>
 * Parser p = new Parser();
 * Parser.MutableTypeData<Integer> example = p.new MutableTypeData<>(0);
 * //first method get the value of the variable
 * example.getValor();
 * //second method set the value of th variable
 * example.setValor(55); //now example.getValor()=55
 * //third method get the type of the variable
 * example.getTipo(); //return the type of the variable, in this case are "Integer";
 * </code>
 * </pre>
 */
public class MutableTypeData<T> {
    private T valor;
    
    public MutableTypeData(T valor) {
        this.valor = valor;
    }
    
    public T getValor() {
        return valor;
    }
    
    public void setValor(T valor) {
        this.valor = valor;
    }
    
    public Class<?> getTipo() {
        return valor.getClass();
    }
}
/**
 * This function have or can have 2 "functions", the first is cordinate(similar to an orchestrator)the search and delete proccess to remove block comments in the file, and the second, be an API to the user of the function 'RemoveNestedBlockComments'
 * @param mode mode for know how proccess the block comments, both preserve the struct of the file and the code between block comments
<p>SingleEnd: search only the first close of the comment, independent if are nested block comments(not check the content, not are important)</p>
<p>NestedEnd: search and verify that all nested comments are closed correctly(check the content and are important)</p>
 * @param Read_File_in File to proccess
 * @param Delimiter Delimiter or Identificator to indicate the Start of a Block Comment
 * @param delimiterEnd Delimiter or Identificator to indicate the End of a Block Comment
 * @param DelimiterNumLine Optional parameter for ignore or get some String before delimiter and not proccess this(thought for somethings like the numberofLine)
 * @return 0 = SUCCESS, -1 = ERROR
 * @throws IOException If an I/O error occurs
 * @throws ParsingException If the Delimiter or delimiterEnd is null, or if an error occurs during parsing
 */
public int removeBlockComments(ReadmodeBlock mode, String Read_File_in, String Delimiter, String delimiterEnd, Character DelimiterNumLine) throws IOException, ParsingException {

  MutableTypeData<String>line5 = new MutableTypeData<>("");
  // variable to store the actual character and line (utlized just in the methods RemoveBlockComments and RemoveNestedBlockComments)
  //Variable para almacenar el carácter actual y linea (utilizado solo en los métodos RemoveBlockComments y RemoveNestedBlockComments)
   System.out.printf("\nREMOVING BLOCK COMMENTS FROM THE FILE: '%s'...\n\n", Read_File_in);
   if(Delimiter == null){
     throw new ParsingException("Error: The 'Delimiter' parameter is null, need be a String with the start of block comment");
   }
  if (delimiterEnd == null) {
    throw new ParsingException("Error: The 'delimiterEnd' parameter is null, need be a String with the end of block comment");
  }
    line5.setValor(null);
    String nLine = null;
    try (BufferedReader ReadFile = new BufferedReader(new FileReader(Read_File_in));
         BufferedWriter WritteFile = new BufferedWriter(new FileWriter("tempWithoutBlockComments.txt"))) {
            String line;
            //search in the completely file and remove
            //buscar en el archivo completo y eliminar
            while((line = ReadFile.readLine()) != null){
                if(line.equals("") || line.equals("\n") || line.equals("\0")) continue;
                line5.setValor(line);
                int n; 
                //if find the delimiter that indicate the start of comment
                //si encuentra el delimitador que indica el inicio de un comentario
                if((n = searchString(false, line5.getValor(), Delimiter, 0, null, false)) != -1){
                    if(n == -2 || n == -3) return Return.ERROR.getValue(); //Error
                    else{
                        //Get the number line
                        //obtener el numero de linea
                        if(DelimiterNumLine != null){
                            int h = line5.getValor().indexOf(DelimiterNumLine);
                            nLine = line5.getValor().substring(0, h);
                        }
                        //copy all before of the start of the delimiter 
                        //copiar todo antes de el incio de el delimitador
                        String getString = line5.getValor().substring(0, n);
                        WritteFile.write(getString);
                        //variables for upload the actual index and know the number of lines proccesed
                        //variables para actualizar el inidce actual y saber cuantaas lineas fueron procesadas
                        MutableTypeData<Integer> index = new MutableTypeData<>(n);
                        MutableTypeData<Integer> LinesJump = new MutableTypeData<Integer>(0);
                        ArrayList<String> between = new ArrayList<>();
                        MutableTypeData<Integer>last = new MutableTypeData<>(0);
                        MutableTypeData<Boolean>lastCallFlag = new MutableTypeData<>(false);
                        ProccessBlockComments data = new ProccessBlockComments(mode, line5, ReadFile, nLine, Delimiter, delimiterEnd, DelimiterNumLine, index, LinesJump, false, between, last, false, lastCallFlag);
                        if(removeNestedBlockComments(data) != Return.SUCCESS.getValue()) return Return.ERROR.getValue();
                               
                            //conseverd the structure of the file including the prosecced lines
                            //conservar la estructure de el archivo incluyendo las lineas procesadas                        
                            for(int i= 0; i < LinesJump.getValor(); i++){
                            WritteFile.newLine();
                        }
                        for(String str : between){
                            if(!str.equals("")) WritteFile.write(" " + str);
                        }
                            
                       //get all the content after the end of comment
                       //obtener todo lo que hay despues de el comentario
                        if(mode == ReadmodeBlock.NestedEnd && line5.getValor().indexOf(last.getValor()) != '\n' && line5.getValor().indexOf(last.getValor()) != '\0') getString= line5.getValor().substring((last.getValor()+1), line5.getValor().length());
                        else if(mode == ReadmodeBlock.SingleEnd){
                            String ischaracterofDel = line5.getValor().substring(last.getValor() + delimiterEnd.length(), line5.getValor().length());
                            Character m = ischaracterofDel.charAt(0);
                            if(m.equals(delimiterEnd.charAt(delimiterEnd.length()-1))){
                                if(ischaracterofDel.length() > 1) getString = ischaracterofDel.substring(1, ischaracterofDel.length());
                                else getString = "";
                            }
                        }
                    if(!getString.equals("")) WritteFile.write(" " + getString);//if are a void line(""), write a newLine
                    }
                }
                //write the completely line
                //escribir la linea completa
                else WritteFile.write(line5.getValor());//Escrbir la linea
                    WritteFile.newLine();
                
                
            }
    } catch (IOException e) {
        throw new ParsingException("Error in the reading or writing file: " + e.getMessage(), e);
    }
    catch (ParsingException e) {
        throw new ParsingException("Error in the parsing: " + e.getMessage(), e);
    }

    // Actualizar el archivo de entrada
    //Upload the input file
    File infile = new File(Read_File_in);
    if (infile.delete()) {
        File temp = new File("tempWithoutBlockComments.txt");
        if (temp.renameTo(infile)) {
            System.out.printf("The file 'tempWithoutBlockComments.txt' is renamed to '%s'\n", Read_File_in);
            System.out.printf("\nTHE FILE '%s' IS CLEAN OF BLOCK COMMENTS\n", Read_File_in);
            return Return.SUCCESS.getValue();
        }
       throw new ParsingException(String.format("Error trying to rename file 'tempWithoutBlockComments.txt' to '%s'", Read_File_in));
    }
    throw new ParsingException(String.format("Error trying to delete the file '%s'", Read_File_in));
}
//--------------------------------------------------------------
/*mode for know how proccess the block comments
*SingleEnd: search only the first close of the comment, independent if are nested block comments(not check the content, not are important)
*NestedEnd: search and verify that all nested comments are closed correctly(check the content and are important)
*/
/*modo para saber como procesar los comentarios en bloque
 * SingleEnd: busca solo el primer cierre de el comentario, independientemente de si hay comentarios andidados(no interesa el contenido)
 * NestedEnd: busca y verifica que todos los comentarios anidados este cerrados correctamente(interesa el conteindo)
 */
public enum ReadmodeBlock{
    NestedEnd,
    SingleEnd
}

/**
 * This function remove the block comments and nested block comments if are setup for make this, this change his configuratino depende of the mode proporcionated flexibility and adaptability
 * @param dataForProccess Datas for porccess the block comments thats are wrapper in the class 'ProccessBlockComments'
 * @return 0 = SUCCES, -1 = ERROR
 * @throws IOException If an I/O error occurs
 * @throws ParsingException If the lastRecursiveCallFlag is null or a call method is not correct
 */
//All in this function are a proccess you can divide that in aux functions but not is necesary because just reduce a little bit the number of lines, but not is necesary, and this in this implementation are used aux functions too
public int removeNestedBlockComments(ProccessBlockComments dataForProccess) throws IOException, ParsingException { 
    if(dataForProccess == null){
        throw new ParsingException("Error: The parameter 'dataForProccess' is null, need be a ProccessBlockComments object");
    }
    if(dataForProccess.line == null){
        throw new ParsingException("Error: The parameter 'line' is null, need be a MutableTypeData<String> object");
    }
    if(dataForProccess.lastRecursiveCallFlag == null){
        throw new ParsingException("Error: The parameter 'lastRecursiveCallFlag' is null, need be a MutableTypeData<Boolean> object");
    }

    dataForProccess.lastRecursiveCallFlag.setValor(true);//initialize always in true //incializar siempre en verdadero
    MutableTypeData<Integer> actual = new MutableTypeData<>(0);
    try{
    //Read until the end of comment
    //Leer hasta el final del comentario
    while(true){
      int endDelIndex = 0, startDelIndex = 0; //variables for watch the result of the search //variables para ver el resultado de la busqueda
      //search the delimiter that indicate the start of the comment
      //Buca el delimitador que indica el iniciio de un comentario
      startDelIndex = searchString(false, dataForProccess.line.getValor(), dataForProccess.DelimiterStart, dataForProccess.indexActualInTheLine.getValor(), null, false); 
     int m = startDelIndex;
      //remove the delimiter readed and upload de line
      //eliminar el delimitador ya leido y actualizar la linea
        if(startDelIndex >= 0 && startDelIndex != dataForProccess.line.getValor().length()){ 
            StringBuilder newLine = new StringBuilder(dataForProccess.line.getValor());
            newLine.delete(startDelIndex, startDelIndex+dataForProccess.DelimiterStart.length());
            dataForProccess.line.setValor(newLine.toString());
            dataForProccess.indexActualInTheLine.setValor(0);
        }
         //Search the delimiter of end comment
    //buscar el delimitador de final de comentario
        endDelIndex = searchString(false, dataForProccess.line.getValor(), dataForProccess.DelimiterEnd, dataForProccess.indexActualInTheLine.getValor(), null, false);
        
        //remove the delimiter after readed
        //eliminar el delimitador una vez leido
        if(dataForProccess.mode == ReadmodeBlock.NestedEnd && endDelIndex >= 0 && endDelIndex != dataForProccess.line.getValor().length()){ 
            StringBuilder newLine = new StringBuilder(dataForProccess.line.getValor());
            newLine.delete(endDelIndex, endDelIndex+dataForProccess.DelimiterEnd.length());
            dataForProccess.line.setValor(newLine.toString());
            dataForProccess.indexActualInTheLine.setValor(0);
        }
        //search the delimiter in the uploading line, for search nested comments
        //buscar el delimitador en la linea actualizada, para buscar comentarios anidados
    startDelIndex = searchString(false, dataForProccess.line.getValor(), dataForProccess.DelimiterStart, dataForProccess.indexActualInTheLine.getValor(), null, false); 
        
    if(endDelIndex == -2 || startDelIndex == -2 || (endDelIndex == -3 || startDelIndex == -3)) return Return.ERROR.getValue(); //error in the parameters 
         
        //if not find in this line
        //si no encuentra en esta line
      //Upload the parameters, uploading the line, the index, and the number of line actual
      //Actualizar los parametros, actualizando la linea, el indice y el numbero de linea actual
      else if(endDelIndex == -1 && startDelIndex == -1){
        //Get the number of line if it has
        //Obtener el numero de linea si lo tiene
        actual.setValor((int)dataForProccess.line.getValor().length());

        while((char)((int)actual.getValor()) == '\n') actual.setValor(dataForProccess.ReadFile.read()); 
        
         if(dataForProccess.DelimiterNumLine != null){
            int h = dataForProccess.line.getValor().indexOf(dataForProccess.DelimiterNumLine);
            dataForProccess.NumberOfLine = dataForProccess.line.getValor().substring(0, h);
        }
        //Check if the character actual is the end of the before line
        //Revisar si el caracter actual es el final de la linea anteriror
        MutableTypeData<Boolean> contain = new MutableTypeData<>(false);
        dataForProccess.line.setValor(get(dataForProccess.ReadFile, Readmode.CompletelyLine, dataForProccess.DelimiterNumLine, actual, contain));
            //If find EOF and not are content before
        //Si se llego a EOF y no habia contenido antes
        if(dataForProccess.line.getValor().equals("") || dataForProccess.line.getValor().equals("\n") || dataForProccess.line.getValor().equals("\0") && !contain.getValor()) break;
        //Upload the parameters, uploading the index
        //Actualizar los parametros, actualizando el indice
        dataForProccess.indexActualInTheLine.setValor(0);
        if(dataForProccess.countOfLineProcessed != null){
        int moreLine = dataForProccess.countOfLineProcessed.getValor()+1;
        dataForProccess.countOfLineProcessed.setValor(moreLine);
        }
        dataForProccess.itsMultiLine = true;
        continue;
      } 
      
        
        //if has a nested comment block
           //Si tiene un comentario en bloque anidado
    if(startDelIndex >= 0 || (dataForProccess.itsMultiLine && m >= 0)){    
        //Dependend the mode
        //Dependiendo de el modo
        switch (dataForProccess.mode){
          case NestedEnd:
            //if has code between comments get it
            //si tiene codigo entre comentarios extraerlo
             if(!(endDelIndex > startDelIndex) && endDelIndex >= 0 && !dataForProccess.itsMultiLine){
            String newl;
            if(startDelIndex != -1) newl = dataForProccess.line.getValor().substring(endDelIndex, startDelIndex);//get the string between comments
            else newl = dataForProccess.line.getValor().substring(endDelIndex, dataForProccess.line.getValor().length());
            dataForProccess.BetweenComments.add(newl);
            }
            dataForProccess.recursiveCall = true;
         if(removeNestedBlockComments(dataForProccess) != 0) return Return.ERROR.getValue();
         break;
          case SingleEnd: 
          //if has code between comments get it
        //si tiene codigo entre comentarios extraerlo
        if(!(endDelIndex > startDelIndex) && endDelIndex >= 0 && !dataForProccess.itsMultiLine){
            boolean edit = false;
            String newl;
            if(!(endDelIndex+dataForProccess.DelimiterEnd.length() > dataForProccess.line.getValor().length())){
            if(startDelIndex != -1 && !(endDelIndex+dataForProccess.DelimiterEnd.length() >= startDelIndex)) newl = dataForProccess.line.getValor().substring(endDelIndex+dataForProccess.DelimiterEnd.length(), startDelIndex);//get the string between comments
            else if(startDelIndex != -1 && endDelIndex + dataForProccess.DelimiterEnd.length() >= startDelIndex){
                newl = dataForProccess.line.getValor().substring(startDelIndex+dataForProccess.DelimiterStart.length(), dataForProccess.line.getValor().length()); //if n+delimiterEnd length is equal or grether than r, stary r(priorize n(coment end delimiter))(thats appear when push something like '*/*/' because r starts in 2 and n in 1(in this example) in this mode)
                  int s = searchString(false, newl, dataForProccess.DelimiterStart, 0, null, false);
                  if(s != -1){
                     int indexStart = s + (startDelIndex+dataForProccess.DelimiterStart.length());
                  newl = dataForProccess.line.getValor().substring(startDelIndex+dataForProccess.DelimiterStart.length(), indexStart);
                  }
                  edit = true;
                }
                 else newl = dataForProccess.line.getValor().substring(endDelIndex+dataForProccess.DelimiterEnd.length(), dataForProccess.line.getValor().length());
            dataForProccess.BetweenComments.add(newl);
            }
            //if is in mode SingleEnd, upload n, because in this mode search only the first close
                //remove the delimiter after readed
               //eliminar el delimitador una vez leido
               if(!(edit) && endDelIndex >= 0 && endDelIndex != dataForProccess.line.getValor().length()){ 
                  StringBuilder newLine = new StringBuilder(dataForProccess.line.getValor());
                   newLine.delete(endDelIndex, endDelIndex+dataForProccess.DelimiterEnd.length());
                   dataForProccess.line.setValor(newLine.toString());
                   dataForProccess.indexActualInTheLine.setValor(0);
                }
                //if are editing the code because find a case like this example '*/*/'(conflict into delimiter end and delimiter start) remove this conflict after processed
                else if(edit){
                    StringBuilder newLine = new StringBuilder(dataForProccess.line.getValor());
                   newLine.delete(endDelIndex, startDelIndex+dataForProccess.DelimiterEnd.length());
                   dataForProccess.line.setValor(newLine.toString());
                   dataForProccess.indexActualInTheLine.setValor(0);
                }
        }
        dataForProccess.recursiveCall = true;
          if(removeNestedBlockComments(dataForProccess) != 0 ) return Return.ERROR.getValue();
          break;
          default:
          System.err.println("Error in the argument 'mode'\n");
          return Return.ERROR.getValue();
      }
    }

       if(endDelIndex >= 0){
        //Si es una llamda recursiva, y hay un delimitador final actualziar el parametro 'lastEndofCommentDelimiter', need be a recursive call, because if not are that proccess not is necessary, because after of that 'indexActualInTheLine' upload with index of the first appear of Delimiter End commentBlock
        //If are a recursive call, and have a final delimiter upload the parameter 'lastEndofCommentDelimiter', nesecita ser una llamda recursiva porque sino es una este proceso no es necesario, porque despues de esto 'IndexActualInTheLine' se actualiza con el indice de la primera aparacion de un delimitador de final de comentario en bloque 
       if(dataForProccess.recursiveCall && (dataForProccess.lastRecursiveCallFlag.getValor() == true) && endDelIndex >= 0){
        if(endDelIndex > 0) dataForProccess.lastEndofCommentDelimiter.setValor(endDelIndex-1);//upload with the index of the las end comment delimiter
        else if(endDelIndex == 0) dataForProccess.lastEndofCommentDelimiter.setValor(endDelIndex);
       dataForProccess.lastRecursiveCallFlag.setValor(false);//set the value of flag in the last call recursive for stay his value
       }
       dataForProccess.indexActualInTheLine.setValor(endDelIndex-1);
        return Return.SUCCESS.getValue();
      } 
    }
  } catch (IOException e) {
    throw new ParsingException("Error: " + e.getMessage());
  }
  catch(ParsingException e1){
    throw new ParsingException("Error: "+e1.getMessage());
  }
      // If find the end of file without closing the comment
    // Si encuentra el final del archivo sin cerrar el comentario
            System.err.println("Error in the line: "+ dataForProccess.NumberOfLine +"\nDETAILS:Find the 'End of file' and don't closing a comment block\n");
            return Return.ERROR.getValue();
}
//--------------------------------------------------------------
/**
 * Remove chars considerateds 'void' in a file like tabs and others
 * @param Read_File_In, File to search and remove void characters
 * @param VoidCharacterStart Remove a character considerated 'void'
 * @return void
 * @throws ParsingException if the file is null or empty, or if an error occurs during file processing
 */
public void removeVoidChars(String Read_File_In, Character VoidCharacterStart) throws ParsingException {
    if(Read_File_In == null){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    if(Read_File_In.isEmpty()){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    System.out.printf("\nREMOVING VOID CHARS FROM THE FILE: '%s'...\n\n", Read_File_In);
    // Open the file for reading
    // Abrir el archivo para lectura y el archivo de escritura
    try(Reader ReadFile = new FileReader(Read_File_In); Writer WritteFile = new FileWriter("tempWithoutVoidChars.txt")){
        int c = ReadFile.read(); // Read the first character
                                  // Leer el primer carácter
        //While don't find EOF (End of file)
        //Mientras no encuentre EOF (Fin de archivo)
        while(c != -1){
            if(VoidCharacterStart != null){
            while(c != -1 && (char)c != '\0' && (char)c != ' ' && (char)c != '\t' && (char)c != '\r' && (char)c !='\f' && (char)c != '\u000B' && (char)c != '\u2028'  && (char)c != VoidCharacterStart){
                WritteFile.write((char)c);
                c = ReadFile.read(); // Read the next character
                                     // Leer el siguiente carácter
            }
                if(c == -1){
                    break;
                }
                //ignore it
                //ignóralo
                else{
                   c = ReadFile.read(); // Read the next character
                                         // Leer el siguiente carácter
                  continue;
                }
        }
        else {
            while(c != -1 && (char)c != '\0' && (char)c != ' ' && (char)c != '\t' && (char)c != '\r' && (char)c !='\f' && (char)c != '\u000B' && (char)c != '\u2028'){
                WritteFile.write((char)c);
                c = ReadFile.read(); // Read the next character
                                     // Leer el siguiente carácter
            }
                if(c == -1){
                    break;
                }
                //ignore it
                //ignóralo
                else{
                   c = ReadFile.read(); // Read the next character
                                         // Leer el siguiente carácter
                   continue;
                }
        }
    }
 }
 catch(IOException e){
        throw new ParsingException("Error: " + e.getMessage());
    }
    //Upload the input file
    //Actualizar el archivo de entrada
    File infile = new File(Read_File_In);
    if(infile.delete()){
        File temp = new File("tempWithoutVoidChars.txt");
        if(temp.renameTo(infile)){
            System.out.printf("The file 'tempWithoutVoidLines.txt' is rename to '%s'\n", Read_File_In);
            System.out.printf("\nTHE FILE '%s' IS CLEAN OF VOID CHARS\n", Read_File_In);
            return;
        }
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    throw new ParsingException("Error: The file to proccess is empty or not exist");
}
//--------------------------------------------------------------
/**
 * Numerate lines from 1-n (where n are the num of lines in the file)
 * @param Read_File_in File to numerate his lines
 * @return void
 * @throws ParsingException if the file is null or empty, or if an error occurs during file processing
 */
public void numLines(String Read_File_in) throws ParsingException {
    if(Read_File_in == null){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    if(Read_File_in.isEmpty()){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    System.out.printf("\nADDING LINE NUMBERS TO THE FILE: '%s'...\n\n", Read_File_in);
    // Open the file for reading and the file for writing
    // Abrir el archivo para lectura y el archivo de escritura
    try (Reader ReadFile = new FileReader(Read_File_in);
         Writer writterFile = new FileWriter("fileWithNumLines.txt")) {
       
        int c = ReadFile.read();
        int line = 0;
        // Read the file character by character
        // Leer el archivo carácter por carácter
        while (c != -1) {
            // Escribir el número de línea
            // Write the line number
            writterFile.write(String.valueOf(line));
            writterFile.write(' ');

            // Escribir el contenido de la línea
            // Write the content of the line
            while (c != -1 && c != '\n') {
                writterFile.write((char) c);
                c = ReadFile.read();
            }

            // Escribir salto de línea si fue leído
            // Write a newline if it was read
            if (c == '\n') {
                writterFile.write('\n');
                c = ReadFile.read(); // Leer el siguiente carácter para la próxima línea
                                    // Read the next character for the next line
                line++;
            }
        }

    } catch (IOException e) {
        throw new ParsingException("Error: " + e.getMessage());
    }
    // Upload the input file
    // Actualizar el archivo de entrada
    File infile = new File(Read_File_in);  

    if (infile.delete()) {
        File temp = new File("fileWithNumLines.txt");
        if (temp.renameTo(infile)) {
            System.out.printf("The file 'fileWithNumLines.txt' is rename to '%s'\n", Read_File_in);
            System.out.printf("\nTHE FILE '%s' ARE NUMERATED\n", Read_File_in);
            return;
        }
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    throw new ParsingException("Error: To try to delete de file '" +Read_File_in+"'");
}
//--------------------------------------------------------------
//This enum is used to define the read mode of the file for the method get
//Este enum se utiliza para definir el modo de lectura del archivo para el método get
public enum Readmode{
    NumberLine,
    CompletelyLine
}
/**
 * Get a String or a line in a file can be type 'Reader'(uploading too the index in the reader file), this get or search in a line not in all the file
 * @param fileIn File open in Reader mode
 * @param mode  mode type 'Readmode', thats have 2 modes
 * <p>NumberLine: get the "numberofLine" or a String before delimiter for number line from the file and return this</p>
 * <p>CompletelyLine: get a completely line from the file and return this</p>
 * @param forNumberLine_Delimiter If select the mode 'NumberLine' need put this parameter, else don't put
 * @param actual If you want get and mantain informated about the index in the file cand put a actual parameter to upload this in real time
 * @param containsBeforeEOForEndLine If you want know if before EOF(End of File) or End of Line have characters 
 * @return NULL = VOID LINE, Or a String with the content of the line or number line
 * @throws ParsingException,IOException if the fileIn is null or an error occurs during file processing
 */
public String get(Reader fileIn, Readmode mode, Character forNumberLine_Delimiter, MutableTypeData<Integer> actual, MutableTypeData<Boolean> containsBeforeEOForEndLine) throws ParsingException,IOException {
    if(fileIn == null){ 
        throw new ParsingException("Error: Need put a file to proccess");
    }
    int c = 0;
    if(actual != null) {
        c = actual.getValor();
    } else {
        c = fileIn.read(); // Lee el primer carácter
        
        // Filtrar BOM si existe
        if (c == 0xFEFF) {
            c = fileIn.read(); // Leer el siguiente carácter después del BOM
        }
    }
    
    StringBuilder result = new StringBuilder();
    if(containsBeforeEOForEndLine != null) {
        containsBeforeEOForEndLine.setValor(false);
    }
    
    if(mode == Readmode.NumberLine) {
        while (c != -1 && (char)c != forNumberLine_Delimiter) {
            result.append((char)c);
            if(containsBeforeEOForEndLine != null) {
                containsBeforeEOForEndLine.setValor(true);
            }
            if(actual != null) {
                actual.setValor(c);
            }
            c = fileIn.read();
        }
        return result.toString();
    }
    else if(mode == Readmode.CompletelyLine) {
        while (c != -1 && (char)c != '\n') {
            // Solo agregar caracteres imprimibles o espacios
            if ((c >= 32 && c <= 126) || c == 9 || c == 32) { // ASCII imprimible o tabs/espacios
                result.append((char)c);
            }
            
            if(containsBeforeEOForEndLine != null) {
                containsBeforeEOForEndLine.setValor(true);
            }
            if(actual != null) {
                actual.setValor(c);
            }
            c = fileIn.read();
        }
        return result.toString();
    }
    
    return null;
}
//--------------------------------------------------------------
/**
 * Remove a String before the delimiter, and the delimimter, thought for remove numberofLines, but can use for remove a string before a delimiter
 * @param file_in File to clear 
 * @param delimiter Delimiter that indicate the start of writte in the temp file, for last rename or upload the input file
 * @return void
 * @throws ParsingException if the file_in is null or empty, or if an error occurs during file processing
 */
public void removeNLine(String file_in, Character delimiter) throws ParsingException {
    if(file_in == null){
        throw new ParsingException("Error: Need put a file to proccess");
    }
    if(file_in.isEmpty()){
        throw new ParsingException("Error: The file to proccess is empty");
    }
    System.out.printf("\nFINAL CLEANING THE FILE: '%s'...\n\n", file_in);
    // open the file for reading and the file for writing
    // Abrir el archivo para lectura y el archivo de escritura
    try(Reader readFile = new FileReader(file_in); Writer WrtterFile = new FileWriter("tempFinalClean.txt")){
        int c = 0;
        while(true){
            //Get the number line and ignore it
            //Obtener el número de línea y ignorarlo
            get(readFile, Readmode.NumberLine, delimiter, null, null); 
            c = readFile.read(); // Read the next character
                                    // Leer el siguiente carácter
           if(c == -1) break;
           if((char)c == '\n' || (char)c == '\0'){
            WrtterFile.write('\n');
            while(c != -1 && (char)c != '\n'){
                c = readFile.read(); // Read the next character
                                    // Leer el siguiente carácter
            }
            continue;
           }
           //if find a character after the number line, write the line without the number line
           //si encuentra un carácter después del número de línea, escriba la línea sin el número de línea
           else{
            while(c != -1 && (char)c != '\n'){
                WrtterFile.write((char)c);
                c = readFile.read(); // Read the next character
                                    // Leer el siguiente carácter
             }
             if(c == -1) break;
            WrtterFile.write('\n');
           }

       } 
    }
    catch(IOException e){
       throw new ParsingException("Error while processing the file: " + e.getMessage());
    }
    //Upload the input file
    //Actualizar el archivo de entrada
    File infile = new File(file_in);
    if(infile.delete()){
        File temp = new File("tempFinalClean.txt");
        if(temp.renameTo(infile)){
            System.out.printf("The file 'tempFinalClean.txt' is rename to '%s'\n", file_in);
            System.out.printf("\nTHE FILE '%s' IS CLEAN TO THE VOID LINES AND NUMBER LINE\n", file_in);
            return;
        }
       else throw new ParsingException("Error to try rename the file: " + file_in);
    }
    else throw new ParsingException("Error to try delte the file: " + file_in);
}
//--------------------------------------------------------------
/**
 * trate a special type of strings of a file, search the first appears of this while remove voidChars to the same time, and when find this, search the first space appear after this, an put a character '~' for separe this
 * @param lineToidentify lines for special trate
 * @param inputFile file for 
 * <pre>
 * <code>
 * //if i want serach a string some "function" in my file and trate this can be use this
 * //my file contains "HI\nthi s is a function 123\nfuncti on hello 123"
 * Parser n = new Parser();
 * ArrayList<String> specials = new ArrayList<>();
 * specials.add("function");
 * n.trateSpecialsStrings(specials, "myFile.txt");
 * //So the result are "HI\nthisisafunction1234\nfunctionhello~123"
 * //This occurs because inside the funtion while remove "voidChars" like spaces and tabs and others, the method compares continously when are in the line, and verify if this are contains in the arrayList, and this need be exactly
 * //for this reason in the before example the line "this is a function 123" and dont conicide for exactly string "fuction" in the arrayList, because before to te string "function" are others characters and don't concide
 * //but in the next line "functi on hello 123" the string "functi on" concide exactly with the string in the arrayList, so the method put a '~' after the "hello" part because the function when find this ignore or jump(but if are a void char remove it) the next character and after this search the first space, and remove all spaces and tabs and others void characters
 * </code>
 * </pre>
 * @return void
 * @throws ParsingException if the inputFile is null or empty, or if an error occurs during file processing
 */
public void trateSpecialsStrings(ArrayList<String> lineToidentify, String inputFile) throws ParsingException {
    System.out.printf("\nTRATE SPECIALS STRINGS FROM THE FILE: '%s'...\n\n", inputFile);
    if (lineToidentify == null || lineToidentify.isEmpty()) {
       throw new ParsingException("Error: Need put some value in the string of input.");
    }
    String tempFile = "tempSpecialsStrings.txt";

    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            StringBuilder newLine = new StringBuilder(line);

            // Construir la línea sin espacios ni caracteres vacíos
            StringBuilder withoutSpaces = new StringBuilder();
            for (int i = 0; i < newLine.length(); i++) {
                char c = newLine.charAt(i);
                if (c != ' ' && c != '\t' && c != '\r' && c != '\f' && c != '\u000B' && c != '\u2028' && c != '\0') {
                    withoutSpaces.append(c);
                }
                else{
                    if(c == ' ' || c == '\t' || c == '\r'){
                         newLine.deleteCharAt(i);
                         i=0;
                         }
                    if(!withoutSpaces.isEmpty())break;
                }
            }

            // Si la línea sin espacios es igual a la cadena de entrada
            String forCompare = withoutSpaces.toString().trim();
            if (lineToidentify.contains(forCompare)) {
                int indexOfSpace = newLine.indexOf(" ");
                if (indexOfSpace != -1) {
                    newLine.setCharAt(indexOfSpace, '~');
                }
            }

            // Eliminar todos los espacios y caracteres vacíos (incluyendo el ^ si lo pusimos)
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < newLine.length(); i++) {
                char c = newLine.charAt(i);
                if (c != ' ' && c != '\t' && c != '\r' && c != '\f' && c != '\u000B' && c != '\u2028' && c != '\0') {
                    result.append(c);
                }
            }

            writer.write(result.toString());
            writer.newLine();
        }

    } catch (IOException e) {
        throw new ParsingException("Error while processing the file: " + e.getMessage());
    }

    // Reemplaza el archivo original por el temporal
    File original = new File(inputFile);
    File temporal = new File(tempFile);

    if (original.delete()) {
        if (temporal.renameTo(original)) {
            System.out.printf("THE FILE '%s' ARE BE CLEANING AND UPLOAD.\n", inputFile);
            return;
        } else {
            throw new ParsingException("Error to try rename the file: " + inputFile);
        }
    } else {
        throw new ParsingException("Error to try remove the original file: " + inputFile);
    }
}
//--------------------------------------------------------------
/** 
 * Remove void lines in a file, this function read the file line by line and remove all lines that are empty or contain only whitespace characters
 * @param Read_File_in File to remove void lines
 * @return void
 * @throws ParsingException if the file is null or empty, or if an error occurs during file processing
*/
public void removeVoidLines(String Read_File_in) throws ParsingException {
    if(Read_File_in == null){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    if(Read_File_in.isEmpty()){
        throw new ParsingException("Error: The file to proccess is empty or not exist");
    }
    System.out.printf("\nREMOVING VOID LINES FROM THE FILE: '%s'...\n\n", Read_File_in);
    // Open the file for reading and the file for writing
    // Abrir el archivo para lectura y el archivo de escritura
    try(BufferedReader ReadFile = new BufferedReader(new FileReader(Read_File_in)); BufferedWriter WritteFile = new BufferedWriter(new FileWriter("tempWithoutVoidLines.txt"))){
        String line;
        while((line = ReadFile.readLine()) != null){
            if(line.trim().isEmpty()) continue; // Skip empty lines
            WritteFile.write(line);
            WritteFile.newLine();
        }
    } catch (IOException e) {
        throw new ParsingException("Error in the reading or writing file: " + e.getMessage(), e);
    }
    // Reemplaza el archivo original por el temporal
    File original = new File(Read_File_in);
    File temporal = new File("tempWithoutVoidLines.txt");

    if (original.delete()) {
        if (temporal.renameTo(original)) {
            System.out.printf("THE FILE '%s' ARE BE CLEANING AND UPLOAD.\n", Read_File_in);
            return;
        } else {
            throw new ParsingException("Error to try rename the file: " + Read_File_in);
        }
    } else {
        throw new ParsingException("Error to try remove the original file: " + Read_File_in);
    }
}
//END THE PROCCES TO PREPARE FILES(TERMINA EL PROCESO DE PREPARACIÓN DE ARCHIVOS)--------------------------------------------------------------
}
