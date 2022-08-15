package javaCode;

import clojure.stacktrace__init;
import clojure.java.api.Clojure;
import clojure.lang.IFn;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.map.Area;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

@ScriptManifest(name = "Rusty Fisher", description = "Fishing script supporting trout, salmon, lobster, shark and more.", author = "Rusty", version = 1.0, category = Category.FISHING, image = "")
public class javaShim extends AbstractScript {

  Map<String, Object> config = new HashMap<>();

  static {
    Thread.currentThread().setContextClassLoader(javaShim.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("rusty-fisher.treetraverse"));
    require.invoke(Clojure.read("rusty-fisher.gui"));
  }

  private Object Gui(Object scriptConfig) {
    IFn createGui = Clojure.var("rusty-fisher.gui", "createGui");
    return createGui.invoke(scriptConfig);
  }

  private Object TraverseTree(Object scriptConfig) {
    IFn TraverseBehaviorTree = Clojure.var("rusty-fisher.treetraverse", "traverseTree");
    return TraverseBehaviorTree.invoke(scriptConfig);
  }

  private Runnable goGui = new Runnable() {
    public void run() {
      Gui(config);
    }
  };

  @Override
  public void onStart() {
    try {
      SwingUtilities.invokeLater(goGui);
    } catch (Exception e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      MethodProvider.logError(exceptionAsString);
      stop();
    }
    MethodProvider.log("Starting Fishing Script...");
  }

  @Override
  public int onLoop() {
    TraverseTree(config);
    return Calculations.random(250, 500);
  }
}
