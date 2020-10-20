//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 30 "gramatica.y"
package AnalizadorSintactico;
import AnalizadorLexico.*;
import AnalizadorLexico.Error;
import java.util.ArrayList;
import CodigoIntermedio.*;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short MULTI_LINEA=258;
public final static short CTEDOUBLE=259;
public final static short S_ASIGNACION=260;
public final static short S_MAYOR_IGUAL=261;
public final static short S_MENOR_IGUAL=262;
public final static short CTEENTERA=263;
public final static short S_PORCENTAJE=264;
public final static short COMENTARIO=265;
public final static short S_DISTINTO=266;
public final static short S_IGUAL=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short ENDIF=270;
public final static short UNTIL=271;
public final static short DO=272;
public final static short PRINT=273;
public final static short BEGIN=274;
public final static short END=275;
public final static short INT=276;
public final static short DOUBLE=277;
public final static short first=278;
public final static short last=279;
public final static short length=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    1,    3,    3,    3,    3,    3,    5,    5,    5,    5,
    6,    7,    7,    7,    2,    2,    8,    8,    8,    8,
    8,   13,   13,   12,   12,   12,   12,   12,   12,   14,
   14,   11,   11,   11,   11,   11,   11,   16,   16,   16,
   18,   18,   18,   19,   19,   19,   19,   19,   19,   19,
   20,   20,   20,   20,   20,   20,   20,   21,   22,   10,
   10,   10,   10,   10,   23,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,   15,   15,   15,   24,
   24,   24,   25,   25,   25,   25,   25,   25,   26,   26,
   27,   27,   27,    4,    4,   17,   17,   17,   17,   17,
   17,
};
final static short yylen[] = {                            2,
    5,    1,    4,    4,    4,    3,    2,    3,    1,    2,
    1,    3,    2,    2,    3,    2,    3,    3,    1,    1,
    1,    4,    4,    4,    2,    1,    1,    1,    1,    1,
    2,    1,    4,    4,    3,    4,    4,    4,    3,    1,
    1,    5,    5,    5,    4,    4,    5,    3,    3,    3,
    3,    2,    3,    4,    1,    2,    5,    5,    3,    2,
    4,    1,    2,    5,    5,    3,    2,    2,    0,    7,
    7,    4,    4,    5,    1,    5,    4,    8,    7,    6,
    6,    6,    9,    9,    8,    5,    1,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    2,    2,    3,    3,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   75,    0,    0,  104,  105,    0,    0,
    0,   11,    0,   26,   27,   28,   29,   30,    0,    0,
    0,    0,    0,   94,    0,   93,   13,    0,    0,   16,
   31,    0,    0,    0,    0,    0,   90,   96,    0,    0,
    0,   95,   68,    0,    0,    0,    0,    0,   10,   25,
    0,    0,    0,   20,   19,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  108,  109,  111,  110,  106,
  107,    0,    0,    0,    0,    0,   98,   97,    0,    0,
    0,   52,    0,    0,    0,   40,   41,    0,    0,    0,
    0,    0,    0,    0,    0,   15,    0,    0,   12,    0,
    0,    0,    0,    0,    0,    0,    0,   69,    0,    0,
    0,    0,    0,    0,    0,    0,  100,    0,    0,    0,
   99,   36,    0,   53,   51,    0,    0,   50,    0,    0,
    0,   91,   92,   33,    0,    0,    0,    4,    3,    0,
    0,    0,    0,    0,   17,   18,   37,   34,   38,    0,
    0,   59,    0,   73,   72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,    0,    0,    0,    0,
   74,   44,   42,   43,    1,   23,   24,   22,    0,    0,
    0,    0,    0,    0,    0,    0,   86,   76,  101,  102,
  103,    0,    0,    0,    0,    0,    0,   57,    0,   81,
    0,    0,    0,   80,   82,    0,    0,   66,   71,   70,
    0,    0,    0,    0,    0,    0,   85,   78,    0,    0,
    0,   64,   84,   83,
};
final static short yydgoto[] = {                          9,
   10,   59,   30,   13,   53,   54,   55,   14,   15,   16,
   17,   18,   19,   88,   33,   34,   72,   35,   61,  170,
   20,  153,   21,   36,   37,   38,  121,
};
final static short yysindex[] = {                      -195,
   -9,  -62,   -1,    0,   43,  565,    0,    0,    0, -181,
  565,    0, -156,    0,    0,    0,    0,    0,  -52,  536,
  543,  -38,  -34,    0,  -26,    0,    0,  150, -179,    0,
    0,  -52,  418,   44,  536,   -4,    0,    0, -240,  230,
  290,    0,    0, -100,  246,  -59,  -24,  584,    0,    0,
  -45,  -54,  -29,    0,    0,  304,  -26,  565, -209,    0,
 -124,  196,  565, -151, -173,    0,    0,    0,    0,    0,
    0,  -26, -224,  -27,   62,  -41,    0,    0,  -26,  -26,
  332,    0, -121,  -26,  -26,    0,    0,   17,   79,   87,
   94,  269,   81,  226,  298,    0, -111, -222,    0,  -93,
   95,   59,   70,  398,  565,  100,    0,    0,  -49,  124,
  453,  565,  135,  -90,   13,   -3,    0,  156,  159,  160,
    0,    0,  142,    0,    0,   -4,   -4,    0,   -3,  556,
  143,    0,    0,    0,  144,  145,  153,    0,    0,  158,
  149,  151,  152,  129,    0,    0,    0,    0,    0,  184,
  489,    0,  556,    0,    0,   -1,  187,  509,   -1,   -1,
  -56,  190,  219,  224,  232,    0,  565,  312,    0,    4,
    0,    0,    0,    0,    0,    0,    0,    0,  565,  221,
   33,  245,  244,  248,  249,  253,    0,    0,    0,    0,
    0,  522,  565,  254,    0,  266,  565,    0,  267,    0,
  122,  563,   63,    0,    0,  271,  530,    0,    0,    0,
  376,  277,   -1,   -1,  565,  291,    0,    0,  292,  295,
  565,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -33,    0,    0,    0,    0,    0,    0,    0,  380,
  382,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   27,    0,    0,    0,    0,    0,    0,    0,
    0,  415,    0,    0,    0,   49,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  386,    0,    0,    0,    0,
    0,    1,    5,    0,    0,    0,    0,    0,    0, -131,
    0,    0,    0,    0,  337,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  405,    0,  410,    0,    0,    0,    0,    0,
    0,   99,  119, -104,    0,    0,  -96,    0,    0,    0,
    0,    0,    0,  378,    0,  -30,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   71,   91,    0,  -16,    0,
    0,    0,    0,    0,    0,  127,  147,    0,    0,  414,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  155,    0,    0,    0,    0,    0,    0,  -88,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -99,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  163,    0,    0,  347,    0,  -81,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  175,    0,    0,  164,    0,    0,    0,    0,    0,
  167,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  544,   97,    0,    0,  339,  340,  537,    0,    0,
    0,    0,  691,    0,   -5,  -15,  408,  501,  407,  301,
    0,    0,    0,  111,  108,    0,    0,
};
final static int YYTABLESIZE=905;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        125,
   21,   28,  187,   70,   14,   71,   29,   32,   57,  155,
   49,   73,   76,   96,  100,   79,   86,   80,   29,   74,
   27,   70,   87,   71,   48,   76,   32,   32,   39,   99,
   28,  122,  117,  142,   27,   29,   98,   84,   41,   79,
  143,   80,   85,   29,   21,   97,   45,    2,   87,   27,
  102,  103,   41,  118,  119,  120,   39,   29,    3,   21,
    1,    2,    4,    5,  105,  106,  116,   32,   32,   32,
   88,   32,    3,   32,   47,  129,    4,    5,    6,   77,
    7,    8,   44,   78,   82,   32,   32,   32,   32,   87,
   89,   87,   48,   87,    7,    8,   12,  115,   35,   51,
   52,   79,  123,   80,   45,    2,   49,   87,   87,  134,
   87,   88,   79,   88,   80,   88,    3,  148,   39,  135,
    4,    5,  112,  113,   26,   26,   45,  136,  149,   88,
   88,   89,   88,   89,  137,   89,   26,   55,   55,  138,
   26,   26,   26,   26,  108,  109,   46,  130,  131,   89,
   89,  141,   89,  147,   77,   89,    2,   90,  152,   25,
   25,   41,  144,   52,   60,   60,   29,   26,   26,   54,
   54,   25,   56,   56,   79,   25,   25,   25,   25,   26,
  160,   62,  156,   26,   26,   26,   26,   58,   58,  126,
  127,  132,  133,  159,   29,  163,   45,    2,  164,  165,
  166,  171,  172,  173,   66,   67,  154,   56,    3,   68,
   69,  174,    4,    5,  124,   93,  175,   22,   23,   97,
   24,   25,   66,   67,   26,   49,   32,   68,   69,    3,
   23,   47,   24,    4,    5,   28,   26,    7,    8,   48,
   29,  176,  179,  177,  178,  183,   22,   23,  188,   24,
   25,    7,    8,   26,   40,   23,   21,   24,    3,  189,
   14,   26,    4,    5,  190,   28,    7,    8,  161,   23,
   29,   24,  191,  196,   21,   26,   21,   21,   14,  198,
   14,   14,   32,   32,  139,   28,   32,   32,   32,   70,
   29,   71,   32,   32,   32,   32,   32,   32,   32,   32,
   32,   32,  199,  200,   87,   87,  203,  204,   28,   87,
   87,  205,  208,   29,   87,   87,   87,   87,   87,   87,
   87,   87,   87,   87,  209,  210,   88,   88,   70,  215,
   71,   88,   88,  214,   29,  218,   88,   88,   88,   88,
   88,   88,   88,   88,   88,   88,   89,   89,   29,  222,
  223,   89,   89,  224,   35,   35,   89,   89,   89,   89,
   89,   89,   89,   89,   89,   89,   35,   35,   35,   35,
   35,   35,   35,   35,   39,   39,   29,  211,   23,    2,
   24,    9,   45,   45,   26,    7,   39,   39,   39,   39,
   39,   39,   39,   39,   45,   45,   45,   45,   45,   45,
   45,   45,   46,   46,    6,   40,   23,   75,   24,    8,
   77,   77,   26,    5,   46,   46,   46,   46,   46,   46,
   46,   46,   77,   77,   77,   77,   77,   77,   77,   77,
   79,   79,   67,   61,  217,   70,   65,   71,  145,  146,
   81,   83,   79,   79,   79,   79,   79,   79,   79,   79,
    0,   92,   23,  181,   24,   25,   95,   95,   26,   95,
   79,   95,   80,    3,    0,    0,    0,    4,    5,    0,
  110,    0,    0,    0,   95,    0,   95,   70,    0,   71,
    0,   92,   23,    0,   24,   25,    0,    0,   26,    0,
   66,   67,    0,    3,    0,   68,   69,    4,    5,   45,
    2,   92,   23,   43,   24,   25,    0,    0,   26,    0,
    0,    3,    0,    3,  201,    4,    5,    4,    5,    0,
    0,    0,    0,    0,   92,   23,    0,   24,   25,   66,
   67,   26,    0,    0,   68,   69,    3,   31,    0,    0,
    4,    5,    0,   11,    0,   40,   23,   50,   24,   46,
    0,    0,   26,   45,    2,    0,   60,   65,   31,  101,
   23,    0,   24,    0,   64,    3,   26,   45,    2,    4,
    5,   60,  140,    0,    0,    0,    0,    0,    0,    3,
    0,   31,   50,    4,    5,  193,  194,  128,   23,    0,
   24,   95,   26,   26,   26,  107,    0,    0,   31,    0,
  114,  104,   25,   25,   26,    0,  111,    0,   26,   26,
   26,   26,    0,    0,   25,  162,   63,    0,   25,   25,
   25,   25,    0,    0,    0,    0,    0,    0,   31,    0,
   31,   50,    0,   25,   25,    0,   66,   67,    0,    0,
   50,   68,   69,    0,    0,   25,    0,   50,  151,   25,
   25,   25,   25,   45,    2,  158,  182,    0,    0,  185,
  186,    0,    0,    0,    0,    3,  169,    0,    0,    4,
    5,    0,  150,  168,    0,   95,   95,    0,   66,   67,
   95,   95,    0,   68,   69,    0,    0,   50,    0,  169,
    0,   32,    0,   42,   50,    0,  168,    0,    0,    0,
    0,  212,    0,    0,  195,    0,    0,    0,   45,    2,
  192,    0,   32,  219,  220,   42,    0,    0,   42,    0,
    3,    0,  197,    0,    4,    5,  202,  157,   50,    0,
    0,   42,    0,   50,   91,   32,  207,    0,   50,    0,
    0,    0,    0,   50,   45,    2,   42,   42,    0,    0,
    0,    0,   32,    0,    0,    0,    3,   50,  221,    0,
    4,    5,   42,  180,   45,    2,    0,    0,    0,   42,
   42,   42,    0,    0,   42,   42,    3,   45,    2,    0,
    4,    5,   32,  184,   32,   45,    2,    0,    0,    3,
    0,   45,    2,    4,    5,    0,  206,    3,   62,    2,
    0,    4,    5,    3,  216,   42,    0,    4,    5,   58,
    3,   45,    2,    0,    4,    5,   63,    0,   45,    2,
   45,    2,    0,    3,    0,    0,    0,    4,    5,  167,
    3,    0,    3,  213,    4,    5,    4,    5,    0,   94,
    2,    0,    0,    0,    0,    0,   42,    0,    0,   42,
   42,    3,    0,    0,    0,    4,    5,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   42,   42,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   59,   60,    0,   62,   45,   41,   61,   59,
   41,   46,   28,   59,   44,   43,  257,   45,   45,   25,
   59,   60,  263,   62,   41,   41,    0,   61,   91,   59,
   40,   59,  257,  256,   59,   45,   91,   42,   40,   43,
  263,   45,   47,   45,   44,   91,  256,  257,    0,   59,
   56,   57,   40,  278,  279,  280,   91,   45,  268,   59,
  256,  257,  272,  273,  274,  275,   72,   41,   42,   43,
    0,   45,  268,   47,  256,   81,  272,  273,  274,  259,
  276,  277,   40,  263,   41,   59,   60,   61,   62,   41,
    0,   43,  274,   45,  276,  277,    0,  271,    0,  256,
  257,   43,   41,   45,  256,  257,   10,   59,   60,   93,
   62,   41,   43,   43,   45,   45,  268,   59,    0,   41,
  272,  273,  274,  275,  256,  257,    0,   41,   59,   59,
   60,   41,   62,   43,   41,   45,  268,  269,  270,   59,
  272,  273,  274,  275,  269,  270,    0,  269,  270,   59,
   60,  263,   62,   59,    0,  256,  257,  258,   59,  256,
  257,   40,  256,  257,  269,  270,   45,  256,  257,  269,
  270,  268,  269,  270,    0,  272,  273,  274,  275,  268,
  271,  270,   59,  272,  273,  274,  275,  269,  270,   79,
   80,   84,   85,   59,   45,   40,  256,  257,   40,   40,
   59,   59,   59,   59,  261,  262,  256,  260,  268,  266,
  267,   59,  272,  273,  256,  275,   59,  256,  257,   91,
  259,  260,  261,  262,  263,  256,  260,  266,  267,  268,
  257,  256,  259,  272,  273,   40,  263,  276,  277,  256,
   45,   93,   59,   93,   93,   59,  256,  257,   59,  259,
  260,  276,  277,  263,  256,  257,  256,  259,  268,   41,
  256,  263,  272,  273,   41,   40,  276,  277,  256,  257,
   45,  259,   41,  270,  274,  263,  276,  277,  274,   59,
  276,  277,  256,  257,   59,   40,  260,  261,  262,   60,
   45,   62,  266,  267,  268,  269,  270,  271,  272,  273,
  274,  275,  270,   59,  256,  257,   59,   59,   40,  261,
  262,   59,   59,   45,  266,  267,  268,  269,  270,  271,
  272,  273,  274,  275,   59,   59,  256,  257,   60,   59,
   62,  261,  262,  271,   45,   59,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  256,  257,   45,   59,
   59,  261,  262,   59,  256,  257,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  275,  268,  269,  270,  271,
  272,  273,  274,  275,  256,  257,   45,  256,  257,    0,
  259,    0,  256,  257,  263,    0,  268,  269,  270,  271,
  272,  273,  274,  275,  268,  269,  270,  271,  272,  273,
  274,  275,  256,  257,    0,  256,  257,  258,  259,    0,
  256,  257,  263,    0,  268,  269,  270,  271,  272,  273,
  274,  275,  268,  269,  270,  271,  272,  273,  274,  275,
  256,  257,  270,  270,   59,   60,  270,   62,  100,  100,
   33,   35,  268,  269,  270,  271,  272,  273,  274,  275,
   -1,  256,  257,  153,  259,  260,   42,   43,  263,   45,
   43,   47,   45,  268,   -1,   -1,   -1,  272,  273,   -1,
  275,   -1,   -1,   -1,   60,   -1,   62,   60,   -1,   62,
   -1,  256,  257,   -1,  259,  260,   -1,   -1,  263,   -1,
  261,  262,   -1,  268,   -1,  266,  267,  272,  273,  256,
  257,  256,  257,    3,  259,  260,   -1,   -1,  263,   -1,
   -1,  268,   -1,  268,  271,  272,  273,  272,  273,   -1,
   -1,   -1,   -1,   -1,  256,  257,   -1,  259,  260,  261,
  262,  263,   -1,   -1,  266,  267,  268,    1,   -1,   -1,
  272,  273,   -1,    0,   -1,  256,  257,   11,  259,    6,
   -1,   -1,  263,  256,  257,   -1,   20,   21,   22,  256,
  257,   -1,  259,   -1,   21,  268,  263,  256,  257,  272,
  273,   35,  275,   -1,   -1,   -1,   -1,   -1,   -1,  268,
   -1,   45,   46,  272,  273,  274,  275,  256,  257,   -1,
  259,   48,  256,  257,  263,   59,   -1,   -1,   62,   -1,
   64,   58,  256,  257,  268,   -1,   63,   -1,  272,  273,
  274,  275,   -1,   -1,  268,  115,  270,   -1,  272,  273,
  274,  275,   -1,   -1,   -1,   -1,   -1,   -1,   92,   -1,
   94,   95,   -1,  256,  257,   -1,  261,  262,   -1,   -1,
  104,  266,  267,   -1,   -1,  268,   -1,  111,  105,  272,
  273,  274,  275,  256,  257,  112,  156,   -1,   -1,  159,
  160,   -1,   -1,   -1,   -1,  268,  130,   -1,   -1,  272,
  273,   -1,  275,  130,   -1,  261,  262,   -1,  261,  262,
  266,  267,   -1,  266,  267,   -1,   -1,  151,   -1,  153,
   -1,    1,   -1,    3,  158,   -1,  153,   -1,   -1,   -1,
   -1,  201,   -1,   -1,  168,   -1,   -1,   -1,  256,  257,
  167,   -1,   22,  213,  214,   25,   -1,   -1,   28,   -1,
  268,   -1,  179,   -1,  272,  273,  183,  275,  192,   -1,
   -1,   41,   -1,  197,   44,   45,  193,   -1,  202,   -1,
   -1,   -1,   -1,  207,  256,  257,   56,   57,   -1,   -1,
   -1,   -1,   62,   -1,   -1,   -1,  268,  221,  215,   -1,
  272,  273,   72,  275,  256,  257,   -1,   -1,   -1,   79,
   80,   81,   -1,   -1,   84,   85,  268,  256,  257,   -1,
  272,  273,   92,  275,   94,  256,  257,   -1,   -1,  268,
   -1,  256,  257,  272,  273,   -1,  275,  268,  256,  257,
   -1,  272,  273,  268,  275,  115,   -1,  272,  273,  274,
  268,  256,  257,   -1,  272,  273,  274,   -1,  256,  257,
  256,  257,   -1,  268,   -1,   -1,   -1,  272,  273,  274,
  268,   -1,  268,  271,  272,  273,  272,  273,   -1,  256,
  257,   -1,   -1,   -1,   -1,   -1,  156,   -1,   -1,  159,
  160,  268,   -1,   -1,   -1,  272,  273,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  201,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  213,  214,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","MULTI_LINEA","CTEDOUBLE",
