import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;



import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.*;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.CompilerEnvirons.*;
import org.mozilla.javascript.ErrorReporter;



public class FunctionCallVisit implements NodeVisitor {
    static ArrayList<FunctionCall> functionCalls = new ArrayList<FunctionCall>();
    static ArrayList<FunctionNode> functionNodes = new ArrayList<FunctionNode>();

    private boolean recordingComments;
    private boolean recordingLocalJsDocComments;
    
    public boolean visit(AstNode node) {
        if(node instanceof FunctionCall){
            functionCalls.add((FunctionCall) node);

        }
        if(node.getType() ==  Token.FUNCTION  && node instanceof FunctionNode){
            functionNodes.add((FunctionNode) node);
        }
        return true;
    }
    
    public ArrayList<FunctionCall> getFunctionCalls(){
        return functionCalls;
    }

    public ArrayList<FunctionNode> getFunctionNode(){
        return functionNodes;
    }
    public void setRecordingComments(boolean record) {
        recordingComments = record;
    }
    
   /* public static void main(String args[])
    {
    
    	
    	CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
    	compilerEnvirons.setRecordingComments(true);
    	compilerEnvirons.setRecordingLocalJsDocComments(true);
    	compilerEnvirons.setStrictMode(true);
    	File testScript = new File("C://Users//I338008//Documents//GIT Documents//JSCurrentFile.txt");
    	FileReader fileReader = new FileReader(testScript);
  	  
    	Parser p = new Parser(compilerEnvirons, null);
    	//AstRoot astRoot = p.parse(fileReader, null, 1);
    	
    	AstRoot astRoot = p.parse(fileReader, null, 1);
    	//AstRoot astRoot = new Parser(compilerEnvirons,null).parse(fileReader, null, 1);
  	  
    	
    	FunctionCallVisit functionCallVisit = new FunctionCallVisit();
    	  astRoot.visit(functionCallVisit);
    	  
    	  
    	  
    	  ArrayList<FunctionNode> functionNodes = functionCallVisit.getFunctionNode();
    	  for(FunctionNode functionNode : functionNodes){
    	      System.out.println("function name:"+functionNode.getName()
    	              +" param count:"+functionNode.getParamCount());
    	             // +" js doc:"+functionNode.getJsDoc());
    	      
    	      //get a list of all the parameters for this function
    	      List<AstNode> params = functionNode.getParams();
    	      for(AstNode paramNod : params){
    	         System.out.println("name of parameter:"+paramNod.getString()
    	              +" type:"+paramNod.getType());
    	      }
    	      System.out.println("");      
    	  }
    }*/
}