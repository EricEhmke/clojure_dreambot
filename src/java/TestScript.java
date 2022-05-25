package testscriptshim;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(name = "Test Script", description = "With Clojure",
                author = "Developer Name", version = 1.0,
                category = Category.WOODCUTTING, image = "")
public class TestScript extends AbstractScript {

  static {
    Thread.currentThread().setContextClassLoader(
        TestScript.class.getClassLoader());
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("dreambot_test.core"));
  }

  @Override
  public int onLoop() {
    MethodProvider.log("My first script");
    return 1000;
  }
}
