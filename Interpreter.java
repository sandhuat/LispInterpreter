import java.util.*;
public class Interpreter {
    // An implementation of LISP Interpreter with capability to process user defined functions and pre-defined operations
    // Input Example: (PLUS 2 3) 
    static String inpt = "";
    static int parenCount = 0;
    static String printView="";
	public static final String EQ = "EQ";
    public static final String ATOM = "ATOM";
    public static final String INT = "INT";
    public static final String NULL = "NULL";
    public static final String CAR = "CAR";
    public static final String CDR = "CDR";
    public static final String CONS = "CONS";
    public static final String QUOTE = "QUOTE";
    public static final String COND = "COND";
	public static final String EOF = "EOF";
    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    public static final String NUMERIC_ATOM = "NUMERIC_ATOM";
    public static final String LITERAL_ATOM = "LITERAL_ATOM";
    public static final String OPEN_PARENTHESES = "OPEN PARENTHESES";
    public static final String CLOSING_PARENTHESES = "CLOSING PARENTHESES";
    public static final String ERROR = "ERROR";
    public static final String T = "T";
    public static final String NIL = "NIL";
    static Node quoteNode=null;
    public static final String PLUS = "PLUS";
    public static final String MINUS = "MINUS";
    public static final String TIMES = "TIMES";
    public static final String LESS = "LESS";
    public static final String GREATER = "GREATER";
    private static Node dlist = new Node("NIL");
    private static Node nillNode = new Node("NIL");
    private static Node alist = new Node("NIL");
    public static Map<String, Integer> builtFnMapping;
    static Token current;

    public static HashMap<String, Integer> fnParameter;
    public static void main(String[] args)
	{
        fnParameter = new HashMap<String, Integer>();
       nillNode.type=LITERAL_ATOM;
       nillNode.nameCond="NIL";
       nillNode.nameCond="NIL";
		Scanner scan = new Scanner(System.in);

			while(scan.hasNextLine()) {
            String vh=scan.nextLine();
            if(vh.length()>0)
            inpt+=" "+vh;
		}
		 builtFnMapping = new HashMap<>();
        builtFnMapping.put("CAR", 1);
        builtFnMapping.put("CDR", 1);
        builtFnMapping.put("PLUS", 2);
        builtFnMapping.put("MINUS", 2);
        builtFnMapping.put("TIMES", 2);
        builtFnMapping.put("LESS", 2);
        builtFnMapping.put("GREATER", 2);
        builtFnMapping.put("EQ", 2);
        builtFnMapping.put("ATOM", 1);
        builtFnMapping.put("INT", 1);
        builtFnMapping.put("NULL", 1);
        builtFnMapping.put("CONS", 2);
        builtFnMapping.put("QUOTE", 1);
        builtFnMapping.put("COND", 3);
        builtFnMapping.put("DEFUN", 3);
       
		if(!inpt.isEmpty())
		{
			Init(inpt);
           
        ParseStart();
		}
		else
		{
			 System.out.println("ERROR: Empty expression " );
		}
    }
	public static void Init(String text){

        current = getNextToken(text,0);
    }
    public static Token GetCurrent() {

        return current;
    }
    public static void MoveToNext(String text) {

        current = getNextToken(text,current.idx+1);
    }
    static public void ParseStart() 
	{
		//System.out.println("PareseStart is "+current.type);
        do {
			
            if(current.type==null || current.type.equals(ERROR))
			{
                System.out.println("ERROR: Invalid token " + current.value);
                System.exit(0);
            }
			else if(current.type.equals(CLOSING_PARENTHESES))
			{
				
                System.out.println("ERROR: Closing parenthesis without open parenthesis");
                System.exit(0);
            }
			else 
			{
                Node root = new Node();
                root = generateParse(root);
                if (parenCount == 0) 
				{
                
                    try {
                       // Node newRoot=eval(root);
						 //System.out.println("Change 1 is "+newRoot.val);
						//  printLevelOrder(newRoot);
						 //  System.out.println("Change 11 is "+newRoot.initlzed);
                       // printListView(newRoot);
                       
                       // System.out.println(printView);
                        //printView="";
                      //  printLevelOrder(root);
                       
                        System.out.print(eval(root,alist));
                    } catch (InterException e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }

                    System.out.println();
                } 
				else 
				{
					
					if(parenCount>0)
				       System.out.println("ERROR: Open Parenthesis without closing parenthesis");
					else
                    System.out.println("ERROR: Expression not fitting the language grammar");
                    System.exit(0);
                }
            }
			
        }
        while (GetCurrent()!=null && (GetCurrent().type!=null && !GetCurrent().type.equals(EOF)));
    }

