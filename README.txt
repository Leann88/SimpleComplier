

Objective

	The objective of was to create simple compiler for the VSPL language in java. The language was to be extended to support arithmetic operations as well as the priority of the operations. The extended grammar must follow the LL(1) rules for grammar. Afterwards the recursive automata must be created as well as parsing table that will be used to program the compiler. Finally, the created compiler must be tested to see if it functions correctly.

Part 1 and 2: Modified LL (1) Grammar

1	<program> --> begin (<statement> ;)* <statement> end

2	 <statement> --><id> := <expression>

3	<expression>--> <OP> <OP’’> <expression’>

4	<expression’> -->ADDSUB <OP> <OP’’> <expression’> |  ε

5	 <OP> --><value> <OP’>

6	<OP’> -->EXPO <value> <OP’> |  ε

7	<OP’’> --> MULDIV <value> <OP’> | ε

8	 <value> --> number | (<expression>) | <id> 

9	 <id> --> ID


Lexical patterns for Non-terminals
ID[A-Za-z]+[A-Za-z0-9]*
number  [0-9]+
ADDSUB  + | -
MULDIV  * | /
EXPO  **

First and Follow List
1.	FIRST (<program>) = {begin}
2.	FIRST (<statement>) = FIRST (<id>) = {ID}
3.	 FIRST (<expression>) = FIRST (<OP>) = FIRST (<value>) = {number, (, FIRST (<id>)} = {number, (, ID}
4.	FIRST (<expression’>) = {ADDSUB, ε}
5.	FIRST (<OP>) = FIRST (<value>) = {number, (, FIRST (<id>)} = {number, (, ID}
6.	FIRST (<OP’>) = {EXPO, ε} 
7.	FIRST (<OP’’>) = {MULDIV, ε}
8.	FIRST (<value>) = {number, (, FIRST (<id>)} = {number, (, ID}
9.	FIRST (<id>) = {ID}



1.	FOLLOW (<program>) = {$}
2.	FOLLOW (<statement>) = {end, ;}
3.	FOLLOW (<expression>) ={), FOLLOW (<statement>)}= {),end, ;}
4.	FOLLOW (<expression’>) = FOLLOW (<expression>) = FOLLOW (<statement>) = {end, ;}
5.	FOLLOW (<OP>) = {[FIRST (<expression’>) - ε] U FOLLOW (<expression’>)} = {ADDSUB, FOLLOW (<expression>)} = {ADDSUB, ), FOLLOW (<statement>)} = {ADDSUB, ), end, ;}
6.	FOLLOW (<OP’>) = {FOLLOW (OP)} = …= {ADDSUB, ), end, ;}
7.	FOLLOW (<OP”>) = N/A 
8.	FOLLOW (<value>) = {[FIRST(<OP’>)- ε] U FOLLOW (<OP>), FIRST (<OP’’>) }=…= {EXPO, MULDIV, ADDSUB, ), end}
9.	FOLLOW (<id>) = {:=, FOLLOW (<value>)}=…= {:=, EXPO, MULDIV, ADDSUB, ), }

Parsing Table


Assumptions

•	If an error was found in the file the program would immediately terminate instead of continuing to read the rest of the file
•	Only () brackets are needed to be accounted for, {} and [] are not used
•	If a variable used in expression was not previously declared then an error will occur
•	If a variable is not given a value it will receive a default value of 0
•	Order of operation the compiler was supposed to follow was BEDMAS 
•	A space between each value, whether it be a variable, arithmetic operation, assignment operator or keyword, must be present in the test files for it to be properly read.
•	Compiler does not read comments

Problems Encountered

There were no major problems encountered when designing and implementing the simple java compiler. One observation made was that the END token is always printed before the finally variable that was assigned. After tracing the new code as well as testing original code it was found that this may be a result of the parseProgram(). Considering the output of the given example was allowed to display mentioned observation it was assumed that it was allowed.

Part 7: Testing - Input and Output

<Name of File || Input || Output>

<exampleInput.txt ||
BEGIN Var1 := ten;
Var2 := one ; 
Var3 := ten - one;
Var4 := Var1 + ten;
Var3 := Var3 + Var2
END  hjk ||
default input is from file exampleInput.txt
BEGIN Var1 := ten;
Var1 assign 10
Var2 := one ; 
Var2 assign 1
Var3 := ten - one;
Var3 assign 9
Var4 := Var1 + ten;
Var4 assign 20
Var3 := Var3 + Var2
END 
Var3 assign 10
 hjk 
