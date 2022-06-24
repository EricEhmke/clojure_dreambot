(ns dreambot-test.utils.walking
  (:require
   [dreambot-test.utils.antiban :as antiban]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.walking.impl Walking]
 [org.dreambot.api.utilities.impl Condition])

(defn isAtDestination
  "Checks whether the player is in the destination area"
  [destinationArea]
  (.contains destinationArea (Client/getLocalPlayer)))

(defn walkNext
  "Makes a single action to walk to the destination"
  [destinationArea]
  (let [walked (Walking/walk (.getRandomTile destinationArea))]
    (MethodProvider/sleep (antiban/pollingTime 2000 200))
    walked))

(defn isTraveling
  "Returns a Condition which checks if the player is traveling"
  []
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (.isMoving (Client/getLocalPlayer)))))

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
                         (Walking/walk (.getRandomTile destinationArea))
                         (MethodProvider/sleep (antiban/pollingTime 1000 200))
                         (if (= lastLoc (currentLocation))
                           (MethodProvider/log "Aborting: character potentially stuck...")
                           (recur (.getTile (Client/getLocalPlayer)))))))]
    (travelFunc (currentLocation))))
