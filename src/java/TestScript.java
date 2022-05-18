package testscriptshim;

// import clojure.java.api.Clojure;
// import clojure.lang.IFn;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(name = "Script Name", description = "My script description!",
                author = "Developer Name", version = 1.0,
                category = Category.WOODCUTTING, image = "")
public class TestScript extends AbstractScript {

  @Override
  public int onLoop() {
    return 0;
  }
}
