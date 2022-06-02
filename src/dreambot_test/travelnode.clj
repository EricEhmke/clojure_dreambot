(ns dreambot-test.travelnode
  (:use clojure.core.matrix.random))
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

;; TODO these are values that can be passed in
(def timeOutTime (int 330000)) ;; 5.5 minutes

;; TODO these are values that can be passed in
(def pollingTime
  "Generates a normally distrubted polling time along a guasian distribution"
  (fn [] (let [pollTime (rand-gaussian 20000 10000)]
           (if (< 200 pollTime)
             pollTime
             (recur)))))

(defn travelTo
  []
  ;;TODO implement logic for traveling
  (let [currentLocation (fn [] (.getTile (Client/getLocalPlayer)))
        travelFunc (fn [lastLoc]
                     (if (Bank/openClosest)
                       (do
                         (MethodProvider/log "At bank")
                         (MethodProvider/log "One travel loop completed.")
                         (MethodProvider/sleep 10000))
                       (do
                         (MethodProvider/log "Not at bank sleeping for sec")
                         (MethodProvider/sleep 750) ;;TODO: Put a random small-ish number here
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
    (execute [] (travelTo) (int 3000))))
