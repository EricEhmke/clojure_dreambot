(ns dreambot-test.treetraverse
  (:require [dreambot-test.utils.fishing :as fishing]
            [dreambot-test.utils.banking :as banking]
            [dreambot-test.utils.inventory :as inv]
            [dreambot-test.utils.behaviortree :as btree]
            [dreambot-test.utils.areas :as areas]
            [dreambot-test.utils.equipment :as equipment]))

(import
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider])

;; ;; TODO should come in through the java shim
;; (def scriptConfig
;;   {:fishType "Raw Lobster"
;;    :depositItems ["Lobster"]
;;    :fishingArea "Catherby"
;;    :cookArea "Catherby"
;;    :cookItem ["Raw Lobster"]})

(defn bankSequence
  [depositItems cookItem]
  (and (inv/inventoryIsFull)
       (not (Inventory/contains cookItem))
       (btree/travelTo (.getArea (Bank/getClosestBankLocation) 10))
       (banking/deposit depositItems)))

(defn cookSequence
  [cookItem cookingArea]
  (and (inv/inventoryIsFull)
       (inv/hasRequiredItems cookItem)
       (btree/travelTo (areas/cookingAreas (keyword cookingArea)))))

(defn fishSequence
  [fishType fishingZone]
  (and (inv/hasRequiredItems (equipment/requiredFishingEquipment (keyword fishType)))
       (inv/hasInventorySpace)
       (btree/travelTo (areas/fishingAreas (keyword fishingZone)))
       (fishing/goFishing fishType)))

(defn traverseTree
  [scriptConfig]
  (let [scriptConfig (into {} scriptConfig)]
    (when (true? (scriptConfig :start))
      (or (fishSequence (scriptConfig :fishType) (scriptConfig :fishingArea))
          (bankSequence (scriptConfig :depositItemsl) (scriptConfig :cookItem))))))
