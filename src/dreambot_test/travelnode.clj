(ns dreambot-test.travelnode
  (:require [dreambot-test.utils.utilities :as utils]))
;; TODO: In the future I want to pass a callback or something to this function. There will be too much duplciation doing it this way
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.utilities.impl Condition])

(defn inventoryIsFull
  "Checks if player's inventory is full"
  []
  (Inventory/isFull))

;; (defn atDestination
;;   "Checks if the player is at the given destination"
;;   []
;;   ;; Catherby bank
;;   (.contains (new Area 2806 3441 2811 3438) (Client/getLocalPlayer)))

(defn isTraveling
  "Returns a Condition which checks if the player is traveling"
  []
  (reify Condition
    (verify [this]
      "Evaluates cond and returns a bool"
      (.isMoving (Client/getLocalPlayer)))))

(defn travelTo
  []
  ;;TODO implement logic for traveling
  (let [currentLocation (fn [] (.getTile (Client/getLocalPlayer)))
        travelFunc (fn [lastLoc]
                     (if (Bank/openClosest)
                       (do
                         (MethodProvider/log "Arrived at bank...")
                         (MethodProvider/sleep (utils/pollingTime)))
                       (do
                         (MethodProvider/log "Not at bank sleeping for sec")
                         (MethodProvider/sleep (utils/pollingTime 1.2 0.2))
                         (if (= lastLoc (currentLocation))
                           (MethodProvider/log "Aborting: character potentially stuck...")
                           (recur (.getTile (Client/getLocalPlayer)))))))]
    (travelFunc (currentLocation))))

(defn TravelNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 3))
    ;; Will return 'true' when these conditions are met
    (accept [] (MethodProvider/log "Evaluating whether to travel...") (and (inventoryIsFull)))
    ;; What will execute when this node runs.
    (execute [] (travelTo) (int 5000))))
