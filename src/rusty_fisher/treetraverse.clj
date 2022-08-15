(ns rusty-fisher.treetraverse
  (:require [rusty-fisher.utils.fishing :as fishing]
            [rusty-fisher.utils.banking :as banking]
            [rusty-fisher.utils.inventory :as inv]
            [rusty-fisher.utils.behaviortree :as btree]
            [rusty-fisher.utils.areas :as areas]
            [rusty-fisher.utils.walking :as walking]))

(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider])

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
  (and (inv/hasRequiredItems (get-in fishing/fishMap [(keyword fishType) :requiredEquipment]))
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
          (bankSequence (get-in fishing/fishMap [(keyword (scriptConfig :fishType)) :requiredEquipment]) (scriptConfig :cookItem))))))
