(ns dreambot-test.treetraverse
  (:require [dreambot-test.utils.fishing :as fishing]
            [dreambot-test.utils.banking :as banking]
            [dreambot-test.utils.inventory :as inv]
            [dreambot-test.utils.behaviortree :as btree]
            [dreambot-test.utils.areas :as areas]
            [dreambot-test.utils.equipment :as equipment]))

(import
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.methods.container.iml Inventory])

;; TODO should come in through the java shim
(def scriptConfig
  {:fishType "Raw Lobster"
   :depositItems ["Lobster"]
   :fishingArea (areas/fishingAreas :catherby)
   :cookArea (areas/cookingAreas :catherby)
   :cookItem ["Raw Lobster"]})

(defn bankSequence
  [depositItems]
  (and (inv/inventoryIsFull)
       (not (Inventory/contains (scriptConfig :cookItem)))
       (btree/travelTo (.getArea (Bank/getClosestBankLocation) 10))
       (banking/deposit depositItems)))

(defn cookSequence
  [cookItem]
  (and (inv/inventoryIsFull)
       (inv/hasRequiredItems cookItem)
       (btree/travelTo ())))

(defn fishSequence
  [fishType fishingZone]
  (and (inv/hasRequiredItems (equipment/requiredFishingEquipment (keyword fishType)))
       (inv/hasInventorySpace)
       (btree/travelTo fishingZone)
       (fishing/goFishing fishType)))

(defn traverseTree
  []
  ;; TODO: required equipment should be looked up depending on the fish type
  (or (fishSequence (scriptConfig :fishType) (scriptConfig :fishingArea))
      (bankSequence (scriptConfig :depositItems))))
