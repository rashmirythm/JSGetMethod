
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;



public class MethodDiff {
               private static final String currentfile = null;
               private static Git git;
               private static Repository repository;
               private static FileOutputStream fop = null;
               private static byte[] contentInBytes;
               
               private static String[] FindGitCommitFiles( ObjectId treeId ) throws IOException {
                   Collection<String> pathNames = new ArrayList<>();
                   TreeWalk treeWalk = new TreeWalk( repository );
                   treeWalk.setRecursive( true );
                   treeWalk.setPostOrderTraversal( true );
                   treeWalk.addTree( treeId );
                   while( treeWalk.next() ) {
                     pathNames.add( treeWalk.getPathString() );
                   }
                   
                   return ( String[] )pathNames.toArray( new String[ pathNames.size() ] );
                 }
               
               public static void createFile(ObjectId tree, String filename, String filechanged) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
                              
                              File file = new File(filename);
                              TreeWalk treeWalk = new TreeWalk(repository);
                   treeWalk.addTree(tree);
                   treeWalk.setRecursive(true);
                   treeWalk.setFilter(PathFilter.create(filechanged));
                   if (!treeWalk.next()) 
                   {
                     System.out.println("Nothing found!");
                     return;
                   }
                   ObjectId objectId = treeWalk.getObjectId(0);
                   ObjectLoader loader = repository.open(objectId);
               
                   ByteArrayOutputStream out = new ByteArrayOutputStream();
                   loader.copyTo(out);
                   