    public static Node generateParse(Node root)
    {
        Node temp = root;
		if(current!=null && current.type!=null)
        {
			if(current.type.equals(ERROR)){
            System.out.println("ERROR: Invalid token " + current.value);
            System.exit(0);
        }else if(current.type.equals(null)){
            System.out.println("ERROR: Invalid token " + current.value);
            System.exit(0);
        }else if(GetCurrent().type.equals(EOF)){
            if(parenCount>0)
            System.out.println("ERROR: Open Parenthesis without closing parenthesis");
            else
            System.out.println("ERROR: Expression not fitting the language grammar");
            System.exit(0);
        } else if(current.type.equals(OPEN_PARENTHESES)){
            parenCount++;
            MoveToNext(inpt);
            while(current!=null && current.type!=null && !current.type.equals(CLOSING_PARENTHESES)){
                Node Node = generateParse((new Node()));
                temp.left = Node;
                temp.right = new Node();
                temp = temp.right;
                if(GetCurrent().type!=null && GetCurrent().type.equals(EOF)){
                    break;
                }
            }
            if(current.type!=null && current.type.equals(CLOSING_PARENTHESES)){
                parenCount--;
            }

            MoveToNext(inpt);
            temp.val = "NIL";
            temp.initlzed = true;
            temp.type = current.type;
            temp.nameCond=temp.val;
            
            return root;
		}
        else if(!current.type.equals(CLOSING_PARENTHESES)){
            root.val = current.value;
            root.initlzed = true;
            
            root.type = current.type;
            if(root.type.equals(LITERAL_ATOM))
            root.nameCond=root.val;
            MoveToNext(inpt);
            return root;
        }
		}
        return root;
    }

    static void printParse(Node root){
        if(root == null){
            return;
        }else if(root.left == null && root.val == null) {
            return;
        }else{
            if(root.val==null){
                System.out.print("(");
            }else if(root.val == NIL){
                System.out.print(root.val);
            }else{
                System.out.print(root.val);
            }
            printParse(root.left);
            if(root.right != null){
                System.out.print(" . ");
            }
            printParse(root.right);
            if(root.right != null & root.left != null){
                System.out.print(")");
            }
        }

    }
	public static Token getNextToken(String inptxt, Integer idx) {
      
        Token token = new Token();
        String typtkn = null;
        int i =0;
	    String word = "";
        if(idx>=inptxt.length()){
            typtkn = EOF;
        }

        for (i = idx; i < inptxt.length(); i++) {
            char c = inptxt.charAt(i);
            
            if(c!= ' ' && c != '(' && c != ')')
			{
                word = word + Character.toString(c);
            }
            int asciival = (int) c;
    
            boolean condsa=(asciival>=65 && asciival<=90) || (asciival>=48 && asciival<=57) || c == '\n' || c == '\r' || c == ' ' || c=='(' || c==')' || asciival==32 ||asciival==13 ||asciival==10;
            if(!condsa)
            {
                //System.out.println("ERROR is "+asciival);
                typtkn=ERROR;
            }
			if(asciival >= 97 && asciival <= 122)
			{
				typtkn=ERROR;
			}
            else if (asciival >= 65 && asciival <= 90) {
                if (typtkn == null) {
                    typtkn = LITERAL_ATOM;
                    
                }else if (typtkn.equals(NUMERIC_ATOM)){
                    typtkn = ERROR;
                }
            }
            else if (asciival >= 48 && asciival <= 57) {
                if (typtkn != LITERAL_ATOM && typtkn != ERROR) {
                    typtkn = NUMERIC_ATOM;
                }
            }
            

            if (c == '\n' || c == '\r' || c == ' ') {
                if(word.length()!=0){
                    break;
                }
            }else if (c == '('){
                if(word.length() ==0){
                    typtkn = OPEN_PARENTHESES;
                }else {
                    i = i - 1;
                }
                break;
            }else if (c == ')'){
                if(word.length()==0){
                    typtkn = CLOSING_PARENTHESES;
                }else{
                    i = i-1;
                }
                break;
            }
			else if(c=='-')
			{
				typtkn=ERROR;
			}

        }
        token.type = typtkn;
        token.value = word;
        token.idx = i;
        return (token);
    }

