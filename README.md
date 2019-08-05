# LispInterpreter

An interpreter for LISP with support for the predefined functions like PLUS, MINUS, TIMES, EQ, LESS, GREATER ATOM, QUOTE, CDR, CAR, NULL COND, CONS and user-defined functions. 

(PLUS (PLUS 3 5) (TIMES 4 4))
(DEFUN DIFF (X Y)
(COND ( (EQ X Y) NIL ) (T T) ) ) )

(DIFF 5 6)

(DEFUN MEM (X LIST)
(COND ( (NULL LIST) NIL )
( T (COND
( (EQ X (CAR LIST)) T )
( T (MEM X (CDR LIST)))))))

(MEM 3 (QUOTE (2 3 4)))

