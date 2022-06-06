package javacode;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.script.impl.TaskScript;

@ScriptManifest(name = "Fishing Script", description = "Task Script With nodes", author = "Developer Name", version = 1.0, category = Category.WOODCUTTING, image = "")
public class TestScript extends TaskScript {

  static {
    Thread.currentThread().setContextClassLoader(TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("dreambot-test.banknode"));
    require.invoke(Clojure.read("dreambot-test.fishnode"));
    require.invoke(Clojure.read("dreambot-test.travelnode"));
    require.invoke(Clojure.read("dreambot-test.utils.utilities"));
  }

  private TaskNode BankTaskNode() {
    IFn BankNode = Clojure.var("dreambot-test.banknode", "BankNode");
    return (TaskNode) BankNode.invoke();
  }

  private TaskNode FishingTaskNode() {
    IFn FishingNode = Clojure.var("dreambot-test.fishnode", "FishNode");
    return (TaskNode) FishingNode.invoke();
  }

  private TaskNode TravelNode() {
    IFn TravelNode = Clojure.var("dreambot-test.travelnode", "TravelNode");
    return (TaskNode) TravelNode.invoke();
  }

  @Override
  public void onStart() {
    // One task node for each action (like banking, droping, fishing etc)
    // Task nodes should return their priority

    MethodProvider.log("Staring Fishing Script...");
    addNodes(FishingTaskNode(), TravelNode()); //
  }
}
