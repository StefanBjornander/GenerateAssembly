//8984
package generate;

import java.io.*;
import java.math.*;
import java.util.*;

public class Main {
  private static int m_batCount = 0;
//  private static boolean m_extra = false;
  
  private static String[] floatIntUnaryOperator = new String[]{"fild", "fist", "fistp"};
  private static String[] floatUnaryOperator = new String[]{"fld", "fst", "fstp"};
        
  private static String[] intOperator = new String[]{"int"}; // mov ax, bx; mov ax, 100; mov ax, [bx]
  //private static String[] movOperator = new String[]{"mov"};
  private static String[] intBinaryOperator = new String[]{"mov", "add", "sub", "or", "xor", "and", "cmp"};
  private static String[] shiftOperator = new String[]{"shl", "shr"};
//  private static String[] cmpOperator = new String[]{"cmp"};

  private static String[] fstswOperator = new String[]{"fstsw"};
  private static String[] wordOperator = new String[]{"fldcw", "fstcw"};
  private static String[] singleOperator = new String[]{"nop", "ret", /*"finit", "fwait",*/ "fchs", "fabs", "ftst", "lahf", "sahf", "fldz", "fld1", "fadd", "faddp", "fsub", "fsubp", "fsubr", "fsubrp", "fmul", "fmulp", "fdiv", "fdivp", "fdivr", "fdivrp", "fcompp"};
  private static String[] intUnaryOperator = new String[]{"neg", "not", "mul", "imul", "div", "idiv", "inc", "dec"};
  private static String[] shortJumpOperator = new String[]{"jz", "jnz", "jc", "jnc", "je", "jne", "jl", "jle", "jb", "jbe", "jg", "jge", "ja", "jae"};
  private static String[] longJumpOperator = new String[]{"jmp", "call"};
  
  private static String[] ax = new String[]{"ax"};
  private static String[] cl = new String[]{"cl"};
  private static String[] register8 = new String[]{"al", "ah", "bl", "bh", "cl", "ch", "dl", "dh"};
  private static String[] register16 = new String[]{"ax", "bx", "cx", "dx", "si", "di", "sp", "bp"};
  private static String[] register16index = new String[]{"bx", "si", "di", "bp"};
//  private static String[] register16extra = new String[]{"ds", "cs"};
  private static String[] register32 = new String[]{"eax", "ebx", "ecx", "edx", "esi", "edi", "esp", "ebp"};
//  private static String[] register32index = new String[]{"ebx", "esi", "edi", "ebp"};
  private static String[] registerfloat = new String[]{"st0", "st1", "st2", "st3", "st4", "st5", "st6", "st7"};

  //private static String pathName = "C:\\Users\\Stefan\\Documents\\C_Compiler\\nasm\\Test\\";
  //private static String pathName = "C:\\Users\\Stefan\\Documents\\GenerateAssembler\\test\\";
  //private static String executeName = "C:\\Users\\Stefan\\Documents\\GenerateAssembler\\src\\generate\\";
  private static String pathName = "C:\\Test\\x\\";
  //private static String executeName = "C:\\Test\\";
  
  private static PrintStream m_debugStream;
  private enum Status {Single, Register, Memory, Value, RegisterRegister, RegisterMemory, RegisterValue, MemoryRegister, MemoryValue, Target};
  
  private static final int Offset_Zero = 0;
  private static final int Offset_Byte = 1;
  private static final int Offset_Word = 256;
//  private static final int Offset_Word = 65536;
  
  private static final int Value_Zero = 0;
  private static final int Value_Byte = 1;
  private static final int Value_Word = 256;
  private static final int Value_DWord = 65536;

  private static final int Shift_Byte = 7;
  private static final int Shift_Word = 15;
  private static final int Shift_DWord = 31;
  
  private static int m_count = 0;
  
