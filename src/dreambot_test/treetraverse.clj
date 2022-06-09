(ns dreambot-test.treetraverse
  (:require [dreambot-test.utils.fishing :as fishing]
            [dreambot-test.utils.banking :as banking]
            [dreambot-test.utils.inventory :as inv]
            [dreambot-test.utils.behaviortree :as btree]))

(import [org.dreambot.api.methods.map Area]
        [org.dreambot.api.methods.container.impl.bank Bank])

(defn bankSequence
  []
  (and (inv/inventoryIsFull) (btree/travelFallback (.getArea (Bank/getClosestBankLocation) 10)) (banking/deposit ["Raw Lobster"])))

(defn fishSequence
  [tools fishingZone]
  (and (inv/hasRequiredTools tools) (inv/hasInventorySpace) (btree/travelFallback fishingZone) (fishing/goFishing)))

(defn traverseTree
  []
  (or (fishSequence ["Lobster pot"] (new Area 2833 3436 2861 3421)) (bankSequence)))
