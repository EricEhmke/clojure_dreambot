(ns dreambot-test.banknode)

(import org.dreambot.api.script TaskNode)

(defn BankNode
  []
  (proxy [TaskNode] []
    (priority (int 2))
    (accept (.isFull getInventory))
    (execute (.dropAll getInventory) (int 300))))
