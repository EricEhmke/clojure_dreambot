(ns dreambot-test.banknode)

(import [org.dreambot.api.methods.container.impl Inventory])

(defn BankNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 2))
    (accept [] (Inventory/isFull))
    (execute [] (Inventory/dropAll) (int 3000))))
