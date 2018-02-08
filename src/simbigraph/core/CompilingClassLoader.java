package simbigraph.core;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.tools.*;

import javax.tools.JavaCompiler.*;
import org.eclipse.jdt.internal.compiler.tool.*;


/**
 * @author Eugene Ershov
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 *
 * A CompilingClassLoader compiles your Java source on-the-fly. It
 * checks for nonexistent .class files, or .class files that are older
 * than their corresponding source code.
 */
public class CompilingClassLoader extends ClassLoader
{
	private static final long serialVersionUID = 1L;
	
	public Class compileClass(String code, String className)
	{
		try
		{
			boolean webStartCompiler = true;
			
			/*if (webStartCompiler)
			{
				CustomEclipseCompiler compiler = new CustomEclipseCompiler();
				Class compiledClass = compiler.compile(className, code);

				if (compiledClass != null)
				{
					return compiledClass;
				}
				else
				{
					mainWindow.getBottomTabbedPane().setSelectedIndex(2);
				}
			}
			else*/
			{
				JavaCompiler javac = new EclipseCompiler();//ToolProvider.getSystemJavaCompiler();
				

				//MyDiagnosticListener diagnosticListener = new MyDiagnosticListener();
				DiagnosticListener<JavaFileObject> diagnosticListener = new DiagnosticListener<JavaFileObject>()
				{
					@Override
					public void report(Diagnostic<? extends JavaFileObject> diagnostic)
					{
					/*	DefaultTableModel model = (DefaultTableModel)mainWindow.getProblemsTable().getModel();
						model.addRow(new Object[]{diagnostic.getKind(),
								diagnostic.getMessage(Locale.ENGLISH).replace("\n", " "),
								"Line: " + diagnostic.getLineNumber() + ", column: " + diagnostic.getColumnNumber(),
						});*/
						//mainWindow.getProblemsTable().getColumnModel().getColumn(0).setCellRenderer(new TableCellIconRenderer(new ImageIcon(getClass().getResource("/icons/IconError.png")), "Error"));
					}
				};

				StandardJavaFileManager sjfm = javac.getStandardFileManager(diagnosticListener, null, null);

				//List<File> classPaths = Arrays.asList(new File("file:///C:/qwe/qwe.jar"));
				//sjfm.setLocation(StandardLocation.CLASS_PATH, classPaths);

				SpecialClassLoader cl = new SpecialClassLoader();
				SpecialJavaFileManager fileManager = new SpecialJavaFileManager(sjfm, cl);


				/*ZERO_ARGUMENT_OPTIONS = new HashSet<String>();
				ZERO_ARGUMENT_OPTIONS.add("-progress");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-proceedOnError");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-time");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-v");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-version");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-showversion");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-deprecation");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-help");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-?");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-help:warn");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-?:warn");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-noExit");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-verbose");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-referenceInfo");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-inlineJSR");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-g");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-g:none");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-nowarn");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-warn:none");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-preserveAllLocals");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-enableJavadoc");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-Xemacs");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-X");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-O");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-1.3");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-1.4");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-1.5");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-5");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-5.0");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-1.6");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-6");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-6.0");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-proc:only");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-proc:none");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-XprintProcessorInfo");//$NON-NLS-1$
				ZERO_ARGUMENT_OPTIONS.add("-XprintRounds");//$NON-NLS-1$

				FILE_MANAGER_OPTIONS = new HashSet<String>();
				FILE_MANAGER_OPTIONS.add("-bootclasspath");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-encoding");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-d");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-classpath");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-cp");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-sourcepath");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-extdirs");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-endorseddirs");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-s");//$NON-NLS-1$
				FILE_MANAGER_OPTIONS.add("-processorpath");//$NON-NLS-1$

				ONE_ARGUMENT_OPTIONS = new HashSet<String>();
				ONE_ARGUMENT_OPTIONS.addAll(FILE_MANAGER_OPTIONS);
				ONE_ARGUMENT_OPTIONS.add("-log");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-repeat");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-maxProblems");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-source");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-target");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-processor");//$NON-NLS-1$
				ONE_ARGUMENT_OPTIONS.add("-classNames");//$NON-NLS-1$*/

				Writer out = new PrintWriter(System.err);
				List<String> options = Arrays.asList("-nowarn", "-time", "-O");//Collections.emptyList();
				Iterable<String> classes = null;
				List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, code));

				CompilationTask compile = javac.getTask(out, fileManager, diagnosticListener, options, classes, compilationUnits);
				fileManager.close();

				if (compile.call())
				{
					return cl.findClass(className);
				}
				else
				{
				//	mainWindow.getBottomTabbedPane().setSelectedIndex(2);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}


	// Given a filename, read the entirety of that file from disk
	// and return it as a byte array.
	private byte[] getBytes(String filename) throws IOException
	{
		// Find out the length of the file
		File file = new File(filename);
		long len = file.length();
		// Create an array that's just the right size for the file's
		// contents
		byte raw[] = new byte[(int)len];
		// Open the file
		FileInputStream fin = new FileInputStream(file);
		// Read all of it into the array; if we don't get all,
		// then it's an error.
		int r = fin.read(raw);
		if (r != len)
			throw new IOException("Can't read all, " + r + " != " + len);
		// Don't forget to close the file!
		fin.close();
		// And finally return the file contents as an array
		return raw;
	}

	// Spawn a process to compile the java source code file
	// specified in the 'javaFile' parameter. Return a true if
	// the compilation worked, false otherwise.
	private boolean compile(String javaFile) throws IOException
	{
//		DefaultTableModel model = (DefaultTableModel)mainWindow.getProblemsTable().getModel();
//		model.setRowCount(0);

		MyDiagnosticListener listener = new MyDiagnosticListener();

		JavaCompiler compiler = new EclipseCompiler();//ToolProvider.getSystemJavaCompiler();
		List<String> options = Arrays.asList("-nowarn", "-time", "-O");

		if (compiler == null)
		{
			throw new NullPointerException("Cannot provide system compiler.");
		}

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(listener, null, null);

		//Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjects(javaFile);
		CompilationTask task = compiler.getTask(null, fileManager, listener, options, null, fileManager.getJavaFileObjects(javaFile));
		fileManager.close();
		return task.call();
	}

	// The heart of the ClassLoader -- automatically compile
	// source as necessary when looking for class files
	public Class loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		// Our goal is to get a Class object
		Class clas = null;
		// First, see if we've already dealt with this one
		clas = findLoadedClass(name);
		//System.out.println("findLoadedClass: " + clas);
		// Create a pathname from the class name
		// E.g. java.lang.Object => java/lang/Object
		String fileStub = name.replace('.', '/');
		// Build objects pointing to the source code (.java) and object
		// code (.class)
		String javaFilename = System.getProperty("java.io.tmpdir") + "/simbigraphTMP/" + fileStub + ".java";
		String classFilename = System.getProperty("java.io.tmpdir") + "/simbigraphTMP/" + fileStub + ".class";
		File javaFile = new File(javaFilename);
		File classFile = new File(classFilename);
		//System.out.println("j " + javaFile.lastModified() + " c " + 
		// classFile.lastModified());
		//   First, see if we want to try compiling. We do if (a) there
		//   is source code, and either (b0) there is no object code,
		//   or (b1) there is object code, but it's older than the source
		if   (javaFile.exists() && (!classFile.exists() || javaFile.lastModified() > classFile.lastModified()))
		{
			try
			{
				// Try to compile it. If this doesn't work, then
				// we must declare failure. (It's not good enough to use
				// and already-existing, but out-of-date, classfile)
				if (!compile(javaFilename) || !classFile.exists())
				{
					//jTabbedPaneWorkspace.setSelectedIndex(jTabbedPaneWorkspace.getTabCount() - 1);
					//mainWindow.getBottomTabbedPane().setSelectedIndex(2);
					throw new ClassNotFoundException("Compile failed: " + javaFilename);
				}
			}
			catch(IOException ie)
			{
				// Another place where we might come to if we fail
				// to compile
				throw new ClassNotFoundException(ie.toString());
			}
		}
		// Let's try to load up the raw bytes, assuming they were
		// properly compiled, or didn't need to be compiled
		try
		{
			// read the bytes
			byte raw[] = getBytes(classFilename);
			// try to turn them into a class
			clas = defineClass(name, raw, 0, raw.length);
		}
		catch(IOException ie)
		{
			// This is not a failure! If we reach here, it might
			// mean that we are dealing with a class in a library,
			// such as java.lang.Object
		}
		//System.out.println("defineClass: " + clas);
		// Maybe the class is in a library -- try loading
		// the normal way
		if (clas == null)
		{
			clas = findSystemClass(name);
		}
		//System.out.println("findSystemClass: " + clas);
		// Resolve the class, if any, but only if the "resolve"
		// flag is set to true

		if (resolve && clas != null)
			resolveClass(clas);
		// If we still don't have a class, it's an error
		if (clas == null)
			throw new ClassNotFoundException(name);
		// Otherwise, return the class
		return clas;
	}

	class MyDiagnosticListener implements DiagnosticListener<JavaFileObject>
	{
		public void report(Diagnostic<? extends JavaFileObject> diagnostic)
		{
//			System.out.println("Code->" +  diagnostic.getCode());
//			System.out.println("Column Number->" + diagnostic.getColumnNumber());
//			System.out.println("End Position->" + diagnostic.getEndPosition());
//			System.out.println("Kind->" + diagnostic.getKind());
//			System.out.println("Line Number->" + diagnostic.getLineNumber());
//			System.out.println("Message->"+ diagnostic.getMessage(Locale.ENGLISH));
//			System.out.println("Position->" + diagnostic.getPosition());
//			System.out.println("Source" + diagnostic.getSource());
//			System.out.println("Start Position->" + diagnostic.getStartPosition());
//			System.out.println("\n");
//
//			try {
//				System.out.println(diagnostic.getSource().getCharContent(true).toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}


			/*DefaultTableModel model = (DefaultTableModel)mainWindow.getProblemsTable().getModel();
			model.addRow(new Object[]{diagnostic.getKind(),
					diagnostic.getMessage(Locale.ENGLISH).replace("\n", " "),
					"Line: " + diagnostic.getLineNumber() + ", column: " + diagnostic.getColumnNumber(),
			});
			mainWindow.getProblemsTable().getColumnModel().getColumn(0).setCellRenderer(new TableCellIconRenderer(new ImageIcon(getClass().getResource("/icons/IconError.png")), "Error"));*/
			//mainWindow.getProblemsTable().setValueAt(diagnostic.getKind(), mainWindow.getProblemsTable().getRowCount() - 1, 0);
			//mainWindow.getProblemsTable().setValueAt(diagnostic.getMessage(Locale.ENGLISH), mainWindow.getProblemsTable().getRowCount() - 1, 1);
			//mainWindow.getProblemsTable().setValueAt("Line: " + diagnostic.getLineNumber() + ", column: " + diagnostic.getColumnNumber(), mainWindow.getProblemsTable().getRowCount() - 1, 2);
		}
	}
}



class MemorySource extends SimpleJavaFileObject
{
	private String src;
	
