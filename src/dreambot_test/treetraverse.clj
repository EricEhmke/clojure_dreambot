(ns dreambot-test.treetraverse
  (:require [dreambot-test.fishnode :as fishnode]
            [dreambot-test.banknode :as banknode]))

(import [org.dreambot.api.methods.map Area])

(defn traverseTree
  []
  (and (fishnode/fishSequence "Lobster pot" (new Area 2833 3436 2861 3421)) (banknode/bankSequence)))
