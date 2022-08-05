package javacode;

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

@ScriptManifest(name = "Fishing Script", description = "Task Script With Behavior Trees", author = "Developer Name", version = 2.0, category = Category.FISHING, image = "")
public class TestScript extends AbstractScript {

  Map<String, Object> config = new HashMap<>();

  static {
    Thread.currentThread().setContextClassLoader(TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("dreambot-test.treetraverse"));
    require.invoke(Clojure.read("dreambot-test.gui"));
  }

  private Object Gui(Object scriptConfig) {
    IFn createGui = Clojure.var("dreambot-test.gui", "createGui");
    return createGui.invoke(scriptConfig);
  }

  private Object TraverseTree(Object scriptConfig) {
    IFn TraverseBehaviorTree = Clojure.var("dreambot-test.treetraverse", "traverseTree");
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
