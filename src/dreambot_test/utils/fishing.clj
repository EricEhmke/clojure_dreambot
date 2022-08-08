(ns dreambot-test.utils.fishing
  (:require [dreambot-test.utils.walking :as walk]
            [dreambot-test.utils.antiban :as antiban]
            [dreambot-test.utils.utilities :as utils]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition]
 [org.dreambot.api.methods.input Camera])

(def fishTypeInteract
  "Maps the fish type to the correct right-click actions.
  Used to identify if you can fish the desired fish at a spot"
  {:Lobster ["Cage"]
   :Shrimp ["Small Net" "Net"]
   :Anchovies ["Small Net" "Net"]})

(defn hasActionFilter
  "Filters NPC that have an actionName in their right-click menu"
  [actionNames]
  (proxy [org.dreambot.api.methods.filter.Filter] []
    (match [npc]
      (if (nil? npc)
        false
        (let [hasAction (some #(.hasAction npc (into-array [%])) actionNames)]
          (if (nil? hasAction)
            false
            true))))))

(defn isFishing
  "Returns a Condition which checks if the player is animating"
  [fishingSpot]
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (if (< 40 (rand-int 100)) ;; 40% of the time we notice if the fishing spot has moved.
        (.isAnimating (Client/getLocalPlayer))
        (.exists fishingSpot)))))

(defn getCorrectAction
  [npc fishType]
  (let [interactOptions (fishTypeInteract (keyword fishType))]
    (some #(when (.hasAction npc (into-array [%])) %) interactOptions)))

(defn goFishing
  "Fishs"
  [fishType]
  (let [fishingSpot  (NPCs/closest (hasActionFilter (fishTypeInteract (keyword fishType))))
        isFishing (isFishing fishingSpot)]
    (if (not (nil? fishingSpot))
      (do
        (when (not (.isOnScreen fishingSpot))
          (Camera/rotateToEntity fishingSpot))
        (if (.interact fishingSpot (getCorrectAction fishingSpot fishType)) ;; call getActions on the entity and call the action that the entity has
          (do
            (MethodProvider/log "Interacted with fishing spot...")
            (when (> 70 (rand-int 100)) (antiban/moveMouseOutOfScreen))
            (MethodProvider/sleep 1000) ;; Allows time for the client to register a character as moving
            (MethodProvider/sleepWhile (walk/isTraveling) utils/timeOutTime (antiban/pollingTime))
            (while (.verify isFishing)
              (when (> (Client/getIdleTime) (antiban/pollingTime 300000 200000))
                (antiban/antiLogout))
              (MethodProvider/sleep (antiban/pollingTime 16000 5000)))
            (utils/clearDialogue) ;; Clear level up or inventory full dialogues
            true)
          false))

      (do
        (MethodProvider/log "No suitable fishing spot in area...")
        false))))