	public MemorySource(String name, String src)
	{
		super(URI.create("file:///" + name + ".java"), Kind.SOURCE);
		this.src = src;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		return src;
	}
	
	public OutputStream openOutputStream()
	{
		throw new IllegalStateException();
	}
	
	public InputStream openInputStream()
	{
		return new ByteArrayInputStream(src.getBytes());
	}
}

class MemoryByteCode extends SimpleJavaFileObject
{
	private ByteArrayOutputStream baos;
	
	public MemoryByteCode(String name)
	{
		super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
	}
	
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		throw new IllegalStateException();
	}
	
	public OutputStream openOutputStream()
	{
		baos = new ByteArrayOutputStream();
		return baos;
	}
	
	public InputStream openInputStream()
	{
		throw new IllegalStateException();
	}
	
	public byte[] getBytes()
	{
		return baos.toByteArray();
	}
}

class SpecialClassLoader extends ClassLoader
{
	private Map<String, MemoryByteCode> m = new HashMap<String, MemoryByteCode>();

	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		MemoryByteCode mbc = m.get(name);
		if (mbc == null)
		{
			mbc = m.get(name.replace(".","/"));
			if (mbc == null)
			{
				return super.findClass(name);
			}
		}
		return defineClass(name, mbc.getBytes(), 0, mbc.getBytes().length);
	}

	public void addClass(String name, MemoryByteCode mbc)
	{
		m.put(name, mbc);
	}
}

class SpecialJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
{
	public SpecialClassLoader xcl;
	
	public SpecialJavaFileManager(StandardJavaFileManager sjfm, SpecialClassLoader xcl)
	{
		super(sjfm);
		this.xcl = xcl;
	}
	
	public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException
	{
		MemoryByteCode mbc = new MemoryByteCode(name);
		xcl.addClass(name, mbc);
		return mbc;
	}

	public ClassLoader getClassLoader(Location location)
	{
		return xcl;
	}
}