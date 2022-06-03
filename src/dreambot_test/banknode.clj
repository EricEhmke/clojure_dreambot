(ns dreambot-test.banknode
  (require [dreambot-test.core.utils.utilites :as utils]))

(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.utilities.impl Condition])

(defn interactWithBank
  [])

(defn BankNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 2))
    (accept [] (MethodProvider/log "Evaluating whether to bank...") (Bank/isOpen))
    (execute [] (interactWithBank) (int 3000))))