  public static void main(String[] args)
  {
    try {
      m_debugStream = new PrintStream("C:\\Users\\Stefan\\Documents\\C_Compiler\\nasm\\Test\\Debug.txt");
      File directory = new File(pathName);
      
      if (directory.isDirectory()) {
        for (String fileName : directory.list()) {
          File file = new File(pathName + fileName);
          file.delete();
        }
      }

      PrintStream batStream = new PrintStream("C:\\Test\\Execute.bat");

//      write(batStream, Status.Single, floatBinaryOperator, null, null);
//      write(batStream, Status.Register, floatBinaryOperator, registerfloat, null);
//      write(batStream, Status.RegisterRegister, floatBinaryOperator, registerfloat, registerfloat);

      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "word", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, register16index, null, "word", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, register16index, null, "dword", Offset_Word);
//      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "word", Offset_Word);
//      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "dword", Offset_Word);

      write(batStream, Status.Memory, floatUnaryOperator, null, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, null, null, "qword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, register16index, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, register16index, null, "qword", Offset_Word);
//      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "dword", Offset_Word);
//      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "qword", Offset_Word);

      write(batStream, Status.RegisterRegister, shiftOperator, register8, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register16, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register32, cl);
      write(batStream, Status.RegisterValue, shiftOperator, register8, null, null, 0);
      write(batStream, Status.RegisterValue, shiftOperator, register16, null, null, 0);
      write(batStream, Status.RegisterValue, shiftOperator, register32, null, null, 0);
      
      write(batStream, Status.MemoryValue, shiftOperator, null, null, "byte", Offset_Word, Value_Byte); // add byte [100], 20
      write(batStream, Status.MemoryValue, shiftOperator, null, null, "word", Offset_Word, Value_Byte); // add word [100], 20
      write(batStream, Status.MemoryValue, shiftOperator, null, null, "dword", Offset_Word, Value_Byte); // add dword [100], 20
      write(batStream, Status.MemoryValue, shiftOperator, register16index, null, "byte", Offset_Word, Value_Byte); // add byte [bp + 10], 20
      write(batStream, Status.MemoryValue, shiftOperator, register16index, null, "word", Offset_Word, Value_Byte); // add word [bp + 10], 20
      write(batStream, Status.MemoryValue, shiftOperator, register16index, null, "dword", Offset_Word, Value_Byte); // add dword [bp + 10], 20

      write(batStream, Status.RegisterRegister, intBinaryOperator, register8, register8); // add ax, bx
      write(batStream, Status.RegisterRegister, intBinaryOperator, register16, register16);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register32, register32);
      
      write(batStream, Status.RegisterValue, intBinaryOperator, register8, null, null, 0, Value_Byte); // add ax, 10
      write(batStream, Status.RegisterValue, intBinaryOperator, register16, null, null, 0, Value_Word);
      write(batStream, Status.RegisterValue, intBinaryOperator, register32, null, null, 0, Value_DWord);      
      //write(batStream, Status.RegisterValue, movOperator, register16extra, null, null, 0, Value_Word);

      write(batStream, Status.RegisterMemory, intBinaryOperator, register8, null, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16, null, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32, null, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register8, register16index, Offset_Word); // add ax, [bp + 10]
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32, register16index, Offset_Word);
//      write(batStream, Status.RegisterMemory, intBinaryOperator, register8, register32index, Offset_Word); // add ax, [bp + 10]
//      write(batStream, Status.RegisterMemory, intBinaryOperator, register16, register32index, Offset_Word);
//      write(batStream, Status.RegisterMemory, intBinaryOperator, register32, register32index, Offset_Word);
      
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register8, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register16, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register32, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register8, Offset_Word); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register16, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register32, Offset_Word);
//      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register8, Offset_Word); // add [bp + 10], ax
//      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register16, Offset_Word);
//      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register32, Offset_Word);
      
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "byte", Offset_Word, Value_Byte); // add byte [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "word", Offset_Word, Value_Word); // add word [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "dword", Offset_Word, Value_DWord); // add dword [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "byte", Offset_Word, Value_Byte); // add byte [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "word", Offset_Word, Value_Word); // add word [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "dword", Offset_Word, Value_DWord); // add dword [bp + 10], 20
//      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "byte", Offset_Word, Value_Byte); // add byte [bp + 10], 20
//      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "word", Offset_Word, Value_Word); // add word [bp + 10], 20
//      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "dword", Offset_Word, Value_DWord); // add dword [bp + 10], 20
      
      write(batStream, Status.Memory, intUnaryOperator, null, null, "byte", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "dword", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register16index, null, "byte", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register16index, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register16index, null, "dword", Offset_Word);
