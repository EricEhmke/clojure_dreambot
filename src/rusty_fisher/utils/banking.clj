(ns rusty-fisher.utils.banking
  (:require [rusty-fisher.utils.antiban :as antiban]))

(import
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.container.impl.bank Bank])

(defn deposit
  [depositExcept]
  (MethodProvider/log "Attempting to deposit...")
  (when (Bank/open)
    (MethodProvider/log "Bank Opened...")
    (antiban/sleepFor (antiban/reactionDelay 99)) ;; TODO: sleep after we open the bank. Maybe a normal average w/ large stdev?
    (Bank/depositAllExcept (into-array depositExcept))
    (antiban/sleepFor (antiban/reactionDelay 99)) ;; this short sleep after deposit is OK before we close
    (Bank/close)))

(defn walkAndOpenClosest
  "Walks to the closest bank. If at bank, opens it"
  []
  (let [walkOrOpen (Bank/openClosest)]
    (antiban/sleepFor (antiban/reactionDelay)) ;; Sleep to prevent fast actions
    walkOrOpen))
