(ns rusty-fisher.utils.fishing
  (:require [rusty-fisher.utils.walking :as walk]
            [rusty-fisher.utils.antiban :as antiban]
            [rusty-fisher.utils.utilities :as utils]
            [rusty-fisher.constants :as constants]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition]
 [org.dreambot.api.methods.input Camera]
 [java.util Arrays])

(def fishMap
  {:Lobster {:interactCommand ["Cage"] :requiredActions [["Cage" "Harpoon"]] :requiredEquipment ["Lobster pot"]}
   :Shrimp {:interactCommand ["Small Net" "Net"] :requiredActions [["Small Net" "Bait"] ["Net" "Bait"]] :requiredEquipment ["Small fishing net"]} ;; Items in nested lists: each item is OR
   :Anchovies {:interactCommand ["Small Net" "Net"] :requiredActions [["Small Net" "Bait"] ["Net" "Bait"]] :requiredEquipment ["Small fishing net"]}
   :Trout {:interactCommand ["Lure"] :requiredActions [["Lure" "Bait"]] :requiredEquipment ["Fly fishing rod" "Feather"]}
   :Salmon {:interactCommand ["Lure"] :requiredActions [["Lure" "Bait"]] :requiredEquipment ["Fly fishing rod" "Feather"]}
   :Pike {:interactCommand ["Bait"] :requiredActions [["Lure" "Bait"]] :requiredEquipment ["Fishing rod" "Fishing bait"]}
   :Tuna {:interactCommand ["Harpoon"] :requiredActions [["Cage" "Harpoon"] ["Net" "Harpoon"]] :requiredEquipment ["Harpoon"]}
   :Swordfish {:interactCommand ["Harpoon"] :requiredActions [["Cage" "Harpoon"] ["Net" "Harpoon"]] :requiredEquipment ["Harpoon"]}
   :Shark {:interactCommand ["Harpoon"] :requiredActions [["Net" "Harpoon"] ["Big Net" "Harpoon"]] :requiredEquipment ["Harpoon"]}
   :Sardine {:interactCommand ["Bait"] :requiredActions [["Lure" "Bait"] ["Net" "Bait"]] :requiredEquipment ["Fishing rod" "Fishing bait"]}
   :Herring {:interactCommand ["Bait"] :requiredActions [["Lure" "Bait"] ["Net" "Bait"]] :requiredEquipment ["Fishing rod" "Fishing bait"]}
   :Cod {:interactCommand ["Net"] :requiredActions [["Net" "Harpoon"] ["Big Net" "Harpoon"]] :requiredEquipment ["Big fishing net"]}
   :Bass {:interactCommand ["Net"] :requiredActions [["Net" "Harpoon"] ["Big Net" "Harpoon"]] :requiredEquipment ["Big fishing net"]}
   :Mackerel {:interactCommand ["Net"] :requiredActions [["Net" "Harpoon"] ["Big Net" "Harpoon"]] :requiredEquipment ["Big fishing net"]}})

(defn arrayHasItems
  "Checks if one array contains all items of another array"
  [array items]
  (every? #(.contains (Arrays/asList array) %) items))

(defn hasActionFilter
  "Filters NPC that have an actionName in their right-click menu"
  [actionNames]
  (proxy [org.dreambot.api.methods.filter.Filter] []
    (match [npc]
      (if (nil? npc)
        false
        (let [hasAction (some #(arrayHasItems (.getActions npc) %) actionNames)]
          (if (nil? hasAction)
            false
            true))))))

(defn isFishing
  "Returns a Condition which checks if the player is fishing"
  [fishingSpot]
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (if (< 40 (rand-int 100)) ;; 40% of the time we notice if the fishing spot has moved while we're still in fishing animation.
        (.isAnimating (Client/getLocalPlayer))
        (.exists fishingSpot)))))

(defn getCorrectAction
  [npc fishType]
  (let [interactOptions (get-in fishMap [(keyword fishType) :interactCommand])]
    (some #(when (.hasAction npc (into-array [%])) %) interactOptions)))

(defn goFishing
  "Fishs"
  [fishType]
  (let [fishingSpot  (NPCs/closest (hasActionFilter (get-in fishMap [(keyword fishType) :requiredActions])))
        isFishing (isFishing fishingSpot)]
    (if (not (nil? fishingSpot))
      (do
        (when (not (.isOnScreen fishingSpot))
          (Camera/rotateToEntity fishingSpot))

        (if (.interact fishingSpot (getCorrectAction fishingSpot fishType)) ;; call getActions on the entity and call the action that the entity has
          (do
            (MethodProvider/log "Interacted with fishing spot...")
            (when (> 70 (rand-int 100)) (antiban/moveMouseOutOfScreen))
            (antiban/sleepFor 1000) ;; Allows time for the client to register a character as moving
            (MethodProvider/sleepWhile (walk/isTraveling) constants/timeOutTime (antiban/reactionDelayActive))
            (antiban/sleepFor 9000) ;; Allow time to begin fishing at slow spots (trout/salmon)
            (while (.verify isFishing)
              (when (> (Client/getIdleTime) (antiban/reactionDelay 10))
                (antiban/antiLogout))
              (antiban/sleepFor (antiban/pollingTime 16000 5000)))
            (utils/clearDialogue) ;; Clear level up or inventory full dialogues
            true)
          false))

      (do
        (MethodProvider/log "No suitable fishing spot in area...")
        false))))