Error: after END - more tokens before EOF>

<exampleInput2.txt ||
BEGIN Var1 := 2;
Var2 := 3 ; 
Var3 := ten - one;
Var4 := Var1 ** Var2;
Var3 := Var3 * Var2
END ||
default input is from file exampleInput2.txt
BEGIN Var1 := 2;
Var1 assign 2
Var2 := 3 ; 
Var2 assign 3
Var3 := ten - one;
Var3 assign 9
Var4 := Var1 ** Var2;
Var4 assign 8
Var3 := Var3 * Var2
END 
Var3 assign 27
analysis complete, no syntax error>


<exampleInput3.txt ||
BEGIN Var1 := ten;
Var2 := ;
END ||
default input is from file exampleInput3.txt
BEGIN Var1 := ten;
Var1 assign 10
Var2 := ;
Error: Var2 value needed
Var2 assign 0
VAR3 := 
END
Error: VAR3 value needed
VAR3 assign 0
analysis complete, syntax errors found>

<exampleInput4.txt ||
BEGIN VAR1+= ||
default input is from file exampleInput4.txt
BEGIN VAR1+=
Error:  assignment symbol expected>

<exampleInput5.txt ||
BEGIN VAR1 := 3 + 2 ** 3 * 5;
VAR2 := (3 ** 3 + 8 * 2) + 6 * 2;
VAR3 := VAR2/5;
VAR4 := 3 ** 2 ** 3 * 3 * (2 + 8);
VAR5 := (3 + 9) ** 2;
VAR6 := (3 + 9 * 2 ** 2) + 8;
VAR7 := 14 + 4 * 2 ** 3;
VAR8 := 243 + 4 * 2 ** 3 + (8)
END ||
default input is from file exampleInput5.txt
BEGIN VAR1 := 3 + 2 ** 3 * 5;
VAR1 assign 43
VAR2 := (3 ** 3 + 8 * 2) + 6 * 2;
VAR2 assign 55
VAR3 := VAR2/5;
VAR3 assign 11
VAR4 := 3 ** 2 ** 3 * 3 * (2 + 8);
VAR4 assign 21870
VAR5 := (3 + 9) ** 2;
VAR5 assign 144
VAR6 := (3 + 9 * 2 ** 2) + 8;
VAR6 assign 47
VAR7 := 14 + 4 * 2 ** 3;
VAR7 assign 46
VAR8 := 243 + 4 * 2 ** 3 + (8)
END
VAR8 assign 283
analysis complete, no syntax error>

<exampleInput6.txt
VAR2 := (3 ** 3 + 8 * 2) + 6 * 2;
VAR3 := VAR2/5 ||
default input is from file exampleInput6.txt
VAR2 
Error: BEGIN token expected!>

<exampleInput7.txt ||
BEGIN VAR1 := (3 ** 3 + 8 * 2) + 6 * 2;
VAR3 := VAR2/5
END ||
default input is from file exampleInput7.txt
BEGIN VAR1 := (3 ** 3 + 8 * 2) + 6 * 2;
VAR1 assign 55
VAR3 := VAR2/
Error: VAR2 not declared
5
END
VAR3 assign 0
analysis complete, syntax errors found>

<exampleInput8.txt ||
BEGIN Var1 := ten;
Var2 := Var1 + (9 * 8 ;
END ||
default input is from file exampleInput8.txt
BEGIN Var1 := ten;
Var1 assign 10
Var2 := Var1 + (9 * 8 ;
Error: Not valid, a ")" is needed>

<exampleInput9.txt ||
BEGIN Var1 := ten;
VAR2 := (3 + 9 * 2 ** 2 * 3) + 8;
VAR3 := 4 + 9 * 6 ** 4 * 2 * 2;
VAR5 := 2 + 2 * 4 ** 4 / 2 ** 2 + 11
END ||
default input is from file exampleInput9.txt
BEGIN Var1 := ten;
Var1 assign 10
VAR2 := (3 + 9 * 2 ** 2 * 3) + 8;
VAR2 assign 119
VAR3 := 4 + 9 * 6 ** 4 * 2 * 2;

VAR3 assign 46660
VAR4 := 2 + 2 * 4 ** 4 / 2 ** 2 + 11; 
VAR4 assign 141
VAR4 := 2 + 2 * 4 ** 4 / 2 ** 2 * 3 + 11
END
VAR4 assign 397
analysis complete, no syntax error>