//      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "byte", Offset_Word);
//      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "word", Offset_Word);
//      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "dword", Offset_Word);

/*     write(batStream, Status.MemoryValue, cmpOperator, register16index, null, "byte", Value_Byte);
      write(batStream, Status.MemoryValue, cmpOperator, register16index, null, "word", Value_Word);
      write(batStream, Status.MemoryValue, cmpOperator, register16index, null, "dword", Value_DWord);*/

      write(batStream, Status.Register, intUnaryOperator, register8, null);
      write(batStream, Status.Register, intUnaryOperator, register16, null);
      write(batStream, Status.Register, intUnaryOperator, register32, null);

      write(batStream, Status.Register, fstswOperator, ax, null, null, 0);
      write(batStream, Status.Memory, wordOperator, register16index, null, null, Offset_Word);
//      write(batStream, Status.Memory, wordOperator, register32index, null, null, Offset_Word);
      write(batStream, Status.Value, intOperator, null, null, null, 0, Value_Byte);      
      write(batStream, Status.Single, singleOperator, null, null);
      
//      m_extra = false;
//      write(batStream, Status.Target, shortJumpOperator, null, null, null, 0, Value_Byte);
      write(batStream, Status.Target, shortJumpOperator, null, null, null, 0, Value_Word);
      write(batStream, Status.Target, longJumpOperator, null, null, null, 0, Value_Word);
      write(batStream, Status.Target, longJumpOperator, register16, null);
      write(batStream, Status.Memory, longJumpOperator, register16index, null, Offset_Word);
      write(batStream, Status.Memory, longJumpOperator, null, null, Offset_Word);
      //write(batStream, Status.Target, longJumpOperator, register32, null);
      //write(batStream, Status.Memory, longJumpOperator, register16, null, Offset_Zero);
      //write(batStream, Status.Memory, longJumpOperator, register16, null, Offset_Byte);
      //write(batStream, Status.Memory, longJumpOperator, register16, null, Offset_Word);
      //write(batStream, Status.Memory, longJumpOperator, register16index, null, Offset_Zero);
      //write(batStream, Status.Memory, longJumpOperator, register16index, null, Offset_Byte);
      //write(batStream, Status.Memory, wordOperator, register16index, null, null, Offset_Word);
//      write(batStream, Status.Memory, longJumpOperator, register32index, null);

      // -----------------------------------------------------------------------------------------
      
