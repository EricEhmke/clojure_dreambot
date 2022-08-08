(ns dreambot-test.utils.behaviortree
  (:require [dreambot-test.utils.walking :as walking]
            [dreambot-test.utils.antiban :as antiban]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.dialogues Dialogues]
 [org.dreambot.api.utilities.impl Condition])

(defn travelTo
  "A fallback node for traveling to a destination"
  [destination]
  ;; TODO: use an atom to handle character being stuck (check that the char is not at its last known loc))
  (MethodProvider/log "Walking to destination")
  (or (walking/isInArea destination) (walking/walkNext destination)))

(defn clearDialogue
  []
  (when (and (Dialogues/inDialogue) (Dialogues/canContinue))
    (Dialogues/continueDialogue)
    (MethodProvider/sleep (antiban/pollingTime))))
