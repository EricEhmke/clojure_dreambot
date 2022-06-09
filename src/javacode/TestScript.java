package javacode;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import io.netty.channel.VoidChannelPromise;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.script.AbstractScript;

@ScriptManifest(name = "Fishing Script", description = "Task Script With Behavior Trees", author = "Developer Name", version = 2.0, category = Category.FISHING, image = "")
public class TestScript extends AbstractScript {

  static {
    Thread.currentThread().setContextClassLoader(TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    // require.invoke(Clojure.read("dreambot-test.banknode"));
    // require.invoke(Clojure.read("dreambot-test.fishnode"));
    // require.invoke(Clojure.read("dreambot-test.travelnode"));
    // require.invoke(Clojure.read("dreambot-test.utils.utilities"));
    require.invoke(Clojure.read("dreambot-test.treetraverse"));
  }

  // private TaskNode BankTaskNode() {
  // IFn BankNode = Clojure.var("dreambot-test.banknode", "BankNode");
  // return (TaskNode) BankNode.invoke();
  // }

  // private TaskNode FishingTaskNode() {
  // IFn FishingNode = Clojure.var("dreambot-test.fishnode", "FishNode");
  // return (TaskNode) FishingNode.invoke();
  // }

  // private TaskNode TravelNode() {
  // IFn TravelNode = Clojure.var("dreambot-test.travelnode", "TravelNode");
  // return (TaskNode) TravelNode.invoke();
  // }

  private Object TraverseTree() {
    IFn TraverseBehaviorTree = Clojure.var("dreambot-test.treetraverse", "traverseTree");
    return TraverseBehaviorTree.invoke();
  }

  @Override
  public void onStart() {
    MethodProvider.log("Starting Fishing Script...");
  }

  @Override
  public int onLoop() {
    // One task node for each action (like banking, droping, fishing etc)
    // Task nodes should return their priority
    // TODO Call the clojure script that tick through the tree
    TraverseTree();
    return Calculations.random(250, 500);
  }
}
