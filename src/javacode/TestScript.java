package javacode;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.AbstractScript;

@ScriptManifest(name = "Fishing Script", description = "Task Script With Behavior Trees", author = "Developer Name", version = 2.0, category = Category.FISHING, image = "")
public class TestScript extends AbstractScript {

  static {
    Thread.currentThread().setContextClassLoader(TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("dreambot-test.treetraverse"));
  }

  private Object TraverseTree() {
    IFn TraverseBehaviorTree = Clojure.var("dreambot-test.treetraverse", "traverseTree");
    return TraverseBehaviorTree.invoke();
  }

  @Override
  public void onStart() {
    MethodProvider.log("Starting Fishing Script...");
  }
  //TODO: Create a gui to choose:
  // TODO: Choose type of fish, location, whether to cook, bank location

  @Override
  public int onLoop() {
    TraverseTree();
    return Calculations.random(250, 500);
  }
}
