(ns dreambot-test.utils.utilities
  (:require
   [dreambot-test.utils.antiban :as antiban]))

(import
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.dialogues Dialogues])

(def timeOutTime (int 330000)) ;; 5.5 minutes

(defn clearDialogue
  "Clears any dialogue with the Continue... text"
  []
  (when (and (Dialogues/inDialogue) (Dialogues/canContinue))
    (Dialogues/continueDialogue)
    (MethodProvider/sleep (antiban/pollingTime))))
