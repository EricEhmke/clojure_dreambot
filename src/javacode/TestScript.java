package javacode;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.script.impl.TaskScript;

@ScriptManifest(name = "Test Script", description = "Task Script With nodes", author = "Developer Name", version = 1.0, category = Category.WOODCUTTING, image = "")
public class TestScript extends TaskScript {

  static {
    Thread.currentThread().setContextClassLoader(TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("dreambot-test.core"));
    require.invoke(Clojure.read("dreambot-test.banknode"));
  }
  // IFn onLoopClojure = Clojure.var("dreambot-test.core", "onLoop");

  private TaskNode BankTaskNode() {
    IFn BankNode = Clojure.var("dreambot-test.banknode", "BankNode"); // TODO: Figure out how to import and use this
    return (TaskNode) BankNode.invoke();
  }

  @Override
  public void onStart() {
    // One task node for each action (like banking, droping, fishing etc)
    // Task nodes should return their priority
    // return (int) onLoopClojure.invoke();
    MethodProvider.log("Started ");
    addNodes(BankTaskNode()); // TODO: add notes
  }
}