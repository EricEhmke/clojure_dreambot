(ns dreambot-test.travelnode)

(import [org.dreambot.api.methods.container.impl Inventory])

(defn TravelNode
  [location conditions]
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 3))
    ;; Will return 'true' when these conditions are met
    (accept [] (every? true? conditions))
    ;; What will execute when this node runs.
    (execute [] (travelTo location) (int 3000))))

(defn travelTo
  [location]
  ;;TODO implement logic for traveling
  )