"S_ASIGNACION","S_MAYOR_IGUAL","S_MENOR_IGUAL","CTEENTERA","S_PORCENTAJE",
"COMENTARIO","S_DISTINTO","S_IGUAL","IF","ELSE","ENDIF","UNTIL","DO","PRINT",
"BEGIN","END","INT","DOUBLE","first","last","length",
};
final static String yyrule[] = {
"$accept : programa",
"programa : declaraciones BEGIN bloques_de_sentencias END ';'",
"programa : declaraciones",
"programa : declaraciones BEGIN error ';'",
"programa : BEGIN bloques_de_sentencias END ';'",
"programa : declaraciones BEGIN bloques_de_sentencias END",
"programa : BEGIN bloques_de_sentencias END",
"programa : BEGIN bloques_de_sentencias",
"programa : declaraciones BEGIN bloques_de_sentencias",
"programa : bloques_de_sentencias",
"declaraciones : declaraciones sentencia_declarativa",
"declaraciones : sentencia_declarativa",
"sentencia_declarativa : tipo variables ';'",
"sentencia_declarativa : error ';'",
"sentencia_declarativa : tipo variables",
"sentencia_declarativa : tipo error ';'",
"sentencia_declarativa : error sentencia_declarativa",
"variables : variables ',' lista_variables",
"variables : variables ',' coleccion",
"variables : coleccion",
"variables : lista_variables",
"lista_variables : ID",
"coleccion : ID '[' CTEENTERA ']'",
"coleccion : error '[' CTEENTERA ']'",
"coleccion : ID '[' error ']'",
"bloques_de_sentencias : bloques_de_sentencias sentencia",
"bloques_de_sentencias : sentencia",
"sentencia : sentencia_control",
"sentencia : sentencia_seleccion",
"sentencia : print",
"sentencia : asignacion",
"sentencia : error sentencia",
"lado_izquierdo : ID",
"lado_izquierdo : ID '[' subindice ']'",
"asignacion : lado_izquierdo S_ASIGNACION expresion ';'",
"asignacion : lado_izquierdo S_ASIGNACION expresion",
"asignacion : error S_ASIGNACION expresion ';'",
"asignacion : lado_izquierdo S_ASIGNACION error ';'",
"asignacion : lado_izquierdo '=' expresion ';'",
"asignacion : lado_izquierdo '=' expresion",
"subindice : ID",
"subindice : CTEENTERA",
"print : PRINT '(' MULTI_LINEA ')' ';'",
"print : PRINT '(' lado_izquierdo ')' ';'",
"print : PRINT '(' error ')' ';'",
"print : PRINT '(' MULTI_LINEA ')'",
"print : PRINT '(' lado_izquierdo ')'",
"print : error '(' MULTI_LINEA ')' ';'",
"condicion_sin_parentesis : expresion operador expresion",
"condicion_sin_parentesis : error operador expresion",
"condicion_sin_parentesis : expresion operador error",
"condicion : '(' condicion_sin_parentesis ')'",
"condicion : condicion_sin_parentesis ')'",
"condicion : '(' condicion_sin_parentesis error",
"cuerpo_if : BEGIN bloques_de_sentencias END ';'",
"cuerpo_if : sentencia",
"cuerpo_if : bloques_de_sentencias sentencia",
"cuerpo_if : bloques_de_sentencias BEGIN bloques_de_sentencias END ';'",
"cuerpo_if : BEGIN bloques_de_sentencias END ';' bloques_de_sentencias",
"cuerpo_if : bloques_de_sentencias END ';'",
"cuerpo_if : BEGIN bloques_de_sentencias",
"cuerpo_else : BEGIN bloques_de_sentencias END ';'",
"cuerpo_else : sentencia",
"cuerpo_else : bloques_de_sentencias sentencia",
"cuerpo_else : bloques_de_sentencias BEGIN bloques_de_sentencias END ';'",
"cuerpo_else : BEGIN bloques_de_sentencias END ';' bloques_de_sentencias",
"cuerpo_else : bloques_de_sentencias END ';'",
"cuerpo_else : BEGIN bloques_de_sentencias",
"sentecia_if_condicion : IF condicion",
"$$1 :",
"sentencia_seleccion : sentecia_if_condicion cuerpo_if ELSE $$1 cuerpo_else ENDIF ';'",
"sentencia_seleccion : error condicion cuerpo_if ELSE cuerpo_else ENDIF ';'",
"sentencia_seleccion : sentecia_if_condicion cuerpo_if ENDIF ';'",
"sentencia_seleccion : sentecia_if_condicion cuerpo_if ENDIF error",
"sentencia_seleccion : error condicion cuerpo_if ENDIF ';'",
"inicio_do : DO",
"sentencia_control : inicio_do sentencia UNTIL condicion ';'",
"sentencia_control : inicio_do sentencia UNTIL condicion",
"sentencia_control : inicio_do BEGIN bloques_de_sentencias END ';' UNTIL condicion ';'",
"sentencia_control : inicio_do BEGIN bloques_de_sentencias END ';' UNTIL condicion",
"sentencia_control : inicio_do bloques_de_sentencias END ';' condicion ';'",
"sentencia_control : inicio_do error END ';' condicion ';'",
"sentencia_control : inicio_do bloques_de_sentencias sentencia UNTIL condicion ';'",
"sentencia_control : inicio_do bloques_de_sentencias BEGIN bloques_de_sentencias END ';' UNTIL condicion ';'",
"sentencia_control : inicio_do BEGIN bloques_de_sentencias END ';' bloques_de_sentencias UNTIL condicion ';'",
"sentencia_control : inicio_do BEGIN bloques_de_sentencias END ';' UNTIL error ';'",
"sentencia_control : inicio_do sentencia UNTIL error ';'",
"expresion : termino",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : CTEENTERA",
"factor : CTEDOUBLE",
"factor : lado_izquierdo",
"factor : metodos",
"factor : '-' CTEENTERA",
"factor : '-' CTEDOUBLE",
"metodos : ID '.' nombre_metodo",
"metodos : ID '.' ID",
"nombre_metodo : first '(' ')'",
"nombre_metodo : last '(' ')'",
"nombre_metodo : length '(' ')'",
"tipo : INT",
"tipo : DOUBLE",
"operador : '<'",
"operador : '>'",
"operador : S_MAYOR_IGUAL",
"operador : S_MENOR_IGUAL",
"operador : S_IGUAL",
"operador : S_DISTINTO",
};

