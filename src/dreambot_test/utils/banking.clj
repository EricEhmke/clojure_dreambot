(ns dreambot-test.utils.banking
  (:require [dreambot-test.utils.antiban :as antiban]))

(import
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.container.impl.bank Bank])

(defn deposit
  [item]
  (when (Bank/open)
    (MethodProvider/log "Bank Opened...")
    (MethodProvider/sleep (antiban/pollingTime))
    (Bank/depositAllExcept item)
    (MethodProvider/sleep (antiban/pollingTime))
    (Bank/close)))
