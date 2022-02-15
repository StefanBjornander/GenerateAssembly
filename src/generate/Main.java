// 82418
package generate;

import java.io.*;
//import java.math.*;
//import java.util.*;

public class Main {
  private static int m_batCount = 0;
//  private static boolean m_extra = false;
  
  private static String[] floatIntUnaryOperator = new String[]{"fild", "fist", "fistp"};
  private static String[] floatUnaryOperator = new String[]{"fld", "fst", "fstp"};
        
  private static String[] intOperator = new String[]{"int"}; // mov ax, bx; mov ax, 100; mov ax, [bx]
  private static String[] movOperator = new String[]{"mov"};
  private static String[] intBinaryOperatorNoMov = new String[]{"add", "sub", "or", "xor", "and", "cmp"};
  private static String[] intBinaryOperator = new String[]{"mov", "add", "sub", "or", "xor", "and", "cmp"};
  private static String[] shiftOperator = new String[]{"shl", "shr"};
//  private static String[] cmpOperator = new String[]{"cmp"};

  private static String[] fstswOperator = new String[]{"fstsw"};
  private static String[] wordOperator = new String[]{"fldcw", "fstcw"};
  private static String[] singleOperator = new String[]{"nop", "finit", "fwait", "fchs", "fabs", "ftst", "lahf", "sahf", "fldz", "fld1", "fadd", "faddp", "fsub", "fsubp", "fsubr", "fsubrp", "fmul", "fmulp", "fdiv", "fdivp", "fdivr", "fdivrp", "fcompp"};
  private static String[] intUnaryOperator = new String[]{"neg", "not", "mul", "imul", "div", "idiv", "inc", "dec"};
  private static String[] shortJumpOperator = new String[]{"jz", "jnz", "jc", "jnc", "je", "jne", "jl", "jle", "jb", "jbe", "jg", "jge", "ja", "jae"};
  private static String[] longJumpOperator = new String[]{"jmp"};
  
  private static String[] ax = new String[]{"ax"};
  private static String[] cl = new String[]{"cl"};
  private static String[] register8low = new String[]{"al", "bl", "cl", "dl"};
  private static String[] register8high = new String[]{"ah", "bh", "ch", "dh"};
  private static String[] register8rex = new String[]{"r8b", "r9b", "r10b", "r11b", "r12b", "r13b", "r14b", "r15b"};
  private static String[] register8all = new String[]{"al", "bl", "cl", "dl", "ah", "bh", "ch", "dh",
                                                      "r8b", "r9b", "r10b", "r11b", "r12b", "r13b", "r14b", "r15b"};
  //private static String[] register16index = new String[]{"ax", "bx", "cx", "dx", "si", "di", "sp", "bp"};
  private static String[] register32index = new String[]{"eax", "ebx", "ecx", "edx", "esi", "edi", "esp", "ebp"};
  private static String[] register64index = new String[]{"rax", "rbx", "rcx", "rdx", "rsi", "rdi", "rsp", "rbp"};
  private static String[] register16value = new String[]{"ax", "bx", "cx", "dx",
                                                         "r8w", "r9w", "r10w", "r11w", "r12w", "r13w", "r14w", "r15w"};
  private static String[] register32value = new String[]{"eax", "ebx", "ecx", "edx",
                                                         "r8d", "r9d", "r10d", "r11d", "r12d", "r13d", "r14d", "r15d"};
  private static String[] register64value = new String[]{"rax", "rbx", "rcx", "rdx",
                                                         "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15"};
  private static String[] register16extra = new String[]{"ds", "es"};
  

//  private static String[] register32 = new String[]{"ebx", "esi", "edi", "ebp",
//                                                         "r8d", "r9d", "r10d", "r11d", "r12d", "r13d", "r14d", "r15d"};
  private static String[] registerfloat = new String[]{"st0", "st1", "st2", "st3", "st4", "st5", "st6", "st7"};

  private static String pathName = "C:\\Test\\x\\";
  private static String executeName = "C:\\Test\\";
  
  //private static PrintStream m_debugStream;
  private enum Status {Single, Register, Memory, Value, RegisterRegister, RegisterMemory, RegisterValue, MemoryRegister, MemoryValue, Target};
  
  private static final long Offset_Zero = 0L;
  private static final long Offset_Byte = 1L;
  private static final long Offset_Word = 256L;
  private static final long Offset_DWord = 65536L;
//  private static final long Offset_QWord = 4294967296L;
  