//#line 492 "gramatica.y"

AnalizadorLexico analizadorL;
AnalizadorSintactico analizadorS;
TablaSimbolos tablaSimbolo;
ControladorArchivo controladorArchivo;
ControladorTercetos controladorTercetos;
AnalizadorCodigoIntermedio analizadorCI;

public void setLexico(AnalizadorLexico al) {
       analizadorL = al;
}

public void setSintactico (AnalizadorSintactico as){
	analizadorS = as;
}

public void setTS (TablaSimbolos ts){
	tablaSimbolo = ts;
}

public void setControladorArchivo ( ControladorArchivo ca){
	controladorArchivo = ca;
}

public void setCodigoIntermedio(AnalizadorCodigoIntermedio aci){
	analizadorCI = aci;
}

public void setControladorTercetos ( ControladorTercetos ct){
	controladorTercetos = ct;
}

int yylex()
{
	int val = analizadorL.yylex();
	yylval = new ParserVal((Token)tablaSimbolo.get(analizadorL.getYYlex()));
    return val;
}

public boolean tipoCompatible(Token t1, Token t2){
	String tipo1 = t1.getTipo();
	String tipo2 = t2.getTipo();
	return (tipo1 == tipo2);
}

public boolean es_cte_entera(Token aux){
	return CTEENTERA == (int)aux.getAtributo("Nro_Token");
}	

