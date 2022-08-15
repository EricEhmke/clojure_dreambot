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
    (MethodProvider/sleep (antiban/pollingTime))
    (Bank/depositAllExcept (into-array depositExcept))
    (MethodProvider/sleep (antiban/pollingTime))
    (Bank/close)))

(defn walkAndOpenClosest
  "Walks to the closest bank. If at bank, opens it"
  []
  (let [walkOrOpen (Bank/openClosest)]
    (MethodProvider/sleep (antiban/pollingTime)) ;; Sleep to prevent fast actions
    walkOrOpen))
