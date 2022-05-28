(ns dreambot-test.travelnode)

(import [org.dreambot.api.methods.container.impl Inventory])

(defn travelTo
  []
  ;;TODO implement logic for traveling
  false)

(defn TravelNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 3))
    ;; Will return 'true' when these conditions are met
    (accept [] (every? true? []))
    ;; What will execute when this node runs.
    (execute [] (travelTo) (int 3000))))