/*      
      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "word", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "word", Offset_Word);
      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "dword", Offset_Word);

      write(batStream, Status.Memory, floatUnaryOperator, null, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, null, null, "qword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "dword", Offset_Word);
      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "qword", Offset_Word);

      write(batStream, Status.RegisterMemory, intBinaryOperator, register8, register32index, Offset_Word); // add ax, [bp + 10]
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16, register32index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32, register32index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register8, null, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16, null, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32, null, Offset_Word);
      
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register8, Offset_Word); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register16, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register32, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register8, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register16, Offset_Word);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register32, Offset_Word);
      
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "byte", Offset_Word, Value_Byte); // add byte [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "word", Offset_Word, Value_Word); // add word [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "dword", Offset_Word, Value_DWord); // add dword [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "byte", Offset_Word, Value_Byte); // add byte [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "word", Offset_Word, Value_Word); // add word [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "dword", Offset_Word, Value_DWord); // add dword [100], 20
      
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "byte", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "dword", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "byte", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "dword", Offset_Word);

//     write(batStream, Status.MemoryValue, cmpOperator, register32index, null, "byte", Value_Byte);
//      write(batStream, Status.MemoryValue, cmpOperator, register32index, null, "word", Value_Word);
//      write(batStream, Status.MemoryValue, cmpOperator, register32index, null, "dword", Value_DWord);

//      write(batStream, Status.Register, fstswOperator, ax, null, null, 0);
      write(batStream, Status.Memory, wordOperator, register32index, null, null, Offset_Word);
//      write(batStream, Status.Value, intOperator, null, null, null, 0, Value_Byte);      
//      write(batStream, Status.Single, singleOperator, null, null);
      
//      m_extra = false;
      write(batStream, Status.Target, longJumpOperator, null, null, null, 0, Value_DWord);
      write(batStream, Status.Memory, longJumpOperator, register32index, null);
*/

      // -----------------------------------------------------------------------------------------
      
/*      m_extra = true;
      write(batStream, Status.Target, shortJumpOperator, null, null, null, 0, Value_Byte);
      write(batStream, Status.Target, longJumpOperator, null, null, null, 0, Value_Word);*/

      batStream.println("@pause");
      batStream.close();
      m_debugStream.close();      

      System.out.println("Generated " + m_count + " files.");
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues) throws Exception
  {
    write(batStream, status, opValues, leftValues, rightValues, null, 0, 0);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, String text) throws Exception
  {
    write(batStream, status, opValues, leftValues, rightValues, text, 0, 0);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, int offset) throws Exception
  {
    write(batStream, status, opValues, leftValues, rightValues, null, offset, 0);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, String text, int offset) throws Exception
  {
    write(batStream, status, opValues, leftValues, rightValues, text, offset, 0);  
  }

  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                            String text, int offset, int value) throws Exception
  {
    if (offset == 0) {
      write2(batStream, status, opValues, leftValues, rightValues, text, 0, value);
    }
    else if (offset != 0) {
      switch (offset) {
        case Offset_Zero:
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Zero, value);
          break;

        case Offset_Byte:
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Zero, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Byte, value);
          break;

        case Offset_Word:
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Zero, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Byte, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Word, value);
          break;

