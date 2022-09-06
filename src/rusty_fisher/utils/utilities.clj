(ns rusty-fisher.utils.utilities
  (:require
   [rusty-fisher.utils.antiban :as antiban]))

(import
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.dialogues Dialogues]
 [org.dreambot.api.script ScriptManager])

(defn clearDialogue
  "Clears any dialogue with the Continue... text"
  []
  (when (and (Dialogues/inDialogue) (Dialogues/canContinue))
    (Dialogues/continueDialogue)
    (antiban/sleepFor (antiban/reactionDelay 95))))

(defn quitScript
  "Quits the dreambot script and logs optional message"
  ([]
   (quitScript "Quitting Script..."))
  ([quitMessage]
   (MethodProvider/log quitMessage)
   (.stop (ScriptManager/getScriptManager))))