  private static final String Value_Zero = "0h";
  private static final String Value_Byte = "1h";
  private static final String Value_Word = "100h";
  private static final String Value_DWord = "10000h";
  private static final String Value_QWord = "100000000h";

  private static final int Shift_Byte = 7;
  private static final int Shift_Word = 15;
  private static final int Shift_DWord = 31;
  private static final int Shift_QWord = 63;
  
  private static int m_count = 0;
  
  public static void main(String[] args)
  {
    try {
      //m_debugStream = new PrintStream("C:\\Users\\Stefan\\Documents\\C_Compiler\\nasm\\Test\\Debug.txt");
      File directory = new File(pathName);
      
      if (directory.isDirectory()) {
        for (String fileName : directory.list()) {
          File file = new File(pathName + fileName);
          file.delete();
        }
      }

      PrintStream batStream = new PrintStream(executeName + "Execute.bat");

      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "word", Offset_DWord);
      write(batStream, Status.Memory, floatIntUnaryOperator, null, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "word", Offset_DWord);
      write(batStream, Status.Memory, floatIntUnaryOperator, register32index, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, floatIntUnaryOperator, register64index, null, "word", Offset_DWord);
      write(batStream, Status.Memory, floatIntUnaryOperator, register64index, null, "dword", Offset_DWord);

      write(batStream, Status.Memory, floatUnaryOperator, null, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, floatUnaryOperator, null, null, "qword", Offset_DWord);
      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, floatUnaryOperator, register32index, null, "qword", Offset_DWord);
      write(batStream, Status.Memory, floatUnaryOperator, register64index, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, floatUnaryOperator, register64index, null, "qword", Offset_DWord);

/*      write(batStream, Status.RegisterRegister, shiftOperator, register8low, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register8high, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register8rex, cl);*/
      write(batStream, Status.RegisterRegister, shiftOperator, register8all, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register16value, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register32value, cl);
      write(batStream, Status.RegisterRegister, shiftOperator, register64value, cl);
      write(batStream, Status.RegisterValue, shiftOperator, register8all, null, null, Offset_Byte);
      write(batStream, Status.RegisterValue, shiftOperator, register16value, null, null, Offset_Byte);
      write(batStream, Status.RegisterValue, shiftOperator, register32value, null, null, Offset_Byte);
      write(batStream, Status.RegisterValue, shiftOperator, register64value, null, null, Offset_Byte);
      
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8low, register8low);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8low, register8high);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8low, register8rex);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8high, register8low);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8high, register8high);
      //write(batStream, Status.RegisterRegister, intBinaryOperator, register8high, register8rex);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8rex, register8low);
      //write(batStream, Status.RegisterRegister, intBinaryOperator, register8rex, register8high);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register8rex, register8rex);
      //write(batStream, Status.RegisterRegister, intBinaryOperator, register16index, register16index);
      //write(batStream, Status.RegisterRegister, intBinaryOperator, register16index, register16value);
      //write(batStream, Status.RegisterRegister, intBinaryOperator, register16value, register16index);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register16value, register16value);
      write(batStream, Status.RegisterRegister, movOperator, register16value, register16extra);
      write(batStream, Status.RegisterRegister, movOperator, register16extra, register16value);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register32index, register32index);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register32index, register32value);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register32value, register32index);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register32value, register32value);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register64index, register64index);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register64index, register64value);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register64value, register64index);
      write(batStream, Status.RegisterRegister, intBinaryOperator, register64value, register64value);
      
      write(batStream, Status.RegisterValue, intBinaryOperator, register8all, null, null, 0, Value_Byte); // add ax, 10
      //write(batStream, Status.RegisterValue, intBinaryOperator, register16index, null, null, 0, Value_Word);
      write(batStream, Status.RegisterValue, intBinaryOperator, register16value, null, null, 0, Value_Word);
      write(batStream, Status.RegisterValue, intBinaryOperator, register32index, null, null, 0, Value_DWord);
      write(batStream, Status.RegisterValue, intBinaryOperator, register32value, null, null, 0, Value_DWord);
      write(batStream, Status.RegisterValue, intBinaryOperatorNoMov, register64index, null, null, 0, Value_DWord);
      write(batStream, Status.RegisterValue, intBinaryOperatorNoMov, register64value, null, null, 0, Value_DWord);
      write(batStream, Status.RegisterValue, movOperator, register64index, null, null, 0, Value_QWord);
      write(batStream, Status.RegisterValue, movOperator, register64value, null, null, 0, Value_QWord);

      write(batStream, Status.RegisterMemory, intBinaryOperator, register8all, null, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register16index, null, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16value, null, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32index, null, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32value, null, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64index, null, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64value, null, Offset_DWord);

      //write(batStream, Status.RegisterMemory, intBinaryOperator, register8value, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register8all, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register8all, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register16value, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16value, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register16value, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register16index, register16index, Offset_Word);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register16index, register32index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register16index, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register32value, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32value, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32value, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register32index, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32index, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register32index, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register64value, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64value, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64value, register64index, Offset_DWord);
      //write(batStream, Status.RegisterMemory, intBinaryOperator, register64index, register16index, Offset_Word);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64index, register32index, Offset_DWord);
      write(batStream, Status.RegisterMemory, intBinaryOperator, register64index, register64index, Offset_DWord);
      
      //write(batStream, Status.MemoryRegister, intBinaryOperator, null, register16index, Offset_Word);
      //write(batStream, Status.MemoryRegister, intBinaryOperator, null, register16index, Offset_DWord);
      write(batStream, Status.MemoryRegister, intBinaryOperator, null, register32index, Offset_DWord);
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register8value, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register16index, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register16value, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register32index, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register32value, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register64index, Offset_Word); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register16index, register64value, Offset_Word); // add [bp + 10], ax

      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register8all, Offset_DWord); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register16index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register16value, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register32index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register32value, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register64index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register32index, register64value, Offset_DWord); // add [bp + 10], ax

      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register8all, Offset_DWord); // add [bp + 10], ax
      //write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register16index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register16value, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register32index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register32value, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register64index, Offset_DWord); // add [bp + 10], ax
      write(batStream, Status.MemoryRegister, intBinaryOperator, register64index, register64value, Offset_DWord); // add [bp + 10], ax

      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "byte", Offset_DWord, Value_Byte); // add byte [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "word", Offset_DWord, Value_Word); // add word [100], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, null, null, "dword", Offset_DWord, Value_DWord); // add dword [100], 20

      //write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "byte", Offset_Word, Value_Byte); // add byte [bp + 10], 20
      //write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "word", Offset_Word, Value_Word); // add word [bp + 10], 20
      //write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "dword", Offset_Word, Value_DWord); // add dword [bp + 10], 20
      //write(batStream, Status.MemoryValue, intBinaryOperator, register16index, null, "qword", Offset_Word, Value_DWord); // add dword [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "byte", Offset_DWord, Value_Byte); // add byte [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "word", Offset_DWord, Value_Word); // add word [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "dword", Offset_DWord, Value_DWord); // add dword [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register32index, null, "qword", Offset_DWord, Value_DWord); // add dword [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register64index, null, "byte", Offset_DWord, Value_Byte); // add byte [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register64index, null, "word", Offset_DWord, Value_Word); // add word [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register64index, null, "dword", Offset_DWord, Value_DWord); // add dword [bp + 10], 20
      write(batStream, Status.MemoryValue, intBinaryOperator, register64index, null, "qword", Offset_DWord, Value_DWord); // add dword [bp + 10], 20
      
      write(batStream, Status.Memory, intUnaryOperator, null, null, "byte", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "word", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, null, null, "qword", Offset_DWord);
      //write(batStream, Status.Memory, intUnaryOperator, register16index, null, "byte", Offset_Byte);
      //write(batStream, Status.Memory, intUnaryOperator, register16index, null, "word", Offset_Word);
      //write(batStream, Status.Memory, intUnaryOperator, register16index, null, "dword", Offset_DWord);
      //write(batStream, Status.Memory, intUnaryOperator, register16index, null, "qword", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "byte", Offset_Byte);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, register32index, null, "qword", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, register64index, null, "byte", Offset_Byte);
      write(batStream, Status.Memory, intUnaryOperator, register64index, null, "word", Offset_Word);
      write(batStream, Status.Memory, intUnaryOperator, register64index, null, "dword", Offset_DWord);
      write(batStream, Status.Memory, intUnaryOperator, register64index, null, "qword", Offset_DWord);

      write(batStream, Status.Register, intUnaryOperator, register8all, null);
      //write(batStream, Status.Register, intUnaryOperator, register16index, null);
      write(batStream, Status.Register, intUnaryOperator, register16value, null);
      write(batStream, Status.Register, intUnaryOperator, register32index, null);
      write(batStream, Status.Register, intUnaryOperator, register32value, null);
      write(batStream, Status.Register, intUnaryOperator, register32index, null);
      write(batStream, Status.Register, intUnaryOperator, register32value, null);

      write(batStream, Status.Register, fstswOperator, ax, null, null, 0);
      //write(batStream, Status.Memory, wordOperator, register16index, null, null, Offset_Word);
      write(batStream, Status.Memory, wordOperator, register32index, null, null, Offset_DWord);
      write(batStream, Status.Memory, wordOperator, register64index, null, null, Offset_DWord);
      write(batStream, Status.Value, intOperator, null, null, null, 0, Value_Byte);      
      write(batStream, Status.Single, singleOperator, null, null);
      
      write(batStream, Status.Target, shortJumpOperator, null, null, null, 0, Value_Byte);
      write(batStream, Status.Target, longJumpOperator, null, null, null, 0, Value_DWord);
      write(batStream, Status.Target, longJumpOperator, register64index, null);
      write(batStream, Status.Target, longJumpOperator, register64value, null);

      batStream.println("@pause");
      batStream.close();
      //m_debugStream.close();      

      System.out.println("Generated " + m_count + " files.");
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues) throws Exception {
    write(batStream, status, opValues, leftValues, rightValues, null, 0, Value_Zero);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, String text) throws Exception {
    write(batStream, status, opValues, leftValues, rightValues, text, 0, Value_Zero);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, long offset) throws Exception {
    write(batStream, status, opValues, leftValues, rightValues, null, offset, Value_Zero);
  }
  
  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues, String text, long offset) throws Exception
  {
    write(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
  }

  private static void write(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                            String text, long offset, String value) throws Exception
  {
    if (offset == Offset_Zero) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
    }
    else if (offset == Offset_Byte) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "1h", value);
    }
    else if (offset == Offset_Word) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "1h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "100h", value);
    }
    else if (offset == Offset_DWord) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "1h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "100h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "10000h", value);
    }
    else if (offset == Offset_DWord) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "1h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "100h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "10000h", value);
    }
