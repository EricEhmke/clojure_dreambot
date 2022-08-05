(ns dreambot-test.utils.walking
  (:require
   [dreambot-test.utils.antiban :as antiban]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.walking.impl Walking]
 [org.dreambot.api.utilities.impl Condition])

(defn isInArea
  "Checks whether the player is in the given area"
  [area]
  (.contains area (Client/getLocalPlayer)))

(defn walkNext
  "Makes a single action to walk to the destination"
  [destinationArea]
  (let [destinationTile (.getRandomTile destinationArea)]
    (MethodProvider/sleep (antiban/pollingTime 2000 200))
    (if (and (Walking/canWalk destinationTile) (Walking/shouldWalk))
      (Walking/walk destinationTile)
      true))) ;; return true to satisfy the behavior tree node as having performed this action

(defn isTraveling
  "Returns a Condition which checks if the player is traveling"
  []
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (.isMoving (Client/getLocalPlayer)))))

(defn findClosestArea
  [areas]
  (apply min-key (fn [x] (.distance (.getNearestTile x (Client/getLocalPlayer)))) areas))

(defn travelTo
  "Travels to the destinationArea uninterrupted"
  [destinationArea]
  (let [currentLocation (fn [] (.getTile (Client/getLocalPlayer)))
        travelFunc (fn [lastLoc]
                     (if (.contains destinationArea (Client/getLocalPlayer))
                       (do
                         (MethodProvider/log "Arrived at destination...")
                         (MethodProvider/sleep (antiban/pollingTime)))
                       (do
                         (walkNext destinationArea)
                         (MethodProvider/sleep (antiban/pollingTime 1000 200))
                         (if (= lastLoc (currentLocation))
                           (MethodProvider/log "Aborting: character potentially stuck...")
                           (recur (.getTile (Client/getLocalPlayer)))))))]
    (travelFunc (currentLocation))))