    static boolean nilch(Node s)
    {
        return s.initlzed && s.val.equals(NIL);
    }

    static int operandInExps(Node s) {
        if(s.initlzed)
            return 0;
        else
            {
               // System.out.println("List check is "+listCheck(s));
                return 1+operandInExps(s.right);
            }
        
    }
	
	
	
	static class Token 
	{
    String type;
	int idx;
    String value;
    
	public Token()
	{

    }
    public Token(String type2, String value2, Integer idxc) {
        this.type = type2;
        this.value = value2;
        this.idx = idxc;
    }

    
	}
    static Node evaluateRoot(Node s,Node alist) throws InterException
    {
        if(s.val.equals(T) || s.val.equals(NIL) || s.type.equals(NUMERIC_ATOM))
        {
            return s;
        }
		else if (s.type.equals(LITERAL_ATOM)) {
           // System.out.println("sfgf  is "+s.val);
                if (literalStored(s, alist)) 
                {
                    return getLiteralValue(s, alist);
                } 
            
        }
        String msg="Undefined atom cannot be evaluated: " + s.val;
        if(builtFnMapping.containsKey(s.val))
        msg="Predefined function "+s.val+" cannot be used here as operand ";
        throw new InterException(msg);
    }
    private static boolean literalStored(Node roots, Node alist) throws InterException{
      //  System.out.println("literalStored ");
     //   printLevelOrder(alist);
        if (alist == null || roots == null || alist.left == null) {
            return false;
        } else {
            if (carop(alist) != null && carop(carop(alist)) != null) {
                if (roots.nameCond != null&& roots.val.equals((carop(carop(alist))).val)) {
                    return true;
                } else {
                    return literalStored(roots, cdrop(alist));
                }
            } else {
                return false;
            }
        }
    }
    private static Node getLiteralValue(Node roots, Node alist) throws InterException{
        if (alist == null) {
            return nillNode;
        } else {
            if (eq(roots, carop(carop(alist))).val.equals("T")) {
                return (cdrop(carop(alist)));
            } else {
                return getLiteralValue(roots, cdrop(alist));
            }
        }
    }
    public static Node eq(Node s1, Node s2) throws InterException
    {

        if (s1.type ==  LITERAL_ATOM && s2.type ==  LITERAL_ATOM) {
            Node root = new Node();
            root.val = s1.val.equals(s2.val) ? T : NIL;
            root.type = LITERAL_ATOM;
            root.nameCond=root.val;
            root.initlzed = true;
            return root;
        }
        if (s1.type ==  NUMERIC_ATOM && s2.type ==  NUMERIC_ATOM) {
            Node root = new Node();
            root.val = s1.val==(s2.val) ? T : NIL;
            root.type = LITERAL_ATOM;
            root.nameCond=root.val;
            root.initlzed = true;
            return root;
        }

       throw new InterException("ERROR: Operands of EQ are of different types "+s1.val+" "+s2.val);
    }
	static public class Node 
	{
	boolean initlzed = false;
    Node left;
    Node right;
    String val;
    String nameCond=null;
    String type=null;
	public Node()
	{

    }
    public Node(String value2) 
	{
        this.val = value2;
        this.initlzed = true;
        left = null;
        right = null;
        
    }
    public Node(String value2,String type2) 
	{
        this.val = value2;
        this.initlzed = true;
        left = null;
        right = null;
        this.type = type2;
    }

    

    @Override
    public String toString() {
        if (initlzed)
			return val;
        StringBuilder strb = new StringBuilder("(");
        Node t;

        for (t = this; !t.initlzed; t = t.right) {
            strb.append(t.left);
			// System.out.print(t.left+"--");
            if (!t.right.initlzed) {
                strb.append(" ");
            }
        }

        if (t.val.equals("NIL")) {
            strb.append(")");
        } else {
            strb.append(" . " + t.val + ")");
        }
        return strb.toString();
    }
}

static void printListView(Node root)
{
    if (root.initlzed)
    {
        printView=root.val;
        return;
    }
    printView+="(";
    Node t=root;
 while(!t.initlzed)
 {
	
    // System.out.println("in whle");
     printView+=t.left;
     if (!t.right.initlzed) {
         printView+=" ";
     }
 }

    if (t.val.equals("NIL")) {
         printView+=")";
    } else {
        printView+=" . " + t.val + ")";
        //strb.append(" . " + t.val + ")");
    }
    //return strb.toString();
}