public boolean es_cte_double(Token aux){
	return CTEDOUBLE == (int)aux.getAtributo("Nro_Token");
}

void yyerror(String s) {
	if(s.contains("under"))
		System.out.println("par:"+s);
}


//#line 624 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 38 "gramatica.y"
{analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 2:
//#line 39 "gramatica.y"
{analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 3:
//#line 40 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
break;
case 4:
//#line 41 "gramatica.y"
{analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraPRINCIPAL,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea() ));}
break;
case 5:
//#line 42 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 6:
//#line 43 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 7:
//#line 44 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 8:
//#line 45 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 9:
//#line 46 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea())); }
break;
case 12:
//#line 53 "gramatica.y"
{ String tipo = ((Token) val_peek(2).obj).getValor();
										analizadorCI.declaracionParametros(tipo, (ArrayList<Token>)val_peek(1).obj,tablaSimbolo,controladorArchivo);
						analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraDECLARACION,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  ));}
break;
case 13:
//#line 56 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 14:
//#line 57 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO",    controladorArchivo.getLinea()-1));}
break;
case 15:
//#line 58 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 16:
//#line 59 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorDeclaracionVar,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 17:
//#line 62 "gramatica.y"
{ArrayList<Token> t1 =(ArrayList<Token>)val_peek(2).obj;
						ArrayList<Token> t2 =(ArrayList<Token>)val_peek(0).obj;
						t1.addAll(t2);
						yyval = new ParserVal(t1);}