/*          
        case Offset_Word:
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Zero, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Byte, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Word, value);
          write2(batStream, status, opValues, leftValues, rightValues, text, Offset_Word, value);
          break;*/
      }
    }
  }
  
  private static void write2(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                             String text, int offset, int value) throws Exception
  {
    if (value == 0) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, 0);
    }
    else {
      switch (value) {
        case Value_Zero:
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
          break;

        case Value_Byte:
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
          break;

        case Value_Word:
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Word);
          break;

        case Value_DWord:
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Word);
          write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_DWord);
          break;
      }
    }
  }
  
  private static void write3(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                             String text, int offset, int value) throws Exception
  {    
    for (String op : opValues) {
      if ((leftValues != null) && (rightValues != null)) {
        for (String left : leftValues) {
          for (String right : rightValues) {
            add(batStream, status, op, left, right, text, offset, value);
          } 
        }
      }
      else if (leftValues != null) {
        for (String left : leftValues) {
          add(batStream, status, op, left, null, text, offset, value);
        } 
      }
      else if (rightValues != null) {
        for (String right : rightValues) {
          add(batStream, status, op, null, right, text, offset, value);
        } 
      }
      else {
        add(batStream, status, op, null, null, text, offset, value);
      }
    }
  }

  private static int shiftValue(int value) {
    switch (value) {
      case Value_Byte:
        return 7;
        
      case Value_Word:
        return 15;
        
      case Value_DWord:
        return 31;
    }

    return 0;
  }

  private static void add(PrintStream batStream, Status status, String op, String left, String right,
                          String text, int offset, int value) throws Exception
  { 
    String fileOp = op;
    op = op.equals("call") ? "jmp" : op;
    
    if (text != null) {
      fileOp += "_" + text;
      op += " " + text;
    }

    String fileName;

    switch (status) {
      case Single:
        fileName = fileOp + "_null_null_null";
        break;
        
      case Register:
        fileName = fileOp + "_" + left + "_null_null";
        break;

      case Memory:
        fileName = fileOp + "_" + ((left != null) ? left : "null") + "_" + offset + "_null";
        break;
        
      case Value:
        fileName = fileOp + "_" + value + "_null_null";
        break;

      case RegisterMemory:
        fileName = fileOp + "_" + left + "_" + ((right != null) ? right : "null") + "_" + offset;
        break;

      case RegisterRegister:
        fileName = fileOp + "_" + left + "_" + right + "_null";
        break;

      case RegisterValue:
        fileName = fileOp + "_" + left + "_" + value + "_null";
        break;

      case MemoryRegister:
        fileName = fileOp + "_" + ((left != null) ? left : "null") + "_" + offset + "_" + right;
        break;

      case MemoryValue:
        fileName = fileOp + "_" + ((left != null) ? left : "null") + "_" + offset + "_" + value;
        break;

      case Target:
        fileName = fileOp + "_" + ((left != null) ? left : value) + "_null_null";
//        fileName = fileOp + "_" + ((left != null) ? left : value) + "_" + (m_extra ? 1 : "null") + "_null";
        break;
        
      default:
        fileName = null;
    }
    
    ++m_count;
    File file = new File(pathName + fileName + ".asm");
    
    if (file.exists()) {
      System.out.println(file);
    }
    
    PrintStream asmStream = new PrintStream(file);
    
    switch (status) {
      case Single:
        asmStream.println("          " + op);
        break;
        
      case Register:
        asmStream.println("          " + op + " " + left);
        break;

      case Memory:
        asmStream.println("          " + op + " [" + ((left != null) ? (left + " + ") : "") + offset + "]");
        break;
        
      case Value:
        asmStream.println("          " + op + " " + value);
        break;

      case RegisterRegister:
        asmStream.println("          " + op + " " + left + ", " + right);
        break;

      case RegisterMemory:
        asmStream.println("          " + op + " " + left + ", [" + ((right != null) ? (right + " + ") : "") + offset + "]");
        break;

      case RegisterValue:
        if (op.equals("shl") || op.equals("shr")) {
          asmStream.println("          " + op + " " + left + ", " + shiftValue(value));
        }
        else {
          asmStream.println("          " + op + " " + left + ", " + value);
        }
        break;

      case MemoryRegister:
        asmStream.println("          " + op + " [" + ((left != null) ? (left + " + ") : "") + offset + "], " + right);
        break;

      case MemoryValue:
        asmStream.println("          " + op + " [" + ((left != null) ? (left + " + ") : "") + offset + "], " + value);
        break;

      case Target:
        if (left != null) {
          asmStream.println("          " + op + " " + left);
        }
        else if (value == Value_Zero) {
          asmStream.println("          " + op + " target");
          asmStream.println("target:");
        }
        else if (value == Value_Byte) {
          asmStream.println("          " + op + " target");
          asmStream.println("          nop");
          asmStream.println("target:");
        }
        else {
          asmStream.println("          " + op + " target");

          for (int index = 0; index < 256; ++index) {
            asmStream.println("          nop");
          }
          
          asmStream.println("target:");          
        }
        break;
    }
    
    asmStream.close();
    String nasmPath = "@C:\\Users\\Stefan\\Documents\\C_Compiler\\nasm-2.11.02\\nasm";
    
    if (((++m_batCount) % 100) == 0) {
      System.out.println(m_batCount);
      batStream.println("@echo " + m_batCount);
    }
    
    batStream.println(nasmPath + " " + pathName + fileName + ".asm -o " + pathName + fileName + ".com");
  }
}