/*    else if (offset == Offset_QWord) {
      write2(batStream, status, opValues, leftValues, rightValues, text, "0h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "1h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "100h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "10000h", value);
      write2(batStream, status, opValues, leftValues, rightValues, text, "100000000h", value);
    }*/
  }
  
  private static void write2(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                             String text, String offset, String value) throws Exception
  {
    if (value.equals(Value_Zero)) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
    }
    else if (value.equals(Value_Byte)) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
    }
    else if (value.equals(Value_Word)) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Word);
    }
    else if (value.equals(Value_DWord)) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Word);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_DWord);
    }
    else if (value == Value_QWord) {
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Zero);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Byte);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_Word);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_DWord);
      write3(batStream, status, opValues, leftValues, rightValues, text, offset, Value_QWord);
    }
  }
  
  private static void write3(PrintStream batStream, Status status, String[] opValues, String[] leftValues, String[] rightValues,
                             String text, String offset, String value) throws Exception
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

  private static int shiftValue(String value) {
    if (value.equals(Value_Byte)) {
      return 7;
    }
    else if (value.equals(Value_Word)) {
      return 15;
    }
    else if (value.equals(Value_DWord)) {
      return 31;
    }
    else {
      return 63;
    }
  }

  private static void add(PrintStream batStream, Status status, String op, String left, String right,
                          String text, String offset, String value) throws Exception
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
      //System.out.println(file);
    }
    
    PrintStream asmStream = new PrintStream(file);
    asmStream.println("          bits 64");
    
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
    String nasmPath = "C:\\Users\\Stefan\\Documents\\A A C_Compiler_Assembler - A 64 bits\\nasm-2.13.03\\nasm";
    
    if (((++m_batCount) % 100) == 0) {
      System.out.println(m_batCount);
      batStream.println("@echo " + m_batCount);
    }
    
    batStream.println("@\"" + nasmPath + "\" \"" + pathName + fileName + ".asm\" -o \"" + pathName + fileName + ".com\"");
  }
}
