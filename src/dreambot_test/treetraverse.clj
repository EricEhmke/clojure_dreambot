(ns dreambot-test.treetraverse
  (:require [dreambot-test.utils.fishing :as fishing]
            [dreambot-test.utils.banking :as banking]
            [dreambot-test.utils.inventory :as inv]
            [dreambot-test.utils.behaviortree :as btree]))

(import [org.dreambot.api.methods.map Area]
        [org.dreambot.api.methods.container.impl.bank Bank]
        [org.dreambot.api.methods.container.iml Inventory])

(def scriptConfig
  {:requiredEquipment ["Lobster pot"]
   :depositItems ["Lobster"]
   :fishingArea (new Area 2833 3436 3421)
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
  [tools fishingZone]
  (and (inv/hasRequiredItems tools)
       (inv/hasInventorySpace)
       (btree/travelTo fishingZone)
       (fishing/goFishing)))

(defn traverseTree
  []
  (or (fishSequence (scriptConfig :requiredEquipment) (scriptConfig :fishingArea))
      (bankSequence (scriptConfig :depositItems))))
