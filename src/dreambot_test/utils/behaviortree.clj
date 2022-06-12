(ns dreambot-test.utils.behaviortree
  (:require [dreambot-test.utils.walking :as walking]))

(defn travelTo
  "A fallback node for traveling to a destination"
  [destination]
  (or (walking/isAtDestination destination) (walking/walkNext destination)))
