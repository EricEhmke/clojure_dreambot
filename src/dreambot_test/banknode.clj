(ns dreambot-test.banknode
  (:require [dreambot-test.utils.utilities :as utils]))

(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.utilities.impl Condition])

(defn interactWithBank
  []
  (Bank/depositAll "Lobster")
  (Bank/close)
  ;; You can exit the bank with the close method or just walk away.
  ;; I normally just walk away.
  )

(defn BankNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 2))
    (accept [] (MethodProvider/log "Evaluating whether to bank...") (and (Bank/isOpen)))
    (execute [] (interactWithBank) (int 3000))))