    static boolean listCheck(Node s)
    {
        return (nilch(s) || listCheck(s.right));
    }
	
     static Node eval(Node s, Node alist) throws InterException
	 {

         //printLevelOrder(dlist);
        if(s.initlzed){
            return evaluateRoot(s,alist);
        }
        return evaluateTree(s,alist);
    }

     

    static  Node evaluateTree(Node s,Node alist) throws InterException
    {
        if(!listCheck(s))
        {
            throw new InterException("ERROR: "+s +" is not a list");
        }
        String opcode = s.left.val;
        if (operandInExps(s) < 2 && builtFnMapping.containsKey(opcode))
        {
            
            throw new InterException("ERROR: Not sufficient number of operands for " + s);
        }
        
        if (!s.left.initlzed)
        {
            throw new InterException("ERROR: No opcode defined for " + s.left);
        }
		if(opcode.equals("CAR") || opcode.equals("CDR"))
		{
            if (operandInExps(s) != 2)
            {
                throw new InterException("ERROR: Number of operands other than 1 for "+s.left);
            }
            Node s1 = eval(s.right.left,alist);
    
            if (s1.initlzed)
            {
                throw new InterException("ERROR: " + s1 + " is an atom");
            }

            switch (opcode)
            {
                case CAR:
                return s1.left;
                case CDR:
                return s1.right;
                default:
                    throw new InterException("ERROR: Not defined: " + opcode);
            }
		}
		else if(opcode.equals("EQ")||opcode.equals("PLUS") || opcode.equals("MINUS") ||opcode.equals("GREATER")|| opcode.equals("TIMES") || opcode.equals("LESS"))
        {
            if (operandInExps(s) != 3) {
                throw new InterException("ERROR: Number of operands other than 2 for " + s.left);
            }

            Node s1 = eval(s.right.left,alist);
            Node s2 = eval(s.right.right.left,alist);

            if (operandInExps(s1) > 1) {
                throw new InterException("ERROR : Operands cannot be list for "+ opcode );
            }
            if (operandInExps(s2) > 1) {
                throw new InterException("ERROR : Operands cannot be list for " + opcode );
            }

            if (opcode.equals(EQ) &&
                    s1.initlzed && s1.type ==  LITERAL_ATOM &&
                    s2.initlzed && s2.type ==  LITERAL_ATOM) {
                Node root = new Node();
                root.val = s1.val.equals(s2.val) ? T : NIL;
                root.type = LITERAL_ATOM;
                root.nameCond=root.val;
                root.initlzed = true;
                return root;
            }

            if (s1.type != NUMERIC_ATOM ) {
                throw new InterException("ERROR: " + s1 + " is not a number");
            }
            if (s2.type != NUMERIC_ATOM) {
                throw new InterException("ERROR: " + s2 + " is not a number");
            }
			 

            int a = Integer.parseInt(s1.val);
            int b = Integer.parseInt(s2.val);
            
            switch (opcode)
            {
                case PLUS:
                    {
                    Node root = new Node();
                    root.type = NUMERIC_ATOM;
                    root.val = Integer.toString(a + b);
                    root.initlzed = true;
                    
                    return root;
                    }
                case MINUS:
                    {
                    Node root = new Node();
                    root.type = NUMERIC_ATOM;
                    root.val = Integer.toString(a - b);
                    root.initlzed = true;
                    return root;
                    }
                case LESS:
                    {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = a < b ? T : NIL;
                    root.nameCond=root.val;
                    root.initlzed = true;
                    return root;
                    }
                case GREATER:
                    {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = a > b ? T : NIL;
                    root.nameCond=root.val;
                    root.initlzed = true;
                    return root;
                    }
                case TIMES:
                    {
                    Node root = new Node();
                    root.type = NUMERIC_ATOM;
                    root.val = Integer.toString(a * b);
                    root.initlzed = true;
                    return root;
                    }

                case EQ:
                    {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = a == b ? T : NIL;
                    root.nameCond=root.val;
                    root.initlzed = true;
                    return root;
                    }
                default:
                    throw new InterException("ERROR:Not defined: " + opcode);
            }
		}
		else if(opcode.equals("COND"))
		{
            
            for (Node t = s.right; !t.initlzed; t = t.right) {
                
                try{
                if (!listCheck(t.left)) {
                    throw new InterException("ERROR: "+t.left + " is not a list for COND");
                }
            }
            catch(NullPointerException e)
            {
                throw new InterException("ERROR: "+t.left + " is not a list for COND");
            }
             
                if (operandInExps(t.left) != 2) {
                    throw new InterException("ERROR: Need 2 operands(boolean and evaluation) in list for COND in " + t.left);
                }
            }

            for (Node t = s.right; !t.initlzed; t = t.right) {
                Node b = eval(t.left.left,alist);
              
                Node e = eval(t.left.right.left,alist);

                if (!nilch(b)) {
                    return e;
                }
            }

            throw new InterException("ERROR: No clause resulted in truth in COND");
		}
		else if(opcode.equals("CONS"))
		{
            if (operandInExps(s) != 3) {
                throw new InterException("ERROR: Number of operands other than 2 for "+s.left);
            }
            //Node root=cons(s.right.left,s.right.right.left);
            Node s1 = eval(s.right.left,alist);
            Node s2 = eval(s.right.right.left,alist);

            Node root2 = new Node();
            root2.left = s1;
            root2.right = s2;
            Node root= root2;
            
            return root;
		}
		else if(opcode.equals("ATOM") || opcode.equals("INT") || opcode.equals("NULL"))
		{
            if (operandInExps(s) != 2) 
			{
                throw new InterException("ERROR: Number of operands other than 1 for "+s.left);
            }


            Node s1 = eval(s.right.left,alist);

            switch (opcode) {
                case ATOM:
                {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = s1.initlzed ? T : NIL;
                    root.initlzed = true;
                    return root;
                }
                case INT: {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = s1.initlzed && s1.type == NUMERIC_ATOM ? T : NIL;
                    root.initlzed = true;
                    return root;
                }
                case NULL: {
                    Node root = new Node();
                    root.type = LITERAL_ATOM;
                    root.val = nilch(s1) ? T : NIL;
                    root.initlzed = true;
                    return root;
                }
                default:
                    throw new InterException("ERROR: Not defined - " + opcode);
            }
		}

		else if(opcode.equals("QUOTE"))
		{
           return s.right.left;
        }
        else if(opcode.equals("DEFUN"))
        {
            //System.out.println("Hello defin");
            //printLevelOrder(s);
        if (functionCheck(s)) 
        {
              //  System.out.println("Before addTodlist call");
             // printLevelOrder(s);
           addTodlist(cdrop(s));
          //System.out.println("Function name is "+carop(cdrop(s)).val);
          Node root = new Node();
         root.type = LITERAL_ATOM;

         root.val=carop(cdrop(s)).val;
         root.nameCond=root.val;
         root.initlzed = true;
           return root;
        }
        
            //
           // return s.right.left;
        }
		else
		{
           // System.out.println("DSf is "+carop(s).val);
           // printLevelOrder(s);
           if(carop(s).type==NUMERIC_ATOM)
           {
               //return s;
               throw new InterException("ERROR : Function name cannot be numeric: " +carop(s).val);
           }
           if(dlist.left==null && dlist.right==null)
           throw new InterException("ERROR : Function " + carop(s).val + " is undefined prior to function call ");
          // printLevelOrder(dlist);
         /* if (functionIndlist(carop(s), alist)) 
          {
            // System.out.println("DSa is "+getLiteralValue(carop(s),alist).type);
             if(getLiteralValue(carop(s),alist).type.equals(NUMERIC_ATOM))
             throw new InterException("ERROR : Function name cannot be numeric: " +getLiteralValue(carop(s),alist).val+" because "+carop(s).val+" is a parameter here");
              return getLiteralValue(carop(s),alist);
          }*/
          
          if (!functionIndlist(carop(s), dlist)) 
          {
              String msg="ERROR : Function " + carop(s).val + " is undefined prior to function call ";
              if(carop(s).val.equals("T") || carop(s).val.equals("NIL"))
              {
                  msg="ERROR : T or NIL cannot be used as function names ";
              }
              throw new InterException(msg);
          }
            return applyFunction(carop(s),evlist(cdrop(s), alist, dlist), alist);
        //throw new InterException("ERROR: Not defined - " + opcode);
      
		}
        return new Node();	
    }
    static Node cons2(Node left, Node right) throws InterException
    {
          Node root2 = new Node();
            root2.left = left;
            root2.nameCond=null;
            root2.right = right;
            return root2;
    }
    private static boolean functionCheck(Node roots) throws InterException
    {
        String fName = carop(cdrop(roots)).val;
        Node froots = carop(cdrop(roots));
        //printLevelOrder(roots.right.right.left);
     //   System.out.println("condition is "+listCheck(roots.right.right.left));
      //  System.out.println("number of operands is "+operandInrootss(roots));
        if(operandInExps(roots)!=4)
        {
            throw new InterException("ERROR : Function " + fName + " has more/less operands and not conforming to the form (DEFUN F s1 s2) ");
          
        }
        
        if (froots.type == LITERAL_ATOM && functionNameNotPredefined(fName))
       {

            Node temp = carop(cdrop(cdrop(roots)));
            ArrayList<String> paramList = new ArrayList<String>();
            if(!listCheck(temp))
            {
                throw new InterException("ERROR : Function " + fName + " does not have S1 as a list in the definition form (DEFUN F s1 s2) ");
            }
            while (temp != null) {
                
                if (temp.left != null) 
                {
                    if (temp.left.type == LITERAL_ATOM) {
                        String parameter = temp.left.val;
                        if (parameter.equals("T") || parameter.equals("NIL")) {
                            throw new InterException("ERROR : Function " + fName + "() cannot have T or NIL as a parameter name");
                        }
                        else if(builtFnMapping.containsKey(parameter))
                        {
                            throw new InterException("ERROR : Function " + fName + "() has predefined opcode as a parameter name");
                        
                        }
                         else 
                        {
                            paramList.add(parameter);
                        }
                    } else {
                        throw new InterException("ERROR : Function " + fName + "() has non-symbolic parameter name");
                        
                    }
                }
                temp = temp.right;
            }

            fnParameter.put(fName, paramList.size());
            if (paramList.size() != new HashSet<String>(paramList).size()) 
            {
                fnParameter.remove(fName);
                throw new InterException("ERROR : Function " + fName+ "() has duplicate parameters");
            }
        } 
        else 
        {
            throw new InterException("ERROR : Function " + fName+ "() name is predefined function. Select another name");
          
        }
        return true;
    }
    private static void addTodlist(Node roots) throws InterException
    {
        //printLevelOrder(roots);
      //  System.out.println("DA "+car(roots).val);
        Node temp = cons2(carop(roots), cons2(carop(cdrop(roots)), carop(cdrop(cdrop(roots)))));
        dlist = cons2(temp, dlist);
    }
    private static boolean functionNameNotPredefined(String functionName)  throws InterException
    {
        if (atomIsNumber(functionName)) {
            return false;
        }
        if (builtFnMapping.containsKey(functionName) || functionName.equals("T") || functionName.equals("NIL")){
            return false;
        }
        return true;
    }
    static int height(Node node)
    {
        if (node == null)
            return 0;
        else
        {
            /* compute the depth of each subtree */
            int lDepth = height(node.left);
            int rDepth = height(node.right);

            /* use the larger one */
            if (lDepth > rDepth)
                return (lDepth + 1);
            else
                return (rDepth + 1);
        }
    }
    
   
    private static Node carop(Node roots) throws InterException {
     //   System.out.println(roots.nameCond);
    // System.out.println("In carop "+roots);
    // System.out.println("In carop 2 "+roots.nameCond);
    //printLevelOrder(roots);
       if (roots==null || (roots.nameCond != null && roots.nameCond.equals("NIL"))|| (roots.nameCond != null && roots.nameCond.equals("T"))) 
       {
            return nillNode;
        } 
        else if(!roots.initlzed)
        {
            return roots.left;
        }
        else 
        {

            if (roots.type!=null && (roots.type.equals(LITERAL_ATOM) || roots.type.equals(NUMERIC_ATOM))) {
                throw new InterException("ERROR : CAR cannot be applied to an atom");
               
        }
            return roots.left;
        }
        
    }
    private static Node cdrop(Node roots) throws InterException{
       // System.out.println("In cdrop "+roots);
        //System.out.println("In cdrop 2 "+roots.nameCond);
        //System.out.println("In cdrop "+roots.right.val);
        //printLevelOrder(roots);
        if(roots.right==null && roots.right==null)
        {
            return nillNode;   
        }
      if ((roots.nameCond != null && roots.nameCond.equals("NIL"))|| (roots.nameCond != null && roots.nameCond.equals("T"))) {
        return nillNode;
        } 
        else if(!roots.initlzed)
        {
        return roots.right;
        }
        else 
        {
        if (roots.type!=null && roots.type.equals(LITERAL_ATOM) || roots.type.equals(NUMERIC_ATOM)) {
            throw new InterException("ERROR : CDR cannot be applied to an atom.");  
        }
        return roots.right;
}
    }
    private static boolean atomIsNumber(String atom)throws InterException {
        int i = 0;

        if ((atom.charAt(i) >= 48 && atom.charAt(i) <= 57) || atom.charAt(0) == '+' || atom.charAt(0) == '-') 
        {
            if (atom.charAt(0) == '-' || atom.charAt(0) == '+') 
            {
                i++;
            }

            while (i < atom.length()) 
            {
                if (!(atom.charAt(i) >= 48 && atom.charAt(i) <= 57)) 
                    return false;
                
                i++;
            }
        }
        else 
        {
            return false;
        }
        return true;
    }
    private static Node evlist(Node paramList, Node alist, Node dlist3) throws InterException 
    {
        if (paramList.nameCond != null && paramList.nameCond.equals("NIL")) 
        {
            return nillNode;
        } 
        else 
        {
            return cons2(eval(carop(paramList), alist),evlist(cdrop(paramList), alist, dlist3));
        }
        
    }
private static boolean functionIndlist(Node roots,Node dlist3) throws InterException 
{
        //printLevelOrder(dlist3);
       // if(dlist3!=null && dlist3.left==null && dlist3.right==null && dlist.val.equals("NIL"))
        //return false;
        if ((dlist3.nameCond != null && dlist3.nameCond.equals("NIL")) || (dlist3.nameCond != null && roots.nameCond.equals("NIL"))) 
        {
        return false;
        }
        if (eq(roots, carop(carop(dlist3))).val.equals("T")) 
        {
        return true;
        }
        else
        {
        return functionIndlist(roots, cdrop(dlist3));
        }
}
private static Node makeNewalist(Node litralVar, Node litralVal, Node alist) throws InterException
{
    if (litralVar.nameCond != null && litralVar.nameCond.equals("NIL")) 
    {
        return alist;
    }
    else 
    {
        return (cons2(cons2(carop(litralVar), carop(litralVal)),makeNewalist(cdrop(litralVar), cdrop(litralVal), alist)));
    }
}
    private static Node applyFunction(Node function, Node paramList, Node alist) throws InterException 
    {
      //  System.out.println("SAdsa is "+function.val);
        int paramno;
        Node temp = paramList;
        int num = 0;
        while (temp != null) {
            if (temp.left != null) {
                num++;
            }
            temp = temp.right;
        }
        paramno=num;
       // printLevelOrder(dlist);
            if (functionIndlist(function, dlist)) 
            {
            if (fnParameter.get(function.val) == paramno) 
            {
                Node paraone= carop(getLiteralValue(function, dlist));
                Node paratwo=paramList;

                Node alistnew;
                if (paraone.nameCond != null && paraone.nameCond.equals("NIL")) 
                {
                    alistnew=alist;
                } 
                else 
                {
                    alistnew=makeNewalist(paraone, paratwo, alist);
                }
                return eval(cdrop(getLiteralValue(function, dlist)),alistnew);
            } 
            else 
            {
                throw new InterException("ERROR : Call of function "+ function.val+ "() has  number of parameters different than function definition");
            }
        } 
        else 
        {
           
            throw new InterException("ERROR : Function "+ function.val+ "() is neither predefined or user-defined");
        }
         
    }
    static void printLevelOrder(Node root)
    {
        System.out.println("\nThe level order is");
        int h = height(root);
        int i;
        for (i=1; i<=h; i++)
        {
            printGivenLevel(root, i);
            System.out.println();
        }
        System.out.println("END OF PARSE TREE");
    }
    /* Print nodes at a given level */
    static void printGivenLevel(Node root, int level)
    {
        if (root == null)
            return;
            //System.out.print(root.initlzed+" ");
        if (level == 1)
        System.out.print(root.val+" ");
        else if (level > 1)
        {
            printGivenLevel(root.left, level-1);
            printGivenLevel(root.right, level-1);
        }
    }
     static class InterException extends Exception { 
        private static final long serialVersionUID = 1L;

        public InterException(String ERRORMessage) {
            super(ERRORMessage);
        }
    }
   
}