break;
case 18:
//#line 66 "gramatica.y"
{ArrayList<Token> t1 =(ArrayList<Token>)val_peek(2).obj;
						ArrayList<Token> t2 =(ArrayList<Token>)val_peek(0).obj;
						t1.addAll(t2);
						yyval = new ParserVal(t1);}
break;
case 19:
//#line 70 "gramatica.y"
{yyval = new ParserVal((ArrayList<Token>)val_peek(0).obj);}
break;
case 20:
//#line 71 "gramatica.y"
{yyval = new ParserVal((ArrayList<Token>)val_peek(0).obj);}
break;
case 21:
//#line 74 "gramatica.y"
{	ArrayList<Token> lista = new ArrayList<>();
					Token t = (Token)val_peek(0).obj;
					if (!t.existeAtributo("Uso")){
						t.AgregarAtributo("Uso",AnalizadorLexico.variable);
					}
                			lista.add(t);
                			yyval = new ParserVal(lista); }
break;
case 22:
//#line 84 "gramatica.y"
{	ArrayList<Token> lista = new ArrayList<>();
					Token t = (Token)val_peek(3).obj;
					if (!t.existeAtributo("Uso")){
						t.AgregarAtributo("Uso",AnalizadorLexico.coleccion);
						t.AgregarAtributo("Length",((Token)val_peek(1).obj).getAtributo("Valor"));
					}
                			lista.add(t);
                			yyval = new ParserVal(lista); }
break;
case 23:
//#line 92 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorDeclaracionColeccion,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 24:
//#line 93 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorSubIndice,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 31:
//#line 104 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorSentencias,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 32:
//#line 107 "gramatica.y"
{	/*chequeo semantico variable no declarada*/
					Token t1 = (Token)val_peek(0).obj;						
		    			if  ( !t1.existeAtributo("Uso") ) 
							analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));

						yyval = new ParserVal( t1 );}
break;
case 33:
//#line 113 "gramatica.y"
{	Token t1 = (Token)val_peek(3).obj;						
		    		if  ( !t1.existeAtributo("Uso") ) {
					analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
					yyval = new ParserVal(t1);
				}else{
				String tipo = (String)((Token)val_peek(3).obj).getAtributo("Tipo_Uso");
				TercetoColeccionIndice terceto = new TercetoColeccionIndice ( new Token(ControladorTercetos.CGI), (Token)val_peek(3).obj ,  (Token)val_peek(1).obj , controladorTercetos.getProxNumero() );
				controladorTercetos.addTerceto (terceto);
				Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
				Token aux = new Token( controladorTercetos.numeroTercetoString() ) ;
				nuevo.AgregarAtributo("Tipo_Uso",tipo);
				nuevo.AgregarAtributo("Uso",AnalizadorLexico.variable);
				nuevo.AgregarAtributo("Direccion",true);
				aux.AgregarAtributo("Direccion",true);
				aux.AgregarAtributo("Tipo_Uso",tipo);
				aux.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
				tablaSimbolo.add("@aux"+aux.getValor(),aux);
				yyval = new ParserVal( nuevo );}
				}
break;
case 34:
//#line 135 "gramatica.y"
{ analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraASIG,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); 
														Token t1 = (Token) val_peek(3).obj;
														Token t2 = (Token) val_peek(1).obj;
														if ( (t1 != null) && (t2 != null) ){
															if(!tipoCompatible(t1,t2))
																analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
														}
													if (t1.existeAtributo("Uso")){
														if (((int)t1.getAtributo("Uso")) == AnalizadorLexico.variable){
															TercetoAsignacion terceto;
															terceto = new TercetoAsignacion (  new Token(AnalizadorLexico.S_ASIGNACION, ":=" ) , (Token)val_peek(3).obj ,   (Token)val_peek(1).obj , controladorTercetos.getProxNumero() );
														
															controladorTercetos.addTerceto (terceto);	
														} else{
															TercetoColeccionAsignacion terceto = new TercetoColeccionAsignacion( new Token(AnalizadorLexico.S_ASIGNACION, ":=" ) , (Token)val_peek(3).obj ,  (Token)val_peek(1).obj , controladorTercetos.getProxNumero() );
														
															controladorTercetos.addTerceto (terceto);
														}
													}							
														yyval = new ParserVal((Token)val_peek(3).obj);
							}
