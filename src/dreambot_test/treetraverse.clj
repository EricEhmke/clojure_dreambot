(ns dreambot-test.treetraverse
  (:require [dreambot-test.utils.fishing :as fishing]
            [dreambot-test.utils.banking :as banking]
            [dreambot-test.utils.inventory :as inv]
            [dreambot-test.utils.behaviortree :as btree]
            [dreambot-test.utils.areas :as areas]
            [dreambot-test.utils.equipment :as equipment]
            [dreambot-test.utils.walking :as walking]))

(import
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api Client])

(defn bankSequence
  [depositAllExcept cookItem]
  (and (inv/inventoryIsFull)
       (not (Inventory/contains cookItem))
       (banking/walkAndOpenClosest)
       (banking/deposit depositAllExcept)))

(defn cookSequence
  [cookItem cookingArea]
  (and (inv/inventoryIsFull)
       (inv/hasRequiredItems cookItem)
       (btree/travelTo (areas/cookingAreas (keyword cookingArea)))))

(def fishingSpots (atom []))

(defn travelToFishingSpot
  []
  (let [closestFishingSpot (walking/findClosestArea @fishingSpots)
        atDestination (walking/isInArea closestFishingSpot)]
    (if atDestination
      ;; Remove this spot from the atom since its been visited
      (do
        (swap! fishingSpots #(remove (fn [n] (= n closestFishingSpot)) %))
        atDestination)
      (walking/walkNext closestFishingSpot))))

(defn fishSequence
  [fishType fishingZone]
  (when (empty? @fishingSpots) (reset! fishingSpots (areas/fishingAreas (keyword fishingZone))))
  (and (inv/hasRequiredItems (equipment/requiredFishingEquipment (keyword fishType)))
       (inv/hasInventorySpace)
       (or
        (and (fishing/goFishing fishType)
            ;; Reset the atom/fishingSpots when we find a spot
             (reset! fishingSpots (areas/fishingAreas (keyword fishingZone))))
        (travelToFishingSpot))))

(defn traverseTree
  [scriptConfig]
  (let [scriptConfig (into {} scriptConfig)]
    (when (true? (scriptConfig :start))
      (or (fishSequence (scriptConfig :fishType) (scriptConfig :fishingArea))
          (bankSequence (equipment/requiredFishingEquipment (keyword (scriptConfig :fishType))) (scriptConfig :cookItem))))))