                              if (!file.exists()) {
                                             file.createNewFile();
                              }
                              fop = new FileOutputStream(file);
                              contentInBytes = out.toString().getBytes();
                   fop.write(contentInBytes);
               }

   /*private static PrettyPrinterConfiguration ppc = null;

    class ClassPair {
        final ClassOrInterfaceDeclaration clazz;
        final String name;
        ClassPair(ClassOrInterfaceDeclaration c, String n) {
            clazz = c;
            name = n;
        }
    }

    public static PrettyPrinterConfiguration getPPC() {
        if (ppc != null) {
            return ppc;
        }
        PrettyPrinterConfiguration localPpc = new PrettyPrinterConfiguration();
        localPpc.setColumnAlignFirstMethodChain(false);
        localPpc.setColumnAlignParameters(false);
        localPpc.setEndOfLineCharacter("");
        localPpc.setIndent("");
        localPpc.setPrintComments(false);
        localPpc.setPrintJavaDoc(false);

        ppc = localPpc;
        return ppc;
    }

    public static <N extends Node> List<N> getChildNodesNotInClass(Node n, Class<N> clazz) {
        List<N> nodes = new ArrayList<>();
        for (Node child : n.getChildNodes()) {
            if (child instanceof ClassOrInterfaceDeclaration) {
                // Don't go into a nested class
                continue;
            }
            if (clazz.isInstance(child)) {
                nodes.add(clazz.cast(child));
            }
            nodes.addAll(getChildNodesNotInClass(child, clazz));
        }
        return nodes;
    }

    private List<ClassPair> getClasses(Node n, String parents, boolean inMethod) {
        List<ClassPair> pairList = new ArrayList<>();
        for (Node child : n.getChildNodes()) {
            if (child instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration)child;
                String cName = parents+c.getNameAsString();
                if (inMethod) {
                    System.out.println(
                            "WARNING: Class "+cName+" is located inside a method. We cannot predict its name at"
                            + " compile time so it will not be diffed."
                    );
                } else {
                    pairList.add(new ClassPair(c, cName));
                    pairList.addAll(getClasses(c, cName + "$", inMethod));
                //}
            } else if (child instanceof MethodDeclaration || child instanceof ConstructorDeclaration) {
                pairList.addAll(getClasses(child, parents, true));
            } else {
                pairList.addAll(getClasses(child, parents, inMethod));
            }
        }
        return pairList;
    }

    private List<ClassPair> getClasses(String file) {
        try {
            CompilationUnit cu = JavaParser.parse(new File(file));
            return getClasses(cu, "", false);
        } catch (FileNotFoundException f) {
            throw new RuntimeException("Could not find file: "+f);
        }
    }

    public static String getSignature(String className, CallableDeclaration m) {
       return className+"."+m.getSignature().asString();
    }
   */
    public static HashSet<String> FindMethodsChangedofFiles(String filechanged, ObjectId treeId) throws IOException {
        HashSet<String> changedMethods = new HashSet<>();
        HashMap<String, String> methods = new HashMap<>();
        
        String file1 = "C:\\Users\\I338008\\Documents\\GIT Documents\\JSCurrentFile.txt";
                              String file2 = "C:\\Users\\I338008\\Documents\\GIT Documents\\JSParentFile.txt";
        
        //ObjectId treeId = ObjectId.fromString(commitHash);
                   RevWalk revWalk = new RevWalk(repository);
                   RevCommit commit = revWalk.parseCommit(treeId);
                   RevTree tree = commit.getTree();
        createFile(tree,file1,filechanged);
                                 RevCommit[] Parents = commit.getParents();
                                                for(RevCommit parent:Parents) {
                                                RevCommit ParentCommit = revWalk.parseCommit(parent.getId());
                                                RevTree ParentTree = ParentCommit.getTree();
                                                createFile(ParentTree,file2,filechanged);
                                                }
               
        MethodDiff md = new MethodDiff();

        // Load all the method and constructor values into a Hashmap from File1
        
        /*List<ClassPair> cList = md.getClasses(file1);

        for (ClassPair c : cList) {
            List<ConstructorDeclaration> conList = getChildNodesNotInClass(c.clazz, ConstructorDeclaration.class);
            List<MethodDeclaration> mList = getChildNodesNotInClass(c.clazz, MethodDeclaration.class);
            for (MethodDeclaration m : mList) {
                String methodSignature = getSignature(c.name, m);

                if (m.getBody().isPresent()) {
                    methods.put(methodSignature, m.getBody().get().toString(getPPC()));
                } else {
                    System.out.println("Warning: No Body for "+file1+" "+methodSignature);
                }
            }
            for (ConstructorDeclaration con : conList) {
                String methodSignature = getSignature(c.name, con);
                methods.put(methodSignature, con.getBody().toString(getPPC()));
            }
        }

        // Compare everything in file2 to what is in file1 and log any differences
        cList = md.getClasses(file2);
        for (ClassPair c : cList) {
            List<ConstructorDeclaration> conList = getChildNodesNotInClass(c.clazz, ConstructorDeclaration.class);
            List<MethodDeclaration> mList = getChildNodesNotInClass(c.clazz, MethodDeclaration.class);
            for (MethodDeclaration m : mList) {
                String methodSignature = getSignature(c.name, m);

                if (m.getBody().isPresent()) {
                    String body1 = methods.remove(methodSignature);
                    String body2 = m.getBody().get().toString(getPPC());
                    if (body1 == null || !body1.equals(body2)) {
                        // Javassist doesn't add spaces for methods with 2+ parameters...
                        changedMethods.add(methodSignature.replace(" ", ""));
                    }
                } else {
                    System.out.println("Warning: No Body for "+file2+" "+methodSignature);
                }
            }
            for (ConstructorDeclaration con : conList) {
                String methodSignature = getSignature(c.name, con);
                String body1 = methods.remove(methodSignature);
                String body2 = con.getBody().toString(getPPC());
                if (body1 == null || !body1.equals(body2)) {
                    // Javassist doesn't add spaces for methods with 2+ parameters...
                    changedMethods.add(methodSignature.replace(" ", ""));
                }
            }
            // Anything left in methods was only in the first set and so is "changed"
            for (String method : methods.keySet()) {
                // Javassist doesn't add spaces for methods with 2+ parameters...
                changedMethods.add(method.replace(" ", ""));
            }
        }*/
        return changedMethods;
    }


   /* private static void removeComments(Node node) {
        for (Comment child : node.getAllContainedComments()) {
            child.remove();
        }
    }*/
    public static HashSet<String> FindMethodsChanged(String commitHash) throws IOException {
               File gitWorkDir = new File("C:/Users/I338008/git/JSParser");
                   git = Git.open(gitWorkDir);
                   repository = git.getRepository();
                   HashSet<String> changedMethods = new HashSet<String>();
                   
                   ObjectId treeId = ObjectId.fromString(commitHash);
                   RevWalk revWalk = new RevWalk(repository);
                   RevCommit commit = revWalk.parseCommit(treeId);
                   RevTree tree = commit.getTree();
                   String[] Pathnames=FindGitCommitFiles(tree);
                   System.out.println("List of files:");
                   for(String path:Pathnames) {
                              System.out.println(path);
                   }
                   
                   for (String filename:Pathnames) {
                              changedMethods.addAll(FindMethodsChangedofFiles(filename,treeId));
                   }
                   return changedMethods;
                   
                   
    }
    
    //******************************************************************************
    /*public ScriptOrFnNode parse() throws IOException {

    	  Object nodeTree;
		if (nodeTree == null) {
    	    Reader reader = new FileReader(jsFile);

    	    CompilerEnvirons compilerEnv = new CompilerEnvirons();
    	    ErrorReporter errorReporter = compilerEnv.getErrorReporter();

    	    Parser parser = new Parser(compilerEnv, errorReporter);

    	    String sourceURI;

    	    try {
    	      sourceURI = jsFile.getCanonicalPath();
    	    } catch (IOException e) {
    	      sourceURI = jsFile.toString();
    	    }

    	    nodeTree = parser.parse(reader, sourceURI, 1);
    	  }
    	  return nodeTree;
    	}*/
    
    //*******************************************************************************
    
    /*public static AstRoot parcing()
    {
    	CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
    	  compilerEnvirons.setRecordingComments(true);
    	  compilerEnvirons.setRecordingLocalJsDocComments(true);
    	  compilerEnvirons.setStrictMode(true);
    	  File testScript = new File("C://Users//I338008//Documents//GIT Documents//JSCurrentFile.txt");
    	  FileReader fileReader = new FileReader(testScript);
    	  AstRoot astRoot = new Parser(compilerEnvirons).parse(fileReader, null, 1);
    	  
    }*/
    
    
    
}