break;
case 35:
//#line 156 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1));}
break;
case 36:
//#line 157 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorAsignacion,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
break;
case 37:
//#line 158 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorAsignacion,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
break;
case 38:
//#line 159 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorAsignacionIgual,"ERROR SINTACTICO", controladorArchivo.getLinea() )); }
break;
case 39:
//#line 160 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorAsignacionIgual,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
break;
case 40:
//#line 163 "gramatica.y"
{	Token t1 = (Token)val_peek(0).obj;						
		    	if  ( !t1.existeAtributo("Uso") ) {
				analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
			} else if ((int)t1.getAtributo("Uso") == AnalizadorLexico.coleccion){
				analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorColeccionPorVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
			yyval = new ParserVal((Token)val_peek(0).obj);}
break;
case 41:
//#line 169 "gramatica.y"
{yyval = new ParserVal((Token)val_peek(0).obj);}
break;
case 42:
//#line 172 "gramatica.y"
{	TercetoPrint terceto = new TercetoPrint (  new Token(AnalizadorLexico.PRINT,"Print") , (Token)val_peek(2).obj , null, controladorTercetos.getProxNumero() );
					terceto.setNroPrint(controladorTercetos.getNroPrint());
					controladorTercetos.addTerceto (terceto);
					controladorTercetos.addPrint(terceto);
					analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraPrint,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 43:
//#line 177 "gramatica.y"
{	TercetoPrint terceto = new TercetoPrint ( new Token(AnalizadorLexico.PRINT,"Print") , (Token)val_peek(2).obj , null, controladorTercetos.getProxNumero() );
					if (((Token)val_peek(2).obj).existeAtributo("Uso")){
						if ((int)((Token)val_peek(2).obj).getAtributo("Uso") == AnalizadorLexico.coleccion){	analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorFaltaIndiceColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
					}
					terceto.setTipoPrint(1);
					controladorTercetos.addTerceto (terceto);
					analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraPrint,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 44:
//#line 184 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPrint1,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 45:
//#line 185 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 46:
//#line 186 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1)); }
break;
case 47:
//#line 187 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPrint2,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 48:
//#line 190 "gramatica.y"
{TercetoComparacion terceto = new TercetoComparacion (  (Token)val_peek(1).obj  , (Token)val_peek(2).obj , (Token)val_peek(0).obj , controladorTercetos.getProxNumero() );
															controladorTercetos.addTerceto (terceto);
															
															if(!tipoCompatible((Token)val_peek(2).obj,(Token)val_peek(0).obj))
																analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
															Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
															Token aux = new Token( controladorTercetos.numeroTercetoString() );
															nuevo.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
															aux.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
															nuevo.AgregarAtributo("Valor",((Token) val_peek(1).obj).getValor());
															aux.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
															tablaSimbolo.add("@aux"+aux.getValor(),aux);
															yyval = new ParserVal(nuevo);
							analizadorS.addEstructura( new Error ( AnalizadorSintactico.estructuraCONDICION,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 49:
//#line 204 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorCondicionI,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 50:
//#line 205 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorCondicionD,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 51:
//#line 209 "gramatica.y"
{ yyval = val_peek(1);}
break;
case 52:
//#line 210 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorParentesisA,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 53:
//#line 211 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorParentesisC,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 56:
//#line 217 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 57:
//#line 218 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 58:
//#line 219 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorElse ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 59:
//#line 220 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorBEGIN,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 60:
//#line 221 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()  ));}
break;
case 63:
//#line 226 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 64:
//#line 227 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 65:
//#line 228 "gramatica.y"
{analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 66:
//#line 229 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorBEGIN,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 67:
//#line 230 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorEND,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 68:
//#line 233 "gramatica.y"
{	TercetoIf terceto = new TercetoIf (  (new Token( ControladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
											terceto.setTipo(((Token)val_peek(0).obj).getTipo());
											terceto.setTipoSalto(((Token)val_peek(0).obj).getValor());
											controladorTercetos.addTerceto (terceto);
											controladorTercetos.apilar(); 
										}
break;
case 69:
//#line 241 "gramatica.y"
{	
													TercetoIf terceto = new TercetoIf (  new Token( ControladorTercetos.BI)  , null, null, controladorTercetos.getProxNumero() );
													controladorTercetos.addTerceto (terceto);
													controladorTercetos.desapilar();
													controladorTercetos.apilar();
										}
break;
case 70:
//#line 247 "gramatica.y"
{ 	controladorTercetos.desapilar();
													analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 71:
//#line 249 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPalabraIF,"ERROR SINTACTICO", controladorArchivo.getLinea()  )); }
break;
case 72:
//#line 251 "gramatica.y"
{ controladorTercetos.desapilar();
                     												analizadorS.addEstructura (new Error ( AnalizadorSintactico.estructuraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 73:
//#line 253 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
break;
case 74:
//#line 254 "gramatica.y"
{ analizadorS.addError (new Error ( AnalizadorSintactico.errorPalabraIF,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()  )); }
break;
case 75:
//#line 258 "gramatica.y"
{ TercetoLabel tercetoLabel = new TercetoLabel(null,null,null,controladorTercetos.getProxNumero());
				controladorTercetos.addTerceto(tercetoLabel);
		controladorTercetos.apilarDo();
		analizadorS.addEstructura (new Error ( AnalizadorSintactico.inicioDO,"ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()-1  )); }
break;
case 76:
//#line 263 "gramatica.y"
{ 	TercetoIf terceto = new TercetoIf (  (new Token( ControladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
											terceto.setTipo(((Token)val_peek(1).obj).getTipo());
											terceto.setTipoSalto(((Token)val_peek(1).obj).getValor());
											controladorTercetos.addTerceto (terceto);
											controladorTercetos.desapilarDo(terceto);
													analizadorS.addEstructura (new Error (AnalizadorSintactico.estructuraDO, "ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()));	}
break;
case 77:
//#line 269 "gramatica.y"
{ 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
break;
case 78:
//#line 271 "gramatica.y"
{ TercetoIf terceto = new TercetoIf ( (new Token( ControladorTercetos.BF)  ), new Token( controladorTercetos.numeroTercetoString()  ), null, controladorTercetos.getProxNumero() );
													terceto.setTipo(((Token)val_peek(1).obj).getTipo());
													terceto.setTipoSalto(((Token)val_peek(1).obj).getValor());
													controladorTercetos.addTerceto (terceto);
													controladorTercetos.desapilarDo(terceto);
														analizadorS.addEstructura (new Error (AnalizadorSintactico.estructuraDO, "ESTRUCTURA SINTACTICA", controladorArchivo.getLinea()));}
break;
case 79:
//#line 277 "gramatica.y"
{ controladorTercetos.desapilarError();	
													analizadorS.addError (new Error ( AnalizadorSintactico.errorPuntoComa,"ERROR SINTACTICO", controladorArchivo.getLinea()-1 )); }
break;
case 80:
//#line 279 "gramatica.y"
{ 	controladorTercetos.desapilarError();
												analizadorS.addError (new Error (AnalizadorSintactico.errorDOUNTIL, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
break;
case 81:
//#line 281 "gramatica.y"
{ 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error (AnalizadorSintactico.errorDO, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
break;
case 82:
//#line 283 "gramatica.y"
{	controladorTercetos.desapilarError();
													analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 83:
//#line 285 "gramatica.y"
{	controladorTercetos.desapilarError();
																	analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 84:
//#line 287 "gramatica.y"
{	controladorTercetos.desapilarError();
																analizadorS.addError (new Error ( AnalizadorSintactico.errorBloqueEnBeginEnd ,"ERROR SINTACTICO", controladorArchivo.getLinea() ));}
break;
case 85:
//#line 289 "gramatica.y"
{ 	controladorTercetos.desapilarError();
														analizadorS.addError (new Error (AnalizadorSintactico.errorDOCondicion, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
break;
case 86:
//#line 291 "gramatica.y"
{ 	controladorTercetos.desapilarError();
										analizadorS.addError (new Error (AnalizadorSintactico.errorDOCondicion, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
break;
case 87:
//#line 295 "gramatica.y"
{yyval=val_peek(0);}
break;
case 88:
//#line 296 "gramatica.y"
{	Token t1 = (Token) val_peek(2).obj;
										Token t2 = (Token) val_peek(0).obj;
										if ((!t1.existeAtributo("Uso")) && (!t2.existeAtributo("Uso"))){
											if (es_cte_entera(t1) && es_cte_entera(t2)){
												int calculo_int = Integer.parseInt(t1.getValor()) + Integer.parseInt(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoInt, AnalizadorLexico.CTEENTERA, calculo_int);
												tablaSimbolo.add(String.valueOf(calculo_int),t);
												yyval=new ParserVal(t);
													}
											else if (es_cte_double(t1) && es_cte_double(t2)){
													double calculo_double = Double.parseDouble(t1.getValor()) + Double.parseDouble(t2.getValor());
													Token t = new Token(AnalizadorLexico.tipoDouble, AnalizadorLexico.CTEDOUBLE, calculo_double);
													t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
													tablaSimbolo.add(String.valueOf(calculo_double),t);
													yyval=new ParserVal(t);
														}
										}	
										else {
											if ( (t1 != null) && (t2 != null) ){
												if(!tipoCompatible(t1,t2))
													analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
											}
											String valor ="+";
											TercetoExpresion terceto = new TercetoExpresion ( new Token((int) valor.charAt(0),"+"  ), (Token)val_peek(2).obj ,  (Token)val_peek(0).obj , controladorTercetos.getProxNumero() );
											controladorTercetos.addTerceto (terceto);
											Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
											nuevo.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
											nuevo.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
											tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
											yyval = new ParserVal(nuevo);
										}
									}
break;
case 89:
//#line 328 "gramatica.y"
{	
										Token t1 = (Token) val_peek(2).obj;
										Token t2 = (Token) val_peek(0).obj;
										if ((!t1.existeAtributo("Uso")) && (!t2.existeAtributo("Uso"))){
											if (es_cte_entera(t1) && es_cte_entera(t2)){
												int calculo_int = Integer.parseInt(t1.getValor()) - Integer.parseInt(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoInt, AnalizadorLexico.CTEENTERA, calculo_int);
												tablaSimbolo.add(String.valueOf(calculo_int),t);
												yyval=new ParserVal(t);
											}
											else 
												if (es_cte_double(t1) && es_cte_double(t2)){
													double calculo_double = Double.parseDouble(t1.getValor()) - Double.parseDouble(t2.getValor());
													Token t = new Token(AnalizadorLexico.tipoDouble, AnalizadorLexico.CTEDOUBLE, calculo_double);
													t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
													tablaSimbolo.add(String.valueOf(calculo_double),t);
													yyval=new ParserVal(t);
												}
										}else {	
											if ( (t1 != null) && (t2 != null) ){
												if(!tipoCompatible(t1,t2))
													analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));		
												String valor ="-";
												TercetoExpresion terceto = new TercetoExpresion (  new Token((int) valor.charAt(0),"-"  ), (Token)val_peek(2).obj ,  (Token)val_peek(0).obj , controladorTercetos.getProxNumero() );
												controladorTercetos.addTerceto (terceto);
												Token nuevo = new Token( controladorTercetos.numeroTercetoString() ) ;
												nuevo.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
												nuevo.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
												tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
												yyval = new ParserVal(nuevo);
												}
										}
									}
break;
case 90:
//#line 364 "gramatica.y"
{yyval=val_peek(0);}
break;
case 91:
//#line 365 "gramatica.y"
{	
									Token t1 = (Token) val_peek(2).obj;
									Token t2 = (Token) val_peek(0).obj;
									if ((!t1.existeAtributo("Uso")) && (!t2.existeAtributo("Uso"))){
										if (es_cte_entera(t1) && es_cte_entera(t2)) {
											int calculo_int = Integer.parseInt(t1.getValor()) * Integer.parseInt(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoInt, AnalizadorLexico.CTEENTERA, calculo_int);
												tablaSimbolo.add(String.valueOf(calculo_int),t);
												yyval=new ParserVal(t);
											}
										else 
											if (es_cte_double(t1) && es_cte_double(t2)){
												double calculo_double = Double.parseDouble(t1.getValor()) * Double.parseDouble(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoDouble, AnalizadorLexico.CTEDOUBLE, calculo_double);
												t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
												tablaSimbolo.add(String.valueOf(calculo_double),t);
												yyval=new ParserVal(t);
											}
									} else {									
										if ( (t1 != null) && (t2 != null) ){
											if(!tipoCompatible(t1,t2))
												analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
										}
										String valor ="*";
										TercetoExpresionMult terceto = new TercetoExpresionMult (  new Token((int) valor.charAt(0), "*"  ),(Token)val_peek(2).obj ,  (Token)val_peek(0).obj , controladorTercetos.getProxNumero() );
										controladorTercetos.addTerceto (terceto);
										Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
										nuevo.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
										nuevo.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
										tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
										yyval = new ParserVal(nuevo);
									}
								}
break;
case 92:
//#line 399 "gramatica.y"
{
									Token t1 = (Token) val_peek(2).obj;
									Token t2 = (Token) val_peek(0).obj;
									if ((!t1.existeAtributo("Uso")) && (!t2.existeAtributo("Uso"))){
										if (es_cte_entera(t1) && es_cte_entera(t2)) {
											int calculo_int = Integer.parseInt(t1.getValor()) / Integer.parseInt(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoInt, AnalizadorLexico.CTEENTERA, calculo_int);
												tablaSimbolo.add(String.valueOf(calculo_int),t);
												yyval=new ParserVal(t);
											}
										else 
											if (es_cte_double(t1) && es_cte_double(t2)){
												double calculo_double = Double.parseDouble(t1.getValor()) / Double.parseDouble(t2.getValor());
												Token t = new Token(AnalizadorLexico.tipoDouble, AnalizadorLexico.CTEDOUBLE, calculo_double);
												t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
												tablaSimbolo.add(String.valueOf(calculo_double),t);
												yyval=new ParserVal(t);
											}
									} else {
										if ( (t1 != null) && (t2 != null) ){
											if(!tipoCompatible(t1,t2))
												analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorTiposDiferentes,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
										}
										String valor ="/";
										TercetoExpresionDiv terceto = new TercetoExpresionDiv (  new Token((int) valor.charAt(0), "/" ) , (Token)val_peek(2).obj ,  (Token)val_peek(0).obj , controladorTercetos.getProxNumero() );
										controladorTercetos.addTerceto (terceto);
										Token nuevo = new Token( controladorTercetos.numeroTercetoString() );
										nuevo.AgregarAtributo("Tipo",((Token)val_peek(2).obj).getTipo());
										nuevo.AgregarAtributo("Uso",AnalizadorLexico.vAuxiliar);
										tablaSimbolo.add("@aux"+nuevo.getValor(),nuevo);
										yyval = new ParserVal(nuevo);
									}
								}
break;
case 93:
//#line 434 "gramatica.y"
{yyval=val_peek(0);}
break;
case 94:
//#line 435 "gramatica.y"
{	if(!((Token)val_peek(0).obj).existeAtributo("AuxDouble"))
						((Token)val_peek(0).obj).AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
					yyval=val_peek(0);}
break;
case 95:
//#line 438 "gramatica.y"
{ if (((Token)val_peek(0).obj).existeAtributo("Uso")){
					if ((int)((Token)val_peek(0).obj).getAtributo("Uso") == AnalizadorLexico.coleccion){	analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorFaltaIndiceColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));}
					}
					yyval=val_peek(0);}
break;
case 96:
//#line 442 "gramatica.y"
{yyval=val_peek(0);}
break;
case 97:
//#line 443 "gramatica.y"
{Object valor =((Token)val_peek(0).obj).getAtributo("Valor");
					Token t = new Token (AnalizadorLexico.tipoInt,CTEENTERA,"-"+valor);
					analizadorL.getTablaSimbolos().add("-"+valor,t);
					yyval=new ParserVal(t);}
break;
case 98:
//#line 447 "gramatica.y"
{Object valor =((Token)val_peek(0).obj).getAtributo("Valor");
				if (!analizadorL.getTablaSimbolos().existe("-"+valor)){
					Token t = new Token (AnalizadorLexico.tipoDouble,CTEDOUBLE,"-"+valor);
					if(!t.existeAtributo("AuxDouble"))
						t.AgregarAtributo("AuxDouble",analizadorL.getVAuxDouble());
					analizadorL.getTablaSimbolos().add("-"+valor,t);
					yyval=new ParserVal(t);}
				else
					yyval=new ParserVal(analizadorL.getTablaSimbolos().get("-"+valor));}
break;
case 99:
//#line 458 "gramatica.y"
{Token t1= (Token)val_peek(2).obj;
				if  ( !t1.existeAtributo("Uso") ) {
					analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorNoExisteVariable,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
				}else if ((int)t1.getAtributo("Uso") == AnalizadorLexico.variable){
				analizadorCI.addError (new Error ( AnalizadorCodigoIntermedio.errorVariablePorColeccion,"ERROR DE GENERACION DE CODIGO INTERMEDIO", controladorArchivo.getLinea()  ));
				}else { t1 = analizadorCI.getTokenMetodo((Token)val_peek(2).obj,(Token)val_peek(0).obj,analizadorL,controladorTercetos,tablaSimbolo);}
				yyval = new ParserVal( t1 );
				}
break;
case 100:
//#line 466 "gramatica.y"
{ analizadorS.addError (new Error (AnalizadorSintactico.errorMetodo, "ERROR SINTACTICO", controladorArchivo.getLinea()));}
break;
case 101:
//#line 469 "gramatica.y"
{yyval= new ParserVal(new Token(AnalizadorLexico.first,"first"));}
break;
case 102:
//#line 470 "gramatica.y"
{yyval= new ParserVal(new Token(AnalizadorLexico.last,"last"));}
break;
case 103:
//#line 471 "gramatica.y"
{yyval= new ParserVal(new Token(AnalizadorLexico.length,"length"));}
break;
case 104:
//#line 474 "gramatica.y"
{yyval = new ParserVal(  new Token( AnalizadorLexico.tipoInt ) );}
break;
case 105:
//#line 475 "gramatica.y"
{yyval = new ParserVal(  new Token( AnalizadorLexico.tipoDouble ) );}
break;
case 106:
//#line 480 "gramatica.y"
{ String valor = "<";
							  yyval = new ParserVal(  new Token((int) valor.charAt(0),"<" ) ); }
break;
case 107:
//#line 482 "gramatica.y"
{ String valor = ">";
		 					  yyval = new ParserVal(  new Token((int) valor.charAt(0), ">" ) );}
break;
case 108:
//#line 484 "gramatica.y"
{ yyval = new ParserVal(  new Token(AnalizadorLexico.S_MAYOR_IGUAL,">=" ) ); }
break;
case 109:
//#line 485 "gramatica.y"
{ yyval = new ParserVal(  new Token(AnalizadorLexico.S_MENOR_IGUAL,"<=" ) ); }
break;
case 110:
//#line 486 "gramatica.y"
{ yyval = new ParserVal(  new Token(AnalizadorLexico.S_IGUAL, "==" ));}
break;
case 111:
//#line 487 "gramatica.y"
{ yyval = new ParserVal(  new Token(AnalizadorLexico.S_DISTINTO, "!="));	}
break;
//#line 1447 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
