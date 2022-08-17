(ns rusty-fisher.utils.behaviortree
  (:require [rusty-fisher.utils.walking :as walking]
            [rusty-fisher.utils.antiban :as antiban]))

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
    (MethodProvider/sleep (antiban/reactionDelay 95))))
